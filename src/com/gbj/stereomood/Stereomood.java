package com.gbj.stereomood;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Stereomood extends StereomoodListActivity {

	private final int TAGS = 0;
	private final int PLAYLISTS = 1;
	private final int LIBRARY = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	    	
    	boolean check = checkLogin();
    	if(!check) {
    		try {
    			login();
    		} catch(Exception e) {
    			error(e.toString());
    		}
    	} else {
    		try {
	    		client = new StereomoodClient(token,
	    				getString(R.string.oauth_consumer_key),
	    				getString(R.string.oauth_consumer_secret));
    		} catch(Exception e) {
    			error(e.toString());
    		}
    	}
    	
  	  String[] categories = getResources().getStringArray(R.array.categories_array);
	  this.setListContent(categories);

	  ListView navigation_lv = getListView();
	  navigation_lv.setTextFilterEnabled(true);

	  navigation_lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {

			try {
				Intent next;
				switch((int) id) {
				case TAGS:
					next = new Intent(Stereomood.this, Tags.class);
			    	Stereomood.this.startActivity(next);
			    	break;
				case PLAYLISTS:
					next = new Intent(Stereomood.this, Playlists.class);
					Stereomood.this.startActivity(next);
					break;
				case LIBRARY:
					try {
						HashMap<String, String> songs = client.getLibrarySongs();
						next = new Intent(Stereomood.this, Songs.class);
						next.putExtra("songs", bundleFromStringHashmap(songs));
						Stereomood.this.startActivity(next);
					} catch (Exception e) {
						error(e.toString());
					}
					break;
				}
			} catch (Exception e) {
				error(e.toString());
			}
	    }
	  });
    }
}