package com.avans.b1project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class schermWachtrijen extends AppCompatActivity {

    private ImageButton backButton;
    private ImageButton buttonKortsteWachtrij;
    private static TextView textViewCobra;
    private static TextView textViewJonkheer;

    public static int waitTimeCobra;
    public static int waitTimeJonkheer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wachtrijen);


        //Creating all the buttons
        backButton = (ImageButton) findViewById(R.id.backButton);
        buttonKortsteWachtrij = (ImageButton) findViewById(R.id.buttonKortsteWachtrij);

        //Creating textviews
        textViewCobra = (TextView) findViewById(R.id.textViewCobraCounter);
        textViewJonkheer = (TextView) findViewById(R.id.textViewJonkheerCounter);

        // attaching clicklisteners to the buttons
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButton();
            }
        });
        buttonKortsteWachtrij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleKorsteWachtrijButton();
            }
        });
    }

    private void handleKorsteWachtrijButton() {
        buttonKortsteWachtrij.setSelected(!buttonKortsteWachtrij.isPressed());

        if (buttonKortsteWachtrij.isPressed()) {
            buttonKortsteWachtrij.setImageResource(R.drawable.buttonshortestrowpressed);

        }

        Intent intent = new Intent(this, schermKorsteWachtrij.class);
        startActivity(intent);

    }

    private void handleBackButton() {
        backButton.setSelected(!backButton.isPressed());

        if (backButton.isPressed()) {
            backButton.setImageResource(R.drawable.buttonbackpressed);

        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public static void berekenWachtrijen(int counterCobra, int counterJonkheer) {
        waitTimeCobra = (counterCobra * 30) / 60;
        waitTimeJonkheer = (counterJonkheer * 30) / 60;

        textViewJonkheer.setText(waitTimeJonkheer + " minuten");
        textViewCobra.setText(waitTimeCobra + " minuten");

    }

}


