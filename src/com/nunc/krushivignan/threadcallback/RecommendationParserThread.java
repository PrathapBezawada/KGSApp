package com.nunc.krushivignan.threadcallback;

/**
 * @author Nildari Prasad
 * 
 */
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.FieldRecommendation;
import com.nunc.krushivignan.db.DBHelper;
import com.nunc.krushivignan.services.RecommendationParser;
import com.nunc.krushivignan.util.JSONUtility;

public class RecommendationParserThread {
	private ArrayList<FieldRecommendation> mRecommendation;
	private String url = null;
	private DBHelper db;

	public RecommendationParserThread(Context context,
			ArrayList<FieldRecommendation> mFarmer, String url) {
		// TODO Auto-generated constructor stub
		this.mRecommendation = mFarmer;
		this.url = url;
		db = ((KrushiVignanApp) ((Activity) context).getApplication())
				.getDatabase();
	}

	public void start() {
		// TODO Auto-generated method stub
		try {
			JSONObject json = JSONUtility.getJSONFromUrl(url.replace(" ","%20"));
			RecommendationParser parser = new RecommendationParser(json,
					mRecommendation);
			parser.parse();
			for (int i = 0; i < mRecommendation.size(); i++) {
				FieldRecommendation rec = mRecommendation.get(i);
				rec.setDistrict(AppInfo.CurrentUser.getDistrict());
				//rec.setTaluk(AppInfo.CurrentUser.getTaluk());
				rec.setId(UUID.randomUUID() + "");
				db.insertRecommendation(rec, AppInfo.CurrentUser.getUserId());
               
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("Error Arised", e + "");
		}
	}

}
