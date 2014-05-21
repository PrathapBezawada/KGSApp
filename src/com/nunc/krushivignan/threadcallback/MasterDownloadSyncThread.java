package com.nunc.krushivignan.threadcallback;

import java.util.ArrayList;

import android.content.Context;

import com.nunc.krushivignan.core.Farmer;
import com.nunc.krushivignan.core.FieldRecommendation;
import com.nunc.krushivignan.core.SoilMaps;
import com.nunc.krushivignan.core.Villages;

public class MasterDownloadSyncThread extends Thread {
	private String recommendationURL;
	private String soilMappingURL;
	private String VillageURL;
	private Context context;
	private String farmerURL;

	public MasterDownloadSyncThread(Context context, String farmerURL,
			String recommendationURL, String soilMappingURL, String VillageURL) {
		this.recommendationURL = recommendationURL;
		this.soilMappingURL = soilMappingURL;
		this.VillageURL = VillageURL;
		this.context = context;
		this.farmerURL = farmerURL;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			fetchSoilMapping(soilMappingURL);
			fetchRecommendations(recommendationURL);
			fetchVillage(VillageURL);
			fetchFarmerField(farmerURL);
			OnComplete();
		} catch (Exception e) {
			OnError(e + "");
		}
	}

	private void fetchFarmerField(String farmerURL) {
		ArrayList<Farmer> list = new ArrayList<Farmer>();
		FarmeParserThread thread = new FarmeParserThread(context,farmerURL, list);
		thread.start();
	}

	private void fetchVillage(String VillageURL) {
		ArrayList<Villages> list = new ArrayList<Villages>();
		VillageParserThread thread = new VillageParserThread(context, list,
				VillageURL);
		thread.start();
	}

	private void fetchSoilMapping(String soilMappingURL) {
		ArrayList<SoilMaps> list = new ArrayList<SoilMaps>();

		SoilParserThread parser = new SoilParserThread(context, list,
				soilMappingURL);
		parser.start();
	}

	private void fetchRecommendations(String recommendationURL) {
		ArrayList<FieldRecommendation> list = new ArrayList<FieldRecommendation>();

		RecommendationParserThread parser = new RecommendationParserThread(
				context, list, recommendationURL);
		parser.start();

	}

	public void OnComplete() {

	}

	public void OnError(String msg) {

	}
}
