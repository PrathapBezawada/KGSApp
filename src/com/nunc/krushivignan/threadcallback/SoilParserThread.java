package com.nunc.krushivignan.threadcallback;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.SoilMaps;
import com.nunc.krushivignan.db.DBHelper;
import com.nunc.krushivignan.services.SoilParser;
import com.nunc.krushivignan.util.JSONUtility;

public class SoilParserThread {
	private ArrayList<SoilMaps> msoil;
	private String url = null;
	private Context context;
	private DBHelper db;

	public SoilParserThread(Context context, ArrayList<SoilMaps> msoil,
			String url) {
		// TODO Auto-generated constructor stub
		this.msoil = msoil;
		this.url = url;
		this.context = context;
		db = ((KrushiVignanApp) ((Activity) context).getApplication())
				.getDatabase();
	}

	public void start() {
		// TODO Auto-generated method stub
		try {

			JSONObject json = JSONUtility.getJSONFromUrl(url);
			SoilParser parser = new SoilParser(context, json, msoil);
			parser.parse();
			// //
			for (int i = 0; i < msoil.size(); i++) {
				SoilMaps map = msoil.get(i);
				map.setDistrictName(AppInfo.CurrentUser.getDistrict());
				map.setUserid(AppInfo.CurrentUser.getUserId());
				db.insertSoilMap(map);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("Error Arised", e + "");
		}
	}

	

}
