package com.kenzahn.zahn;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.kenzahn.zahn.rest.ApiClient;
import com.kenzahn.zahn.rest.ApiController;
import com.kenzahn.zahn.rest.ApiInterface;
import com.kenzahn.zahn.utils.AppPreference;

public class AppApplication extends Application {
    private static AppPreference mAppPref;
    public static AppApplication instance;
    public static ApiInterface mApiInterface;
    public static ApiController mApiController;
    public Activity activity;
    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        mApiController = new ApiController(mApiInterface);

    }

    public static final AppPreference getPreferenceInstance() {
        if (mAppPref == null) {
            mAppPref = new AppPreference(instance);
        }
        return mAppPref;
    }
}
