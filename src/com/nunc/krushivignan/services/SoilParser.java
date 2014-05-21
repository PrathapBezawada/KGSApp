package com.nunc.krushivignan.services;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.SoilMaps;

public class SoilParser {
	private static final String TAG_CROPNAME = "cropname",
			TAG_CROPURL = "cropurl";
	private ArrayList<SoilMaps> msoil;
	private JSONObject object;
	JSONArray subjects = null;
	Context context;

	public SoilParser(Context context, JSONObject json, ArrayList<SoilMaps> soil) {
		// TODO Auto-generated constructor stub
		this.msoil = soil;
		this.object = json;
		this.context = context;
	}

	public void parse() {
		try {
			JSONArray array = object.getJSONArray("SoilMaps");
			for (int i = 0; i < array.length(); i++) {

				JSONObject obj = array.getJSONObject(i);
				String CropName = obj.getString(TAG_CROPNAME);
				String CropURL = AppInfo.downloadImage(context,
						obj.getString(TAG_CROPURL));
				SoilMaps mSoilObj = new SoilMaps();
				mSoilObj.setCropname(CropName);
				mSoilObj.setCropUrl(CropURL);
				msoil.add(mSoilObj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
