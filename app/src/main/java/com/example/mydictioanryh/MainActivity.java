package com.example.mydictioanryh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mydictioanryh.api.DictionaryApiService;
import com.example.mydictioanryh.model.DictionaryApiResponse;
import com.example.mydictioanryh.network.RetrofitClientManager;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private ListView listViewHistory;
    private ArrayAdapter<String> historyAdapter;
    private ArrayList<String> searchHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextSearch = findViewById(R.id.editText_search);
        listViewHistory = findViewById(R.id.listView_history);
        Button buttonSearch = findViewById(R.id.button_search);
        Button buttonClearHistory = findViewById(R.id.button_clear_history);

        searchHistory = new ArrayList<>();
        historyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchHistory);
        listViewHistory.setAdapter(historyAdapter);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editTextSearch.getText().toString().trim();
                if (!word.isEmpty()) {
                    Log.d("MainActivity", "Searching for word: " + word);
                    searchWord(word);
                }
            }
        });

        buttonClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHistory();
            }
        });

        listViewHistory.setOnItemClickListener((parent, view, position, id) -> {
            String word = searchHistory.get(position);
            searchWord(word);
        });
    }

    private void searchWord(String word) {
        DictionaryApiService apiService = RetrofitClientManager.getRetrofitInstance().create(DictionaryApiService.class);
        Call<List<DictionaryApiResponse>> call = apiService.getWordMeaning(word);

        call.enqueue(new Callback<List<DictionaryApiResponse>>() {
            @Override
            public void onResponse(Call<List<DictionaryApiResponse>> call, Response<List<DictionaryApiResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    DictionaryApiResponse dictionaryApiResponse = response.body().get(0);
                    String meaning = extractMeaning(dictionaryApiResponse);
                    searchHistory.add(word);
                    historyAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(MainActivity.this, WordMeaningActivity.class);
                    intent.putExtra("WORD", word);
                    intent.putExtra("MEANING", meaning);
                    startActivity(intent);
                } else {
                    Log.e("MainActivity", "Response unsuccessful or body is null/empty");
                    Toast.makeText(MainActivity.this, "No meaning found for the word: " + word, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DictionaryApiResponse>> call, Throwable t) {
                Log.e("MainActivity", "Error fetching word meaning", t);
                Toast.makeText(MainActivity.this, "Failed to fetch meaning", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void clearHistory() {
        searchHistory.clear();
        historyAdapter.notifyDataSetChanged();
    }

    private String extractMeaning(DictionaryApiResponse response) {
        StringBuilder meanings = new StringBuilder();
        if (response != null && response.getMeanings() != null) {
            for (DictionaryApiResponse.Meaning meaning : response.getMeanings()) {
                if (meaning != null && meaning.getPartOfSpeech() != null && meaning.getDefinitions() != null) {
                    meanings.append(meaning.getPartOfSpeech()).append(": ");
                    for (DictionaryApiResponse.Meaning.Definition definition : meaning.getDefinitions()) {
                        if (definition != null && definition.getDefinition() != null) {
                            meanings.append(definition.getDefinition()).append("; ");
                        }
                    }
                }
            }
        }
        return meanings.toString();
    }
}
