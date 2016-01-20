package com.pecherey.alexey.intechtest.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pecherey.alexey.intechtest.R;
import com.pecherey.alexey.intechtest.activities.PlayerActivity;
import com.pecherey.alexey.intechtest.logic.Constants;
import com.pecherey.alexey.intechtest.logic.Melodies;
import com.pecherey.alexey.intechtest.logic.Melody;
import com.pecherey.alexey.intechtest.logic.MelodyStorage;

/**
 * Created by Алексей on 19.01.2016.
 */
public class GridViewFragment extends Fragment implements onAdapterUpdateEvent {
    public static final String RESOURCE_ID = "resourceId";
    public static final String POSITION = "position";

    public static GridViewFragment create(int resource) {
        GridViewFragment fragment = new GridViewFragment();

        Bundle args = new Bundle();
        args.putInt(RESOURCE_ID, resource);
        fragment.setArguments(args);

        return fragment;
    }

    private MelodyAdapter mAdapter;
    private GridView mGridViewTable;

    private int mSavedScrollPosition;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSavedScrollPosition = mGridViewTable.getFirstVisiblePosition();
        outState.putInt(POSITION, mSavedScrollPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int resource = getArguments().getInt(RESOURCE_ID);

        View view = inflater.inflate(resource, null);

        mAdapter = MelodyAdapter.getAdapter(getActivity());
        mGridViewTable = (GridView) view.findViewById(R.id.gridView);
        mGridViewTable.setAdapter(mAdapter);
        restoreScrollPosition(savedInstanceState);
        mGridViewTable.setOnItemClickListener(new ItemClickListener());

        return view;
    }

    private void restoreScrollPosition(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSavedScrollPosition = savedInstanceState.getInt(POSITION);
        } else {
            mSavedScrollPosition = 0;
        }
        mGridViewTable.setSelection(mSavedScrollPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        onUpdate();
    }

    /**
     * onAdapterUpdateEvent interface
     */
    @Override
    public void onUpdate() {
        mAdapter = MelodyAdapter.getAdapter(getActivity());
        Melodies array = MelodyStorage.getInstance().getMelodies();
        mAdapter.addMelodies(array);
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Melody melody = (Melody) mAdapter.getItem(position);
            startPlayer(melody);
        }

        private void startPlayer(Melody melody) {
            Intent play = new Intent(getActivity(), PlayerActivity.class)
                    .putExtra(Constants.PIC_URL, melody.getPicUrl())
                    .putExtra(Constants.DEMO_URL, melody.getDemoUrl());
            startActivity(play);
        }
    }
}
