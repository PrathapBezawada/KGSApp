package com.nunc.krushivignan.core;

import java.io.Serializable;

public class Mapdata implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String latitude, longitude, title, description;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
