package com.nunc.krushivignan.services;

/**
 * @author Nildari Prasad
 * 
 */
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nunc.krushivignan.core.Field;

public class FieldParser {
	private static final String TAG_FARMERID = "farmerid",
			TAG_FIELDId = "fieldid", TAG_FARMNAME = "farm_name",
			TAG_FIELD = "fields", TAG_FARMAREA = "total_farm_area",
			TAG_CULTIVATION_AREA = "cultivation_area",
			TAG_CROPNAME = "crop_name",
			TAG_CROPVARIETY="crop_variety",
			TAG_VILLAGE = "village",
			TAG_YEAR="year",
			TAG_SEASON="season",
			TAG_SOWINGDATE="sowing_date",
			TAG_FIELD_IMAGE="field_image",
			TAG_GOVT_SUB="govtsubsidy",
			TAG_SEED_RATE="seed_rate",
	        TAG_SEED_SOURCE="seed_source",
	        TAG_UREA="field_urea",
	        TAG_DAP="field_dap",
	        TAG_POTASH="field_potash",
	        TAG_FIELD_COMPLEX="field_complex",
	        TAG_FIELD_ZINC="field_zinc",
	        TAG_FIELD_BORAX="field_borax",
	        TAG_FIELD_GYPSUM="field_gypsum",
			TAG_LATITUDE = "farm_lat", 
			TAG_LONGITUDE = "farm_long";
	private ArrayList<Field> mField;
	private JSONObject object;
	private JSONArray subjects = null;
	String farm_latitude, farm_longitude;

	public FieldParser(JSONObject object, ArrayList<Field> mField) {
		// TODO Auto-generated constructor stub
		this.mField = mField;
		this.object = object;
	}

	public void parse() {
		try {
			JSONArray array = object.getJSONArray("FieldListResult");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				String farmerId = obj.getString(TAG_FARMERID);
				String FieldID = obj.getString(TAG_FIELDId);
				String Farm_Name = obj.getString(TAG_FARMNAME);
				String Fields = obj.getString(TAG_FIELD);
				String Farm_Area = obj.getString(TAG_FARMAREA);
				String Cult_Area = obj.getString(TAG_CULTIVATION_AREA);
				String Crop_Name = obj.getString(TAG_CROPNAME);
				String Crop_variety=obj.getString(TAG_CROPVARIETY);
				String farm_Village = obj.getString(TAG_VILLAGE);
				String Year=obj.getString(TAG_VILLAGE);
				String Season=obj.getString(TAG_SEASON);
				String Sowing_Daye=obj.getString(TAG_SOWINGDATE);
				String Field_image=obj.getString(TAG_FIELD_IMAGE);
				String Govt_Sub=obj.getString(TAG_GOVT_SUB);
				String Seed_rate=obj.getString(TAG_SEED_RATE);
				String Seed_source=obj.getString(TAG_SEED_SOURCE);
				String Urea=obj.getString(TAG_UREA);
				String   Dap=obj.getString(TAG_DAP);
				String Potash=obj.getString(TAG_POTASH);
				String Field_Complex=obj.getString(TAG_FIELD_COMPLEX);
				String  Zinc=obj.getString(TAG_FIELD_ZINC);
				String  Borex=obj.getString(TAG_FIELD_BORAX);
				String  Gypsum=obj.getString(TAG_FIELD_GYPSUM);
				farm_latitude = obj.getString(TAG_LATITUDE);
				farm_longitude = obj.getString(TAG_LONGITUDE);
				Field mFieldObj = new Field();
				mFieldObj.setFarmerid(farmerId);
				mFieldObj.setFarm_ID(FieldID);
				mFieldObj.setFarm_Name(Farm_Name);
				mFieldObj.setFields(Fields);
				mFieldObj.setFarm_Area(Farm_Area);
				mFieldObj.setCult_Area(Cult_Area);
				mFieldObj.setCrop_Name(Crop_Name);
				mFieldObj.setCrop_Variety(Crop_variety);
				mFieldObj.setFarm_Village(farm_Village);
				mFieldObj.setYear(Year);
				mFieldObj.setSeason(Season);
				mFieldObj.setSowingdate(Sowing_Daye);
				mFieldObj.setFieldimage(Field_image);
				mFieldObj.setGovtsubsidy(Govt_Sub);
				mFieldObj.setSeedrate(Seed_rate);
				mFieldObj.setSeedsource(Seed_source);
				mFieldObj.setUrea(Urea);
				mFieldObj.setDap(Dap);
				mFieldObj.setPotash(Potash);
				mFieldObj.setComplex(Field_Complex);
				mFieldObj.setZincsulphate(Zinc);
				mFieldObj.setBorax(Borex);
				mFieldObj.setGypsum(Gypsum);
				mFieldObj.setSync_Status("1");
				mFieldObj.setLatitude(farm_latitude);
				mFieldObj.setLongitude(farm_longitude);
				if(FieldID.length() > 0){
				mField.add(mFieldObj);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
