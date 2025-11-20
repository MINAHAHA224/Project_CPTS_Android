package com.example.androidapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.androidapplication.data.model.user.InformationDTO;
import com.google.gson.Gson;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "ComputerShopPref";
    private static final String KEY_USER = "keyuser";
    private static final String KEY_TOKEN = "keytoken";
    private static SharedPrefManager instance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveUser(InformationDTO user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.putString(KEY_TOKEN, user.getTokenJWT());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, null) != null;
    }

    public InformationDTO getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(KEY_USER, null);
        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, InformationDTO.class);
        }
        return null;
    }

    public String getToken() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void clear() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}