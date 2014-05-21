package com.nunc.krushivignan.core;

import java.io.Serializable;

public class Training implements Serializable {
	private static final long serialVersionUID = 1L;
	private String Trainingrecord_ID;
	private String Date;
	private String Traininglevel;
	private String User_name;
	private String training_topic;
	private String training_feedback;
	private String State;
	private String User_ID;
	private String District;
	private String Village;
	private String TrainerImage;
	private String Taluk;
	private String Hobli;
	private String Sync_Status;
	private String latitude;
	private String longitude;
	private String FarmerId;
	private String FieldId;
	private String male;
	private String female;
	public String getTraininglevel() {
		return Traininglevel;
	}
	public void setTraininglevel(String traininglevel) {
		Traininglevel = traininglevel;
	}
	public String getTrainingrecord_ID() {
		return Trainingrecord_ID;
	}
	public void setTrainingrecord_ID(String trainingrecord_ID) {
		Trainingrecord_ID = trainingrecord_ID;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getUser_name() {
		return User_name;
	}
	public void setUser_name(String user_name) {
		User_name = user_name;
	}
	public String getTraining_topic() {
		return training_topic;
	}
	public void setTraining_topic(String training_topic) {
		this.training_topic = training_topic;
	}
	public String getTraining_feedback() {
		return training_feedback;
	}
	public void setTraining_feedback(String training_feedback) {
		this.training_feedback = training_feedback;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getUser_ID() {
		return User_ID;
	}
	public void setUser_ID(String user_ID) {
		User_ID = user_ID;
	}
	public String getDistrict() {
		return District;
	}
	public void setDistrict(String district) {
		District = district;
	}
	public String getVillage() {
		return Village;
	}
	public void setVillage(String village) {
		Village = village;
	}
	public String getTrainerImage() {
		return TrainerImage;
	}
	public void setTrainerImage(String trainerImage) {
		TrainerImage = trainerImage;
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
	public String getMale() {
		return male;
	}
	public void setMale(String male) {
		this.male = male;
	}
	public String getFemale() {
		return female;
	}
	public void setFemale(String female) {
		this.female = female;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
