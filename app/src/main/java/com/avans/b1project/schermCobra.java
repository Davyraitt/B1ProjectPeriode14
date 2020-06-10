package com.avans.b1project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

public class schermCobra extends AppCompatActivity {

    private ImageButton backButton;
    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobra_attractie);

        //Creating all the buttons
        backButton = (ImageButton) findViewById(R.id.backButton);

        //Creating the videoview
        videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.acthbaan2;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);


        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);



        // attaching clicklisteners to the buttons
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButton();
            }
        });
    }

    private void handleBackButton() {
        System.out.println("Clicked!");
        backButton.setSelected(!backButton.isPressed());

        if (backButton.isPressed()) {
            backButton.setImageResource(R.drawable.buttonbackpressed);

        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
