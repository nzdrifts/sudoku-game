package com.example.sudokugame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeScreen extends AppCompatActivity {

    private Difficulty difficulty = Difficulty.NONE;
    private Button btnEasy, btnMedium, btnHard, btnPlay, btnStats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        iniViews();

        // Click listeners
        clickListeners();
    }

    private void clickListeners() {
        // Difficulty Listeners
        this.btnEasy.setOnClickListener(v -> {
            this.difficulty = Difficulty.EASY;
            highlightSelectedDifficulty(this.btnEasy);
        });
        this.btnMedium.setOnClickListener(v -> {
            this.difficulty = Difficulty.MEDIUM;
            highlightSelectedDifficulty(this.btnMedium);
        });
        this.btnHard.setOnClickListener(v -> {
            this.difficulty = Difficulty.HARD;
            highlightSelectedDifficulty(this.btnHard);
        });

        // launch stats activity
        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, StatsPage.class));
            }
        });

        // launch main activity
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.difficulty = difficulty;
                if (difficulty!=Difficulty.NONE) {
                    startActivity(new Intent(HomeScreen.this, MainActivity.class));
                }
            }
        });


    }

    // sets appropriate button colours when a difficulty is selected
    private void highlightSelectedDifficulty(Button b) {
        btnEasy.setBackgroundColor(ContextCompat.getColor(this, R.color.difficultyButtons));
        btnMedium.setBackgroundColor(ContextCompat.getColor(this, R.color.difficultyButtons));
        btnHard.setBackgroundColor(ContextCompat.getColor(this, R.color.difficultyButtons));

        b.setBackgroundColor(ContextCompat.getColor(this, R.color.SelectedDifficultyButtons));
    }

    private void iniViews(){
        this.btnStats = findViewById(R.id.statsButton);
        this.btnPlay = findViewById(R.id.playButton);
        this.btnEasy = findViewById(R.id.easyButton);
        this.btnMedium = findViewById(R.id.mediumButton);
        this.btnHard = findViewById(R.id.hardButton);
    }
}