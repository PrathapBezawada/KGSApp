package com.nunc.krushivignan.util;

/**
 * @author Nildari Prasad
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.util.Log;

public class JSONUtility {
	public static JSONObject getJSONFromUrl(String url) {
		JSONObject jObj = null;
		String json = "";
		HttpResponse resp = null;
		InputStream is = null;
		BufferedReader reader = null;
		StringBuilder sb = null;
		try {
			HttpParams params = new BasicHttpParams();
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);
			HttpGet uri = new HttpGet(url);
			DefaultHttpClient client = new DefaultHttpClient(params);

			// Set the timeout in milliseconds until a connection is
			// established.
			int timeoutConnection = 0;
			HttpConnectionParams
					.setConnectionTimeout(params, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 0;
			HttpConnectionParams.setSoTimeout(params, timeoutSocket);
			resp = client.execute(uri);

			StatusLine resStatus = resp.getStatusLine();
			if (resStatus.getStatusCode() == 200) {

				HttpEntity httpEntity = resp.getEntity();
				is = httpEntity.getContent();
			}

			reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);
			sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			json = sb.toString().trim();
			jObj = new JSONObject(json);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
				if (resp != null)
					resp.getEntity().consumeContent();
				if (reader != null) {
					reader.close();
					reader = null;
				}
				if (sb != null) {
					sb = null;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		// return JSON String
		return jObj;

	}
}
