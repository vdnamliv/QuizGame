package com.example.final_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowResultActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private Button completeButton, replayButton, shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        // Nhận điểm từ Intent
        int score = getIntent().getIntExtra("score", 0);

        // Hiển thị điểm trong TextView
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("" + score);

        // Gán sự kiện cho các button
        Button completeButton = findViewById(R.id.completeButton);
        Button replayButton = findViewById(R.id.replayButton);
        Button shareButton = findViewById(R.id.shareButton);

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển về Màn hình 2
                showResultfor2(score);
            }
        });


        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy chủ đề hiện tại từ SharedPreferences của GameActivity
                SharedPreferences preferences = getSharedPreferences("preference_name", MODE_PRIVATE);
                String currentThemeFileName = preferences.getString("themeFileName", "");

                Intent intent = new Intent(ShowResultActivity.this, GameActivity.class);

                intent.putExtra("themeFileName", currentThemeFileName);

                startActivity(intent);

                finish();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cho phép người chơi chia sẻ điểm số qua ứng dụng tin nhắn khác
                String shareMessage = "Total Score: " + score;

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

                // Sử dụng Intent.createChooser để hiển thị danh sách các ứng dụng có thể được sử dụng để chia sẻ
                startActivity(Intent.createChooser(shareIntent, "Chia sẻ điểm số qua..."));
            }
        });


    }
    private void showResultfor2(int score) {
        Intent intent = new Intent(ShowResultActivity.this, Select_type.class);
        intent.putExtra("score", score);
        startActivity(intent);
        finish();
    }
}
