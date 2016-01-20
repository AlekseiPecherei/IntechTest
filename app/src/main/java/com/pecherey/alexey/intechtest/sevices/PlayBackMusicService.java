package com.pecherey.alexey.intechtest.sevices;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.pecherey.alexey.intechtest.activities.MainActivity;
import com.pecherey.alexey.intechtest.activities.PlayerActivity;
import com.pecherey.alexey.intechtest.logic.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PlayBackMusicService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    
    public static final String PROGRESS = "progress";
    public static final String SEEK     = "seek";
    public static final String FINISH   = "finish";
    public static final String STATE    = "state";

    public static final int START   = 1;
    public static final int PAUSE   = 2;
    public static final int STOP    = 3;
    public static final int SEEK_TO = 4;
    public static final int SET_URL = 5;



    private MediaPlayer mMediaPlayer;

    private int currentPos;
    private boolean isPause;
    private boolean isUpdate;

    static String url;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int state = intent.getIntExtra(STATE, 0);

        switch (state) {
            case SET_URL:
                setUrlFromIntent(intent);
                break;
            case START:
                startPlaying();
                break;
            case PAUSE:
                pausePlaying();
                break;
            case STOP:
                stopPlaying();
                break;
            case SEEK_TO:
                seekToPlaying(intent);
                break;
            default:
                break;
        }
        return START_NOT_STICKY;
    }

    private void setUrlFromIntent(Intent intent) {
        url = intent.getStringExtra(Constants.DEMO_URL);
    }

    private void startPlaying() {
        isUpdate = true;
        if (isPause) {
            startMediaPlayer();
            isPause = false;
        } else {
            prepareMediaPlayer(url);
        }
    }

    private void pausePlaying() {
        mMediaPlayer.pause();
        saveCurrentPosition(mMediaPlayer.getCurrentPosition());
        isPause = true;
        isUpdate = false;
    }

    private void seekToPlaying(Intent intent) {
        int seek = intent.getIntExtra(SEEK, 0);
        int to = (int) (seek * mMediaPlayer.getDuration() * 0.01);
        if (mMediaPlayer.isPlaying() || isPause)
            mMediaPlayer.seekTo(to);
        saveCurrentPosition(to);
    }

    private void stopPlaying() {
        isUpdate = false;
        if(mMediaPlayer != null)
            mMediaPlayer.stop();
        saveCurrentPosition(0);
        sendBroadcast(getProgressIntent(0.0));
    }

    private void saveCurrentPosition(int time) {
        currentPos = time;
    }

    private void startMediaPlayer() {
        mMediaPlayer.seekTo(currentPos);
        mMediaPlayer.start();
        new Thread(new ProgressUpdateTask()).start();
    }

    private class ProgressUpdateTask implements Runnable {
        private static final int SLEEP = 200;

        @Override
        public void run() {
            while (isUpdate) {
                if (mMediaPlayer != null) {
                    double progress = caclProgress();
                    sendBroadcast(getProgressIntent(progress));
                }
                threadSleep();
            }
        }

        private double caclProgress() {
            double progress;
            if (mMediaPlayer.isPlaying()) {
                progress = (double) mMediaPlayer.getCurrentPosition()
                        / (double) mMediaPlayer.getDuration();
            } else {
                progress = 0.0;
            }
            return progress;
        }

        private void threadSleep() {
            try {
                TimeUnit.MILLISECONDS.sleep(SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isUpdate = false;
        releaseMediaPlayer();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        startMediaPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void prepareMediaPlayer(String url) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            try {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.setOnCompletionListener(this);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mMediaPlayer.prepare();
                mMediaPlayer.seekTo(currentPos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private Intent getProgressIntent(double progress) {
        return new Intent(PlayerActivity.MUSIC_SERVICE_BROADCAST_ACTION)
                .putExtra(PROGRESS, progress);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        sendBroadcast(new Intent(PlayerActivity.MUSIC_SERVICE_BROADCAST_ACTION)
                .putExtra(FINISH, true));
        isUpdate = false;
        stopSelf();
    }
}