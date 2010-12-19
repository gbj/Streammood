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

public class Songs extends StereomoodListActivity {
	HashMap<String, String> songs;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	songs = stringHashMapFromBundle(getIntent().getExtras().getBundle("songs"));
    	
		String[] names = songs.keySet().toArray(new String[songs.size()]);
		setListContent(names);
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		    	
		    	String song_id = songs.get(((TextView) view).getText());
		    	Intent next = new Intent(Songs.this, SongPlayer.class);
				next.putExtra("song_id", song_id);
				Songs.this.startActivity(next);
		    }
		  });
	}
	
	public HashMap<String, String> stringHashMapFromBundle(Bundle bundle) {
		Iterator<String> iterator = bundle.keySet().iterator();
		HashMap<String, String> map = new HashMap<String, String>();
		map = new HashMap<String, String>();
		while(iterator.hasNext()) {
			String next = (String) iterator.next();
			map.put(next, bundle.getString(next));
		}
		return map;
	}
}