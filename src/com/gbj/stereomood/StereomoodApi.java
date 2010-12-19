package com.gbj.stereomood;

import org.scribe.builder.api.DefaultApi10a;

public class StereomoodApi extends DefaultApi10a {
	@Override
	protected String getAccessTokenEndpoint() {
		return "http://www.stereomood.com/api/oauth/access_token";
	}

	@Override
	protected String getRequestTokenEndpoint() {
		return "http://www.stereomood.com/api/oauth/request_token";
	}
}