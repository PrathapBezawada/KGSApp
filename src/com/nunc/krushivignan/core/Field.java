package com.nunc.krushivignan.core;

import java.io.Serializable;

public class Field implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Farm_ID;
	private String Farm_Name;
	private String Fields;
	private String Farm_Area;
	private String Cult_Area;
	private String Crop_Name;
	private String Crop_Variety;
	private String Farm_Village;
	private String Created_Date;
	private String LastModified_Date;
	private String Sync_Status;
	private String farmerid;

	private String latitude;
	private String longitude;

	private String year;
	private String season;
	private String surveyno;
	private String sowingdate;
	private String seedrate;
	private String seedsource;
	private String urea;
	private String dap;
	private String potash;
	private String complex;
	private String zincsulphate;
	private String borax;
	private String gypsum;
	private String govtsubsidy;
	private String fieldimage;

	public Field() {

	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getSurveyno() {
		return surveyno;
	}

	public void setSurveyno(String surveyno) {
		this.surveyno = surveyno;
	}

	public String getSowingdate() {
		return sowingdate;
	}

	public void setSowingdate(String sowingdate) {
		this.sowingdate = sowingdate;
	}

	public String getSeedrate() {
		return seedrate;
	}

	public void setSeedrate(String seedrate) {
		this.seedrate = seedrate;
	}

	public String getSeedsource() {
		return seedsource;
	}

	public void setSeedsource(String seedsource) {
		this.seedsource = seedsource;
	}

	public String getUrea() {
		return urea;
	}

	public void setUrea(String urea) {
		this.urea = urea;
	}

	public String getDap() {
		return dap;
	}

	public void setDap(String dap) {
		this.dap = dap;
	}

	public String getPotash() {
		return potash;
	}

	public void setPotash(String potash) {
		this.potash = potash;
	}

	public String getComplex() {
		return complex;
	}

	public void setComplex(String complex) {
		this.complex = complex;
	}

	public String getZincsulphate() {
		return zincsulphate;
	}

	public void setZincsulphate(String zincsulphate) {
		this.zincsulphate = zincsulphate;
	}

	public String getBorax() {
		return borax;
	}

	public void setBorax(String borax) {
		this.borax = borax;
	}

	public String getGypsum() {
		return gypsum;
	}

	public void setGypsum(String gypsum) {
		this.gypsum = gypsum;
	}

	public String getGovtsubsidy() {
		return govtsubsidy;
	}

	public void setGovtsubsidy(String govtsubsidy) {
		this.govtsubsidy = govtsubsidy;
	}

	public String getFieldimage() {
		return fieldimage;
	}

	public void setFieldimage(String fieldimage) {
		this.fieldimage = fieldimage;
	}

	public Field(Field field) {
		this.Farm_ID = field.getFarm_ID();
		this.Farm_Name = field.getFarm_Name();
		this.Fields = field.getFields();
		this.Farm_Area = field.getFarm_Area();
		this.Cult_Area = field.getCult_Area();
		this.Crop_Name = field.getCrop_Name();
		this.Crop_Variety = field.getCrop_Variety();
		this.Farm_Village = field.getFarm_Village();
		this.Created_Date = field.getCreated_Date();
		this.LastModified_Date = field.getLastModified_Date();
		this.Sync_Status = field.getSync_Status();
		this.farmerid = field.getFarmerid();
		this.latitude = field.getLatitude();
		this.longitude = field.getLongitude();
		this.year=field.getYear();
		this.season  = field.getSeason();
		this.surveyno = field.getSurveyno();
		this.sowingdate = field.getSowingdate();
		this.seedrate = field.getSeedrate();
		this.seedsource = field.getSeedsource();
		this.urea = field.getUrea();
		this.dap=field.getDap();
		this.potash=field.getPotash();
		this.complex=field.getComplex();
		this.zincsulphate=field.getZincsulphate();
		this.borax=field.getBorax();
		this.gypsum=field.getGypsum();
		this.govtsubsidy=field.getGovtsubsidy();
		this.fieldimage=field.getFieldimage();
		
		
		
		
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

	public String getFarmerid() {
		return farmerid;
	}

	public void setFarmerid(String farmerid) {
		this.farmerid = farmerid;
	}

	public String getFarm_ID() {
		return Farm_ID;
	}

	public void setFarm_ID(String farm_ID) {
		Farm_ID = farm_ID;
	}

	public String getFarm_Name() {
		return Farm_Name;
	}

	public void setFarm_Name(String farm_Name) {
		Farm_Name = farm_Name;
	}

	public String getFields() {
		return Fields;
	}

	public void setFields(String fields) {
		Fields = fields;
	}

	public String getFarm_Area() {
		return Farm_Area;
	}

	public void setFarm_Area(String farm_Area) {
		Farm_Area = farm_Area;
	}

	public String getCult_Area() {
		return Cult_Area;
	}

	public void setCult_Area(String cult_Area) {
		Cult_Area = cult_Area;
	}

	public String getCrop_Name() {
		return Crop_Name;
	}

	public void setCrop_Name(String crop_Name) {
		Crop_Name = crop_Name;
	}

	public String getCrop_Variety() {
		return Crop_Variety;
	}

	public void setCrop_Variety(String crop_Variety) {
		Crop_Variety = crop_Variety;
	}

	public String getFarm_Village() {
		return Farm_Village;
	}

	public void setFarm_Village(String farm_Village) {
		Farm_Village = farm_Village;
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
