package com.pecherey.alexey.intechtest.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pecherey.alexey.intechtest.logic.Melodies;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Алексей on 16.01.2016.
 */
public class RetrofitLogic {
    private final static String LOG_TAG = RetrofitLogic.class.getSimpleName();

    private Retrofit mRestAdapter;
    private ServiceApi mDownLoadService;
    private Call<Melodies> mAsynkCall;
    private Callback<Melodies> mAsynkCallback;

    public RetrofitLogic(Callback<Melodies> callback) {
        Log.d(LOG_TAG, "init retrofit");
        this.mAsynkCallback = callback;
        initRetrofit();
    }

    public void startGetRequest(int limit, int from) {
        Log.d(LOG_TAG, "get from api, limit = " + limit + "; from = " + from);
        mAsynkCall = mDownLoadService.getSongArray(limit, from);
        mAsynkCall.enqueue(mAsynkCallback);
    }

    private void initRetrofit() {
        mRestAdapter = new Retrofit.Builder().baseUrl(ServiceApi.URL)
                .addConverterFactory(getFactoty())
                .build();

        mDownLoadService = mRestAdapter.create(ServiceApi.class);
    }

    private GsonConverterFactory getFactoty() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Melodies.class, new MelodyDeserializer())
                .create();

        return GsonConverterFactory.create(gson);
    }
}
