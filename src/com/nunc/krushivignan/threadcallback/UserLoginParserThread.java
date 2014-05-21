package com.nunc.krushivignan.threadcallback;

/**
 * @author Nildari Prasad
 * 
 */
import org.json.JSONObject;

import android.util.Log;

import com.nunc.krushivignan.core.User;
import com.nunc.krushivignan.services.UserLoginParser;
import com.nunc.krushivignan.util.JSONUtility;

public class UserLoginParserThread extends Thread {
	private User user;
	private String url = null;

	public UserLoginParserThread(User user, String url) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.url = url;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			JSONObject json = JSONUtility.getJSONFromUrl(url);
			UserLoginParser parser = new UserLoginParser(json, user);
			parser.parse();

			OnComplete();
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("Error Arised", e + "");
			OnError(e + "");
		}
	}

	public void OnComplete() {

	}

	public void OnError(String msg) {

	}

}
