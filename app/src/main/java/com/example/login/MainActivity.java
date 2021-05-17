package com.example.login;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.TextView;

import com.example.login.SQLiteConnector;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {
    private EditText usernameTxt;
    private EditText passwordTxt;
    private Button loginBtn;
    private Button registerBtn;

    SQLiteConnector db = new SQLiteConnector(this);

    private boolean authenticate(String username, String password) {
        TextView infoTxt = (TextView)findViewById(R.id.infoTxt);

        if (username.length() == 0 || password.length() == 0) {
            infoTxt.setText("Please fill in all fields!");
            return false;
        }
        try {
            if (db.checkUser(username, password) == false) {
                infoTxt.setText("Username or password was wrong!");
                return false;
            }
        } catch (Exception e) {
            infoTxt.setText("An unexpected error! Try again");
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameTxt = (EditText)findViewById(R.id.usernameTxt);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        registerBtn = (Button)findViewById(R.id.registerBtn);

        Intent myIntent = getIntent();
        String username = myIntent.getStringExtra("username");
        String password = myIntent.getStringExtra("password");
        usernameTxt.setText(username);
        passwordTxt.setText(password);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                if (authenticate(username, password)) {
                    Intent welcome = new Intent(MainActivity.this, WelcomeActivity.class);
                    welcome.putExtra("username", username);
                    startActivity(welcome);
                }
            }
        });
    }
}