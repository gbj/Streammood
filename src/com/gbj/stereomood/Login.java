package com.gbj.stereomood;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	Token requestToken = null;
	Token accessToken = null;
	TokenStorage storage = new TokenStorage(this);
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.login);
    	
    	// Create OAuthService
    	final OAuthService service = new ServiceBuilder()
        	.provider(StereomoodApi.class)
        	.apiKey(getString(R.string.oauth_consumer_key))
        	.apiSecret(getString(R.string.oauth_consumer_secret))
        	.build();

    	// Authorize button
		Button auth = (Button)this.findViewById(R.id.authorize_button);
		auth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String url = "";
				try {
					requestToken = service.getRequestToken();
			    	url = getString(R.string.authorize_url) + "?oauth_token=" + requestToken.getToken();
					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				} catch (Exception e) {
					error(e.toString());
				}
			}
		});
		
		// Login button
		Button login = (Button)this.findViewById(R.id.login_button);
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Verifier pin = new Verifier(((EditText) findViewById(R.id.pin_input)).getText().toString());
				try {
					accessToken = service.getAccessToken(requestToken, pin);
				} catch (Exception e) {
					error(e.toString());
				}

				try {
					storage.setAccessToken(accessToken);
					Intent myIntent = new Intent(Login.this, Stereomood.class);
			    	Login.this.startActivity(myIntent);
				} catch(Exception e) {
					error(getString(R.string.error_saving_user));
				}
			}
		});
    }
	
    public void error(String msg) {
    	final String TAG = "Login";
    	Log.d(TAG, msg);
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}