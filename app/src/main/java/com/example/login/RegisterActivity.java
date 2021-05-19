package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RegisterActivity extends AppCompatActivity {

    private boolean register(String email, String username, String password) throws UnsupportedEncodingException {
        String email_en= URLEncoder.encode(email,"UTF8");
        String username_en= URLEncoder.encode(username,"UTF8");
        MD5_C md5_= new MD5_C();
        String hashpass=md5_.getMd5(password);
        String password_en= URLEncoder.encode(hashpass,"UTF8");
        TextView infoTxt = (TextView)findViewById(R.id.infoTxt2);
        if (email.length() == 0 || username.length() == 0 || password.length() == 0) {
            infoTxt.setText("Please fill in all fields!");
            return false;
        }
        try {
            String home="http://10.0.154.190:8080/api/register.php";
            String url=home+ "?user_name="+username_en+"&user_email="+email_en+"&user_pass="+password_en;
            Log.e("url",url);
            // Request a string response from the provided URL.
            RequestQueue queue= Volley.newRequestQueue(RegisterActivity.this);
            StringRequest stringRequest= new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getInt("check_email")==1){
                                    infoTxt.setText(" Email has already registered!");
                                }
                                if(jsonObject.getInt("check_name")==1){
                                    infoTxt.setText(" Username has already registered!");
                                }
                                if(jsonObject.getInt("success")==1)
                                {
                                    Intent registerIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                    registerIntent.putExtra("username", username);
                                    registerIntent.putExtra("password", password);
                                    startActivity(registerIntent);
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
                try {
                    if (register(email, username, password)) {
                        Log.e("register","register successfuly");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}