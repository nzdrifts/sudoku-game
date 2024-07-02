package com.example.sudokugame;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileHandler  extends AppCompatActivity {

    public static void readFileInBackground(Context context, String fileName, FileReadCallback callback) {
        new Thread(() -> {
            List<String> result = readFromFile(context, fileName);
            callback.onFileRead(result);
        }).start();
    }

    private static List<String> readFromFile(Context context, String fileName) {
        File path = context.getFilesDir();
        File readFrom = new File(path, fileName);
        List<String> lines = new ArrayList<>();

        if (!readFrom.exists()) {
            lines.add("File not found: " + fileName);
            return lines;
        }

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(readFrom)));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            lines.add(e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    lines.add("Failed to close the BufferedReader: " + e.toString());
                }
            }
        }

        return lines;
    }

    public static void writeToFile(Context context, String fileName, List<String> lines) {
        new Thread(() -> writeToFileInternal(context, fileName, lines)).start();
    }

    private static void writeToFileInternal(Context context, String fileName, List<String> lines) {
        File path = context.getFilesDir();
        File writeTo = new File(path, fileName);

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeTo, false))); // Overwrite mode
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface FileReadCallback {
        void onFileRead(List<String> content);
    }
}