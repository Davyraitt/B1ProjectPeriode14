package com.avans.b1project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton wachtrijenButton;
    private ImageButton jonkheerButton;
    private ImageButton bankjesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wachtrijenButton = (ImageButton) findViewById(R.id.wachtrijenButton);
        wachtrijenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonWachtrijen();
            }
        });


        jonkheerButton = (ImageButton) findViewById(R.id.jonkheerButton);
        jonkheerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleJonkheerButton();
            }
        });

        bankjesButton = (ImageButton) findViewById(R.id.bankjesButton);
        bankjesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBankjesButton();
            }
        });

        System.out.println("change");

    }

    private void handleBankjesButton() {
        Intent intent = new Intent(this, Bankjes.class);
        startActivity(intent);
    }



    private void handleJonkheerButton() {
        Intent intent = new Intent(this, jonkheerAttractie.class);
        startActivity(intent);

    }

    private void handleButtonWachtrijen() {
        wachtrijenButton.setSelected(!wachtrijenButton.isPressed());

        if (wachtrijenButton.isPressed()) {
            wachtrijenButton.setImageResource(R.drawable.buttonwachtrijenpressed);

        }

        Intent intent = new Intent(this, Wachtrijen.class);
        startActivity(intent);
    }


}
