package com.nunc.krushivignan.services;

/**
 * @author Nildari Prasad
 * 
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nunc.krushivignan.core.User;

public class UserLoginParser {
	private static final String TAG_USER_LOGINSTATUS = "loginstatus";
	private static final String TAG_USER_USERNAME = "username";
	private static final String TAG_USER_USERID = "userid";
	private static final String TAG_USER_DISTRICT = "district";
	private static final String TAG_USER_TALUK = "taluk";
	private static final String TAG_USER_VILLAGE = "village";
	private static final String TAG_USER_HOBLI="hobli";
	private static final String TAG_USER_ROLE="role";
	private static final String TAG_USER_PARTIAL_SYNC_DATE = "Partial_Sync_Date";
	private static final String TAG_USER_FULL_SYNC_DATE = "Full_Sync_Date";

	private User user;
	private JSONObject object;
	JSONArray subjects = null;

	public UserLoginParser(JSONObject object, User user) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.object = object;
	}

	public void parse() {
		try {
			String usertLoginStatus = object.getString(TAG_USER_LOGINSTATUS);
			String username = object.getString(TAG_USER_USERNAME);
			String userid = object.getString(TAG_USER_USERID);
			String district = object.getString(TAG_USER_DISTRICT);
			String taluk = object.getString(TAG_USER_TALUK);
			String hobli=object.getString(TAG_USER_HOBLI);
			String role=object.getString(TAG_USER_ROLE);
			String village = null;
			try {
				village = object.getString(TAG_USER_VILLAGE);
			} catch (Exception e) {
				village = "";
			}
			String partialSync = null;
			try {
				partialSync = object.getString(TAG_USER_PARTIAL_SYNC_DATE);
			} catch (Exception e) {
				partialSync = "NEVER";
			}
			String fullSync = null;
			try {
				fullSync = object.getString(TAG_USER_FULL_SYNC_DATE);
			} catch (Exception e) {
				fullSync = "NEVER";
			}

			user.setUserLoginStatus(usertLoginStatus);
			user.setUserName(username);
			user.setUserId(userid);
			user.setDistrict(district);
			user.setTaluk(taluk);
			user.setVillage(village);
			user.setHobli(hobli);
			user.setRole(role);
			user.setLastPartialSyncTime(partialSync);
			user.setLastFullSyncTime(fullSync);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
