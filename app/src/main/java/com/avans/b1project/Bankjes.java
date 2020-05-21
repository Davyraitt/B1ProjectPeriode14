package com.avans.b1project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Bankjes extends AppCompatActivity {

    private ImageButton attractiesButton;
    private ImageButton bankjeButton1;
    private ImageButton bankjeButton2;
    private ImageButton wachtrijenButton;
    private int counter1;
    private int counter2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankjes);

        counter1 = 0;
        counter2 = 0;

        bankjeButton1 = (ImageButton) findViewById(R.id.benchButton1);
        bankjeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBankjeButton1();
            }
        });

        bankjeButton2 = (ImageButton) findViewById(R.id.benchButton2);
        bankjeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBankjeButton2();
            }
        });


        attractiesButton = (ImageButton) findViewById(R.id.attractiesButton);
        attractiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAttractiesButton();
            }
        });


        wachtrijenButton = (ImageButton) findViewById(R.id.wachtrijenButton);
        wachtrijenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleWachtrijenButton();
            }
        });
    }

    private void handleAttractiesButton() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void handleBankjeButton1() {
        bankjeButton1.setSelected(!wachtrijenButton.isPressed());
        counter1 ++;

        if (bankjeButton1.isPressed()) {
            if ((counter1 % 2) == 0)
            bankjeButton1.setImageResource(R.drawable.benchbusyicon);

            else {
                bankjeButton1.setImageResource(R.drawable.benchnotbusyicon);
            }

        }
    }

    private void handleBankjeButton2() {
        bankjeButton2.setSelected(!wachtrijenButton.isPressed());
        counter2 ++;

        if (bankjeButton2.isPressed()) {
            if ((counter2 % 2) == 0)
                bankjeButton2.setImageResource(R.drawable.benchbusyicon);

            else {
                bankjeButton2.setImageResource(R.drawable.benchnotbusyicon);
            }

        }
    }

    private void handleWachtrijenButton() {
        Intent intent = new Intent(this, Wachtrijen.class);
        startActivity(intent);
    }
}
