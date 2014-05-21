package com.nunc.krushivignan.threadcallback;

/**
 * @author Nildari Prasad
 * 
 */
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.Farmer;
import com.nunc.krushivignan.core.Field;
import com.nunc.krushivignan.core.ServerURL;
import com.nunc.krushivignan.db.DBHelper;
import com.nunc.krushivignan.services.FarmerParser;
import com.nunc.krushivignan.util.JSONUtility;

public class FarmeParserThread {

	private String url = null;
	private Context context;
	private DBHelper db;
	private ArrayList<Farmer> mFarmer;

	public FarmeParserThread(Context context, String url,
			ArrayList<Farmer> mFarmer) {
		// TODO Auto-generated constructor stub
		this.url = url;
		db = ((KrushiVignanApp) ((Activity) context).getApplication())
				.getDatabase();
		this.context = context;
	}

	public void start() {
		// TODO Auto-generated method stub
		try {
			mFarmer = new ArrayList<Farmer>();
			JSONObject json = JSONUtility.getJSONFromUrl(url);
			FarmerParser parser = new FarmerParser(context, json, mFarmer);
			parser.parse();

			for (int i = 0; i < mFarmer.size(); i++) {
				long row = db.insertFarmer(mFarmer.get(i),
						AppInfo.CurrentUser.getUserId(), "1");
				Log.e("row", "" + row);
			}
			fetchFields();
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

	public void fetchFields() {

		ArrayList<Field> mField = new ArrayList<Field>();
		String URL = ServerURL.getFieldByUserURL(AppInfo.CurrentUser
				.getUserId());

		FieldParserThread threadfield = new FieldParserThread(mField, URL);
		threadfield.start();
		for (int j = 0; j < mField.size(); j++) {
			Field field = mField.get(j);
			db.insertField(field, field.getFarmerid(), "1",
					AppInfo.CurrentUser.getUserId());
		}

	}

}
