package com.gbj.stereomood;

import java.util.ArrayList;
import java.util.Iterator;

import android.net.Uri;
import android.net.Uri.Builder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class RESTCall {
	public String base_url;
	public String method;
	public Verb verb;
	public ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
	OAuthService service;
	Token token;
	
	public RESTCall(OAuthService service, Token token, String base_url, String method, Verb verb) {
		this.base_url = base_url;
		this.method = method;
		this.verb = verb;
		this.token = token;
		this.service = service;
	}
	
	public void addParam(String name, String value) {
		this.params.add(new BasicNameValuePair(name, value));
	}

	public Object call() throws JSONException {
//		OAuthRequest request = null;
//		JSONObject data = null;
//		if(verb == Verb.GET) {
			Builder builder = Uri.parse(this.base_url+this.method+".json").buildUpon();
			
			Iterator<BasicNameValuePair> iterator = this.params.iterator();
			while(iterator.hasNext()) {
				BasicNameValuePair param = iterator.next();
				builder.appendQueryParameter(param.getName(), param.getValue());
			}

			OAuthRequest request = new OAuthRequest(Verb.GET, builder.build().toString());
			service.signRequest(token, request);
			Response response = request.send();
			String body = response.getBody();
			
			Object data;
			try {
				data = new JSONArray(body);
			} catch(JSONException e) {
				data = new JSONObject(body);
			}
			return data;
//		}
	}
}

/*public class RESTCall {
	public static final int GET = 0;
	public static final int POST = 1;
	
	public String base_url;
	public String method_name;
	public int method;
	ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
	
	public CommonsHttpOAuthConsumer consumer = null;

	public RESTCall(String base_url, String method_name, int method) {
		this.base_url = base_url;
		this.method_name = method_name;
		this.method = method;
	}
	
	public void addParam(String name, String value) {
		this.params.add(new BasicNameValuePair(name, value));
	}
	
	public void addOAuthConsumer(String key, String secret, String token, String tokenSecret) {
		this.consumer = new CommonsHttpOAuthConsumer(key, secret);
		this.consumer.setTokenWithSecret(token, tokenSecret);
	}

	public HttpEntity getCall() throws Exception {
		HttpClient httpClient = new DefaultHttpClient();
		HttpEntity entity = null;
		
		// Build request URL
		StringBuilder uriBuilder = new StringBuilder(this.base_url);
		uriBuilder.append(this.method_name);
		uriBuilder.append(".json");
		
		Iterator<BasicNameValuePair> iterator = this.params.iterator();
		uriBuilder.append("?");
		while(iterator.hasNext())
			uriBuilder.append(iterator.next().toString());
			uriBuilder.append("&");

//		return uriBuilder.toString();
		
		// Create the request
		HttpGet request = new HttpGet(uriBuilder.toString());
		//URL url = new URL(uriBuilder.toString());
		//HttpURLConnection request = (HttpURLConnection) url.openConnection();
		// Sign the request, if necessary
		if(this.consumer != null ) {
			this.consumer.sign(request);
		}
		// Make the request
		//request.connect();
		//return request.getResponseMessage();
		HttpResponse response = httpClient.execute(request);
		entity = response.getEntity();
		return entity;
	}
	
/*	public HttpEntity postCall() throws ClientProtocolException, IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpEntity entity = null;
		
		// Create the request
		HttpPost request = new HttpPost(this.base_url);
		request.setEntity(new UrlEncodedFormEntity(this.params));
		
		// Sign the request, if necessary
		if(this.consumer != null) {
			this.consumer.sign(request);
		}
		
		// Make the request
		HttpResponse response = httpClient.execute(request);
		entity = response.getEntity();
		return entity;
	}
	
	public JSONObject call() throws Exception {
		String result = null;
		HttpEntity entity = null;
		JSONObject data = null;
		if(this.method == RESTCall.GET) {
			entity = this.getCall();
		} /*else {
			entity = this.postCall();
		}
		
		if(entity != null) {
			InputStream instream = entity.getContent();
			result = convertStreamToString(instream);
			data = new JSONObject(result);
			instream.close();
		}
		
		return data;
	}
	
	private static String convertStreamToString(InputStream is) {
		  /*
		   * To convert the InputStream to String we use the BufferedReader.readLine()
		   * method. We iterate until the BufferedReader return null which means
		   * there's no more data to read. Each line will appended to a StringBuilder
		   * and returned as String.
		   * 
		   * (c) public domain: http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/11/a-simple-restful-client-at-android/
		   
		  BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		  StringBuilder sb = new StringBuilder();

		  String line = null;
		  try {
		   while ((line = reader.readLine()) != null) {
		    sb.append(line + "\n");
		   }
		  } catch (IOException e) {
		   e.printStackTrace();
		  } finally {
		   try {
		    is.close();
		   } catch (IOException e) {
		    e.printStackTrace();
		   }
		  }
		  return sb.toString();
	}
}*/
