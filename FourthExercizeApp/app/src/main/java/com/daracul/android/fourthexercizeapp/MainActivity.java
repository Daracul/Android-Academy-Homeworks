package com.daracul.android.fourthexercizeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity {
    private LeftLeg leftLeg;
    private  RightLeg rightLeg;
    Semaphore semaphore;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text_view);

    }

    @Override
    public void onStart() {
        super.onStart();
        semaphore = new Semaphore(1,true);
        leftLeg = new LeftLeg(semaphore);
        rightLeg = new RightLeg(semaphore);
        new Thread(leftLeg).start();
        new Thread(rightLeg).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        leftLeg.stopRun();
        rightLeg.stopRun();
    }

    private static class LeftLeg implements Runnable {
        private boolean isRunning = true;
        Semaphore semaphore;

        LeftLeg(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        public void stopRun(){
            isRunning=false;
        }

        @Override
        public void run() {

            while (isRunning) {
                try {
                    semaphore.acquire();
                    System.out.println("Left step");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphore.release();

            }
        }
    }

    private static class RightLeg implements Runnable {
        private boolean isRunning = true;
        Semaphore semaphore;

        RightLeg(Semaphore semaphore) {
            this.semaphore = semaphore;
        }
        public void stopRun(){
            isRunning=false;
        }

        @Override
        public void run() {
            while (isRunning){
                try {
                    semaphore.acquire();
                    System.out.println("Right step");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphore.release();

            }
        }
    }
}


