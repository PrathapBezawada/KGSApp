package com.nunc.krushivignan.core;

public class SoilMaps {
	private String districtName;
	private String userid;
	private String cropname;
	private String cropUrl;
	

	public String getCropname() {
		return cropname;
	}

	public void setCropname(String cropname) {
		this.cropname = cropname;
	}

	public String getCropUrl() {
		return cropUrl;
	}

	public void setCropUrl(String cropUrl) {
		this.cropUrl = cropUrl;
	}

	public SoilMaps() {

	}

	public SoilMaps(SoilMaps maps) {
		districtName = maps.getDistrictName();
		userid = maps.getUserid();
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	

}
