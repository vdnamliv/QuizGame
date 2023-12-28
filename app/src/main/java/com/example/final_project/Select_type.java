package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Select_type extends AppCompatActivity {

    private Button btnGeography, btnHistory, btnScience, btnArt, btnViewAllQuestions;
    private TextView textViewTotalScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);

        textViewTotalScore = findViewById(R.id.textViewTotalScore);
        Intent intent = getIntent();
        if (intent.hasExtra("score")){
            int score = intent.getIntExtra("score", 0);
            textViewTotalScore.setText("Total Score: " + score);
        }


        btnGeography = findViewById(R.id.btnGeography);
        btnHistory = findViewById(R.id.btnHistory);
        btnScience = findViewById(R.id.btnScience);
        btnArt = findViewById(R.id.btnArt);
        btnViewAllQuestions = findViewById(R.id.btnViewAllQuestions);
        //Switch switchDifficult = findViewById(R.id.switchDifficult);

        // Set sự kiện cho từng nút chủ đề :
        btnGeography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuestionAnswerActivity("geo");
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuestionAnswerActivity("his");
            }
        });

        btnScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuestionAnswerActivity("sci");
            }
        });

        btnArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuestionAnswerActivity("art");
            }
        });

        btnViewAllQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startQuestionListAcivity(); }
        });

    }
    private void startQuestionAnswerActivity (String themeFileName){
        //chuyển sang màn hình số 3
        Intent intent = new Intent(Select_type.this, GameActivity.class);
        //putExtra để đính kèm file vào Intent
        intent.putExtra("themeFileName", themeFileName);
        startActivity(intent);
    }


    private void startQuestionListAcivity () {
        Intent intent = new Intent(Select_type.this, QuestionListActivity.class);
        startActivity(intent);
    }
}


