package com.pecherey.alexey.intechtest.activities;

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
import com.pecherey.alexey.intechtest.logic.Constants;
import com.pecherey.alexey.intechtest.logic.MelodyLoader;

public class MusicElementsActivity extends AppCompatActivity {
    public final static String BROADCAST_ACTION = MusicElementsActivity.class.getCanonicalName();
    private final static String LOG_TAG = MusicElementsActivity.class.getSimpleName();

    private static GridViewFragment melodiesTableFragment
            = GridViewFragment.create(R.layout.table_fragment);
    private static GridViewFragment melodiesListFragment
            = GridViewFragment.create(R.layout.list_fragment);

    private BroadcastReceiver mReceiver;
    private ProgressBar mProgressBar;

    public static IntentFilter getIntentFilter() {
        return new IntentFilter(BROADCAST_ACTION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialToolbar();

        mReceiver = createBroadcastReceiver();
        IntentFilter filter = getIntentFilter();
        registerReceiver(mReceiver, filter);

        sendInitialLoadRequest(savedInstanceState);
    }

    private BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(Constants.LoadStatus.getName(), 0);
                switch (status) {
                    case Constants.LoadStatus.LOAD_START:
                        Log.e(LOG_TAG, "start loading");
                        mProgressBar.setVisibility(View.VISIBLE);
                        break;
                    case Constants.LoadStatus.LOAD_FINISH:
                        Log.e(LOG_TAG, "finish loading");
                        mProgressBar.setVisibility(View.GONE);
                        break;
                    case Constants.LoadStatus.LOAD_MORE:
                        Log.e(LOG_TAG, "receive load request");
                        mProgressBar.setVisibility(View.VISIBLE);
                        MelodyLoader.getInstance(getApplicationContext()).load();
                        break;
                    case Constants.LoadStatus.LOAD_ERROR:
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Constants.View current = Constants.View.values()[position];
                switch (current) {
                    case TABLE:
                        transaction.replace(R.id.fragment, melodiesTableFragment);
                        break;
                    case LIST:
                        transaction.replace(R.id.fragment, melodiesListFragment);
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
    }

    private void sendInitialLoadRequest(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            MelodyLoader.getInstance(this).load();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}