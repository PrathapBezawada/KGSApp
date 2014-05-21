package com.nunc.krushivignan.core;

import java.io.Serializable;

public class Villages implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String UserID;
	private String Village_Name,Hobli,Taluk;

	public String getTaluk() {
		return Taluk;
	}

	public void setTaluk(String taluk) {
		Taluk = taluk;
	}

	public Villages() {

	}

	public Villages(Villages field) {
		this.UserID = field.getUserID();
		this.Village_Name = field.getVillage_Name();
		this.Hobli = field.getHobli();
		this.Taluk=field.getTaluk();
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String farm_ID) {
		UserID = farm_ID;
	}

	public String getVillage_Name() {
		return Village_Name;
	}

	public void setVillage_Name(String farm_Name) {
		Village_Name = farm_Name;
	}

	public String getHobli() {
		return Hobli;
	}

	public void setHobli(String farm_Name) {
		Hobli = farm_Name;
	}
	}
