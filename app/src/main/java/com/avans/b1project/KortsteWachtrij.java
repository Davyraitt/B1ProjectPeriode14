package com.avans.b1project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class KortsteWachtrij extends AppCompatActivity {

    private ImageButton backButton;
    private static ImageView arrowPointer;
    static MqttAndroidClient client;
    String topic;
    Wachtrijen wachtrijen = new Wachtrijen();


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


        // setting the arrow
        pointToBord(wachtrijen.getWaitTimeCobra(), wachtrijen.getWaitTimeJonkheer());

    }




    private void handleBackButton() {
        backButton.setSelected(!backButton.isPressed());

        if (backButton.isPressed()) {
            backButton.setImageResource(R.drawable.buttonbackpressed);

        }

        Intent intent = new Intent(this, Wachtrijen.class);
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
        try {
            int qos = 2; //Ieder bericht moet maar 1x binnenkomen
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Message message = new Message("Bot" , "This is a test message",null,new Date());
                    String topictestWriting = topic;
                    String payloadtest = whatToWrite;
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payloadtest.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        client.publish(topictestWriting, message);


                    } catch (UnsupportedEncodingException | MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void calculateWaitTimes() {

    }


    private static void pointToBord (int waitTimeCobra, int waitTimeJonkheer){
        if (waitTimeCobra > waitTimeJonkheer) {
            arrowPointer.setImageResource(R.drawable.shortestjonkheer);
            writeToMQTT("Android/B1/Bord", "130");
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            writeToMQTT("Android/B1/Bord", waitTimeJonkheer + " min");
        }

        if (waitTimeCobra < waitTimeJonkheer) {
            arrowPointer.setImageResource(R.drawable.shortestcobra);
            writeToMQTT("Android/B1/Bord", "70");

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            writeToMQTT("Android/B1/Bord", waitTimeCobra + " min");
        }

        if (waitTimeCobra == waitTimeJonkheer){
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