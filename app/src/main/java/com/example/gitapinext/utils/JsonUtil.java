package com.example.gitapinext.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.gitapinext.model.User;

public class JsonUtil {

    private static GsonBuilder gsonBuilder;

    static {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
    }

    private static Gson getGson() {
        return gsonBuilder.create();
    }

    public static List<User> getList(String data) {
        JSONArray jsonarray;
        List<User> users = new ArrayList<>();
        try {
            jsonarray = new JSONArray(data);
            for (int i = 0; i < jsonarray.length(); i++) {
                Object userJson = jsonarray.get(i);
                User object = getGson().fromJson(userJson.toString(), User.class);
                users.add(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return users;
    }

}
