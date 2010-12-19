package com.gbj.stereomood;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Tags extends StereomoodListActivity {
	String[] tags;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	ArrayList<String> al = null;
		try {
			al = client.getTags();
		} catch (Exception e) {
			error(e.toString());
		}
		tags = al.toArray(new String[al.size()]);
		setListContent(tags);
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {

				error(tags[(int) id]);
		    }
		  });
	}
}