package com.pecherey.alexey.intechtest.logic;

/**
 * Created by Алексей on 18.01.2016.
 * instead Data Base
 */
public class MelodyStorage {
    private static MelodyStorage ourInstance = new MelodyStorage();

    public static MelodyStorage getInstance() {
        return ourInstance;
    }

    private MelodyStorage() {
    }

    private Melodies mComplitedArray = new Melodies();

    public void setMelodies(Melodies melodies) {
        mComplitedArray.addAll(melodies);
    }

    public Melodies getMelodies() {
        return mComplitedArray;
    }
}
