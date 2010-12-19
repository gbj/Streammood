package com.gbj.stereomood;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Playlists extends StereomoodListActivity {
	HashMap<String, String> playlists;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
		try {
			playlists = client.getPlaylists();
		} catch (Exception e) {
			error(e.toString());
		}
		String[] names = playlists.keySet().toArray(new String[playlists.size()]);
		setListContent(names);
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {

		    	String playlist_id = playlists.get(((TextView) view).getText());
				try {
					HashMap<String, String> songs = client.getPlaylistSongs(playlist_id);
					Intent next = new Intent(Playlists.this, Songs.class);
					next.putExtra("songs", bundleFromStringHashmap(songs));
					Playlists.this.startActivity(next);
				} catch (Exception e) {
					error(e.toString());
				}
		    	
		    }
		  });
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