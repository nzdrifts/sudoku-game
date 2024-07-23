package com.example.sudokugame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static Difficulty difficulty = Difficulty.NONE;
    private static final int GRID_SIZE = 9;
    private int[][] originalBoard;
    private Button[][] buttons = new Button[GRID_SIZE][GRID_SIZE];
    private Button[] buttons2 = new Button[GRID_SIZE];
    private Button currentSquare, backBtn, tempBtn;
    private TextView tvTime;
    private HashMap<Button, String> btnLockMap = new HashMap<Button, String>();
    private HashMap<Button, Integer> btnCheckMap = new HashMap<Button, Integer>();
    private HashMap<Button, Integer> btnInputLockMap = new HashMap<Button, Integer>(); //<button,no. of places left>
    private GridColours gridColours = new GridColours();
    private int squaresLeftToComplete;
    private Sudoku sudoku;
    private static int oldAvgTime, oldGamesPlayed;//time stored in seconds
    private int currentRow, currentCol;
    SharedPreferences preference;

    // handler for stopwatch
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String time = (String) msg.obj;
                tvTime.setText(time);
            }
        }
    };
    Stopwatch stopwatch = new Stopwatch(handler);


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // setup back button
        this.backBtn = findViewById(R.id.backBtn);
        // setup time textView
        this.tvTime = findViewById(R.id.tvTime);
        // setup temp
        this.tempBtn = findViewById(R.id.button);

        // Setup MainActivity view and button arrays
        firstSetup();

        // Setup Board
        setupBoard();

        // populate btnInputLockMap
        generateLockMap();

        // remove any used up buttons
        for (int i = 0; i < buttons2.length; i++) {
            if(btnInputLockMap.get(buttons2[i])==0){
                // grey out the number on the row
                buttons2[i].setVisibility(View.INVISIBLE);
            }
        }

        // Start stopwatch
        Thread stopwatchThread = new Thread(stopwatch);
        stopwatchThread.start();

        // Click listeners
        clickListeners();
    }

    @SuppressLint("MissingInflatedId")
    private void showPopup() {
        // Inflate the custom layout/view
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.game_finished_popup, null);

        // Create a new PopupWindow
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = 1600;
        boolean focusable = false; // Lets taps outside the popup not dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        //load data from file into oldAvgTimeStr, oldGamesPlayedStr
        oldAvgTime = preference.getInt("avgTime", 0);
        oldGamesPlayed = preference.getInt("gamesPlayed", 0);

        //calculate new avg time
        int numerator = (oldAvgTime*oldGamesPlayed) + stopwatch.getIntSeconds();
        int denominator = oldGamesPlayed+1;
        int newAvgTimeInt = (int) ((double)numerator/(double)denominator);


        // write new average to storage and increment games played
        SharedPreferences.Editor editor = preference.edit();

        // Put data
        editor.putInt("avgTime", newAvgTimeInt);
        editor.putInt("gamesPlayed", oldGamesPlayed+1);

        // Commit the changes
        editor.apply();


        int minutes = (newAvgTimeInt % 3600) / 60;
        int remainingSeconds = newAvgTimeInt % 60;
        String s = String.format("%02d:%02d%n", minutes, remainingSeconds);

        // Set the text in the timetv
        TextView time = popupView.findViewById(R.id.time);
        String timeText = stopwatch.getStringTime();
        time.setText("Time: " + timeText);

        // Set the text in the avg_timetv
        TextView avg_time = popupView.findViewById(R.id.avg_time);
        avg_time.setText("Avg. Time: " + s);
        if(newAvgTimeInt<oldAvgTime){
            avg_time.setTextColor(getResources().getColor(R.color.green));
        } else{
            avg_time.setTextColor(getResources().getColor(R.color.red));
        }

        // stop stopwatch
        stopwatch.stop();

        // Set up a dismiss button in the popup layout
        Button closeButton = popupView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HomeScreen.class));
            }
        });

        // Show the popup window
        popupWindow.showAtLocation(backBtn, Gravity.CENTER, 0, 0);
    }


    private void generateLockMap() {
        for (int i = 1; i <= 9; i++) {
            btnInputLockMap.put(buttons2[i-1],sudoku.getHashMap(i));
        }
    }


    private void setupBoard() {
        int difficultyInt=0;
        switch (difficulty){
            case EASY:
                difficultyInt = 1;
                squaresLeftToComplete = 1;
                break;
            case MEDIUM:
                difficultyInt = 35;
                squaresLeftToComplete = 35;
                break;
            case HARD:
                difficultyInt = 45;
                squaresLeftToComplete = 45;
                break;
        }
        //generate a new board with set difficulty
        Sudoku sudoku = new Sudoku(difficultyInt);
        this.sudoku = sudoku;
        originalBoard = sudoku.OriginalBoard();
        populateGrid(sudoku.getBoard());
    }

    private void clickListeners(){

        // reset data button!!!!!!
        tempBtn.setOnClickListener(v -> {
            // Create a SharedPreferences editor to write data
            SharedPreferences.Editor editor = preference.edit();

            // Put data
            editor.putInt("avgTime", 0);
            editor.putInt("gamesPlayed", 0);

            // Commit the changes
            editor.apply();

        });

        //back button listener
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HomeScreen.class));
            }
        });

        // Listeners for each square
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                final int row = i;
                final int col = j;
                this.buttons[i][j].setOnClickListener(v -> {
                    if (originalBoard != null){
                        Button square = (Button) v;

                        // Highlight necessary squares
                        int[][] colours = gridColours.reset();
                        setColours(colours);
                        colours = gridColours.selectSquare(row, col);
                        setColours(colours);


                        // Set current button
                        currentSquare = square;
                        currentRow = row;
                        currentCol = col;

                        // highlights the selected numbers
                        currentSquare.setBackgroundResource(R.drawable.selected_number_drawable);
                        highlightSquares(currentSquare);
                    }
                });
            }
        }

        // Listeners for input row
        for (int i = 0; i < 9; i++) {
            final int appendVal = i+1;
            this.buttons2[i].setOnClickListener(v -> {
                Button setButton = (Button) v;
                if (currentSquare!=null && btnInputLockMap.get(setButton) != 0){

                    // if square is unlocked
                    if (btnLockMap.get(currentSquare).equals("UnLocked")) {
                        this.currentSquare.setText(String.valueOf(appendVal));
                        // reset colour
                        int[][] colours = gridColours.reset();
                        setColours(colours);
                        colours = gridColours.selectSquare(currentRow, currentCol);
                        setColours(colours);

                        currentSquare.setBackgroundResource(R.drawable.selected_number_drawable);
                        highlightSquares(currentSquare);
                        if (btnCheckMap.get(currentSquare) == Integer.parseInt(currentSquare.getText().toString())) {
                            currentSquare.setTextColor(getResources().getColor(R.color.CorrectNumber));
                            btnLockMap.put(currentSquare,"Locked");
                            squaresLeftToComplete--;
                            // check to see if button on bottom row needs to be locked/reduce number by 1 otherwise
                            btnInputLockMap.put(setButton,btnInputLockMap.get(setButton)-1);
                            if(btnInputLockMap.get(setButton) == 0){
                                // grey out the number on the row
                                setButton.setVisibility(View.INVISIBLE);
                            }
                            // checks to see if game is complete
                            if (squaresLeftToComplete == 0){ //game complete
                                if (difficulty == Difficulty.HARD) {
                                    showPopup();
                                }
                            }
                        } else {
                            currentSquare.setTextColor(getResources().getColor(R.color.IncorrectNumber));
                        }
                    }
                }

            });
        }


    }

    private void highlightSquares(Button currentSquare) {
        for (int a = 0; a < 9; a++) {
            for (int b = 0; b < 9; b++) {
                if (buttons[a][b].getText().toString().equals(currentSquare.getText().toString()) && buttons[a][b] != currentSquare && !buttons[a][b].getText().toString().equals(" ")) {
                    buttons[a][b].setBackgroundResource(R.drawable.other_selected_number_drawable);
                }
            }
        }
    }


    // calculate new average time


    // Initialize a new grid and populate it with values
    private void populateGrid(int[][] board){
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                // reset the text colour
                buttons[row][col].setTextColor(getResources().getColor(R.color.black));

                // append CheckMap
                btnCheckMap.put(buttons[row][col],originalBoard[row][col]);
                if (board[row][col] != 0) {
                    buttons[row][col].setText(String.valueOf(board[row][col]));
                    buttons[row][col].setBackgroundColor(getResources().getColor(R.color.BoardColour));
                    btnLockMap.put(buttons[row][col], "Locked");
                } else {
                    buttons[row][col].setText(" ");
                    btnLockMap.put(buttons[row][col], "UnLocked");
                    buttons[row][col].setBackgroundColor(getResources().getColor(R.color.BoardColour));
                }
            }
        }

    }

    private void setColours(int[][] colours){
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (colours[row][col] == 0) {
                    buttons[row][col].setBackgroundColor(getResources().getColor(R.color.BoardColour));
                }
                if (colours[row][col] == 1) {
                    buttons[row][col].setBackgroundColor(getResources().getColor(R.color.RowAndColGridColour));
                }
                if (colours[row][col] == 2) {
                    buttons[row][col].setBackgroundColor(getResources().getColor(R.color.SelectedSquare));
                }
            }
        }
    }

    private void firstSetup(){

        // CREATE FIRST GRID
        FrameLayout gridContainer = findViewById(R.id.gridContainer);

        // Create GridLayout programmatically
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(GRID_SIZE);
        gridLayout.setRowCount(GRID_SIZE);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);

        // Set up GridLayout params to ensure square buttons
        FrameLayout.LayoutParams gridLayoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        gridLayout.setLayoutParams(gridLayoutParams);

        // Add buttons to GridLayout
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Button button = new Button(this);

                // Set up button parameters
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(row, 1f);
                params.columnSpec = GridLayout.spec(col, 1f);
                if(row%3==0 && col%3==0){
                    params.setMargins(8, 8, 2, 2);
                }else if (col%3==0) {
                    params.setMargins(8, 2, 2, 2);
                }else if (row%3==0) {
                    params.setMargins(2, 9, 2, 2);
                }else{
                    params.setMargins(2, 2, 2, 2);
                }

                button.setLayoutParams(params);
                button.setPadding(0, 0, 0, 0);
                button.setText(" ");
                button.setTextSize(26);
                button.setTextColor(getResources().getColor(R.color.black));

                // Store button reference
                buttons[row][col] = button;
                buttons[row][col].setBackgroundColor(getResources().getColor(R.color.BoardColour));
                btnLockMap.put(button, "");
                btnLockMap.put(buttons[row][col], "Locked");

                // Add button to GridLayout
                gridLayout.addView(button);
            }
        }

        // Add GridLayout to FrameLayout
        gridContainer.addView(gridLayout);






        //CREATE SECOND GRID
        // Create and add the second grid layout using LinearLayout
        LinearLayout gridContainer2 = findViewById(R.id.gridContainer2);
        gridContainer2.setOrientation(LinearLayout.HORIZONTAL);

        // Set up LinearLayout params to ensure square buttons
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f);

        // Add buttons to LinearLayout
        for (int col = 0; col < GRID_SIZE; col++) {
            Button button = new Button(this);
            button.setLayoutParams(buttonParams);
            button.setPadding(0, 0, 0, 0);
            button.setText(String.valueOf(col + 1)); // Set text 1-9

            // Store button reference
            buttons2[col] = button;

            // Add button to LinearLayout
            gridContainer2.addView(button);
        }
    }


}
