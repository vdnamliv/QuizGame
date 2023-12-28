package com.example.final_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup answerRadioGroup;
    private Button submitButton;
    public class Question {

        private String question;
        private List<String> answers;
        private String correctAnswer;

        public Question(String question, List<String> answers, String correctAnswer) {
            this.question = question;
            this.answers = answers;
            this.correctAnswer = correctAnswer;
        }
        //Lấy câu hỏi :
        public String getQuestion() {
            return question;
        }
        //Lấy danh sách câu trả lời :
        public List<String> getAnswers() {
            return answers;
        }
        //lấy câu trả lời đúng :
        public String getCorrectAnswer() {
            return correctAnswer;
        }
    }
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        questionTextView = findViewById(R.id.questionTextView);
        answerRadioGroup = findViewById(R.id.answerRadioGroup);
        submitButton = findViewById(R.id.submitButton);

        // Lấy thông tin về chủ đề từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            String themeFileName = intent.getStringExtra("themeFileName");

            // Kiểm tra xem themeFileName có giá trị hay không
            if (themeFileName != null && !themeFileName.isEmpty()) {
                // Đọc dữ liệu từ file và tạo danh sách câu hỏi
                questions = readQuestionsFromFile(themeFileName);

                // Kiểm tra xem có câu hỏi nào không
                if (questions != null && !questions.isEmpty()) {
                    displayQuestion();

                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkAnswer();
                        }
                    });
                    return; // Kết thúc hàm để tránh tiếp tục thực hiện mã khi không có dữ liệu câu hỏi
                } else {
                    // Hiển thị thông báo nếu không có câu hỏi
                    Toast.makeText(this, "Không tìm thấy câu hỏi.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        // Nếu không có Intent hoặc không có dữ liệu hợp lệ, kết thúc Activity
        finish();
    }


    private void displayQuestion() {
        // Hiển thị câu hỏi và đáp án lên màn hình
        Question currentQuestion = questions.get(currentQuestionIndex);
        questionTextView.setText(currentQuestion.getQuestion());
        List<String> answers = currentQuestion.getAnswers();

        // Đảm bảo số lượng RadioButton trong RadioGroup đúng với số lượng câu trả lời
        int radioGroupChildCount = answerRadioGroup.getChildCount();
        for (int i = 0; i < radioGroupChildCount; i++) {
            RadioButton radioButton = (RadioButton) answerRadioGroup.getChildAt(i);
            if (i < answers.size()) {
                // Nếu câu trả lời có sẵn, hiển thị lên RadioButton
                radioButton.setText(answers.get(i));
                radioButton.setVisibility(View.VISIBLE);
            } else {
                // Nếu không có câu trả lời, ẩn RadioButton thừa đi
                radioButton.setVisibility(View.GONE);
            }
        }
    }

    //hàm này dùng để lưu trữ lại chủ đề hiện tại --> sử dụng nếu ấn "chơi lại"
    private void saveThemeFileName(String themeFileName) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("themeFileName", themeFileName);
        editor.apply();
    }

    private void checkAnswer() {
        int selectedRadioButtonId = answerRadioGroup.getCheckedRadioButtonId();

        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String userAnswer = selectedRadioButton.getText().toString();
            String correctAnswer = questions.get(currentQuestionIndex).getCorrectAnswer();

            if (userAnswer.equals(correctAnswer)) {
                // Người dùng trả lời đúng
                Toast.makeText(this, "Đúng!", Toast.LENGTH_SHORT).show();
                score++;

                // Chuyển sang câu hỏi tiếp theo nếu còn
                if (++currentQuestionIndex < questions.size()) {
                    displayQuestion();
                } else {
                    // Người dùng đã hoàn thành tất cả các câu hỏi
                    showResult(score);
                }
            } else {
                // Người dùng trả lời sai
                Toast.makeText(this, "Sai! Chuyển sang màn hình kết quả.", Toast.LENGTH_SHORT).show();
                showResult(score);
            }
        } else {
            // Người dùng chưa chọn đáp án
            Toast.makeText(this, "Hãy chọn một đáp án.", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Question> readQuestionsFromFile(String fileName) {
        List<Question> questionList = new ArrayList<>();
        try {
            // Đọc tên file từ Intent
            Intent intent = getIntent();

            // Kiểm tra chuỗi null hoặc trống rỗng
            if (fileName == null || fileName.isEmpty()) {
                Toast.makeText(this, "Tên file không hợp lệ", Toast.LENGTH_SHORT).show();
                finish();
                return questionList;
            }
            // Nếu chuỗi không null
            if (intent != null) {
                String themeFileName = intent.getStringExtra("themeFileName");
                if (themeFileName != null && !themeFileName.isEmpty()) {
                    fileName = themeFileName;
                }
            }

            // Sử dụng tên file để xây dựng ID tài nguyên
            int resourceId = getResources().getIdentifier(fileName, "raw", getPackageName());
            InputStream inputStream = getResources().openRawResource(resourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String currentQuestion = null;
            List<String> currentAnswers = new ArrayList<>();
            String correctAnswer = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Câu hỏi:")) {
                    // Bắt đầu một câu hỏi mới
                    if (currentQuestion != null) {
                        questionList.add(new Question(currentQuestion, currentAnswers, correctAnswer));
                    }
                    currentQuestion = line.substring("Câu hỏi:".length()).trim();
                    currentAnswers = new ArrayList<>();
                    correctAnswer = null;
                } else if (line.startsWith("Đáp án đúng:")) {
                    // Đáp án đúng
                    correctAnswer = line.substring("Đáp án đúng:".length()).trim();
                } else if (!line.trim().isEmpty()) {
                    // Các dòng còn lại là đáp án
                    currentAnswers.add(line.trim());
                }
            }

            // Thêm câu hỏi cuối cùng vào danh sách
            if (currentQuestion != null) {
                questionList.add(new Question(currentQuestion, currentAnswers, correctAnswer));
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tải câu hỏi", Toast.LENGTH_SHORT).show();
        }

        return questionList;
    }
    //lưu score --> tạo hàm showResult --> nhả hàm activivy show
    private void showResult(int score) {
        Intent intent = new Intent(GameActivity.this, ShowResultActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
        finish();
    }

    //Lưu số điểm cho màn hình số 2

}
