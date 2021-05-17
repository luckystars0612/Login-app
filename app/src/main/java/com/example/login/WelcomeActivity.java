package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Intent myIntent = getIntent();
        String username = myIntent.getStringExtra("username");

        TextView welcomeTxt = (TextView) findViewById(R.id.welcomeTxt);

        welcomeTxt.setText("Welcome!, " + username);

    }
}