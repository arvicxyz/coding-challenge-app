package com.codingchallenge.app.services.api;

import android.text.TextUtils;

import com.codingchallenge.app.utils.AppSettings;
import com.codingchallenge.app.utils.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static OkHttpClient client;

    private String baseUrl;
    private String controller;

    protected String getBaseUrl() {
        return baseUrl;
    }

    protected void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected String getController() {
        return controller;
    }

    protected void setController(String controller) {
        this.controller = controller;
    }

    public ApiClient() {

        baseUrl = BuildConfig.getApiScheme() + "://" + BuildConfig.getApiHost();
        if (!TextUtils.isEmpty(BuildConfig.getApiPort()))
            baseUrl += ":" + BuildConfig.getApiPort();
        if (!TextUtils.isEmpty(BuildConfig.getApiPort()))
            baseUrl += "/" + BuildConfig.getApiRoot();
        baseUrl += "/";

        client = new OkHttpClient.Builder()
                .connectTimeout(AppSettings.CLIENT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(AppSettings.CLIENT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(AppSettings.CLIENT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    protected String executeGet(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            } else {
                throw new NullPointerException();
            }
        }
    }

    protected String executePost(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            } else {
                throw new NullPointerException();
            }
        }
    }
}
