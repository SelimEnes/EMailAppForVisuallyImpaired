package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.Console;

public class SharedPreferencesHandler {

    static final String PREF_EMAIL = "email";
    static final String PREF_PASSWORD = "password";

    private static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static void setCredentials(Context context, String emailAddress, String password){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_EMAIL, emailAddress);
        editor.putString(PREF_PASSWORD, password);
        editor.commit();
    }
    public static String getEmailAddress(Context context){
        return getSharedPreferences(context).getString(PREF_EMAIL, "");
    }
    public static String getPassword(Context context){
        return getSharedPreferences(context).getString(PREF_PASSWORD, "");
    }
    public static boolean addContact(Context context, String label, String emailAddress){
        if(label == PREF_EMAIL) return false;
        if(getContactAddress(context, label) != "") return false;
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(label, emailAddress);
        editor.commit();
        return true;
    }
    public static String getContactAddress(Context context, String label){
        if(label == PREF_EMAIL) return "";
        return getSharedPreferences(context).getString(label, "");
    }

}
