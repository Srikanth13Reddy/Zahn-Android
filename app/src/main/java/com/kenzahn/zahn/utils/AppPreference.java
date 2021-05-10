package com.kenzahn.zahn.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class AppPreference {
    private final String TAG = "OnlineFlashCards";
    private SharedPreferences mPreference;
    Context mContext;

    public AppPreference(Context mContext) {
        this.mContext = mContext;
        this.mPreference = mContext.getSharedPreferences(this.TAG, Context.MODE_PRIVATE);
    }

    public final void writeString(String tag, String data) {
        SharedPreferences.Editor editor = this.mPreference.edit();
        editor.putString(tag, data).apply();
    }

    @Nullable
    public final String readString(String tag) {
        return this.mPreference.getString(tag, null);
    }

    public final void writeInteger(String tag, int data) {
        SharedPreferences.Editor editor = this.mPreference.edit();
        editor.putInt(tag, data).apply();
    }

    public final int readInteger(String tag) {
        return this.mPreference.getInt(tag, -1);
    }

    public final void writeBoolean(String tag, boolean data) {
        SharedPreferences.Editor editor = this.mPreference.edit();
        editor.putBoolean(tag, data).apply();
    }

    public final boolean readBoolean(String tag) {
        return this.mPreference.getBoolean(tag, false);
    }

    public final void clear() {
        SharedPreferences.Editor editor = this.mPreference.edit();
        editor.clear();
        editor.commit();
    }

}
