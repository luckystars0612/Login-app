package com.example.login;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.MD5_C;
import com.example.login.SQLiteConnector;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {
    private EditText usernameTxt;
    private EditText passwordTxt;
    private Button loginBtn;
    private Button registerBtn;

    //SQLiteConnector db = new SQLiteConnector(this);

    private boolean authenticate(String username, String password) throws UnsupportedEncodingException {
        TextView infoTxt = (TextView)findViewById(R.id.infoTxt);

        String username_en= URLEncoder.encode(username,"UTF8");
        MD5_C md5_= new MD5_C();
        String hashpass=md5_.getMd5(password);
        String password_en= URLEncoder.encode(hashpass,"UTF8");

        if (username.length() == 0 || password.length() == 0) {
            infoTxt.setText("Please fill in all fields!");
            return false;
        }
        try {
            String home="http://10.0.154.190:8080/api/login.php";
            String url=home+ "?user_name="+username_en+"&user_pass="+password_en;
            Log.e("url",url);
            // Request a string response from the provided URL.
            RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
            StringRequest stringRequest= new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getInt("login_success")==1){
                                    Intent welcome = new Intent(MainActivity.this, WelcomeActivity.class);
                                    welcome.putExtra("username", username);
                                    startActivity(welcome);
                                }
                                else{
                                    infoTxt.setText("Username or password is wrong!!");
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error register" , "Error");
                        }
                    }
            );
            queue.add(stringRequest);
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
                try {
                    if (authenticate(username, password)) {
                        Log.e("login","successfull login!");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}