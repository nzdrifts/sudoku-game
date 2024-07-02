package com.example.sudokugame;

import android.os.Handler;
import android.os.Message;

public class Stopwatch implements Runnable{
    private volatile boolean running = true;
    private Handler handler;
    private int seconds;
    private String time;

    public Stopwatch(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        seconds = -1;

        while (running) {
            try {
                seconds++;
                int minutes = (seconds % 3600) / 60;
                int remainingSeconds = seconds % 60;
                this.time = String.format("%02d:%02d%n", minutes, remainingSeconds);
                //appends textview
                Message msg = handler.obtainMessage(1, time);
                handler.sendMessage(msg);
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Stopwatch was interrupted.");
            }
        }
    }

    public int stop() {
        running = false;
        return seconds;
    }

    public String getStringTime(){
        return time;
    }
    public int getIntSeconds(){return seconds;}
}
