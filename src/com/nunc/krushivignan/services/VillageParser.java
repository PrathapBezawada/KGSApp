package com.nunc.krushivignan.services;

/**
 * @author Nildari Prasad
 * 
 */
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.nunc.krushivignan.core.Villages;

public class VillageParser {
	private static final String TAG_USERID = "UserId",
			TAG_VILLAGE_NAME = "village";

	ArrayList<Villages> mVillage;
	private JSONObject object;
	JSONArray subjects = null;
	private Context context;

	public VillageParser(JSONObject object, ArrayList<Villages> mFarmer) {
		// TODO Auto-generated constructor stub
		this.mVillage = mFarmer;
		this.object = object;
	}

		public void parse() {
			try {
			JSONArray array = object.getJSONArray("VillageListResult");
			for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			String village = obj.getString(TAG_VILLAGE_NAME);
			String hobli = obj.getString("hobli");
			String taluk=obj.getString("taluk");
			/*String[] villageArr = village.split(",");
			for (int j = 0; j < villageArr.length; j++) {
			Villages vill = new Villages();
			vill.setVillage_Name(villageArr[j]);
			vill.setHobli(hobli);

			mVillage.add(vill);
			}*/
			Villages vill= new Villages();
			vill.setVillage_Name(village);
			vill.setHobli(hobli);
			vill.setTaluk(taluk);
			mVillage.add(vill);
				}

		} catch (JSONException e) {
			e.printStackTrace();

		}
	}

}
