package com.nunc.krushivignan.core;

public class FarmerFieldSyncStatus {
	private String DataType;
	private String OldId;
	private String NewId;
	private String SyncStatus;

	public String getDataType() {
		return DataType;
	}

	public void setDataType(String dataType) {
		DataType = dataType;
	}

	public String getOldId() {
		return OldId;
	}

	public void setOldId(String oldId) {
		OldId = oldId;
	}

	public String getNewId() {
		return NewId;
	}

	public void setNewId(String newId) {
		NewId = newId;
	}

	public String getSyncStatus() {
		return SyncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		SyncStatus = syncStatus;
	}

}
