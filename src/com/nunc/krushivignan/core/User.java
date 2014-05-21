package com.nunc.krushivignan.core;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userLoginStatus;
	private String userName;
	private String userId;
	private String district;
	private String hobli;
	private String taluk;
	private String village;
	private String role;

	private String lastPartialSyncTime = "NEVER";
	private String lastFullSyncTime = "NEVER";

	public User() {

	}

	public User(User user) {
		this.userLoginStatus = user.getUserLoginStatus();
		this.userName = user.getUserName();
		this.userId = user.getUserId();
		this.district = user.getDistrict();
		this.taluk = user.getTaluk();
		this.hobli=user.getHobli();
		this.village = user.getVillage();
	    this.role=user.getRole();
		this.lastPartialSyncTime = user.getLastPartialSyncTime();
		this.lastFullSyncTime = user.getLastFullSyncTime();
	}



	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getHobli() {
		return hobli;
	}

	public void setHobli(String hobli) {
		this.hobli = hobli;
	}

	public String getLastPartialSyncTime() {
		return lastPartialSyncTime;
	}

	public void setLastPartialSyncTime(String lastPartialSyncTime) {
		this.lastPartialSyncTime = lastPartialSyncTime;
	}

	public String getLastFullSyncTime() {
		return lastFullSyncTime;
	}

	public void setLastFullSyncTime(String lastFullSyncTime) {
		this.lastFullSyncTime = lastFullSyncTime;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getTaluk() {
		return taluk;
	}

	public void setTaluk(String taluk) {
		this.taluk = taluk;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getUserLoginStatus() {
		return userLoginStatus;
	}

	public void setUserLoginStatus(String userLoginStatus) {
		this.userLoginStatus = userLoginStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
