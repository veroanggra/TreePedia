package id.web.proditipolines.amop.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import id.web.proditipolines.amop.activity.LoginActivity;
import id.web.proditipolines.amop.activity.MainActivity;

public class Helper {

    private SharedPreferences.Editor editor;
    private Context ctx;
    private SharedPreferences preferences;
    //String
    private static final String IS_LOGIN = "login";
    public static final String ID = "id";
    public static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @SuppressLint("CommitPrefEdits")
    public Helper(Context context) {
        this.ctx = context;
        preferences = ctx.getSharedPreferences("login", 0);
        editor = preferences.edit();
    }

    public void createLoginSession(String id, String username, String password) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(ID, id);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    private boolean isLooginIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public void cekLogin() {
        if (!this.isLooginIn()) {
            Intent i = new Intent(ctx, LoginActivity.class);
            //menutup semua activity
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i);
        } else {
            Intent i = new Intent(ctx, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i);
        }
    }

    //untuk mendapatkan data
    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(ID, preferences.getString(ID, null));
        user.put(USERNAME, preferences.getString(USERNAME, null));
        user.put(PASSWORD, preferences.getString(PASSWORD, null));
        return user;
    }

    //Logout
    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(ctx, LoginActivity.class);

        //menutup semua activity
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

    @SuppressLint("SimpleDateFormat")
    public static String KonversiTanggal(String tanggalDanWaktu) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(tanggalDanWaktu);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        return df.format(date);
    }
}

