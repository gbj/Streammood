package com.gbj.stereomood;

import java.util.HashMap;
import java.util.Iterator;

import org.scribe.model.Token;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class StereomoodListActivity extends ListActivity {

	TokenStorage storage;
	StereomoodClient client;
	Token token;

	@Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	storage = new TokenStorage(this);
    	client = new StereomoodClient((Token) storage.getAccessToken(),
    			getString(R.string.oauth_consumer_key),
    			getString(R.string.oauth_consumer_secret));
	}
	
	public void setListContent(String[] words) {
    	setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, words));
    }
	
	public void error(String msg) {
    	final String TAG = "Stereomood";
    	Log.d(TAG, msg);
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
	
    public void login() {
    	error("Logging in!");
    	Intent myIntent = new Intent(StereomoodListActivity.this, Login.class);
    	StereomoodListActivity.this.startActivity(myIntent);
    }

    public boolean checkLogin() {
    	Object token_set = storage.getAccessToken();
    	if(token_set != null) {
    		token = (Token) token_set;
    		return true;
    	} else {
    		return false;
    	}
    }
    
	public Bundle bundleFromStringHashmap(HashMap<String, String> map) {
		Iterator<String> iterator = map.keySet().iterator();
		Bundle bundle = new Bundle();
		while(iterator.hasNext()) {
			String next = (String) iterator.next();
			bundle.putString(next, map.get(next));
		}
		return bundle;
	}
}
