package com.example.sudokugame;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Difficulty difficulty = Difficulty.NONE;
    private static final int GRID_SIZE = 9;
    private int[][] originalBoard;
    private Button[][] buttons = new Button[GRID_SIZE][GRID_SIZE];
    private Button[] buttons2 = new Button[GRID_SIZE];
    private Button btnEasy, btnMedium, btnHard, currentSquare, btnGenerateBoard;
    private HashMap<Button, String> btnLockMap = new HashMap<Button, String>();
    private HashMap<Button, Integer> btnCheckMap = new HashMap<Button, Integer>();
    private GridColours gridColours = new GridColours();
    private int currentValue;
    private Button prevButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup MainActivity view
        firstSetup();

        // Initialize views
        iniViews();

        // Click listeners
        clickListeners();
    }

    private void clickListeners(){

        // Difficulty Listeners
        this.btnEasy.setOnClickListener(v ->{this.difficulty = Difficulty.EASY;});
        this.btnMedium.setOnClickListener(v ->{this.difficulty = Difficulty.MEDIUM;});
        this.btnHard.setOnClickListener(v ->{this.difficulty = Difficulty.HARD;});


        // Generate the board buttons
        this.btnGenerateBoard.setOnClickListener(v -> {
            int difficultyInt = 0;
            switch (difficulty){
                case EASY:
                    difficultyInt = 20;
                    break;
                case MEDIUM:
                    difficultyInt = 35;
                    break;
                case HARD:
                    difficultyInt = 50;
                    break;
            }
            if (difficultyInt != 0) {
                //generate a new board with set difficulty
                Sudoku sudoku = new Sudoku(difficultyInt);
                originalBoard = sudoku.OriginalBoard();
                populateGrid(sudoku.getBoard());
                difficulty = Difficulty.NONE;
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

                        // highlights the selected numbers
                        currentSquare.setBackgroundResource(R.drawable.selected_number_drawable);
                        for (int a = 0; a < 9; a++) {
                            for (int b = 0; b < 9; b++) {
                                if (buttons[a][b].getText().toString().equals(currentSquare.getText().toString()) && buttons[a][b] != currentSquare && !buttons[a][b].getText().toString().equals(" ")) {
                                    buttons[a][b].setBackgroundResource(R.drawable.other_selected_number_drawable);
                                }
                            }
                        }
                    }
                });
            }
        }

        // Listeners for input row
        for (int i = 0; i < 9; i++) {
            final int appendVal = i+1;
            this.buttons2[i].setOnClickListener(v -> {
                Button setButton = (Button) v;
                if(btnLockMap.get(currentSquare).equals("UnLocked")) {
                    this.currentSquare.setText(String.valueOf(appendVal));
                    if(btnCheckMap.get(currentSquare) == Integer.parseInt(currentSquare.getText().toString())){
                        currentSquare.setTextColor(getResources().getColor(R.color.CorrectNumber));
                    }else{
                        currentSquare.setTextColor(getResources().getColor(R.color.IncorrectNumber));
                    }
                }
            });
        }


    }


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
                params.setMargins(2, 2, 2, 2);

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

    private void iniViews(){
        this.btnGenerateBoard = findViewById(R.id.generateButton);
        this.btnEasy = findViewById(R.id.easyButton);
        this.btnMedium = findViewById(R.id.mediumButton);
        this.btnHard = findViewById(R.id.hardButton);
    }
}
