package com.pecherey.alexey.intechtest.fragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pecherey.alexey.intechtest.R;
import com.pecherey.alexey.intechtest.activities.MusicElementsActivity;
import com.pecherey.alexey.intechtest.logic.Constants;
import com.pecherey.alexey.intechtest.logic.Melodies;
import com.pecherey.alexey.intechtest.logic.Melody;
import com.squareup.picasso.Picasso;

/**
 * Created by Алексей on 17.01.2016.
 */
public class MelodyAdapter extends BaseAdapter {
    private static MelodyAdapter mInstance;
    private Melodies mMelodiesArray;
    private LayoutInflater mInflater;
    private Context mContext;

    public MelodyAdapter() {
        mMelodiesArray = new Melodies();
    }

    public void updateContext(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void addMelodies(Melodies data) {
        mMelodiesArray.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMelodiesArray.size();
    }

    @Override
    public Object getItem(int position) {
        return mMelodiesArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = mInflater.inflate(R.layout.item, parent, false);
        }
        prepareView(position, view);

        if (isEndChecked(position))
            sendLoadDataBroadcast();
        return view;
    }

    private void prepareView(int position, View view) {
        Melody melody = (Melody) getItem(position);

        ((TextView) view.findViewById(R.id.artist)).setText(melody.getArtist());
        ((TextView) view.findViewById(R.id.title)).setText(melody.getTitle());

        ImageView picture = (ImageView) view.findViewById(R.id.image);
        Picasso.with(mContext).load(melody.getPicUrl()).into(picture);
    }

    private boolean isEndChecked(int position) {
        return position == getCount() - 1;
    }

    private void sendLoadDataBroadcast() {
        Intent intent = new Intent(MusicElementsActivity.BROADCAST_ACTION)
                .putExtra(Constants.LoadStatus.getName(), Constants.LoadStatus.LOAD_MORE);
        mContext.sendBroadcast(intent);
    }
}
