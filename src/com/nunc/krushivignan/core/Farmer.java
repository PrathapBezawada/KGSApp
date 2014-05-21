package com.nunc.krushivignan.core;

import java.io.Serializable;

public class Farmer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Farmer_ID;
	private String First_Name;
	private String Last_Name;
	private String Father_Name;
	private String Mobile_No;
	private String Telephone_No;
	private String Caste;
	private String Sex;
	private String District;
	private String Taluk;
	private String Hobli;
	private String Village;
	private String Created_Date;
	private String LastModified_Date;
	private String Sync_Status;
	private String Farmer_Image;

	private String Total_Acre;
	private String Rain_Fed;
	private String Irrigation;
	private String Plantation;
	private String Fallow;
	private String Survey_No;

	private String latitude;
	private String longitude;

	private String id_type;
	private String id_no;

	public Farmer() {
		// TODO Auto-generated constructor stub
	}

	public Farmer(Farmer farmer) {
		Farmer_ID = farmer.getFarmer_ID();
		First_Name = farmer.getFirst_Name();
		Last_Name = farmer.getLast_Name();
		Father_Name = farmer.getLast_Name();
		Father_Name = farmer.getFather_Name();
		Mobile_No = farmer.getMobile_No();
		Caste = farmer.getCaste();
		Sex = farmer.getSex();
		District = farmer.getDistrict();
		Taluk = farmer.getTaluk();
		Hobli = farmer.getHobli();
		Village = farmer.getVillage();
		Created_Date = farmer.getCreated_Date();
		LastModified_Date = farmer.getLastModified_Date();
		Sync_Status = farmer.getSync_Status();
		Farmer_Image = farmer.getFarmer_Image();
		Total_Acre = farmer.getTotal_Acre();
		Rain_Fed = farmer.getRain_Fed();
		Irrigation = farmer.getIrrigation();
		Plantation = farmer.getPlantation();
		Fallow = farmer.getFallow();
		Survey_No = farmer.getSurvey_No();
		latitude = farmer.getLatitude();
		longitude = farmer.getLongitude();
		id_type = farmer.getId_type();
		id_no = farmer.getId_no();
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getId_no() {
		return id_no;
	}

	public void setId_no(String id_no) {
		this.id_no = id_no;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getTotal_Acre() {
		return Total_Acre;
	}

	public void setTotal_Acre(String total_Acre) {
		Total_Acre = total_Acre;
	}

	public String getRain_Fed() {
		return Rain_Fed;
	}

	public void setRain_Fed(String rain_Fed) {
		Rain_Fed = rain_Fed;
	}

	public String getIrrigation() {
		return Irrigation;
	}

	public void setIrrigation(String irrigation) {
		Irrigation = irrigation;
	}

	public String getPlantation() {
		return Plantation;
	}

	public void setPlantation(String plantation) {
		Plantation = plantation;
	}

	public String getFallow() {
		return Fallow;
	}

	public void setFallow(String fallow) {
		Fallow = fallow;
	}

	public String getSurvey_No() {
		return Survey_No;
	}

	public void setSurvey_No(String survey_No) {
		Survey_No = survey_No;
	}

	public String getFarmer_Image() {
		return Farmer_Image;
	}

	public void setFarmer_Image(String farmer_Image) {
		Farmer_Image = farmer_Image;
	}

	public String getFarmer_ID() {
		return Farmer_ID;
	}

	public void setFarmer_ID(String farmer_ID) {
		Farmer_ID = farmer_ID;
	}

	public String getFirst_Name() {
		return First_Name;
	}

	public void setFirst_Name(String first_Name) {
		First_Name = first_Name;
	}

	public String getLast_Name() {
		return Last_Name;
	}

	public void setLast_Name(String last_Name) {
		Last_Name = last_Name;
	}

	public String getFather_Name() {
		return Father_Name;
	}

	public void setFather_Name(String father_Name) {
		Father_Name = father_Name;
	}

	public String getMobile_No() {
		return Mobile_No;
	}

	public void setMobile_No(String mobile_No) {
		Mobile_No = mobile_No;
	}

	public String getTelephone_No() {
		return Telephone_No;
	}

	public void setTelephone_No(String telephone_No) {
		Telephone_No = telephone_No;
	}

	public String getCaste() {
		return Caste;
	}

	public void setCaste(String caste) {
		Caste = caste;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getDistrict() {
		return District;
	}

	public void setDistrict(String district) {
		District = district;
	}

	public String getTaluk() {
		return Taluk;
	}

	public void setTaluk(String taluk) {
		Taluk = taluk;
	}

	public String getHobli() {
		return Hobli;
	}

	public void setHobli(String hobli) {
		Hobli = hobli;
	}

	public String getVillage() {
		return Village;
	}

	public void setVillage(String village) {
		Village = village;
	}

	public String getCreated_Date() {
		return Created_Date;
	}

	public void setCreated_Date(String created_Date) {
		Created_Date = created_Date;
	}

	public String getLastModified_Date() {
		return LastModified_Date;
	}

	public void setLastModified_Date(String lastModified_Date) {
		LastModified_Date = lastModified_Date;
	}

	public String getSync_Status() {
		return Sync_Status;
	}

	public void setSync_Status(String sync_Status) {
		Sync_Status = sync_Status;
	}

}
