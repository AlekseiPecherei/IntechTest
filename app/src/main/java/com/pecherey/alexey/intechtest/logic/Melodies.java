package com.pecherey.alexey.intechtest.logic;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Алексей on 15.01.2016.
 */
public class Melodies extends ArrayList<Melody> {
    public static Melody create(String artist, String title, String demoUrl, String picUrl) {
        return new Melody(picUrl, demoUrl, title, artist);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends Melody> collection) {
        return super.addAll(collection);
    }
}
