package com.pecherey.alexey.intechtest.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.pecherey.alexey.intechtest.R;
import com.pecherey.alexey.intechtest.fragments.GridViewFragment;
import com.pecherey.alexey.intechtest.fragments.onAdapterUpdateEvent;
import com.pecherey.alexey.intechtest.logic.MelodyLoader;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    private BroadcastReceiver mReceiver;
    private ProgressBar mProgressBar;

    /**
     * Notifications which DowloadService sent via BroadcasrReceiver
     */
    public final static String STATUS = "status";
    public final static int LOAD_START = 100;
    public final static int LOAD_FINISH = 101;
    public final static int LOAD_ERROR = 102;
    public final static int LOAD_MORE = 103;

    public final static String BROADCAST_ACTION = MainActivity.class.getCanonicalName();

    public static IntentFilter getIntentFilter() {
        return new IntentFilter(BROADCAST_ACTION);
    }

    private static GridViewFragment melodiesTableFragment
            = GridViewFragment.create(R.layout.table_fragment);
    private static GridViewFragment melodiesListFragment
            = GridViewFragment.create(R.layout.list_fragment);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialToolbar();

        mReceiver = createBroadcastReceiver();
        IntentFilter filter = getIntentFilter();
        registerReceiver(mReceiver, filter);

        sendFirstlyLoadRequest(savedInstanceState);
    }

    private BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(STATUS, 0);
                switch (status) {
                    case LOAD_START:
                        Log.e(LOG_TAG, "start loading");
                        mProgressBar.setVisibility(View.VISIBLE);
                        break;
                    case LOAD_FINISH:
                        Log.e(LOG_TAG, "finish loading");
                        mProgressBar.setVisibility(View.GONE);
                        updateCurrentFragment();
                        break;
                    case LOAD_MORE:
                        Log.e(LOG_TAG, "receive load request");
                        mProgressBar.setVisibility(View.VISIBLE);
                        MelodyLoader.getInstance(getApplicationContext()).load();
                        break;
                    case LOAD_ERROR:
                        mProgressBar.setVisibility(View.GONE);
                        break;
                }

            }
        };
    }

    private void initialToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] menuItems = getResources().getStringArray(R.array.items);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, menuItems);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerClickListener());

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
    }

    private void sendFirstlyLoadRequest(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            MelodyLoader.getInstance(this).load();
        }
    }

    private void updateCurrentFragment() {
        Fragment current = getFragmentManager().findFragmentById(R.id.fragment);
        if (current != null)
            ((onAdapterUpdateEvent) current).onUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private class SpinnerClickListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            switch (position) {
                case 0:
                    transaction.replace(R.id.fragment, melodiesTableFragment);
                    break;
                case 1:
                    transaction.replace(R.id.fragment, melodiesListFragment);
                    break;
            }
            transaction.commit();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}