package edu.pitt.cs.cs1635.vsc5.group_project;

import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Unlock extends AppCompatActivity {

    private final String image_titles[] = {
            "Img1",
            "Img2",
            "Img3",
            "Img4"
    };

    private final Integer image_ids[] = {
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3,
        R.drawable.img4
    };

    String username;
    String capsuleOwner;
    String capsuleName;

    String question;
    String answer;
    String choice1;
    String choice2;
    String choice3;

    int answerIndex;
    int c1Index;
    int c2Index;
    int c3Index;

    boolean correctAnswerSelected = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            capsuleOwner = extras.getString("capsuleOwner");
            capsuleName = extras.getString("capsuleName");

            question = extras.getString("question");
            answer = extras.getString("answer");
            choice1 = extras.getString("choice1");
            choice2 = extras.getString("choice2");
            choice3 = extras.getString("choice3");
        }

        TextView tvQuestion = (TextView)findViewById(R.id.question);
        final RadioButton btnChoice1 = (RadioButton)findViewById(R.id.choice1);
        RadioButton btnChoice2 = (RadioButton)findViewById(R.id.choice2);
        RadioButton btnChoice3 = (RadioButton)findViewById(R.id.choice3);
        RadioButton btnChoice4 = (RadioButton)findViewById(R.id.choice4);

        tvQuestion.setText(question);
        answerIndex = -1;
        c1Index = -1;
        c2Index = -1;
        c3Index = -1;

        answerIndex = ThreadLocalRandom.current().nextInt(0, 3 + 1);

        do {
            c1Index = ThreadLocalRandom.current().nextInt(0, 3 + 1);
        } while(c1Index == answerIndex);

        do {
            c2Index = ThreadLocalRandom.current().nextInt(0, 3 + 1);
        } while(c2Index == answerIndex || c2Index == c1Index);

        do {
            c3Index = ThreadLocalRandom.current().nextInt(0, 3 + 1);
        } while(c3Index == answerIndex || c3Index == c1Index || c3Index == c2Index);

        String[] choices = new String[4];
        choices[answerIndex] = answer;
        choices[c1Index] = choice1;
        choices[c2Index] = choice2;
        choices[c3Index] = choice3;

        btnChoice1.setText(choices[0]);
        btnChoice2.setText(choices[1]);
        btnChoice3.setText(choices[2]);
        btnChoice4.setText(choices[3]);

        RadioGroup group = (RadioGroup) findViewById(R.id.choiceGroup);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                correctAnswerSelected = false;
                if(answerIndex == 0 && checkedId == R.id.choice1) {
                    correctAnswerSelected = true;
                } else if(answerIndex == 1 && checkedId == R.id.choice2) {
                    correctAnswerSelected = true;
                } else if(answerIndex == 2 && checkedId == R.id.choice3) {
                    correctAnswerSelected = true;
                } else if(answerIndex == 3 && checkedId == R.id.choice4) {
                    correctAnswerSelected = true;
                }
            }
        });

        Button nextBtn = (Button)findViewById(R.id.next);
        nextBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(correctAnswerSelected) {
                    Intent intent = new Intent(Unlock.this, Viewing.class);
                    intent.putExtra("capsuleOwner", capsuleOwner);
                    intent.putExtra("capsuleName", capsuleName);
                    Unlock.this.startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Unlock.this);
                    builder.setMessage("Wrong Answer! Try again")
                            .setNegativeButton("Retry", null).create().show();
                }
            }
        });
    }
}