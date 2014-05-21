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
import com.nunc.krushivignan.core.Villages;
import com.nunc.krushivignan.db.DBHelper;
import com.nunc.krushivignan.services.VillageParser;
import com.nunc.krushivignan.util.JSONUtility;

public class VillageParserThread {
	ArrayList<Villages> mVillage;
	private String url = null;
	private DBHelper db;

	public VillageParserThread(Context context, ArrayList<Villages> mField,
			String url) {
		// TODO Auto-generated constructor stub
		this.mVillage = mField;
		this.url = url;
		db = ((KrushiVignanApp) ((Activity) context).getApplication())
				.getDatabase();
	}

	public void start() {
		// TODO Auto-generated method stub
		try {
			JSONObject json = JSONUtility.getJSONFromUrl(url);
			VillageParser parser = new VillageParser(json, mVillage);
			parser.parse();
		//	db.deleteVillage();
			for (int i = 0; i < mVillage.size(); i++) {
				db.insertVillage(mVillage.get(i),
						AppInfo.CurrentUser.getUserId());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("Error Arised", e + "");
		}
	}

}
