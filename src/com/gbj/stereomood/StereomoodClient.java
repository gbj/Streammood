package com.gbj.stereomood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class StereomoodClient {
	public static final String base_url = "http://www.stereomood.com/api/";

	public Token token;
	OAuthService service;
	
	public StereomoodClient(Token accessToken, String key, String secret) {
		token = accessToken;
		service = new ServiceBuilder()
    		.provider(StereomoodApi.class)
    		.apiKey(key)
    		.apiSecret(secret)
    		.build();
	}
	
	public ArrayList<String> getTags() throws Exception {
		ArrayList<String> tags = new ArrayList<String>();

		RESTCall call = new RESTCall(service, token, StereomoodClient.base_url, "tag/top", Verb.GET);
		JSONArray data = (JSONArray) call.call();

		for(int ii = 0; ii < data.length(); ii++){
			tags.add(data.getJSONObject(ii).getString("value"));
		}
		
		return tags;
	}
	
	public HashMap<String, String> getPlaylists() throws Exception {
		HashMap<String, String> playlists = new HashMap<String, String>();

		RESTCall call = new RESTCall(service, token, StereomoodClient.base_url, "user/playlists/list", Verb.GET);
		JSONObject rdata = (JSONObject) call.call();
		JSONArray data = rdata.getJSONArray("playlists");

		for(int ii = 0; ii < data.length(); ii++){
			playlists.put(data.getJSONObject(ii).getString("title"), data.getJSONObject(ii).getString("id"));
		}
		
		return playlists;
	}
	
	public HashMap<String, String> getPlaylistSongs(String id) throws Exception {
		HashMap<String, String> songs = new HashMap<String, String>();

		RESTCall call = new RESTCall(service, token, StereomoodClient.base_url, "user/playlists/playlist", Verb.GET);
		call.addParam("id", id);
		JSONObject pdata = (JSONObject) call.call();
		JSONArray data = pdata.getJSONArray("songs");
		
		for(int ii = 0; ii < data.length(); ii++){
			songs.put(data.getJSONObject(ii).getString("title"), data.getJSONObject(ii).getString("id"));
		}
		
		return songs;
	}
	
	public HashMap<String, String> getSong(String id) throws Exception {
		HashMap<String, String> data = new HashMap<String, String>();
		
		RESTCall call = new RESTCall(service, token, StereomoodClient.base_url, "song/song", Verb.GET);
		call.addParam("id", id);
		JSONObject rdata = (JSONObject) call.call();
		
		Iterator<String> iterator = rdata.keys();
		while(iterator.hasNext()) {
			String next = iterator.next();
			data.put(next, rdata.getString(next));
		}
		
		return data;
	}
}