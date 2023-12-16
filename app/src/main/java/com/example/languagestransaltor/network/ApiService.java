package com.example.languagestransaltor.network;

import com.example.languagestransaltor.models.TranslateData;
import com.example.languagestransaltor.models.WordData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    // I made this interface to connect microsoft_azure_translate service and get correct response by requirements
    @POST("translate")
    Call<List<TranslateData>> getTranslate(@Header("Ocp-Apim-Subscription-Key") String key,
                                           @Header("Ocp-Apim-Subscription-Region") String region,
                                           @Query("api-version") String version,
                                           @Query("from") String from,
                                           @Query("to") String to,
                                           @Body List<WordData> wordDataList);

}
