package com.example.sudokugame;

import android.widget.Button;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/*
    Sudoku class that performs the mathematical calculations needed to manipulate the board
*/
public class Sudoku {

    int[][] originalBoard = new int[9][9];
    int[][] board;
    int n = 9; //number of rows/columns
    int bw = 3; //width of each box
    int k; //number of squares to remove
    private HashMap<Integer,Integer> btnRemovedHashMap = new HashMap<Integer,Integer>();//<1-9,0>

    Sudoku(int k){
        this.k = k;
        board = new int[n][n];

        // populate HashMap <1-9,0>
        for (int i = 1; i <= 9; i++) {
            this.btnRemovedHashMap.put(i,0);
        }

        fillSquares();
    }

    public int[][] getBoard(){
        return this.board;
    }


    public void fillSquares(){
        // fills the diagonal boxes
        fillDiagonals();

        // fills the remaining boxes
        fillRemaining(0,bw);

        //set original board
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                originalBoard[i][j] = board[i][j];
            }
        }

        // removes k squares
        removeSquares(k);

    }



    public void fillDiagonals(){
        for (int i = 0; i < n; i=i+bw) {
            fillBox(i,i);
        }
    }

    // fill a box (3x3 matrix)
    public void fillBox(int row, int col){ // row and col represent starting index of box
        int num;
        for (int i = 0; i < bw; i++) {
            for (int j = 0; j < bw; j++) {
                do {
                    num = ThreadLocalRandom.current().nextInt(0, n+1);
                }while(usedInBox(row, col, num));
                board[row+i][col+j] = num;
            }
        }
    }

    // returns true if value is already in box
    boolean usedInBox(int row, int col, int num){
        for (int i = 0; i < bw; i++) {
            for (int j = 0; j < bw; j++) {
                if(board[row+i][col+j] == num){return true;}
            }
        }
        return false;
    }

    boolean fillRemaining(int i, int j)
    {
        if (j>=n && i<n-1)
        {
            i = i + 1;
            j = 0;
        }
        if (i>=n && j>=n)
            return true;

        if (i < bw)
        {
            if (j < bw)
                j = bw;
        }
        else if (i < n-bw)
        {
            if (j==(int)(i/bw)*bw)
                j =  j + bw;
        }
        else
        {
            if (j == n-bw)
            {
                i = i + 1;
                j = 0;
                if (i>=n)
                    return true;
            }
        }

        for (int num = 1; num<=n; num++)
        {
            if (CheckIfSafe(i, j, num))
            {
                board[i][j] = num;
                if (fillRemaining(i, j+1))
                    return true;

                board[i][j] = 0;
            }
        }
        return false;
    }

    // Check if safe to put in cell
    boolean CheckIfSafe(int i,int j,int num)
    {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                !usedInBox(i-i%bw, j-j%bw, num));
    }

    // check in the row for existence
    boolean unUsedInRow(int i,int num)
    {
        for (int j = 0; j<n; j++)
            if (board[i][j] == num)
                return false;
        return true;
    }

    // check in the row for existence
    boolean unUsedInCol(int j,int num)
    {
        for (int i = 0; i<n; i++)
            if (board[i][j] == num)
                return false;
        return true;
    }

    private void removeSquares(int k) {
        while (k > 0){
            int i = ThreadLocalRandom.current().nextInt(0, n);
            int j = ThreadLocalRandom.current().nextInt(0, n);
            if(board[i][j]!=0){
                appendHashMap(board[i][j]);
                board[i][j] = 0;
                k--;
            }
        }
    }

    private void appendHashMap(int number) {
        // appends count by 1
        int oldNum = btnRemovedHashMap.get(number);
        btnRemovedHashMap.put(number,oldNum+1);

    }

    public int getHashMap(int i){
        return btnRemovedHashMap.get(i);
    }


    public int[][] OriginalBoard() {
        return originalBoard;
    }

}