package com.pecherey.alexey.intechtest.fragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pecherey.alexey.intechtest.R;
import com.pecherey.alexey.intechtest.activities.MainActivity;
import com.pecherey.alexey.intechtest.logic.Melodies;
import com.pecherey.alexey.intechtest.logic.Melody;

/**
 * Created by Алексей on 17.01.2016.
 */
public class MelodyAdapter extends BaseAdapter {
    private Melodies mMelodiesArray;
    private LayoutInflater mInflater;
    private ImageLoader mLoader;

    private Context mContext;

    private static MelodyAdapter mInstance;

    public static MelodyAdapter getAdapter(Context context) {
        if (mInstance == null) {
            mInstance = new MelodyAdapter(context);
        }
        return mInstance.init(context);
    }

    private MelodyAdapter(Context context) {
        mMelodiesArray = new Melodies();
    }

    private MelodyAdapter init(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;

        mLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        mLoader.init(config);

        return mInstance;
    }

    public void addMelodies(Melodies data) {
        if (data != null) {
            mMelodiesArray.clear();
            mMelodiesArray.addAll(data);
            notifyDataSetChanged();
        }
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
        mLoader.displayImage(melody.getPicUrl(), picture);
    }

    private boolean isEndChecked(int position) {
        return position == getCount() - 1;
    }

    private void sendLoadDataBroadcast() {
        Intent intent = new Intent(MainActivity.BROADCAST_ACTION).putExtra(MainActivity.STATUS, MainActivity.LOAD_MORE);
        mContext.sendBroadcast(intent);
    }
}