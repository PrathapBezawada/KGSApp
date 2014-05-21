package com.nunc.krushivignan.threadcallback;

/**
 * @author Nildari Prasad
 * 
 */
import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.nunc.krushivignan.core.Field;
import com.nunc.krushivignan.services.FieldParser;
import com.nunc.krushivignan.util.JSONUtility;

public class FieldParserThread {
	ArrayList<Field> mField;
	private String url = null;

	public FieldParserThread(ArrayList<Field> mField, String url) {
		// TODO Auto-generated constructor stub
		this.mField = mField;
		this.url = url;
	}

	public void start() {
		// TODO Auto-generated method stub
		try {
			JSONObject json = JSONUtility.getJSONFromUrl(url);
			FieldParser parser = new FieldParser(json, mField);
			parser.parse();
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("Error Arised", e + "");
		}
	}

}
