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
    private static boolean isFirstStart = true;
    private static int FIRST_LOAD = 19;
    private static int NEXT = 6;
    private static int FROM = 0;

    private MelodyLoader() {
    }

    public static MelodyLoader getInstance(Context context) {
        mContext = context;
        return ourInstance;
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
        Log.e("MusicElementsActivity", "limit = " + limit + "; from = " + from);
        Intent loadService = new Intent(mContext, DownloadService.class);
        loadService.putExtra(Constants.PaginationArgs.LIMIT, limit)
                .putExtra(Constants.PaginationArgs.FROM, from);
        mContext.startService(loadService);
    }
}
