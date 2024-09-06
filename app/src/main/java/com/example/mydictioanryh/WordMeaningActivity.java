package com.example.mydictioanryh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WordMeaningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);

        TextView textViewWord = findViewById(R.id.textView_word);
        TextView textViewWordMeaning = findViewById(R.id.textView_word_meaning);
        Button buttonReturn = findViewById(R.id.button_return);

        Intent intent = getIntent();
        String word = intent.getStringExtra("WORD");
        String meaning = intent.getStringExtra("MEANING");

        textViewWord.setText(word);
        textViewWordMeaning.setText(meaning);

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}