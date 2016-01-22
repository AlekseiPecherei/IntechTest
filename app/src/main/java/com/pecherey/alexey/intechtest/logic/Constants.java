package com.pecherey.alexey.intechtest.logic;

/**
 * Created by Алексей on 20.01.2016.
 */
public class Constants {
    public static final String RESOURCE_ID = "resourceId";

    public enum View {
        TABLE,
        LIST
    }

    public static class MelodyAttr {
        public static final String ARTIST = "artist";
        public static final String TITLE = "title";
        public static final String PIC_URL = "picUrl";
        public static final String DEMO_URL = "demoUrl";
    }

    /**
     * Notifications which DowloadService sent via BroadcasrReceiver
     */
    public static class LoadStatus {
        public static final int LOAD_START = 1;
        public static final int LOAD_FINISH = 2;
        public static final int LOAD_ERROR = 3;
        public static final int LOAD_MORE = 4;

        public static String getName() {
            String name = "status";
            return name;
        }
    }

    public static class PaginationArgs {
        public static final String LIMIT = "limit";
        public static final String FROM = "from";
    }

    public static class SaveInstance {
        public static final String PAUSE = "pause";
        public static final String START = "start";
        public static final String PLAYING = "playing";
        public static final String POSITION = "position";
    }

    public static class Player {
        public static final String PROGRESS = "progress";
        public static final String SEEK = "seek";
        public static final String FINISH = "finish";
        public static final String STATE = "state";

        public static final int PAUSE = 1;
        public static final int STOP = 2;
        public static final int SEEK_TO = 3;
        public static final int SET_URL_AND_START_PLAY = 4;
    }
}
