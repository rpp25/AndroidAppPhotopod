package edu.pitt.cs.cs1635.vsc5.group_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText username = (EditText) findViewById(R.id.login_username);
        EditText password = (EditText) findViewById(R.id.login_pw);
        Button btnLogin = (Button)findViewById(R.id.login_button);
        TextView registerLink = (TextView) findViewById(R.id.tv_RegisterHere);

        registerLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent registerIntent = new Intent(MainActivity.this, Register.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });
    }

    // When the login button is pressed
    public void toMain(View view) {
        EditText etUsername = (EditText) findViewById(R.id.login_username);
        EditText etPassword = (EditText) findViewById(R.id.login_pw);

        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        //
        // Error Checking
        //
        if (etUsername.getText().toString().trim().equals("")) {
            etUsername.setError("Username is required!");
            return;
        } else if (etPassword.getText().toString().trim().equals("")) {
            etPassword.setError("Password is required!");
            return;
        }

        //
        // Login request to database
        //
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success) {
                        Intent intent = new Intent(MainActivity.this, Tabs.class);
                        intent.putExtra("username", username);
                        MainActivity.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Login failed!")
                                .setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(loginRequest);
    }
}
