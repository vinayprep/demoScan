package com.example.demoscan;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class RestClient {
    private static Api REST_CLIENT;
    private static String ROOT = "http://dev.oraclemaven.com/api/";

    static {
        setupRestClient();
    }

    private RestClient() {
    }

    public static Api post() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        //to enable log
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(3);
        httpClient.dispatcher(dispatcher);
        httpClient.connectTimeout(180, TimeUnit.SECONDS);
        httpClient.readTimeout(180, TimeUnit.SECONDS);
        httpClient.writeTimeout(180, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        // end

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT)
                .addConverterFactory(CustomConverter.create())
                .client(httpClient.build())
                .build();

        REST_CLIENT = retrofit.create(Api.class);
    }
}
