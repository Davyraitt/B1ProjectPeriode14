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

public class MainActivity extends AppCompatActivity {

    private ImageButton wachtrijenButton;
    private ImageButton jonkheerButton;
    private ImageButton cobraButton;
    private ImageButton bankjesButton;
    private Boolean booleanInJonkheer;
    private Boolean booleanUitJonkheer;
    private Boolean booleanInCobra;
    private Boolean booleanUitCobra;


    private int counterJonkheer;
    private int counterCobra;
    MqttAndroidClient client;
    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //standard boolean value is false
        booleanInCobra = false;
        booleanInJonkheer = false;
        booleanUitCobra = false;
        booleanUitJonkheer = false;


        //counters for the people entering and leaving the attraction
        counterJonkheer = 0;
        counterCobra = 0;

        //setting the onClicKListeners (what happens when the button is pressed)

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

        cobraButton = (ImageButton) findViewById(R.id.cobraButton);
        cobraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCobraButton();
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
        //Generate a randon client ID
        String clientId = MqttClient.generateClientId();

        //Server details
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://maxwell.bps-software.nl:1883", clientId);

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            //Username and password for the server
            options.setUserName("androidTI");
            options.setPassword("&FN+g$$Qhm7j".toCharArray());

            //Connecting..
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //When this method is called the connect is succesfull
                    connectWriter();
                    connectReader();
                    //Connected the writer and reader so we can write and read to the MQTT server

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //Onfailure say we failed
                    System.out.println("Connection failed!!");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    private void connectReader() {
        //Topics we write to...
        String topicBankje1 = "Android/B1/Bankje1";
        String topicBankje2 = "Android/B1/Bankje2";
        final String topicInJonkheer = "Android/B1/UltrasoonInJonkheer";
        final String topicUitJonkheer = "Android/B1/UltrasoonUitJonkheer";
        final String topicInCobra = "Android/B1/UltrasoonInCobra";
        final String topicUitCobra = "Android/B1/UltrasoonUitCobra";


        try {
            Log.d("tag", "client.isConnected()>>>>>>>>" + client.isConnected());
            if (client.isConnected()) {
                //If we succesfully connected we want to follow the following topics
                client.subscribe(topicBankje1, 0);
                client.subscribe(topicBankje2, 0);
                client.subscribe(topicInJonkheer, 0);
                client.subscribe(topicUitJonkheer, 0);
                client.subscribe(topicInCobra, 0);
                client.subscribe(topicUitCobra, 0);

                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        String receivedmsg = new String(message.getPayload());
                        //Above is the message we received...

                        //Based on the text and topic we received, we should update certain values


                        //If the topic equals a certain attraction, we should update the counter of that attraction
                        if (topic.equals(topicInJonkheer)) {
                            if (receivedmsg.contains("ja") && !booleanInJonkheer) {
                                counterJonkheer++;
                                System.out.println("er is iemand voor de ultrasoon!!!!");
                                booleanInJonkheer = true;
                                System.out.println("Counter voor Jonkheer is nu:  " + counterJonkheer + " personen");
                            }

                            if (receivedmsg.contains("nee")) {
                                booleanInJonkheer = false;
                            }

                            schermWachtrijen.berekenWachtrijen(counterCobra, counterJonkheer);
                        }


                        //If the topic equals a certain attraction, we should update the counter of that attraction
                        if (topic.equals(topicUitJonkheer)) {
                            if (receivedmsg.contains("ja") && !booleanUitJonkheer) {
                                if (counterJonkheer > 0) {
                                    counterJonkheer--;
                                }
                                System.out.println("er is iemand voor de ultrasoon!!!!");
                                booleanUitJonkheer = true;
                                System.out.println("Counter voor Jonkheer is nu:  " + counterJonkheer + " personen");
                            }

                            if (receivedmsg.contains("nee")) {
                                booleanUitJonkheer = false;
                            }

                            schermWachtrijen.berekenWachtrijen(counterCobra, counterJonkheer);
                        }


                        //If the topic equals a certain attraction, we should update the counter of that attraction
                        if (topic.equals(topicInCobra)) {
                            if (receivedmsg.contains("ja") && !booleanInCobra) {
                                counterCobra++;
                                System.out.println("er is iemand voor de ultrasoon!!!!");
                                booleanInCobra = true;
                                System.out.println("Counter voor Jonkheer is nu:  " + counterCobra + " personen");
                            }

                            if (receivedmsg.contains("nee")) {
                                booleanInCobra = false;
                            }

                            schermWachtrijen.berekenWachtrijen(counterCobra, counterJonkheer);
                        }


                        //If the topic equals a certain attraction, we should update the counter of that attraction

                        if (topic.equals(topicUitCobra)) {
                            if (receivedmsg.contains("ja") && !booleanUitCobra) {
                                if (counterCobra > 0) {
                                    counterCobra--;
                                }

                                System.out.println("er is iemand voor de ultrasoon!!!!");
                                booleanUitCobra = true;
                                System.out.println("Counter voor Jonkheer is nu:  " + counterCobra + " personen");
                            }

                            if (receivedmsg.contains("nee")) {
                                booleanUitCobra = false;
                            }

                            schermWachtrijen.berekenWachtrijen(counterCobra, counterJonkheer);
                        }

                        //When a pressure sensor for a bench is called, we want to update the icon of the bench
                        if (receivedmsg.contains("Bench1")) {

                            if (receivedmsg.contains("Vrij")) {
                                schermBankjes.setBench1Free();
                            }
                            if (receivedmsg.contains("Bezet")) {
                                schermBankjes.setBench1Busy();
                            }
                        }

                        //When a pressure sensor for a bench is called, we want to update the icon of the bench
                        if (receivedmsg.contains("Bench2")) {

                            if (receivedmsg.contains("Vrij")) {
                                schermBankjes.setBench2Free();
                            }
                            if (receivedmsg.contains("Bezet")) {
                                schermBankjes.setBench2Busy();
                            }
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
            int qos = 2; //Each message can only arrive once
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


    private void handleCobraButton() {
        cobraButton.setSelected(!cobraButton.isPressed());


        if (cobraButton.isPressed()) {
            cobraButton.setImageResource(R.drawable.cobrabuttonpressed);

        }

        Intent intent = new Intent(this, schermCobra.class);
        startActivity(intent);
    }

    private void handleBankjesButton() {
        bankjesButton.setSelected(!bankjesButton.isPressed());


        if (bankjesButton.isPressed()) {
            bankjesButton.setImageResource(R.drawable.buttonbankjespressed);

        }

        Intent intent = new Intent(this, schermBankjes.class);
        startActivity(intent);
    }


    private void handleJonkheerButton() {
        jonkheerButton.setSelected(!jonkheerButton.isPressed());


        if (jonkheerButton.isPressed()) {
            jonkheerButton.setImageResource(R.drawable.jonkheerbuttonpressed);

        }

        Intent intent = new Intent(this, schermJonkheer.class);
        startActivity(intent);

    }

    private void handleButtonWachtrijen() {
        wachtrijenButton.setSelected(!wachtrijenButton.isPressed());

        if (wachtrijenButton.isPressed()) {
            wachtrijenButton.setImageResource(R.drawable.buttonwachtrijenpressed);

        }

        Intent intent = new Intent(this, schermWachtrijen.class);
        startActivity(intent);
    }


}
