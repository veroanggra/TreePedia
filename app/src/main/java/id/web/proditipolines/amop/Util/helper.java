package id.web.proditipolines.amop.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import id.web.proditipolines.amop.activity.LoginActivity;
import id.web.proditipolines.amop.activity.MainActivity;

/**
 * Created by user on 06/06/2017.
 */

public class Helper {

    SharedPreferences.Editor editor;
    Context ctx;
    SharedPreferences preferences;
    //String
    public static final String IS_LOGIN ="login";
    public static final String ID ="id";
    public static final String USERNAME ="username";
    public static final String PASSWORD ="password";
    public Helper(Context context){
        this.ctx =context;
        preferences = ctx.getSharedPreferences("login", 0);
        editor = preferences.edit();
    }

    public void createLoginSession (String id, String username, String password){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(ID, id);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public boolean isLooginIn (){
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public void cekLogin(){
        if (!this.isLooginIn()){
            Intent i = new Intent(ctx, LoginActivity.class);
            //menutup semua activity
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i);
        }else {
            Intent i = new Intent(ctx, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i);
        }
    }

    //untuk mendapatkan data
    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(ID, preferences.getString(ID,null));
        user.put(USERNAME, preferences.getString(USERNAME,null));
        user.put(PASSWORD, preferences.getString(PASSWORD, null));
        return user;
    }

    //Logout
    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i =new Intent(ctx, LoginActivity.class);

        //menutup semua activity
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }
}

