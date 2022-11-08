package com.bawp.playvideoexop;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class UtilCountdown extends AppCompatActivity {
    public CountDownTimer countDownTimer;


    public UtilCountdown(final TextView countdownVw, final int targetSecs) {
        int futureMilliseconds = targetSecs * 1000;

        countDownTimer = new CountDownTimer(futureMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // handling seconds
                int seconds = (int) Math.round(((millisUntilFinished / (double) 1000) % 60)); // millisUntilFinished: 9976 for 9 seconds
                countdownVw.setText(String.valueOf(seconds));

                boolean isFinalCount = seconds < 10;
                countdownVw.setTextSize(50);
                if(isFinalCount) countdownVw.setTextSize(58);
            }

            @Override
            public void onFinish() {
                countdownVw.setText("0");
                countDownTimer.cancel();
            }
        }.start();
    }

    public void start() {
        countDownTimer.start();
    }

    public void cancel() {
        countDownTimer.cancel();
    }

}
