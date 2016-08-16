package com.example.iryna.retrofittest;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.Call;

/**
 * Created by iryna on 16.08.16.
 */
public interface StackOverflowAPI {

    @GET("/2.2/questions?order=desc&sort=creation&site=stackoverflow")
    Call<StackOverflowQuestions> loadQuestions(@Query("tagged") String tags);

}
