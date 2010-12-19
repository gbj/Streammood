package com.gbj.stereomood;

import org.scribe.model.Token;

import android.app.Activity;
import android.content.SharedPreferences;

public class TokenStorage {
	public final static String TOKEN_FILE = "Stereomood_Tokens";
	public final static String ACCESS_TOKEN = "ACCESS_TOKEN";
	public final static String ACCESS_TOKEN_SECRET = "ACCESS_TOKEN_SECRET";
	public static Activity activity;
	
	public TokenStorage(Activity act) {
		activity = act;
	}
	
    public void setAccessToken(Token accessToken) {
    	SharedPreferences settings = activity.getSharedPreferences(TOKEN_FILE, 0);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putString(ACCESS_TOKEN, accessToken.getToken());
    	editor.putString(ACCESS_TOKEN_SECRET, accessToken.getSecret());
    	editor.commit();
    }

    public Object getAccessToken() {
    	SharedPreferences settings = activity.getSharedPreferences(TOKEN_FILE, 0);
    	String token = settings.getString(ACCESS_TOKEN, null);
    	String secret = settings.getString(ACCESS_TOKEN_SECRET, null);
    	if(token != null && secret != null) {
    		return new Token(token, secret);
    	} else {
    		return null;
    	}
    }
    
    public void clear() {
    	SharedPreferences settings = activity.getSharedPreferences(TOKEN_FILE, 0);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.clear();
    	editor.commit();
    }
}