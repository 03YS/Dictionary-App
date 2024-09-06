package com.example.mydictioanryh.api;

import com.example.mydictioanryh.model.DictionaryApiResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApiService {
    @GET("en/{word}")
    Call<List<DictionaryApiResponse>> getWordMeaning(@Path("word") String word);
}
