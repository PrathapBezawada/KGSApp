package com.nunc.krushivignan.core;

import java.io.Serializable;

public class FieldVisit implements Serializable {
	private static final long serialVersionUID = 1L;
	private String FieldVisit_ID;
	private String Purpose;
	private String User_name;
	private String Recommendation;
	private String Observation;
	private String State;
	private String User_ID;
	private String District;
	private String Village;
	private String VisitorImage;
	private String Taluk;
	private String Hobli;
	private String Date;
	private String Sync_Status;
	private String latitude;
	private String longitude;
	private String FarmerId;
	private String FieldId;
	private Boolean isFromList;
	private String VisitType;

	public FieldVisit() {

	}

	public FieldVisit(FieldVisit fieldvisit) {
		this.FieldVisit_ID = fieldvisit.getFieldVisit_ID();
		this.Purpose = fieldvisit.getPurpose();
		this.User_name = fieldvisit.getUser_name();
		this.State = fieldvisit.getState();
		this.Taluk = fieldvisit.getTaluk();
		this.Village = fieldvisit.getVillage();
		this.District = fieldvisit.getDistrict();
		this.Date = fieldvisit.getDate();
		this.VisitorImage = fieldvisit.getVisitorImage();
		this.Sync_Status = fieldvisit.getSync_Status();
		this.latitude = fieldvisit.getLatitude();
		this.longitude = fieldvisit.getLongitude();
		this.VisitType = fieldvisit.getVisitType();
		this.FarmerId = fieldvisit.getFarmerId();
		this.FieldId = fieldvisit.getFieldId();
		this.isFromList = fieldvisit.getIsFromList();
		this.Recommendation = fieldvisit.getRecommendation();
		this.Observation = fieldvisit.getObservation();
		this.User_ID = fieldvisit.getUser_ID();
		this.Hobli=fieldvisit.getHobli();
	}

	public String getHobli() {
		return Hobli;
	}

	public void setHobli(String hobli) {
		Hobli = hobli;
	}

	public String getUser_ID() {
		return User_ID;
	}

	public void setUser_ID(String user_ID) {
		User_ID = user_ID;
	}

	public String getRecommendation() {
		return Recommendation;
	}

	public void setRecommendation(String recommendation) {
		Recommendation = recommendation;
	}

	public String getObservation() {
		return Observation;
	}

	public void setObservation(String observation) {
		Observation = observation;
	}

	public String getVisitType() {
		return VisitType;
	}

	public void setVisitType(String visitType) {
		VisitType = visitType;
	}

	public String getFarmerId() {
		return FarmerId;
	}

	public void setFarmerId(String farmerId) {
		FarmerId = farmerId;
	}

	public String getFieldId() {
		return FieldId;
	}

	public void setFieldId(String fieldId) {
		FieldId = fieldId;
	}

	public Boolean getIsFromList() {
		return isFromList;
	}

	public void setIsFromList(Boolean isFromList) {
		this.isFromList = isFromList;
	}

	public String getVisitorImage() {
		return VisitorImage;
	}

	public void setVisitorImage(String visitorImage) {
		VisitorImage = visitorImage;
	}

	public String getVillage() {
		return Village;
	}

	public void setVillage(String village) {
		Village = village;
	}

	public String getFieldVisit_ID() {
		return FieldVisit_ID;
	}

	public void setFieldVisit_ID(String fieldVisit_ID) {
		FieldVisit_ID = fieldVisit_ID;
	}

	public String getPurpose() {
		return Purpose;
	}

	public void setPurpose(String purpose) {
		Purpose = purpose;
	}

	public String getUser_name() {
		return User_name;
	}

	public void setUser_name(String user_name) {
		User_name = user_name;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
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

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getSync_Status() {
		return Sync_Status;
	}

	public void setSync_Status(String sync_Status) {
		Sync_Status = sync_Status;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
