package com.example.sudokugame;


import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

/*
       Grid class that performs colours calculation on the grid
 */
public class GridColours extends AppCompatActivity {

    private int[][] colorOfButtons = new int[9][9]; //stores the 81 buttons 0-default,1-light blue, 2-darker blue, 3-Locked
    private HashMap<Button, String> buttonMap = new HashMap<Button, String>();


    public int[][] reset(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                colorOfButtons[i][j] = 0;
            }
        }
        return colorOfButtons;
    }

    public int[][] selectSquare(int row, int col){
        int xPos = (int)Math.floor(row/3)*3+1;
        int yPos = (int)Math.floor(col/3)*3+1;
        colourSmallGrid(xPos, yPos);
        colourRowAndCol(row,col);
        this.colorOfButtons[row][col] = 2;
        return colorOfButtons;
    }

    private void colourSmallGrid(int xPos, int yPos) {
        // colour the grid
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                colorOfButtons[xPos+i][yPos+j] = 1;
            }
        }
    }
    public void colourRowAndCol(int xPos, int yPos){
        // colour the row and col
        for (int i = 0; i < 9; i++) {
            colorOfButtons[xPos][i] = 1;
            colorOfButtons[i][yPos] = 1;
        }
    }


}