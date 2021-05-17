package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    SQLiteConnector db = new SQLiteConnector(this);

    private boolean register (String email, String username, String password) {
        TextView infoTxt = (TextView)findViewById(R.id.infoTxt2);
        if (email.length() == 0 || username.length() == 0 || password.length() == 0) {
            infoTxt.setText("Please fill in all fields!");
            return false;
        }

        try {
            if (db.checkUser(email)) {
                infoTxt.setText("Email has already registered!");
                return false;
            }
            if (db.checkUser(username)) {
                infoTxt.setText("Username has already registered!");
                return false;
            }
            User user = new User();
            user.setName(username);
            user.setPassword(password);
            user.setEmail(email);
            db.addUser(user);
        } catch(Exception e) {
            infoTxt.setText("An unexpected error! Try again");
            return false;
        }

        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerBtn = (Button)findViewById(R.id.registerBtn);
        EditText emailTxt = (EditText)findViewById(R.id.emailTxt);
        EditText usernameTxt = (EditText)findViewById(R.id.usernameTxt);
        EditText passwordTxt = (EditText)findViewById(R.id.passwordTxt);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                String email = emailTxt.getText().toString();
                if (register(email, username, password)) {
                    Intent registerIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    registerIntent.putExtra("username", username);
                    registerIntent.putExtra("password", password);
                    startActivity(registerIntent);
                }
            }
        });
    }
}