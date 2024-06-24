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

import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class StatsPage extends AppCompatActivity {

    private TextView easy, medium, hard;
    private Button btnWriteTo, btnBack, btnReadFrom;
    public FileHandler fileHandler = new FileHandler();



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
        this.easy = findViewById(R.id.easyWontv);
        this.medium = findViewById(R.id.mediumWontv);
        this.hard = findViewById(R.id.hardWontv);
        this.btnWriteTo = findViewById(R.id.writeToButton);
        this.btnBack = findViewById(R.id.backButton);

        //loads stats onto page



        // Click listeners
        clickListeners();
    }

    private void clickListeners() {

        // go back to home screen
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatsPage.this, HomeScreen.class));
            }
        });

        this.btnWriteTo.setOnClickListener(v -> {
            String content = "wrote to file x";
            File path = getApplicationContext().getFilesDir();
            try {
                FileOutputStream writer = new FileOutputStream(new File(path, "stats.txt"));
                writer.write(content.getBytes());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }




}