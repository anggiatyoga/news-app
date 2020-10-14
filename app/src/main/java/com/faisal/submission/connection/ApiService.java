package com.faisal.submission.connection;

import com.faisal.submission.Utils;
import com.faisal.submission.model.News;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("top-headlines?country=id&apiKey="+Utils.API_KEY)
    Call<News> getTopHeadlines();
}
