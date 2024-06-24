package com.example.sudokugame;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHandler  extends AppCompatActivity {
    String fileContent;

    public FileHandler(){}

    // runs when game is completed and appends win to appropriate difficulty
    public static void gameCompleted(Difficulty difficulty) {
        switch (difficulty){
            case EASY:

                break;
            case MEDIUM:

                break;
            case HARD:

                break;
        }
    }

    public String readFromFile(String fileName){
            File path = getApplicationContext().getFilesDir();
            File readFrom = new File(path, fileName);
            byte[] content = new byte[(int) readFrom.length()];

            try {
                FileInputStream stream = new FileInputStream(readFrom);
                stream.read(content); // reads from stream and stores in content array
                return new String(content);
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }
        }
}

/*
// *** currently testing ***
        this.btnReset.setOnClickListener(v -> { //(write to)
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

                this.btnReadFrom.setOnClickListener(v -> {
                // display new stats
                String stats = null;
                try {
                stats = loadStats("stats.txt");
                } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
                }
                easy.setText(stats);
                });
                }

public String loadStats(String fileName) throws FileNotFoundException {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, fileName);
        byte[] content = new byte[(int) readFrom.length()];

        try {
        FileInputStream stream = new FileInputStream(readFrom);
        stream.read(content); // reads from stream and stores in content array
        return new String(content);
        } catch (IOException e) {
        e.printStackTrace();
        return e.toString();
        }
        }
        */