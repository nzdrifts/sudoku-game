package com.example.sudokugame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class StatsPage extends AppCompatActivity {

    private TextView gamesPlayed, avgTime;
    private Button btnReset, btnBack, btnWrite;
    private static String gamesPlayedString, avgtimeString;
    private static int avgTimeSeconds=0;
    private Button btnLoad;

    public StatsPage(){
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initialize views
        this.gamesPlayed = findViewById(R.id.hardWontv);
        this.avgTime = findViewById(R.id.avgTimetv);
        this.btnReset = findViewById(R.id.ResetBtn);
        this.btnBack = findViewById(R.id.backButton);

        this.btnWrite = findViewById(R.id.WriteBtn);
        this.btnLoad = findViewById(R.id.LoadBtn);

        gamesPlayedString = "0";
        avgtimeString = "0";
        // Load data
        readFile();

        // Click listeners
        clickListeners();
    }

    // get methods for avgtime and games played
    public static String getGamesPlayedString(){
        return gamesPlayedString;
    }
    public static String getAvgtimeString(){
        return avgtimeString;
    }
    public static int getAverageTimeSecondsInt(){
        return avgTimeSeconds;
    }

    public static void setAvgTimeString(String time){
        avgtimeString = time;
        setAvgTimeSeconds(time);
    }
    public static void setAvgTimeSeconds(String time){
        avgTimeSeconds = splitTimeString(time);
    }

    private static int splitTimeString(String time) {
        // Split the time string by the colon
        String[] parts = time.split(":");

        // Parse minutes and seconds
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);

        // Calculate the total seconds
        return 12;//(minutes * 60) + seconds;
    }

    public static void incrementGamesPlayedString(){
        gamesPlayedString = String.valueOf(Integer.parseInt(gamesPlayedString)+1);
    }

    public void readFile(){
        FileHandler.readFileInBackground(this, "stats.txt", new FileHandler.FileReadCallback() {
            @Override
            public void onFileRead(List<String> content) {
                // Handle the file content
                runOnUiThread(() -> {
                    // Update returnContent var with the content
                    if (content.size() > 0) {
                        gamesPlayedString = content.get(0);
                    }
                    if (content.size() > 1) {
                        avgtimeString = content.get(1);
                    }
                });
            }
        });
    }

    private void clickListeners() {

        // go back to home screen
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatsPage.this, HomeScreen.class));
            }
        });

        // write dummy data
        btnWrite.setOnClickListener(v -> {
            List<String> linesToWrite = new ArrayList<>();
            linesToWrite.add("12");
            linesToWrite.add("12:41");
            FileHandler.writeToFile(this, "stats.txt", linesToWrite);
        });

        btnLoad.setOnClickListener(v -> {
            gamesPlayed.setText(gamesPlayedString);
            avgTime.setText(avgtimeString);
        });

        // reset data (will be invisible and deactivated when given to user)
        btnReset.setOnClickListener(v -> {
            List<String> linesToWrite = new ArrayList<>();
            linesToWrite.add("");
            linesToWrite.add("");

            FileHandler.writeToFile(this, "stats.txt", linesToWrite);
        });

    }




}