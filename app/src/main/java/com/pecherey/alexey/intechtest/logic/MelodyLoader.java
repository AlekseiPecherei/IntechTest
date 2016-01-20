package com.pecherey.alexey.intechtest.logic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pecherey.alexey.intechtest.sevices.DownloadService;

/**
 * Created by Алексей on 19.01.2016.
 */
public class MelodyLoader {
    private static MelodyLoader ourInstance = new MelodyLoader();
    private static Context mContext;

    public static MelodyLoader getInstance(Context context) {
        mContext = context;
        return ourInstance;
    }

    private static boolean isFirstStart = true;
    private static int FIRST_LOAD = 20;
    private static int NEXT = 6;
    private static int FROM = 0;

    private MelodyLoader() {
    }

    public void load() {
        if (isFirstStart) {
            load(FIRST_LOAD, FROM);
            FROM = FIRST_LOAD + 1;
            isFirstStart = false;
        } else {
            load(NEXT, FROM);
            FROM = FROM + NEXT + 1;
        }
    }

    private void load(int limit, int from) {
        Log.e("MainActivity", "limit = " + limit + "; from = " + from);
        Intent loadService = new Intent(mContext, DownloadService.class);
        loadService.putExtra(DownloadService.LIMIT, limit)
                .putExtra(DownloadService.FROM, from);
        mContext.startService(loadService);
    }
}
