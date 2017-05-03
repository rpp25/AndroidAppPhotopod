package edu.pitt.cs.cs1635.vsc5.group_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etUsername = (EditText) findViewById(R.id.register_username);
        final EditText etPassword = (EditText) findViewById(R.id.register_pw);
        final EditText etConfirmPassword = (EditText) findViewById(R.id.register_pw_confirm);
        Button btnRegister = (Button)findViewById(R.id.register_button);
        TextView loginLink = (TextView) findViewById(R.id.register_back_to_login);

        // Register button
        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

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
                else if(!password.equals(etConfirmPassword.getText().toString())) {
                    etPassword.setError("Passwords must match!");
                    return;
                }

                //
                // Register request with database
                //
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String test = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                            System.out.println(test);
                            JSONObject jsonResponse = new JSONObject(test);
                            boolean success = jsonResponse.getBoolean("success");

                            // If successful, go back to login activity and show the toast
                            if(success) {
                                Intent registerIntent = new Intent(Register.this, MainActivity.class);
                                Register.this.startActivity(registerIntent);
                                Context context = getApplicationContext();
                                CharSequence text = "You have been registered!";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                builder.setMessage("Register failed!")
                                        .setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(registerRequest);
            }
        });

        // Back to login link
        loginLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent registerIntent = new Intent(Register.this, MainActivity.class);
                Register.this.startActivity(registerIntent);
            }
        });
    }
}
