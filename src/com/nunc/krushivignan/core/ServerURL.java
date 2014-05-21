package com.nunc.krushivignan.core;

public class ServerURL {
	private static String SERVER_URL = "http://www.krishigyansagar.com//web/";

	public static String getLoginURL(String userName, String password) {
		return SERVER_URL + "loginresponse.php?username=" + userName
				+ "&password=" + password;
		}

	public static String getFarmerURL(String userid) {
		return SERVER_URL + "farmerdata.php?userid=" + userid;
	}

	public static String getFieldURL(String farmerID) {
		return SERVER_URL + "fielddata.php?farmerid=" + farmerID;
	}

	public static String getRecommendationURL(String district, String taluk) {
		return SERVER_URL + "recommendationsdata.php?district=" + district
				+ "&taluk=" + taluk;
	}

	public static String getVillageURL(String userid) {
		return SERVER_URL + "villagedata.php?userid=" + userid;
	}

	public static String getSoilMappingURL(String district) {
		return SERVER_URL + "soilmap.php?district=" + district;
	}

	public static String getFieldByUserURL(String userid) {
		return SERVER_URL + "userfields.php?userid=" + userid;
	}

	public static String getSyncURL() {
		return SERVER_URL + "xml_response.php";
	}
	public static String getFieldVisitSyncURL() {
		return SERVER_URL + "fieldvisit.php";
	}
	public static String getTrainingreportSyncURL() {
		return SERVER_URL + "training.php";
	}
	public static String getCropCuttingSyncURL() {
		return SERVER_URL + "cropcuttingexperiment.php";
	}
}
