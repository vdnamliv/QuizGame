package com.example.final_project;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionListActivity extends AppCompatActivity {

    private ListView questionListView;
    private List<String> allQuestions;
    private ArrayAdapter<String> adapter;
    private List<String> all;
    private View textViewTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        // Thanh toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewTopic = findViewById(R.id.textViewTopic);
        questionListView = findViewById(R.id.questionListView);
        allQuestions = loadAllQuestions();
        //simple_list_item_1 : là layout có sẵn mà ListView sẽ hiển thị
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allQuestions);
        questionListView.setAdapter(adapter);


        //Ấn hiển thị chi tiết từng câu hỏi
        questionListView.setOnItemClickListener((parent, view, position, id) -> {

            all = loadAll();

            // Lấy câu hỏi và đáp án từ danh sách đã load
            String selectedQuestionWithAnswer = all.get(position);

            // Tách câu hỏi và đáp án
            String[] parts = selectedQuestionWithAnswer.split("\\n\\n");

            // Lấy câu hỏi
            String question = parts[0].replace("Câu hỏi: ", "");

            // Lấy đáp án đúng
            String[] answerLines = parts[1].split("\n");
            String correctAnswer = answerLines[answerLines.length - 1].trim();

            // Tách các đáp án a, b, c, d
            String answerOptions = parts[1].replaceAll("[a-d]\\) ", "");
            Log.d("QuestionListActivity", "6: " + answerOptions);

            // Tách các đáp án a, b, c, d
            String[] answerArray = answerOptions.split("\n");
            List<String> answerList = Arrays.asList(answerArray);

            if (answerList.size() >= 4) {
                // Chuyển sang màn hình chi tiết (Màn hình số 6) với thông tin câu hỏi và câu trả lời
                Intent intent = new Intent(QuestionListActivity.this, QuestionDetailActivity.class);
                intent.putExtra("question", question);
                intent.putExtra("correctAnswer", correctAnswer);
                intent.putExtra("optionA", answerList.get(0).trim());
                intent.putExtra("optionB", answerList.get(1).trim());
                intent.putExtra("optionC", answerList.get(2).trim());
                intent.putExtra("optionD", answerList.get(3).trim());

                startActivity(intent);
            }
        });




    }

    //chỉ đọc câu hỏi :
    private List<String> loadAllQuestions() {
        List<String> questions = new ArrayList<>();
        Resources resources = getResources();

        // Lấy danh sách tên tệp tin trong thư mục raw
        String[] questionFiles = {"geo", "his", "sci", "art"};

        for (String fileName : questionFiles) {
            // Lấy ID tài nguyên của tệp tin trong thư mục raw
            int resourceId = resources.getIdentifier(fileName, "raw", getPackageName());

            // Đọc nội dung từ tệp tin và thêm vào danh sách câu hỏi
            List<String> fileQuestions = readQuestionsFromRaw(resourceId);
            questions.addAll(fileQuestions);
        }

        return questions;
    }

    private List<String> readQuestionsFromRaw(int resourceId) {
        List<String> questions = new ArrayList<>();

        // Đọc nội dung từ tệp tin trong thư mục raw
        InputStream inputStream = getResources().openRawResource(resourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            StringBuilder questionBuilder = new StringBuilder();
            String line;

            // Đọc từng dòng từ tệp và thêm vào danh sách câu hỏi
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Câu hỏi: ")) {
                    // Bắt đầu câu hỏi mới
                    questionBuilder = new StringBuilder();
                    questionBuilder.append(line.substring("Câu hỏi: ".length())).append("\n");
                }
                if (line.startsWith("Đáp án đúng: ")) {
                    // Kết thúc câu hỏi, thêm vào danh sách
                    questions.add(questionBuilder.toString().trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return questions;
    }


    //đọc toàn bộ câu hỏi và câu trả lời :
    private List<String> loadAll() {
        List<String> all = new ArrayList<>();
        Resources resources = getResources();

        // Lấy danh sách tên tệp tin trong thư mục raw
        String[] questionFiles = {"geo", "his", "sci", "art"};

        for (String fileName : questionFiles) {
            // Lấy ID tài nguyên của tệp tin trong thư mục raw
            int resourceId = resources.getIdentifier(fileName, "raw", getPackageName());

            // Đọc nội dung từ tệp tin và thêm vào danh sách câu hỏi
            List<String> fileQuestions = readAllFromRaw(resourceId);
            all.addAll(fileQuestions);
        }

        return all;
    }

    private List<String> readAllFromRaw(int resourceId) {
        List<String> questions = new ArrayList<>();

        // Đọc nội dung từ tệp tin trong thư mục raw
        InputStream inputStream = getResources().openRawResource(resourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            StringBuilder questionBuilder = new StringBuilder();
            String line;

            // Đọc từng dòng từ tệp và thêm vào danh sách câu hỏi
            while ((line = reader.readLine()) != null) {
                // Kiểm tra xem dòng có bắt đầu bằng "Câu hỏi:" không
                if (line.startsWith("Câu hỏi:")) {
                    // Nếu có, thêm câu hỏi cũ vào danh sách nếu có
                    if (questionBuilder.length() > 0) {
                        questions.add(questionBuilder.toString().trim());
                    }
                    // Bắt đầu câu hỏi mới
                    questionBuilder = new StringBuilder();
                }

                // Kiểm tra xem dòng có chứa "Đáp án đúng:" không
                if (line.startsWith("Đáp án đúng:")) {
                    // Không thêm dòng này vào câu hỏi, chỉ tách answerOptions
                    String answerOptions = line.replace("Đáp án đúng:", "").trim();
                    questionBuilder.append(answerOptions).append("\n");
                } else {
                    // Thêm dòng vào câu hỏi
                    questionBuilder.append(line).append("\n");
                }
            }

            // Thêm câu hỏi cuối cùng vào danh sách
            if (questionBuilder.length() > 0) {
                questions.add(questionBuilder.toString().trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return questions;
    }









    //thanh search chưa làm được

}
