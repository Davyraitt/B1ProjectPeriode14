package com.avans.b1project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {

    private ImageButton wachtrijenButton;
    private ImageButton jonkheerButton;
    private ImageButton bankjesButton;
    MqttAndroidClient client;
    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topic = "Android/B1";
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

        //Setup the connection to MQTT
        connectToMQTT();

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
                    connectWriter();
                    connectReader();

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

    private void connectReader() {
        String topicTestReading = "Android/B1";
        try {
            Log.d("tag", "mqtt channel name>>>>>>>>" + topicTestReading);
            Log.d("tag", "client.isConnected()>>>>>>>>" + client.isConnected());
            if (client.isConnected()) {
                client.subscribe(topicTestReading, 0);
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        System.out.println("We received a message on the following topic:   " + topic);
                        String receivedmsg = new String(message.getPayload());
                        System.out.println("The message we received is:      " + receivedmsg);

                        if (receivedmsg.contains("Bench1")) {
                            System.out.println("Contains Bench1!");
                            if (receivedmsg.contains("Vrij")) {
                                Bankjes.setBench1Free();
                            }
                            if (receivedmsg.contains("Bezet")) {
                                Bankjes.setBench1Busy();
                            }
                        }

                        if (receivedmsg.contains("Bench2")) {
                            System.out.println("Contains Bench2");
                            if (receivedmsg.contains("Vrij")) {
                                Bankjes.setBench2Free();
                            }
                            if (receivedmsg.contains("Bezet")) {
                                Bankjes.setBench2Busy();
                            }
                        }

                        else {
                            System.out.println("Does not contain bench!");
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });
            }
        } catch (Exception e) {
            Log.d("tag", "Error :" + e);
        }
    }

    private void connectWriter() {
        try {
            int qos = 2; //Ieder bericht moet maar 1x binnenkomen
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Message message = new Message("Bot" , "This is a test message",null,new Date());
                    String topictestWriting = "Android/B1";
                    String payloadtest = "Reset";
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

    private void handleAttractiesButton() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
