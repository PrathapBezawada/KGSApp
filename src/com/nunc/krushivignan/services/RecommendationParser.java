package com.nunc.krushivignan.services;

/**
 * @author Nildari Prasad
 * 
 */
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.nunc.krushivignan.core.FieldRecommendation;

public class RecommendationParser {
	private static final String TAG_CROPTYPE = "crop_type",TAG_UREA="urea",TAG_DAP="dap",
			TAG_MOP="mop",TAG_GYPSUM="gypsum",TAG_ZINCSUL="zinc_sulphate",TAG_BORAX="borax",
			TAG_SSP_SSP="ssp",TAG_SSP_UREA="ssp_urea",TAG_SSP_GYPSUM="ssp_gypsum",TAG_SSP_DAP
			="ssp_dap",TAG_TALUK="taluk";
	private ArrayList<FieldRecommendation> mRecommendation;
	private JSONObject object;
	JSONArray subjects = null;

	public RecommendationParser(JSONObject object, ArrayList<FieldRecommendation> mField) {
		// TODO Auto-generated constructor stub
		this.mRecommendation =mField;
		this.object = object;
	}

	public void parse() {
		try {
				JSONArray array=object.getJSONArray("CropRecommendations");
				for(int i=0;i<array.length();i++){
				 JSONObject obj=array.getJSONObject(i);	
				
					 String CropType=obj.getString(TAG_CROPTYPE);
					 String Urea=obj.getString(TAG_UREA);
					 String DAP=obj.getString(TAG_DAP);
					 String MOP=obj.getString(TAG_MOP);
					 String Gypsum=obj.getString(TAG_GYPSUM);
					 String ZinSulphate=obj.getString(TAG_ZINCSUL);
					 String Borax=obj.getString(TAG_BORAX);
					 String Ssp_Ssp=obj.getString(TAG_SSP_SSP);
					 String Ssp_Urea=obj.getString(TAG_SSP_UREA);
					 String Ssp_Gypsum=obj.getString(TAG_SSP_GYPSUM);
					 String Ssp_Dap=obj.getString(TAG_SSP_DAP);
					 String taluk=obj.getString(TAG_TALUK);
					 
					 FieldRecommendation mRecommendationObj=new FieldRecommendation();
					mRecommendationObj.setCropType(CropType);
					mRecommendationObj.setUrea(Urea);
					mRecommendationObj.setDap(DAP);
					mRecommendationObj.setMop(MOP);
					mRecommendationObj.setGypsum(Gypsum);
					mRecommendationObj.setZinc_sulphate(ZinSulphate);
					mRecommendationObj.setBorax(Borax);
					mRecommendationObj.setSsp_ssp(Ssp_Ssp);
					mRecommendationObj.setSsp_urea(Ssp_Urea);
					mRecommendationObj.setSsp_gypsum(Ssp_Gypsum);
					mRecommendationObj.setSsp_dap(Ssp_Dap);
					mRecommendationObj.setTaluk(taluk);
					mRecommendation.add(mRecommendationObj);
					Log.e("CropId",""+CropType);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
}
