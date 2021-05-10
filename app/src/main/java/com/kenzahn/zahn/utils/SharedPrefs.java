package com.kenzahn.zahn.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


public class SharedPrefs
{
    // User name (make variable public to access from outside)

    public static final String API_KEY = "98745612";
    public static final String TAG = "tag";
;
    // Sharedpref file name
    private static final String PREF_NAME = "FlashCard";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_FIRST = "IS_FIRST";
    public static final String KEY_USERID = "KEY_USERID";
    private static final String KEY_NAME = "KEY_NAME";
    private static final String KEY_SEARCH_USER_ID = "KEY_SEARCH_USER_ID";
    private static final String KEY_Phone_number = "KEY_Phone_number";
    private static final String KEY_EMAIL = "KEY_EMAIL";
    private static final String KEY_PASSWORD = "KEY_PASSWORD";
    private static final String IS_PREMIUMER = "IS_PREMIUMER";
    private static final String IS_PHONE_VERIFIED = "IS_PHONE_VERIFIED";
    private static final String IS_TERMS_ACCEPTED = "IS_TERMS_ACCEPTED";
    private static final String KEY_CREATED_DATE = "KEY_CREATED_DATE";
    public static final String KEY_SC_DAYS = "KEY_SC_DAYS";
    public static final String CONSYNC = "CONSYNC";
    public static final String CANSYNC = "CANSYNC";
    public static final String KEY_ORDER_ID = "KEY_ORDER_ID";
    public static final String KEY_Error = "KEY_Error";

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    Context context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SharedPrefs(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setSuffled()
    { editor.putBoolean(IS_LOGIN, true);
        editor.commit();

    }


    public boolean isContactSyncing()
    {
        return pref.getBoolean(CONSYNC, false);
    }


    public void conSyncSuccess()
    {

        editor.putBoolean(CONSYNC, true);
        editor.commit();

    }



    public boolean isCalctSyncing()
    {
        return pref.getBoolean(CANSYNC, false);
    }


    public void calSyncSuccess()
    {

        editor.putBoolean(CANSYNC, true);
        editor.commit();

    }



    /*Create Login Session*/


    public void setLoginSession(String id, String name, String searchUserId,String phoneNumber, String email, String password, boolean isPremiumUser, boolean isPhoneNumberVerified ,boolean isTermsConditionAccepted,String createdDate) {
        // Storing login value as TRUE

        // Storing name in pref
        editor.putString(KEY_USERID, String.valueOf(id));
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_SEARCH_USER_ID, searchUserId);
        editor.putString(KEY_Phone_number, phoneNumber);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(IS_LOGIN, true);
        editor.putBoolean(IS_PREMIUMER, isPremiumUser);
        editor.putBoolean(IS_PHONE_VERIFIED, isPhoneNumberVerified);
        editor.putBoolean(IS_TERMS_ACCEPTED, isTermsConditionAccepted);
        editor.putString(KEY_CREATED_DATE, createdDate);

        editor.commit();
    }


    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name

        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
//        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
//        user.put(KEY_SEARCH_USER_ID, pref.getString(KEY_SEARCH_USER_ID, null));
//        user.put(KEY_Phone_number, pref.getString(KEY_Phone_number, null));
//        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
//        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
//        user.put(IS_LOGIN, pref.getString(IS_LOGIN, null));
//        user.put(IS_PREMIUMER, pref.getString(IS_PREMIUMER, null));
//        user.put(IS_PHONE_VERIFIED, pref.getString(IS_PHONE_VERIFIED, null));
//        user.put(IS_TERMS_ACCEPTED, pref.getString(IS_TERMS_ACCEPTED, null));
//        user.put(KEY_CREATED_DATE, pref.getString(KEY_CREATED_DATE, null));

        return user;
    }

    public void setScheduleDays(String days)
    {
        editor.putString(KEY_SC_DAYS, String.valueOf(days));
        editor.commit();
    }
    public HashMap<String, String> getSchedularDays()
    {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name

        user.put(KEY_SC_DAYS, pref.getString(KEY_SC_DAYS, null));

        return user;
    }


    public void setOrderId(String days)
    {
        editor.putString(KEY_ORDER_ID, String.valueOf(days));
        editor.commit();
    }
    public HashMap<String, String> getOrderID()
    {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name

        user.put(KEY_ORDER_ID, pref.getString(KEY_ORDER_ID, "0"));

        return user;
    }

    public void setError(String days)
    {
        editor.putString(KEY_Error, String.valueOf(days));
        editor.commit();
    }
    public HashMap<String, String> getError()
    {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name

        user.put(KEY_Error, pref.getString(KEY_Error, "No Data Found"));

        return user;
    }


    public void setFirst()
    {
        editor.putBoolean(IS_FIRST, false);
        editor.commit();
    }
    public boolean isFirst()
    {
        return pref.getBoolean(IS_FIRST, true);
    }


    public boolean isSuffeled() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void logout()
    {
        editor.clear();
        editor.commit();
    }






}

