package com.pecherey.alexey.intechtest.sevices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.pecherey.alexey.intechtest.activities.MainActivity;
import com.pecherey.alexey.intechtest.logic.Melodies;
import com.pecherey.alexey.intechtest.logic.MelodyStorage;
import com.pecherey.alexey.intechtest.retrofit.RetrofitLogic;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DownloadService extends Service implements Callback<Melodies> {
    private final static String LOG_TAG = DownloadService.class.getSimpleName();

    public static final String LIMIT = "limit";
    public static final String FROM = "from";

    private RetrofitLogic mLogic;

    public DownloadService() {
        mLogic = new RetrofitLogic(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int limit = intent.getIntExtra(LIMIT, 1);
        int from = intent.getIntExtra(FROM, 0);
        mLogic.startGetRequest(limit, from);

        sendBroadcast(prepareStatusIntent(MainActivity.LOAD_START));

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onResponse(Response<Melodies> response, Retrofit retrofit) {
        Melodies array = response.body();
        MelodyStorage.getInstance().setMelodies(array);

        Log.d(LOG_TAG, "response return " + array.size() + " elements");
        Log.d(LOG_TAG, "add all elements to adapter, dataSetChanged()");

        sendBroadcast(prepareStatusIntent(MainActivity.LOAD_FINISH));
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(LOG_TAG, "failture: " + t.getMessage());
        sendBroadcast(prepareStatusIntent(MainActivity.LOAD_ERROR));
        stopSelf();
    }

    private static Intent prepareStatusIntent(int status) {
        return new Intent(MainActivity.BROADCAST_ACTION)
                .putExtra(MainActivity.STATUS, status);
    }
}
