package com.pecherey.alexey.intechtest.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.pecherey.alexey.intechtest.R;
import com.pecherey.alexey.intechtest.logic.Constants;
import com.pecherey.alexey.intechtest.sevices.PlayBackMusicService;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public static final int SEEK_BAR_PROGRESS = 100;
    public final static String MUSIC_SERVICE_BROADCAST_ACTION = PlayerActivity.class.getCanonicalName();
    private Button mStart;
    private Button mPause;
    private Button mStop;
    private SeekBar mSeekBar;
    private ImageView mImageView;
    private BroadcastReceiver mReceiver;
    private boolean isPlaying;

    public static IntentFilter getIntentFilter() {
        return new IntentFilter(MUSIC_SERVICE_BROADCAST_ACTION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initialUIComponents();
        initialToolbar();
    }

    private void initialToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.player_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                stopService(new Intent(PlayerActivity.this, PlayBackMusicService.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isPlaying) {
            startAction();
            uiShowPlaying();
            isPlaying = true;
        }
    }

    private void startAction() {
        String url = getDataFromIntent(Constants.MelodyAttr.DEMO_URL);
        startService(createMusicIntent(Constants.Player.SET_URL)
                .putExtra(Constants.MelodyAttr.DEMO_URL, url));
        startService(createMusicIntent(Constants.Player.START));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.SaveInstance.PAUSE, mPause.isEnabled());
        outState.putBoolean(Constants.SaveInstance.START, mStart.isEnabled());
        outState.putBoolean(Constants.SaveInstance.PLAYING, isPlaying);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boolean pause = savedInstanceState.getBoolean(Constants.SaveInstance.PAUSE);
        boolean start = savedInstanceState.getBoolean(Constants.SaveInstance.START);
        isPlaying = savedInstanceState.getBoolean(Constants.SaveInstance.PLAYING);

        controlButtonsEnable(start, pause);
    }

    private void initialUIComponents() {
        String url = getDataFromIntent(Constants.MelodyAttr.PIC_URL);
        mImageView = (ImageView) findViewById(R.id.artist);
        Glide.with(this).load(url).into(mImageView);

        mStart = (Button) findViewById(R.id.start);
        mPause = (Button) findViewById(R.id.pause);
        mStop = (Button) findViewById(R.id.stop);
        mStart.setOnClickListener(this);
        mPause.setOnClickListener(this);
        mStop.setOnClickListener(this);

        mSeekBar = (SeekBar) findViewById(R.id.progress);
        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setMax(SEEK_BAR_PROGRESS);

        mReceiver = createBroadcastReceiver();
        IntentFilter filter = getIntentFilter();
        registerReceiver(mReceiver, filter);
    }

    private String getDataFromIntent(String key) {
        return getIntent().getStringExtra(key);
    }

    private BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateProgress(intent);
                updateUiIfMusicFinish(intent);
            }
        };
    }

    private void updateProgress(Intent intent) {
        /**
         * progress received from service via intent has value of [0;1]
         * we need myltiply this value by SEEK_BAR_PROGRESS constant value 100
         */
        final double progress = intent.getDoubleExtra(Constants.Player.PROGRESS, 0.0);
        final int progressToShow = (int) (SEEK_BAR_PROGRESS * progress);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSeekBar.setProgress(progressToShow);
            }
        });
    }

    private void updateUiIfMusicFinish(Intent intent) {
        if (intent.getBooleanExtra(Constants.Player.FINISH, false)) {
            uiShowPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopService(new Intent(this, PlayBackMusicService.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                startAction();
                uiShowPlaying();
                break;
            case R.id.pause:
                startService(createMusicIntent(Constants.Player.PAUSE));
                uiShowPause();
                break;
            case R.id.stop:
                startService(createMusicIntent(Constants.Player.STOP));
                uiShowPause();
                break;
        }
    }

    private void uiShowPause() {
        controlButtonsEnable(true, false);
    }

    private void uiShowPlaying() {
        controlButtonsEnable(false, true);
    }

    private void controlButtonsEnable(boolean start, boolean pause) {
        mPause.setEnabled(pause);
        mStart.setEnabled(start);
    }

    private Intent createMusicIntent(int state) {
        return new Intent(this, PlayBackMusicService.class)
                .putExtra(Constants.Player.STATE, state);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser)
            startService(createMusicIntent(Constants.Player.SEEK_TO).putExtra(Constants.Player.SEEK, progress));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}