package com.social7.noteapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {
    // We are using localhost/127.0.0.1 and adb reverse tcp:8000 tcp:8000
    // so the physical phone can bypass Windows Firewall issues.
    private static final String BASE_URL = "http://127.0.0.1:8000/api/";
    private static Retrofit retrofit;

    private RetrofitClient() {}

    public static ApiService getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
