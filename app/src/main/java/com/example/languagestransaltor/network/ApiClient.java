package com.example.languagestransaltor.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
// I made this class to connect microsoft_azure_translate service API and get gson response by requirements that key, translate URL,region
    private static String BASE_URL = "https://api.cognitive.microsofttranslator.com/";
    public static String key = "0d3b151910e84396ad865e6ed28ce2c9";
    public static String region = "eastasia";
    public static String version = "3.0";

    public static Retrofit getRetrofit() {
        return new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
