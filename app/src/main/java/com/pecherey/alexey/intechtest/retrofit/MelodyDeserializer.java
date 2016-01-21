package com.pecherey.alexey.intechtest.retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.pecherey.alexey.intechtest.logic.Constants;
import com.pecherey.alexey.intechtest.logic.Melodies;
import com.pecherey.alexey.intechtest.logic.Melody;

import java.lang.reflect.Type;

/**
 * Created by Алексей on 15.01.2016.
 */
public class MelodyDeserializer implements JsonDeserializer<Melodies> {
    public static final String MELODY_ARRAY_NAME = "melodies";


    @Override
    public Melodies deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Melodies melodies = new Melodies();

        JsonObject object = json.getAsJsonObject();
        JsonArray jsonDataArray = object.getAsJsonArray(MELODY_ARRAY_NAME);

        fillMelodiesArray(melodies, jsonDataArray);
        return melodies;
    }

    private void fillMelodiesArray(Melodies melodies, JsonArray array) {
        for (JsonElement element : array) {
            JsonObject melodyObject = element.getAsJsonObject();

            String artist = getValue(melodyObject, Constants.MelodyAttr.ARTIST);
            String title = getValue(melodyObject, Constants.MelodyAttr.TITLE);
            String picUrl = getValue(melodyObject, Constants.MelodyAttr.PIC_URL);
            String demoUrl = getValue(melodyObject, Constants.MelodyAttr.DEMO_URL);

            Melody melody = Melodies.create(artist, title, demoUrl, picUrl);
            melodies.add(melody);
        }
    }

    private String getValue(JsonObject object, String key) {
        return object.get(key).toString().replaceAll("\"", "");
    }
}