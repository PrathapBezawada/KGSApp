package com.nunc.krushivignan.app;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.nunc.krushivignan.db.DBHelper;

public class KrushiVignanApp extends Application {

	private DBHelper db = null;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			db = new DBHelper(this);
			db.open();

		} catch (Exception e) {
			Log.i("Exception", e + "");
		}

	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		db.close();
		super.onTerminate();
	}

	public DBHelper getDatabase() {
		return db;
	}

}
