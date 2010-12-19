package com.gbj.stereomood;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.scribe.model.Token;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SongPlayer extends Activity {

	// Normal Stereomood stuff
	TokenStorage storage;
	StereomoodClient client;
	Token token;
	
	// SongPlayer-specific stuff
	String song_id;
	MediaPlayer player;

	@Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	// Normal Stereomood stuff
    	storage = new TokenStorage(this);
    	client = new StereomoodClient((Token) storage.getAccessToken(),
    			getString(R.string.oauth_consumer_key),
    			getString(R.string.oauth_consumer_secret));
    	
    	// SongPlayer-specific stuff
    	setContentView(R.layout.player);
    	song_id = getIntent().getExtras().getString("song_id");
    	
    	// Default song metadata
    	HashMap<String, String> metadata = new HashMap<String, String>();
    	metadata.put("title", getString(R.string.title_unknown));
    	metadata.put("artist", getString(R.string.artist_unknown));
    	metadata.put("album", getString(R.string.album_unknown));
    	metadata.put("image_url", "");
    	
    	// Get song metadata
    	try {
			metadata = client.getSong(song_id);
		} catch (Exception e) {
			error(e.toString());
		}
		
		// Update view
		ImageView album_art_v = (ImageView)findViewById(R.id.album_art);
		TextView title_v = (TextView)findViewById(R.id.song_title);
		TextView artist_v = (TextView)findViewById(R.id.song_artist);
		TextView album_v = (TextView)findViewById(R.id.song_album);
		
		title_v.setText(metadata.get("title"));
		artist_v.setText(metadata.get("artist"));
		album_v.setText(metadata.get("album"));
		
		// Set up MediaPlayer
		try {
			error(metadata.get("audio_url"));
			player = new MediaPlayer();
			player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				public void onPrepared(MediaPlayer mp) {
					ImageButton b = (ImageButton)findViewById(R.id.play_button);
					b.setImageResource(R.drawable.ic_media_pause);
					mp.start();
				}
			});
			player.reset();
			player.setDataSource(metadata.get("audio_url"));
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.prepareAsync();
		} catch (Exception e) {
			error(e.toString());
		}
		
		// Set up controls
		ImageButton play_b = (ImageButton)findViewById(R.id.play_button);
		ImageButton previous_b = (ImageButton)findViewById(R.id.previous_button);
		ImageButton next_b = (ImageButton)findViewById(R.id.next_button);
		
		play_b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ImageButton b = (ImageButton)findViewById(R.id.play_button);
				if(player.isPlaying()) {
					player.pause();
					b.setImageResource(R.drawable.ic_media_play);
				} else {
					player.start();
					b.setImageResource(R.drawable.ic_media_pause);
				}
			}
		});
		
		// Download art
		Drawable image = downloadArt(getApplicationContext(),
				metadata.get("image_url"),
				metadata.get("title")+".jpg");
		album_art_v.setImageDrawable(image);
		
	}
	
	public void error(String msg) {
    	final String TAG = "Stereomood";
    	Log.d(TAG, msg);
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
	
	private Drawable downloadArt(Context ctx, String url, String saveFilename) {
		try {
			InputStream in = (InputStream) this.fetch(url);
			Drawable d = Drawable.createFromStream(in, "src");
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Object fetch(String address) throws MalformedURLException,IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}
}
