package com.avans.b1project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class schermKorsteWachtrij extends AppCompatActivity {

    private ImageButton backButton;
    private static ImageView arrowPointer;
    static MqttAndroidClient client;
    String topic = "Android/B1/Bord";
    private int qos = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kortste_wachtrij);

        connectToMQTT();

        //Creating all the buttons
        backButton = (ImageButton) findViewById(R.id.backButton);
        arrowPointer = (ImageView) findViewById(R.id.arrowPointer);

        // attaching clicklisteners to the buttons
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButton();
            }
        });


    }


    private void handleBackButton() {
        backButton.setSelected(!backButton.isPressed());

        if (backButton.isPressed()) {
            backButton.setImageResource(R.drawable.buttonbackpressed);

        }

        Intent intent = new Intent(this, schermWachtrijen.class);
        startActivity(intent);
    }

    private void connectToMQTT() {
        //MQTT stuff
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://maxwell.bps-software.nl:1883", clientId);

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("androidTI");
            options.setPassword("&FN+g$$Qhm7j".toCharArray());

            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("We connected!!");
                    try {
                        client.subscribe(topic, qos);

                        // setting the arrow :)
                        pointToBord(schermWachtrijen.waitTimeCobra, schermWachtrijen.waitTimeJonkheer);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Connection failed!!");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    private static void writeToMQTT(final String topic, final String whatToWrite) {
        if (!client.isConnected()) {
            System.out.println(":(");
            Log.d("KortsteWachtrij", "Hij doet niet :(");
            return;
        } else {
            try {
                client.publish(topic, new MqttMessage(whatToWrite.getBytes()));
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }


    }


    private static void pointToBord(int waitTimeCobra, int waitTimeJonkheer) {
        if (waitTimeCobra > waitTimeJonkheer) {
            arrowPointer.setImageResource(R.drawable.shortestcobra);
            writeToMQTT("Android/B1/Bord", "130");
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            writeToMQTT("Android/B1/Bord", waitTimeJonkheer + " min");
        }

        if (waitTimeCobra < waitTimeJonkheer) {
            arrowPointer.setImageResource(R.drawable.shortestjonkheer);
            writeToMQTT("Android/B1/Bord", "70");

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            writeToMQTT("Android/B1/Bord", waitTimeCobra + " min");
        }

        if (waitTimeCobra == waitTimeJonkheer) {
            arrowPointer.setImageResource(R.drawable.shortestmiddle);
            writeToMQTT("Android/B1/Bord", "100");
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            writeToMQTT("Android/B1/Bord", waitTimeCobra + " min");
        }
    }

}
