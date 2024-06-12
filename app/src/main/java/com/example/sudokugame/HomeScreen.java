package com.example.sudokugame;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeScreen extends AppCompatActivity {

    private Difficulty difficulty = Difficulty.NONE;
    private Button btnEasy, btnMedium, btnHard, btnPlay;


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
        });
        this.btnMedium.setOnClickListener(v -> {
            this.difficulty = Difficulty.MEDIUM;
        });
        this.btnHard.setOnClickListener(v -> {
            this.difficulty = Difficulty.HARD;
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

    private void iniViews(){
        this.btnPlay = findViewById(R.id.playButton);
        this.btnEasy = findViewById(R.id.easyButton);
        this.btnMedium = findViewById(R.id.mediumButton);
        this.btnHard = findViewById(R.id.hardButton);
    }
}