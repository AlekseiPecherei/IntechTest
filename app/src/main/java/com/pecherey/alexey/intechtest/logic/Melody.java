package com.pecherey.alexey.intechtest.logic;

/**
 * Created by Алексей on 15.01.2016.
 */
public final class Melody {
    private String picUrl;
    private String demoUrl;
    private String title;
    private String artist;

    public Melody(String picUrl, String demoUrl, String title, String artist) {
        this.picUrl = picUrl;
        this.demoUrl = demoUrl;
        this.title = title;
        this.artist = artist;
    }

    public String getDemoUrl() {
        return demoUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}
