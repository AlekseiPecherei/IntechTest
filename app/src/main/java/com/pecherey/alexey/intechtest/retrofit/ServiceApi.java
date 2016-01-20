package com.pecherey.alexey.intechtest.retrofit;

import com.pecherey.alexey.intechtest.logic.Melodies;
import com.pecherey.alexey.intechtest.sevices.DownloadService;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Алексей on 15.01.2016.
 */
public interface ServiceApi {
    String URL = "https://api-content-beeline.intech-global.com/";

    @GET("public/marketplaces/1/tags/4/melodies")
    Call<Melodies> getSongArray(@Query(DownloadService.LIMIT) int limit, @Query(DownloadService.FROM) int from);
}
