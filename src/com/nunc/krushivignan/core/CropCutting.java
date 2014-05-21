package com.nunc.krushivignan.core;

import java.io.Serializable;

public class CropCutting implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cropCutting_ID;
	private String cce_UserName;
	private String cce_HarvestDate;
	private String cce_HarvestArea;
	private String cce_Fw_Pod_Fp;
	private String cce_Fw_Fodder_Fp;
	private String cce_Fw_Pod_Ip;
	private String cce_Fw_Fodder_Ip;
	private String cce_ssfw_Pod_Fp;
	private String cce_ssfw_Fodder_Fp;
	private String cce_ssfw_Pod_Ip;
	private String cce_ssfw_Fodder_Ip;
	private String cce_Image;
	private String sync_status;
	private String Farmer_Id;
	private String Field_Id;
	private String Latitude;
	private String Longitude;

	public CropCutting(CropCutting crop_Cutting) {
		this.cce_UserName = crop_Cutting.getCce_UserName();
		this.cce_HarvestDate = crop_Cutting.getCce_HarvestDate();
		this.cce_HarvestArea = crop_Cutting.getCce_HarvestArea();
		this.cropCutting_ID = crop_Cutting.getCropCutting_ID();
		this.cce_Fw_Pod_Fp = crop_Cutting.getCce_Fw_Pod_Fp();
		this.cce_Fw_Fodder_Fp = crop_Cutting.getCce_Fw_Fodder_Fp();
		this.cce_Fw_Pod_Ip = crop_Cutting.getCce_Fw_Pod_Ip();
		this.cce_Fw_Fodder_Ip = crop_Cutting.getCce_Fw_Fodder_Ip();
		this.cce_ssfw_Pod_Fp = crop_Cutting.getCce_ssfw_Fodder_Fp();
		this.cce_ssfw_Fodder_Fp = crop_Cutting.getCce_ssfw_Fodder_Fp();
		this.cce_ssfw_Pod_Ip = crop_Cutting.getCce_ssfw_Pod_Ip();
		this.cce_ssfw_Fodder_Ip = crop_Cutting.getCce_ssfw_Fodder_Ip();
		this.sync_status = crop_Cutting.getSync_status();
		this.Farmer_Id = crop_Cutting.getFarmer_Id();
		this.Field_Id = crop_Cutting.getField_Id();
		this.cce_Image = crop_Cutting.getCce_Image();
		this.Latitude = crop_Cutting.getLatitude();
		this.Longitude = crop_Cutting.getLongitude();
	}

	public CropCutting() {
		// TODO Auto-generated constructor stub
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getCce_Image() {
		return cce_Image;
	}

	public void setCce_Image(String cce_Image) {
		this.cce_Image = cce_Image;
	}

	public String getSync_status() {
		return sync_status;
	}

	public void setSync_status(String sync_status) {
		this.sync_status = sync_status;
	}

	public String getFarmer_Id() {
		return Farmer_Id;
	}

	public void setFarmer_Id(String farmer_Id) {
		Farmer_Id = farmer_Id;
	}

	public String getField_Id() {
		return Field_Id;
	}

	public void setField_Id(String field_Id) {
		Field_Id = field_Id;
	}

	public String getCropCutting_ID() {
		return cropCutting_ID;
	}

	public void setCropCutting_ID(String cropCutting_ID) {
		this.cropCutting_ID = cropCutting_ID;
	}

	public String getCce_UserName() {
		return cce_UserName;
	}

	public void setCce_UserName(String cce_UserName) {
		this.cce_UserName = cce_UserName;
	}

	public String getCce_HarvestDate() {
		return cce_HarvestDate;
	}

	public void setCce_HarvestDate(String cce_HarvestDate) {
		this.cce_HarvestDate = cce_HarvestDate;
	}

	public String getCce_HarvestArea() {
		return cce_HarvestArea;
	}

	public void setCce_HarvestArea(String cce_HarvestArea) {
		this.cce_HarvestArea = cce_HarvestArea;
	}

	public String getCce_Fw_Pod_Fp() {
		return cce_Fw_Pod_Fp;
	}

	public void setCce_Fw_Pod_Fp(String cce_Fw_Pod_Fp) {
		this.cce_Fw_Pod_Fp = cce_Fw_Pod_Fp;
	}

	public String getCce_Fw_Fodder_Fp() {
		return cce_Fw_Fodder_Fp;
	}

	public void setCce_Fw_Fodder_Fp(String cce_Fw_Fodder_Fp) {
		this.cce_Fw_Fodder_Fp = cce_Fw_Fodder_Fp;
	}

	public String getCce_Fw_Pod_Ip() {
		return cce_Fw_Pod_Ip;
	}

	public void setCce_Fw_Pod_Ip(String cce_Fw_Pod_Ip) {
		this.cce_Fw_Pod_Ip = cce_Fw_Pod_Ip;
	}

	public String getCce_Fw_Fodder_Ip() {
		return cce_Fw_Fodder_Ip;
	}

	public void setCce_Fw_Fodder_Ip(String cce_Fw_Fodder_Ip) {
		this.cce_Fw_Fodder_Ip = cce_Fw_Fodder_Ip;
	}

	public String getCce_ssfw_Pod_Fp() {
		return cce_ssfw_Pod_Fp;
	}

	public void setCce_ssfw_Pod_Fp(String cce_ssfw_Pod_Fp) {
		this.cce_ssfw_Pod_Fp = cce_ssfw_Pod_Fp;
	}

	public String getCce_ssfw_Fodder_Fp() {
		return cce_ssfw_Fodder_Fp;
	}

	public void setCce_ssfw_Fodder_Fp(String cce_ssfw_Fodder_Fp) {
		this.cce_ssfw_Fodder_Fp = cce_ssfw_Fodder_Fp;
	}

	public String getCce_ssfw_Pod_Ip() {
		return cce_ssfw_Pod_Ip;
	}

	public void setCce_ssfw_Pod_Ip(String cce_ssfw_Pod_Ip) {
		this.cce_ssfw_Pod_Ip = cce_ssfw_Pod_Ip;
	}

	public String getCce_ssfw_Fodder_Ip() {
		return cce_ssfw_Fodder_Ip;
	}

	public void setCce_ssfw_Fodder_Ip(String cce_ssfw_Fodder_Ip) {
		this.cce_ssfw_Fodder_Ip = cce_ssfw_Fodder_Ip;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
