package com.avans.b1project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


public class Bankjes extends AppCompatActivity {

    private static ImageButton attractiesButton;
    private static ImageButton bankjeButton1;
    private static ImageButton bankjeButton2;
    private static ImageButton wachtrijenButton;
    private int counter1;
    private int counter2;
    MqttAndroidClient client;
    String topic;

    public Bankjes() {
        topic = "Android/B1/TestVanuitAndroidStudio";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankjes);


        // creating the buttons
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
//        bankjeButton1.setSelected(!wachtrijenButton.isPressed());
//        counter1++;
//
//        if (bankjeButton1.isPressed()) {
//            if ((counter1 % 2) == 0)
//                bankjeButton1.setImageResource(R.drawable.benchbusyicon);
//
//            else {
//                bankjeButton1.setImageResource(R.drawable.benchnotbusyicon);
//            }
//
//        }
    }

    private void handleBankjeButton2() {
//        bankjeButton2.setSelected(!wachtrijenButton.isPressed());
//        counter2++;
//
//        if (bankjeButton2.isPressed()) {
//            if ((counter2 % 2) == 0)
//                bankjeButton2.setImageResource(R.drawable.benchbusyicon);
//
//            else {
//                bankjeButton2.setImageResource(R.drawable.benchnotbusyicon);
//            }
//
//        }
    }

    public static void setBench1Busy() {
        bankjeButton1.setImageResource(R.drawable.benchbusyicon);
    }

    public static void setBench2Busy() {
        bankjeButton2.setImageResource(R.drawable.benchbusyicon);
    }

    public static void setBench1Free() {
        bankjeButton1.setImageResource(R.drawable.benchnotbusyicon);
    }

    public static void setBench2Free() {
        bankjeButton2.setImageResource(R.drawable.benchnotbusyicon);
    }

    private void handleWachtrijenButton() {
        Intent intent = new Intent(this, Wachtrijen.class);
        startActivity(intent);
    }
}
