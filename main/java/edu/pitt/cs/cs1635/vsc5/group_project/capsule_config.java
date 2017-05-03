package edu.pitt.cs.cs1635.vsc5.group_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class capsule_config extends AppCompatActivity {

    private String username;
    private String capsule_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            capsule_name = extras.getString("capsule_name");
        }
        setContentView(R.layout.activity_capsule_config);
    }

    public void continueShare(View view) {
        EditText days = (EditText) findViewById(R.id.days);
        EditText hours = (EditText) findViewById(R.id.hours);

        EditText question = (EditText) findViewById(R.id.edit_question);
        EditText correct = (EditText) findViewById(R.id.edit_correct);
        EditText incorrect1 = (EditText) findViewById(R.id.edit_incorrect1);
        EditText incorrect2 = (EditText) findViewById(R.id.edit_incorrect2);
        EditText incorrect3 = (EditText) findViewById(R.id.edit_incorrect3);

        //
        // Error Checking
        //
        if (days.getText().toString().trim().equals("")) {
            days.setError("Number of days required!");
            return;
        } else if (hours.getText().toString().trim().equals("")) {
            hours.setError("Number of hours is required!");
            return;
        } else if (question.getText().toString().trim().equals("")) {
            question.setError("A question is required!");
            return;
        } else if (correct.getText().toString().trim().equals("")) {
            correct.setError("A correct answer is required!");
            return;
        } else if (incorrect1.getText().toString().trim().equals("")) {
            incorrect1.setError("An incorrect choice is required!");
            return;
        } else if (incorrect2.getText().toString().trim().equals("")) {
            incorrect2.setError("An incorrect choice is required!");
            return;
        } else if (incorrect3.getText().toString().trim().equals("")) {
            incorrect3.setError("An incorrect choice is required!");
            return;
        }

        int numDays = Integer.parseInt(days.getText().toString());
        int numHours = Integer.parseInt(hours.getText().toString());
        int totalTime = (numDays * 24) + numHours;

        Intent intent = new Intent(this, SelectFriends.class);
        intent.putExtra("username", username);
        intent.putExtra("capsule_name", capsule_name);
        intent.putExtra("time_to_open", totalTime);
        intent.putExtra("question", question.getText().toString());
        intent.putExtra("correct", correct.getText().toString());
        intent.putExtra("incorrect1", incorrect1.getText().toString());
        intent.putExtra("incorrect2", incorrect2.getText().toString());
        intent.putExtra("incorrect3", incorrect3.getText().toString());
        startActivity(intent);
    }

    public void capsuleHome(View view) {
        Intent intent = new Intent(this, Tabs.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
