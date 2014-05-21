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

import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.Farmer;

public class FarmerParser {
	private static final String TAG_FARMER_ID = "farmerid",
			TAG_FRIST_NAME = "fname", TAG_LAST_NAME = "lname",
			TAG_FATHER_NAME = "fathername", TAG_MOBILE = "mobile",
			TAG_TELEPHONE = "telephone", TAG_ADDRESS = "address",
			TAG_DISTRICT = "district", TAG_TALUK = "taluk",
			TAG_HOBLI = "hobli", TAG_VILLAGE = "village", TAG_CASTE = "caste",
			TAG_GENDER="gender",
			TAG_PINCODE = "pincode", TAG_AREA = "area_total",
			TAG_RAINFD = "rainfed", TAG_IRRIGATE = "irrigated",
			TAG_PLANTATION = "plantation", TAG_FOLLOW = "fallow",
			TAG_SURVEYNO = "surveyno", TAG_DATE = "date",
			TAG_MODIFIEDBY = "modifiedby", TAG_MODIFIED_DATE = "modified_date",
			TAG_CREATEDBY = "createdby", TAG_PHOTO = "Photo",
			TAG_PHOTOID = "photo_id", TAG_LAT = "Loc-Lat",
			TAG_LONG = "Loc-Long", TAG_LOC_ALTITUDE = "Loc-Altitude",
			TAG_ACCURACY = "Loc-Accuracy",
			TAG_IDTYPE="ID_type",TAG_IDNO="ID_no";
	ArrayList<Farmer> mFarmer;
	private JSONObject object;
	JSONArray subjects = null;
	private Context context;

	public FarmerParser(Context context, JSONObject object,
			ArrayList<Farmer> mFarmer) {
		// TODO Auto-generated constructor stub
		this.mFarmer = mFarmer;
		this.object = object;
		this.context = context;
	}

	public void parse() {
		try {
			JSONArray array = object.getJSONArray("FarmerListResult");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				String Farmer_ID = obj.getString(TAG_FARMER_ID);
				String First_Name = obj.getString(TAG_FRIST_NAME).trim();
				String Last_Name = obj.getString(TAG_LAST_NAME).trim();
				String Father_Name = obj.getString(TAG_FATHER_NAME);
				String Mobile_No = obj.getString(TAG_MOBILE);
				String Telephone_No = obj.getString(TAG_TELEPHONE);
				String Caste = obj.getString(TAG_CASTE);
				String Gender = obj.getString(TAG_GENDER);
				String District = obj.getString(TAG_DISTRICT);
				String Taluk = obj.getString(TAG_TALUK);
				String Hobli = obj.getString(TAG_HOBLI);
				String Village = obj.getString(TAG_VILLAGE);
				String Created_Date = obj.getString(TAG_CREATEDBY);
				String LastModified_Date = obj.getString(TAG_MODIFIED_DATE);
				String createdDate=obj.getString(TAG_DATE);
				String Farmer_Image = AppInfo.downloadImage(context,
						obj.getString(TAG_PHOTO));
				String Total_Area = obj.getString(TAG_AREA);
				String Rain_Fed = obj.getString(TAG_RAINFD);
				String Irrigation = obj.getString(TAG_IRRIGATE);
				String Plantation = obj.getString(TAG_PLANTATION);
				String Fallow = obj.getString(TAG_FOLLOW);
				String Survey_No = obj.getString(TAG_SURVEYNO);

				 String Loc_Lat = obj.getString(TAG_LAT);
				String Loc_Long = obj.getString(TAG_LONG);
				String Addreas = obj.getString(TAG_ADDRESS);

				String idtype=obj.getString(TAG_IDTYPE);
				String idno=obj.getString(TAG_IDNO);
				Farmer farmer = new Farmer();
				farmer.setFarmer_ID(Farmer_ID);
				farmer.setFirst_Name(First_Name);
				farmer.setLast_Name(Last_Name);
				farmer.setFather_Name(Father_Name);
				farmer.setMobile_No(Mobile_No);
				farmer.setTelephone_No(Telephone_No);
				farmer.setCaste(Caste);
				farmer.setSex(Gender);
				farmer.setDistrict(District);
				farmer.setTaluk(Taluk);
				farmer.setHobli(Hobli);
				farmer.setVillage(Village);
				farmer.setCreated_Date(Created_Date);
				farmer.setLastModified_Date(LastModified_Date);
				farmer.setFarmer_Image(Farmer_Image);
				farmer.setTotal_Acre(Total_Area);
				farmer.setRain_Fed(Rain_Fed);
				farmer.setIrrigation(Irrigation);
				farmer.setPlantation(Plantation);
				farmer.setFallow(Fallow);
				farmer.setSurvey_No(Survey_No);
				farmer.setCreated_Date(Created_Date);
				
				farmer.setSync_Status("1");
				
				farmer.setLatitude(Loc_Lat);
				farmer.setLongitude(Loc_Long);
				farmer.setId_type(idtype);
				farmer.setId_no(idno);
//				farmer.setSex("Male");
				if (Farmer_ID.length() > 0){
					mFarmer.add(farmer);
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();

		}
	}

	

	

}
