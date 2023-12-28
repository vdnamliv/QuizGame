package com.example.final_project;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuestionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        Log.d("QuestionDetailActivity", "onCreate started");

        // Nhận dữ liệu từ intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.d("QuestionDetailActivity", "Received intent extras");
            String question = extras.getString("question");
            String optionA = extras.getString("optionA");
            String optionB = extras.getString("optionB");
            String optionC = extras.getString("optionC");
            String optionD = extras.getString("optionD");
            String correctAnswer = extras.getString("correctAnswer");


            // Display the question in the TextView
            TextView questionTextView = findViewById(R.id.questionTextView);
            questionTextView.setText(question);

            // Display answer options in the TextViews
            TextView optionATextView = findViewById(R.id.optionATextView);
            optionATextView.setText(" " + optionA);

            TextView optionBTextView = findViewById(R.id.optionBTextView);
            optionBTextView.setText(" " + optionB);

            TextView optionCTextView = findViewById(R.id.optionCTextView);
            optionCTextView.setText(" " + optionC);

            TextView optionDTextView = findViewById(R.id.optionDTextView);
            optionDTextView.setText(" " + optionD);

            // Display the correct answer
            TextView correctAnswerTextView = findViewById(R.id.correctAnswerTextView);
            correctAnswerTextView.setText("Đáp án đúng: " + correctAnswer);
        }
    }
}
