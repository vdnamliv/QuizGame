package com.example.final_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        // Nhận điểm số từ Intent
        int score = getIntent().getIntExtra("score", 0);

        // Hiển thị điểm số trong TextView
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
                finish();
            }
        });

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chơi lại với cùng chủ đề --> chưa làm được

                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
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
                // Thực hiện chia sẻ thành tích (sử dụng Intent với ACTION_SEND)
                // Điều này sẽ cho phép người chơi chia sẻ thông điệp với điểm số qua ứng dụng tin nhắn khác
                String shareMessage =  "" + score;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                //startActivity(Intent.createChooser(shareIntent, "Chia sẻ điểm số qua..."));
            }
        });
    }
    private void showResultfor2(int score) {
        Intent intent = new Intent(ShowResultActivity.this, Select_type.class);
        intent.putExtra("score", score);
        startActivity(intent);
        //finish();
    }
}
