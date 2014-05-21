package com.nunc.krushivignan.db;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nunc.krushivignan.core.CropCutting;
import com.nunc.krushivignan.core.Farmer;
import com.nunc.krushivignan.core.Field;
import com.nunc.krushivignan.core.FieldRecommendation;
import com.nunc.krushivignan.core.FieldVisit;
import com.nunc.krushivignan.core.SoilMaps;
import com.nunc.krushivignan.core.Training;
import com.nunc.krushivignan.core.User;
import com.nunc.krushivignan.core.Villages;

public class DBHelper {

	private static final String DATABASE_NAME = "APP_DB.db";

	private static final int DATABASE_VERSION = 1;

	private static String Table_Farmer = "CREATE TABLE IF NOT EXISTS [Farmer] ([Farmer_ID] TEXT,[First_Name] TEXT,[Last_Name] TEXT,[Father_Name] TEXT,[Mobile_No] TEXT, [Telephone_No] TEXT, [Caste] TEXT,[Sex] TEXT,[District] TEXT,[Taluk] TEXT,[Hobli] TEXT,[Village] TEXT,[Created_Date] TEXT,[LastModified_Date] TEXT,[Sync_Status] INTEGER,[Farmer_Image] TEXT,[Total_Acre] TEXT,[Rain_Fed] TEXT,[Irrigated] TEXT,[Plantation] TEXT DEFAULT 0,[Fallow] TEXT,[Survey_No] TEXT,[USERID] TEXT,[Latitude] TEXT,[Longitude] TEXT,[id_type] TEXT, [id_no] TEXT)";
	private static String Table_Field = "CREATE TABLE IF NOT EXISTS [Field] ([Farm_ID] TEXT,[Farm_Name] TEXT,[Fields] TEXT,[Farm_Area] TEXT,[Cult_Area] TEXT,[Crop_Name] TEXT,[Crop_Variety] TEXT,[Farm_Village] TEXT,[Created_Date] TEXT,[LastModified_Date] TEXT,[Sync_Status] TEXT,[Farmer_ID] TEXT,[USERID] TEXT,[latitude] TEXT,[longitude] TEXT,[year] TEXT,[season] TEXT,[surveyno] TEXT,[sowingdate] TEXT, [seedrate] TEXT,[seedsource] TEXT,[urea] TEXT,[dap] TEXT,[potash] TEXT,[complex] TEXT,[zincsulphate] TEXT,[borax] TEXT,[gypsum] TEXT,[govtsubsidy] TEXT,[fieldimage] TEXT)";
	private static String Table_FieldRecommendation = "CREATE TABLE IF NOT EXISTS [FieldRecommendation] ([Id] TEXT,[District] TEXT,[Taluk] TEXT,[Croptype] TEXT,[Urea] TEXT,[DAP] TEXT,[MOP] TEXT,[Gypsum] TEXT,[Zinc_Sulphate] TEXT,[Borax] TEXT, [Farm_ID] TEXT,[USERID] TEXT,[Ssp_Ssp] TEXT,[Ssp_Gypsum] TEXT,[Ssp_Urea] TEXT,[Ssp_Dap] TEXT)";
	private static String Table_Recomendation = "CREATE TABLE IF NOT EXISTS [Recommendation] ([Id] TEXT,[District] TEXT,[Taluk] TEXT,[Croptype] TEXT,[Urea] TEXT,[DAP] TEXT,[MOP] TEXT,[Gypsum] TEXT,[Zinc_Sulphate] TEXT,[Borax] TEXT,[USERID] TEXT,[Ssp_Ssp] TEXT,[Ssp_Gypsum] TEXT,[Ssp_Urea] TEXT,[Ssp_Dap] TEXT,CONSTRAINT [sqlite_autoindex_Recommendation_1] PRIMARY KEY ([Id]))";
	private static String Table_SoilMapping = "CREATE TABLE IF NOT EXISTS [SoilMapping] ([USERID] TEXT,[ITEM_NAME] TEXT,[LOCAL_MAP_IMAGE_PATH] TEXT,[DISTRICT] TEXT)";
	public static String Table_Village = "CREATE TABLE IF NOT EXISTS [Village] ([USERID] TEXT, [Village_Name] TEXT, [Hobli] TEXT, [Taluk]TEXT)";
	public static String Table_fieldVisit = "CREATE TABLE IF NOT EXISTS [FieldVisit] ([Id] TEXT, [USERID] TEXT, [VisitType] TEXT, [Purpose] TEXT, [User_Name] TEXT, [State] TEXT, [District] TEXT, [Taluk] TEXT, [Hobli] TEXT, [Village] TEXT, [Date] TEXT, [Latitude] TEXT, [Longitude] TEXT,[FarmerID] TEXT, [FieldID] TEXT, [isExist] TEXT, [Recommendation] TEXT, [Observation] TEXT, [Sync_Status] INTEGER,[Visitor_Image] TEXT)";
	public static String Table_cropCutting = "CREATE TABLE IF NOT EXISTS [CropCutting] ([Id] TEXT,[User_Name] TEXT, [Harvest_Date] TEXT, [Harvest_Area] TEXT, [Fw_Pod_Fp] TEXT, [Fw_Fodder_Fp] TEXT, [Fw_Pod_Ip] TEXT, [Fw_Fodder_Ip] TEXT, [Ssfw_Pod_Fp] TEXT, [Ssfw_Fodder_Fp] TEXT, [Ssfw_Pod_Ip] TEXT, [Ssfw_Fodder_Ip] TEXT, [Latitude] TEXT, [Longitude] TEXT, [Farmer_ID] TEXT, [Farm_ID] TEXT, [Sync_Status] INTEGER,[Cce_Image] TEXT)";
	public static String Table_Trining = "CREATE TABLE IF NOT EXISTS [TrainingLevel] ([Id] TEXT, [USERID] TEXT, [User_Name] TEXT, [Male] TEXT, [Female] TEXT, [State] TEXT,  [District] TEXT, [Taluk] TEXT, [Hobli] TEXT, [Village] TEXT, [Date] TEXT, [Latitude] TEXT, [Longitude] TEXT,[Traininglevel] TEXT, [TrainingTopic] TEXT, [TrainingFeedback] TEXT, [Sync_Status] INTEGER,[Trainer_Image] TEXT)";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase myDataBase;

	public DBHelper(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(Table_Farmer);
			db.execSQL(Table_Field);
			db.execSQL(Table_FieldRecommendation);
			db.execSQL(Table_Recomendation);
			db.execSQL(Table_SoilMapping);
			db.execSQL(Table_Village);
			db.execSQL(Table_fieldVisit);
			db.execSQL(Table_cropCutting);
			db.execSQL(Table_Trining);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS Farmer");
			db.execSQL("DROP TABLE IF EXISTS Field");
			db.execSQL("DROP TABLE IF EXISTS FieldRecommendation");
			db.execSQL("DROP TABLE IF EXISTS Recommendation");
			db.execSQL("DROP TABLE IF EXISTS SoilMapping");
			db.execSQL("DROP TABLE IF EXISTS Village");
			db.execSQL("DROP TABLE IF EXISTS FieldVisit");
			db.execSQL("DROP TABLE IF EXISTS CropCutting");
			db.execSQL("DROP TABLE IF EXISTS TrainingLevel");

			onCreate(db);
		}
	}

	// ---opens the database---
	public DBHelper open() throws SQLException {
		myDataBase = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	public void clearSyncData(User user) {
		myDataBase.delete(DatabaseStaticField.DATABASE_TABLE_FIELD,
				DatabaseStaticField.KEY_FARMER_USERID + "=?",
				new String[] { user.getUserId() });
		myDataBase.delete(DatabaseStaticField.DATABASE_TABLE_FARMER,
				DatabaseStaticField.KEY_FARMER_USERID + "=?",
				new String[] { user.getUserId() });
		myDataBase.delete("SoilMapping", "USERID=?",
				new String[] { user.getUserId() });
		myDataBase.delete("Village", "USERID=?",
				new String[] { user.getUserId() });
		myDataBase.delete("Recommendation", "USERID=?",
				new String[] { user.getUserId() });
		myDataBase.delete("FieldRecommendation", "USERID=?",
				new String[] { user.getUserId() });
	}

	public void updateFarmerPostSync(String oldId, String newId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseStaticField.KEY_FARMER_ID, newId);
		initialValues.put(DatabaseStaticField.KEY_FARMER_SYNC_STATUS, "1");
		boolean farmerUpdateStatus = myDataBase.update(
				DatabaseStaticField.DATABASE_TABLE_FARMER, initialValues,
				DatabaseStaticField.KEY_FARMER_ID + "=?",
				new String[] { oldId }) > 0;
		boolean fieldUpdateStatus = myDataBase.update(
				DatabaseStaticField.DATABASE_TABLE_FIELD, initialValues,
				DatabaseStaticField.KEY_FARMER_ID + "=?",
				new String[] { oldId }) > 0;

	}

	public void updateFieldPostSync(String farmerId, String oldId, String newId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseStaticField.KEY_FIELD_FARM_ID, newId);
		initialValues.put(DatabaseStaticField.KEY_FARMER_SYNC_STATUS, "1");
		boolean fieldUpdateStatus = myDataBase.update(
				DatabaseStaticField.DATABASE_TABLE_FIELD, initialValues,
				DatabaseStaticField.KEY_FIELD_FARM_ID + "=? and "
						+ DatabaseStaticField.KEY_FARMER_ID + "=?",
				new String[] { oldId, farmerId }) > 0;

	}

	public void updateCropCuttingPostSync(String farmerId, String oldId,
			String newId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseStaticField.KEY_CCE_FIELDID, newId);
		initialValues.put(DatabaseStaticField.KEY__CCE_FARMERID, farmerId);
		boolean fieldUpdateStatus = myDataBase.update(
				DatabaseStaticField.DATABASE_TABLE_CCE, initialValues,
				DatabaseStaticField.KEY_CCE_FIELDID + "=?",
				new String[] { oldId }) > 0;

	}

	public void updateFieldVisitPostSync(String farmerId, String oldId,
			String newId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseStaticField.KEY_FIELDID, newId);
		initialValues.put(DatabaseStaticField.KEY_FARMERID, farmerId);
		boolean fieldUpdateStatus = myDataBase.update(
				DatabaseStaticField.DATABASE_TABLE_FieldVisit, initialValues,
				DatabaseStaticField.KEY_FIELDID + "=?", new String[] { oldId }) > 0;

	}

	public boolean getCCEStatus(String fieldID) {
		String SQL = "Select count(*) as Count from CropCutting where Farm_ID=?";
		Cursor c = myDataBase.rawQuery(SQL, new String[] { fieldID });
		int Count = 0;
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			Count = Integer.parseInt(c.getString(c.getColumnIndex("Count")));
		}
		c.close();
		return Count > 0;
	}

	public void updateCropCuttingSyncStatus(String oldId, String newId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseStaticField.KEY_CCE_SyncStatus, "1");
		initialValues.put(DatabaseStaticField.KEY_CROPCUTTING_ID, newId);
		boolean fieldUpdateStatus = myDataBase.update(
				DatabaseStaticField.DATABASE_TABLE_CCE, initialValues,
				DatabaseStaticField.KEY_CROPCUTTING_ID + "=?",
				new String[] { oldId }) > 0;

	}
	public void updateTrainingReportSyncStatus(String oldId, String newId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_SyncStatus, "1");
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_ID, newId);
		boolean fieldUpdateStatus = myDataBase.update(
				DatabaseStaticField.DATABASE_TABLE_TRAINING, initialValues,
				DatabaseStaticField.KEY_TRAININGLEVEL_ID + "=?",
				new String[] { oldId }) > 0;

	}


	public void updateFieldVisitSyncStatus(String userName) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseStaticField.KEY_CCE_SyncStatus, "1");
		boolean fieldUpdateStatus = myDataBase.update(
				DatabaseStaticField.DATABASE_TABLE_FieldVisit, initialValues,
				DatabaseStaticField.KEY_FIELDVISIT_User_Name + "=?",
				new String[] { userName }) > 0;

	}

	public void clearFieldVisitData(User user) {
		myDataBase.delete("FieldVisit", "USERID=?",
				new String[] { user.getUserId() });
	}

	public boolean updateFarmer(Farmer farmer) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseStaticField.KEY_FARMER_CASTE,
				farmer.getCaste());
		initialValues.put(DatabaseStaticField.KEY_FARMER_DISTRICT,
				farmer.getDistrict());

		initialValues.put(DatabaseStaticField.KEY_FARMER_FATHER_NAME,
				farmer.getFather_Name());
		initialValues.put(DatabaseStaticField.KEY_FARMER_FIRST_NAME,
				farmer.getFirst_Name());
		initialValues.put(DatabaseStaticField.KEY_FARMER_HOBLI,
				farmer.getHobli());
		initialValues.put(DatabaseStaticField.KEY_FARMER_LAST_NAME,
				farmer.getLast_Name());
		initialValues.put(DatabaseStaticField.KEY_FARMER_MOBILE_NO,
				farmer.getMobile_No());
		initialValues.put(DatabaseStaticField.KEY_FARMER_SEX, farmer.getSex());
		initialValues.put(DatabaseStaticField.KEY_FARMER_TALUK,
				farmer.getTaluk());
		initialValues.put(DatabaseStaticField.KEY_FARMER_TELEPHONE_NO,
				farmer.getTelephone_No());
		initialValues.put(DatabaseStaticField.KEY_FARMER_VILLAGE,
				farmer.getVillage());
		initialValues.put(DatabaseStaticField.KEY_FARMER_MODIFIED,
				farmer.getLastModified_Date());
		initialValues.put(DatabaseStaticField.KEY_FARMER_IMAGE,
				farmer.getFarmer_Image());

		initialValues.put(DatabaseStaticField.KEY_FARMER_TOTAL_ACRE,
				farmer.getTotal_Acre());
		initialValues.put(DatabaseStaticField.KEY_FARMER_RAINFED,
				farmer.getRain_Fed());
		initialValues.put(DatabaseStaticField.KEY_FARMER_IRRIGATION,
				farmer.getIrrigation());
		initialValues.put(DatabaseStaticField.KEY_FARMER_PLANTATION,
				farmer.getPlantation());
		initialValues.put(DatabaseStaticField.KEY_FARMER_FALLOW,
				farmer.getFallow());
		initialValues.put(DatabaseStaticField.KEY_FARMER_SURVEY_NO,
				farmer.getSurvey_No());
		initialValues.put(DatabaseStaticField.KEY_FARMER_ID_TYPE,
				farmer.getId_type());
		initialValues.put(DatabaseStaticField.KEY_FARMER_ID_NO,
				farmer.getId_no());
		initialValues.put(DatabaseStaticField.KEY_FARMER_SYNC_STATUS, "0");
		return myDataBase.update(DatabaseStaticField.DATABASE_TABLE_FARMER,
				initialValues, DatabaseStaticField.KEY_FARMER_ID + "=?",
				new String[] { farmer.getFarmer_ID() }) > 0;
	}

	public long insertFarmer(Farmer farmer, String userid, String syncStatus) {
		long rowNo = 0;
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseStaticField.KEY_FARMER_USERID, userid);
		initialValues.put(DatabaseStaticField.KEY_FARMER_ID,
				farmer.getFarmer_ID());
		initialValues.put(DatabaseStaticField.KEY_FARMER_CASTE,
				farmer.getCaste());
		initialValues.put(DatabaseStaticField.KEY_FARMER_DISTRICT,
				farmer.getDistrict());

		initialValues.put(DatabaseStaticField.KEY_FARMER_FATHER_NAME,
				farmer.getFather_Name());
		initialValues.put(DatabaseStaticField.KEY_FARMER_FIRST_NAME,
				farmer.getFirst_Name());
		initialValues.put(DatabaseStaticField.KEY_FARMER_HOBLI,
				farmer.getHobli());
		initialValues.put(DatabaseStaticField.KEY_FARMER_LAST_NAME,
				farmer.getLast_Name());
		initialValues.put(DatabaseStaticField.KEY_FARMER_MOBILE_NO,
				farmer.getMobile_No());
		initialValues.put(DatabaseStaticField.KEY_FARMER_SEX, farmer.getSex());
		initialValues.put(DatabaseStaticField.KEY_FARMER_TALUK,
				farmer.getTaluk());
		initialValues.put(DatabaseStaticField.KEY_FARMER_TELEPHONE_NO,
				farmer.getTelephone_No());
		initialValues.put(DatabaseStaticField.KEY_FARMER_VILLAGE,
				farmer.getVillage());
		initialValues.put(DatabaseStaticField.KEY_FARMER_SYNC_STATUS,
				farmer.getSync_Status());
		initialValues.put(DatabaseStaticField.KEY_FARMER_CREATED,
				farmer.getCreated_Date());
		initialValues.put(DatabaseStaticField.KEY_FARMER_MODIFIED,
				farmer.getLastModified_Date());
		initialValues.put(DatabaseStaticField.KEY_FARMER_IMAGE,
				farmer.getFarmer_Image());

		initialValues.put(DatabaseStaticField.KEY_FARMER_TOTAL_ACRE,
				farmer.getTotal_Acre());
		initialValues.put(DatabaseStaticField.KEY_FARMER_RAINFED,
				farmer.getRain_Fed());
		initialValues.put(DatabaseStaticField.KEY_FARMER_IRRIGATION,
				farmer.getIrrigation());
		initialValues.put(DatabaseStaticField.KEY_FARMER_PLANTATION,
				farmer.getPlantation());
		initialValues.put(DatabaseStaticField.KEY_FARMER_FALLOW,
				farmer.getFallow());
		initialValues.put(DatabaseStaticField.KEY_FARMER_SURVEY_NO,
				farmer.getSurvey_No());
		initialValues.put(DatabaseStaticField.KEY_FARMER_SYNC_STATUS,
				syncStatus);
		initialValues.put(DatabaseStaticField.KEY_FARMER_ID_TYPE,
				farmer.getId_type());
		initialValues.put(DatabaseStaticField.KEY_FARMER_ID_NO,
				farmer.getId_no());
		initialValues.put("Latitude", farmer.getLatitude());
		initialValues.put("Longitude", farmer.getLongitude());
		rowNo = myDataBase.insert(DatabaseStaticField.DATABASE_TABLE_FARMER,
				null, initialValues);

		return rowNo;
	}

	// Field Visit
	public long insertFieldVisit(FieldVisit fieldVisit, String user_id,
			String syncStatus) {
		long rowNo = 0;
		ContentValues initialValues = new ContentValues();

		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_ID,
				fieldVisit.getFieldVisit_ID());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_UserID,
				fieldVisit.getUser_ID());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_Purpose,
				fieldVisit.getPurpose());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_State,
				fieldVisit.getState());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_User_Name,
				fieldVisit.getUser_name());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_District,
				fieldVisit.getDistrict());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_Taluk,
				fieldVisit.getTaluk());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_Hobli,
				fieldVisit.getTaluk());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_Village,
				fieldVisit.getVillage());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_Latitude,
				fieldVisit.getLatitude());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_Longitude,
				fieldVisit.getLongitude());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_Date,
				fieldVisit.getDate());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_TYPE,
				fieldVisit.getVisitType());

		initialValues.put(DatabaseStaticField.KEY_FARMERID,
				fieldVisit.getFarmerId());
		initialValues.put(DatabaseStaticField.KEY_FIELDID,
				fieldVisit.getFieldId());
		initialValues.put(DatabaseStaticField.KEY_ISFROMLIST,
				fieldVisit.getIsFromList());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_RECOMMENDATION,
				fieldVisit.getRecommendation());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_OBSERVATION,
				fieldVisit.getObservation());
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_SyncStatus,
				syncStatus);
		initialValues.put(DatabaseStaticField.KEY_FIELDVISIT_VisitorImage,
				fieldVisit.getVisitorImage());
		rowNo = myDataBase.insert(
				DatabaseStaticField.DATABASE_TABLE_FieldVisit, null,
				initialValues);

		return rowNo;
	}

	// insert Training Record

	public long insertTrainingRecord(Training training, String user_id,
			String syncStatus) {
		long rowNo = 0;
		ContentValues initialValues = new ContentValues();

		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_ID,
				training.getTrainingrecord_ID());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_UserID,
				training.getUser_ID());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_Training,
				training.getTraininglevel());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_User_Name,
				training.getUser_name());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_State,
				training.getState());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_District,
				training.getDistrict());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_Taluk,
				training.getTaluk());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_Hobli,
				training.getHobli());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_Village,
				training.getVillage());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_Latitude,
				training.getLatitude());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_Longitude,
				training.getLongitude());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_Date,
				training.getDate());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_Male,
				training.getMale());

		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_Female,
				training.getFemale());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_TOPIC,
				training.getTraining_topic());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_FEEDBACK,
				training.getTraining_feedback());
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_SyncStatus,
				syncStatus);
		initialValues.put(DatabaseStaticField.KEY_TRAININGLEVEL_TrainerImage,
				training.getTrainerImage());
		rowNo = myDataBase.insert(DatabaseStaticField.DATABASE_TABLE_TRAINING,
				null, initialValues);

		return rowNo;
	}

	public CropCutting getCropCuttingDetails(String sql) {
		Cursor c = myDataBase.rawQuery(sql, null);
		CropCutting crop_Cutting = new CropCutting();
		ArrayList<CropCutting> CCEList = new ArrayList<CropCutting>();
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			do {
				crop_Cutting
						.setCropCutting_ID(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CROPCUTTING_ID)));
				crop_Cutting.setCce_UserName(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_CCE_USERNAME)));

				crop_Cutting
						.setCce_HarvestDate(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_HARVEST_DATE)));
				crop_Cutting
						.setCce_HarvestArea(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_HARVEST_AREA)));
				crop_Cutting
						.setCce_Fw_Pod_Fp(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_FW_POD_FP)));
				crop_Cutting
						.setCce_Fw_Fodder_Fp(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_FW_FODDER_FP)));
				crop_Cutting
						.setCce_Fw_Pod_Ip(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_FW_POD_IP)));
				crop_Cutting
						.setCce_Fw_Fodder_Ip(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_FW_FODDER_IP)));
				crop_Cutting
						.setCce_ssfw_Pod_Fp(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_SSFW_POD_FP)));
				crop_Cutting
						.setCce_ssfw_Fodder_Fp(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_SSFW_FODDER_FP)));
				crop_Cutting
						.setCce_ssfw_Pod_Ip(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_SSFW_POD_IP)));
				crop_Cutting
						.setCce_ssfw_Fodder_Ip(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_SSFW_FODDER_IP)));
				crop_Cutting.setLatitude(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_CCE_LATITUDE)));
				crop_Cutting
						.setLongitude(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_LONGITUDE)));
				crop_Cutting
						.setFarmer_Id(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY__CCE_FARMERID)));
				crop_Cutting.setField_Id(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_CCE_FIELDID)));
				crop_Cutting
						.setSync_status(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_SyncStatus)));
				crop_Cutting.setCce_Image(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_CCE_Image)));
				CCEList.add(crop_Cutting);

			} while (c.moveToNext());

		}
		c.close();
		return crop_Cutting;
	}

	// cropcutting Experiment
	public ArrayList<CropCutting> getCropCuttingData(String sql) {
		Cursor c = myDataBase.rawQuery(sql, null);
		CropCutting crop_Cutting;
		ArrayList<CropCutting> CCEList = new ArrayList<CropCutting>();
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			do {
				crop_Cutting = new CropCutting();
				crop_Cutting
						.setCropCutting_ID(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CROPCUTTING_ID)));
				crop_Cutting.setCce_UserName(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_CCE_USERNAME)));

				crop_Cutting
						.setCce_HarvestDate(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_HARVEST_DATE)));
				crop_Cutting
						.setCce_HarvestArea(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_HARVEST_AREA)));
				crop_Cutting
						.setCce_Fw_Pod_Fp(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_FW_POD_FP)));
				crop_Cutting
						.setCce_Fw_Fodder_Fp(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_FW_FODDER_FP)));
				crop_Cutting
						.setCce_Fw_Pod_Ip(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_FW_POD_IP)));
				crop_Cutting
						.setCce_Fw_Fodder_Ip(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_FW_FODDER_IP)));
				crop_Cutting
						.setCce_ssfw_Pod_Fp(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_SSFW_POD_FP)));
				crop_Cutting
						.setCce_ssfw_Fodder_Fp(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_SSFW_FODDER_FP)));
				crop_Cutting
						.setCce_ssfw_Pod_Ip(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_SSFW_POD_IP)));
				crop_Cutting
						.setCce_ssfw_Fodder_Ip(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_SSFW_FODDER_IP)));
				crop_Cutting.setLatitude(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_CCE_LATITUDE)));
				crop_Cutting
						.setLongitude(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_LONGITUDE)));
				crop_Cutting
						.setFarmer_Id(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY__CCE_FARMERID)));
				crop_Cutting.setField_Id(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_CCE_FIELDID)));
				crop_Cutting
						.setSync_status(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_CCE_SyncStatus)));
				crop_Cutting.setCce_Image(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_CCE_Image)));
				CCEList.add(crop_Cutting);

			} while (c.moveToNext());

		}
		c.close();
		return CCEList;
	}

	public long insertCropCuttingData(CropCutting crop_Cutting, String user_id,
			String farmerID, String FieldId, int syncStatus) {
		long rowNo = 0;
		ContentValues initialValues = new ContentValues();

		initialValues.put(DatabaseStaticField.KEY_CROPCUTTING_ID,
				crop_Cutting.getCropCutting_ID());
		initialValues.put(DatabaseStaticField.KEY_CCE_USERNAME,
				crop_Cutting.getCce_UserName());
		initialValues.put(DatabaseStaticField.KEY_CCE_HARVEST_DATE,
				crop_Cutting.getCce_HarvestDate());
		initialValues.put(DatabaseStaticField.KEY_CCE_HARVEST_AREA,
				crop_Cutting.getCce_HarvestArea());
		initialValues.put(DatabaseStaticField.KEY_CCE_FW_POD_FP,
				crop_Cutting.getCce_Fw_Pod_Fp());
		initialValues.put(DatabaseStaticField.KEY_CCE_FW_FODDER_FP,
				crop_Cutting.getCce_Fw_Fodder_Fp());
		initialValues.put(DatabaseStaticField.KEY_CCE_FW_POD_IP,
				crop_Cutting.getCce_Fw_Pod_Ip());
		initialValues.put(DatabaseStaticField.KEY_CCE_FW_FODDER_IP,
				crop_Cutting.getCce_Fw_Fodder_Ip());
		initialValues.put(DatabaseStaticField.KEY_CCE_SSFW_POD_FP,
				crop_Cutting.getCce_ssfw_Pod_Fp());
		initialValues.put(DatabaseStaticField.KEY_CCE_SSFW_FODDER_FP,
				crop_Cutting.getCce_ssfw_Fodder_Fp());
		initialValues.put(DatabaseStaticField.KEY_CCE_SSFW_POD_IP,
				crop_Cutting.getCce_ssfw_Pod_Ip());
		initialValues.put(DatabaseStaticField.KEY_CCE_SSFW_FODDER_IP,
				crop_Cutting.getCce_ssfw_Fodder_Ip());
		initialValues.put(DatabaseStaticField.KEY_CCE_LATITUDE,
				crop_Cutting.getLatitude());
		initialValues.put(DatabaseStaticField.KEY_CCE_LONGITUDE,
				crop_Cutting.getLongitude());
		initialValues.put(DatabaseStaticField.KEY__CCE_FARMERID,
				crop_Cutting.getFarmer_Id());
		initialValues.put(DatabaseStaticField.KEY_CCE_FIELDID,
				crop_Cutting.getField_Id());
		initialValues.put(DatabaseStaticField.KEY_CCE_SyncStatus, syncStatus);
		initialValues.put(DatabaseStaticField.KEY_CCE_Image,
				crop_Cutting.getCce_Image());
		rowNo = myDataBase.insert(DatabaseStaticField.DATABASE_TABLE_CCE, null,
				initialValues);

		return rowNo;
	}

	public ArrayList<Farmer> getFarmers(String sql) {
		Cursor c = myDataBase.rawQuery(sql, null);
		Farmer farmer;
		ArrayList<Farmer> farmerList = new ArrayList<Farmer>();
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			do {
				farmer = new Farmer();
				farmer.setFarmer_ID(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_ID)));
				farmer.setFirst_Name(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_FIRST_NAME)));
				farmer.setLast_Name(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_LAST_NAME)));
				farmer.setFather_Name(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_FATHER_NAME)));
				farmer.setCaste(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_CASTE)));
				farmer.setDistrict(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_DISTRICT)));
				farmer.setHobli(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_HOBLI)));
				farmer.setMobile_No(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_MOBILE_NO)));
				farmer.setSex(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_SEX)));
				farmer.setTaluk(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_TALUK)));
				farmer.setTelephone_No(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_TELEPHONE_NO)));
				farmer.setVillage(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_VILLAGE)));
				farmer.setFarmer_Image(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_IMAGE)));

				farmer.setTotal_Acre(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_TOTAL_ACRE)));
				farmer.setRain_Fed(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_RAINFED)));
				farmer.setIrrigation(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_IRRIGATION)));
				farmer.setPlantation(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_PLANTATION)));
				farmer.setFallow(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_FALLOW)));
				farmer.setSurvey_No(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_SURVEY_NO)));

				farmer.setCreated_Date(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_CREATED)));
				farmer.setLastModified_Date(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_MODIFIED)));
				farmer.setSync_Status(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_SYNC_STATUS)));

				farmer.setId_type(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_ID_TYPE)));

				farmer.setId_no(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_ID_NO)));

				farmer.setLatitude(c.getString(c.getColumnIndex("Latitude")));
				farmer.setLongitude(c.getString(c.getColumnIndex("Longitude")));

				farmerList.add(farmer);
			} while (c.moveToNext());

		}
		c.close();
		return farmerList;
	}

	public Boolean updateCropCutting(CropCutting crop_Cutting) {
		long rowNo = 0;
		ContentValues initialValues = new ContentValues();

		// initialValues.put(DatabaseStaticField.KEY_CROPCUTTING_ID,
		// crop_Cutting.getCropCutting_ID());
		initialValues.put(DatabaseStaticField.KEY_CCE_USERNAME,
				crop_Cutting.getCce_UserName());
		initialValues.put(DatabaseStaticField.KEY_CCE_HARVEST_DATE,
				crop_Cutting.getCce_HarvestDate());
		initialValues.put(DatabaseStaticField.KEY_CCE_HARVEST_AREA,
				crop_Cutting.getCce_HarvestArea());
		initialValues.put(DatabaseStaticField.KEY_CCE_FW_POD_FP,
				crop_Cutting.getCce_Fw_Pod_Fp());
		initialValues.put(DatabaseStaticField.KEY_CCE_FW_FODDER_FP,
				crop_Cutting.getCce_Fw_Fodder_Fp());
		initialValues.put(DatabaseStaticField.KEY_CCE_FW_POD_IP,
				crop_Cutting.getCce_Fw_Pod_Ip());
		initialValues.put(DatabaseStaticField.KEY_CCE_FW_FODDER_IP,
				crop_Cutting.getCce_Fw_Fodder_Ip());
		initialValues.put(DatabaseStaticField.KEY_CCE_SSFW_POD_FP,
				crop_Cutting.getCce_ssfw_Pod_Fp());
		initialValues.put(DatabaseStaticField.KEY_CCE_SSFW_FODDER_FP,
				crop_Cutting.getCce_ssfw_Fodder_Fp());
		initialValues.put(DatabaseStaticField.KEY_CCE_SSFW_POD_IP,
				crop_Cutting.getCce_ssfw_Pod_Ip());
		initialValues.put(DatabaseStaticField.KEY_CCE_SSFW_FODDER_IP,
				crop_Cutting.getCce_ssfw_Fodder_Ip());
		initialValues.put(DatabaseStaticField.KEY_CCE_LATITUDE,
				crop_Cutting.getLatitude());
		initialValues.put(DatabaseStaticField.KEY_CCE_LONGITUDE,
				crop_Cutting.getLongitude());
		initialValues.put(DatabaseStaticField.KEY__CCE_FARMERID,
				crop_Cutting.getFarmer_Id());
		initialValues.put(DatabaseStaticField.KEY_CCE_FIELDID,
				crop_Cutting.getField_Id());
		initialValues.put(DatabaseStaticField.KEY_CCE_SyncStatus, "0");
		initialValues.put(DatabaseStaticField.KEY_CCE_Image,
				crop_Cutting.getCce_Image());

		return myDataBase.update(DatabaseStaticField.DATABASE_TABLE_CCE,
				initialValues, DatabaseStaticField.KEY_CROPCUTTING_ID + "=?",
				new String[] { crop_Cutting.getCropCutting_ID() }) > 0;
	}

	// /////////////////////////////////////////FEILDS//////////////////////////////////////////////////////////////

	public boolean updateField(Field field) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(DatabaseStaticField.KEY_FIELD_FARM_NAME,
				field.getFarm_Name());

		initialValues.put(DatabaseStaticField.KEY_FIELD_FIELDS,
				field.getFields());
		initialValues.put(DatabaseStaticField.KEY_FIELD_FARM_AREA,
				field.getFarm_Area());
		initialValues.put(DatabaseStaticField.KEY_FIELD_CULT_AREA,
				field.getCult_Area());
		initialValues.put(DatabaseStaticField.KEY_FIELD_CROP_NAME,
				field.getCrop_Name());
		initialValues.put(DatabaseStaticField.KEY_FIELD_CROP_VARIETY,
				field.getCrop_Variety());
		initialValues.put(DatabaseStaticField.KEY_FIELD_FARM_VILLAGE,
				field.getFarm_Village());

		initialValues.put(DatabaseStaticField.KEY_FIELD_LASTMODIFIED_DATE,
				field.getLastModified_Date());
		initialValues.put(DatabaseStaticField.KEY_FIELD_SYNC_STATUS, "0");
		initialValues.put(DatabaseStaticField.KEY_FIELD_YEAR, field.getYear());
		initialValues.put(DatabaseStaticField.KEY_FIELD_SEASON,
				field.getSeason());
		initialValues.put(DatabaseStaticField.KEY_FIELD_SURVEYNO,
				field.getSurveyno());
		initialValues.put(DatabaseStaticField.KEY_FIELD_SOWINGDATE,
				field.getSowingdate());
		initialValues.put(DatabaseStaticField.KEY_FIELD_SEEDRATE,
				field.getSeedrate());
		initialValues.put(DatabaseStaticField.KEY_FIELD_SEEDSOURCE,
				field.getSeedsource());
		initialValues.put(DatabaseStaticField.KEY_FIELD_UREA, field.getUrea());
		initialValues.put(DatabaseStaticField.KEY_FIELD_DAP, field.getDap());
		initialValues.put(DatabaseStaticField.KEY_FIELD_POTASH,
				field.getPotash());
		initialValues.put(DatabaseStaticField.KEY_FIELD_COMPLEX,
				field.getComplex());
		initialValues.put(DatabaseStaticField.KEY_FIELD_ZINCSULPHATE,
				field.getZincsulphate());
		initialValues
				.put(DatabaseStaticField.KEY_FIELD_BORAX, field.getBorax());
		initialValues.put(DatabaseStaticField.KEY_FIELD_GYPSUM,
				field.getGypsum());
		initialValues.put(DatabaseStaticField.KEY_FIELD_GOVTSUBSIDY,
				field.getGovtsubsidy());
		initialValues.put(DatabaseStaticField.KEY_FIELD_FIELDIMAGE,
				field.getFieldimage());

		return myDataBase.update(DatabaseStaticField.DATABASE_TABLE_FIELD,
				initialValues, DatabaseStaticField.KEY_FIELD_FARM_ID + "=?",
				new String[] { field.getFarm_ID() }) > 0;
	}

	public long insertField(Field field, String Farmer_ID, String syncStatus,
			String User_ID) {
		long rowNo = 0;
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseStaticField.KEY_FIELD_FARM_ID,
				field.getFarm_ID());
		initialValues.put(DatabaseStaticField.KEY_FIELD_FARM_NAME,
				field.getFarm_Name());

		initialValues.put(DatabaseStaticField.KEY_FIELD_FIELDS,
				field.getFields());
		initialValues.put(DatabaseStaticField.KEY_FIELD_FARM_AREA,
				field.getFarm_Area());
		initialValues.put(DatabaseStaticField.KEY_FIELD_CULT_AREA,
				field.getCult_Area());
		initialValues.put(DatabaseStaticField.KEY_FIELD_CROP_NAME,
				field.getCrop_Name());
		initialValues.put(DatabaseStaticField.KEY_FIELD_CROP_VARIETY,
				field.getCrop_Variety());
		initialValues.put(DatabaseStaticField.KEY_FIELD_FARM_VILLAGE,
				field.getFarm_Village());
		initialValues.put(DatabaseStaticField.KEY_FIELD_CREATED_DATE,
				field.getCreated_Date());
		initialValues.put(DatabaseStaticField.KEY_FIELD_LASTMODIFIED_DATE,
				field.getLastModified_Date());
		initialValues
				.put(DatabaseStaticField.KEY_FIELD_SYNC_STATUS, syncStatus);
		initialValues.put(DatabaseStaticField.KEY_FARMER_ID, Farmer_ID);
		initialValues.put(DatabaseStaticField.KEY_FARMER_USERID, User_ID);

		initialValues.put(DatabaseStaticField.KEY_FIELD_LATITUDE,
				field.getLatitude());
		initialValues.put(DatabaseStaticField.KEY_FIELD_LONGITUDE,
				field.getLongitude());

		initialValues.put(DatabaseStaticField.KEY_FIELD_YEAR, field.getYear());
		initialValues.put(DatabaseStaticField.KEY_FIELD_SEASON,
				field.getSeason());
		initialValues.put(DatabaseStaticField.KEY_FIELD_SURVEYNO,
				field.getSurveyno());
		initialValues.put(DatabaseStaticField.KEY_FIELD_SOWINGDATE,
				field.getSowingdate());
		initialValues.put(DatabaseStaticField.KEY_FIELD_SEEDRATE,
				field.getSeedrate());
		initialValues.put(DatabaseStaticField.KEY_FIELD_SEEDSOURCE,
				field.getSeedsource());
		initialValues.put(DatabaseStaticField.KEY_FIELD_UREA, field.getUrea());
		initialValues.put(DatabaseStaticField.KEY_FIELD_DAP, field.getDap());
		initialValues.put(DatabaseStaticField.KEY_FIELD_POTASH,
				field.getPotash());
		initialValues.put(DatabaseStaticField.KEY_FIELD_COMPLEX,
				field.getComplex());
		initialValues.put(DatabaseStaticField.KEY_FIELD_ZINCSULPHATE,
				field.getZincsulphate());
		initialValues
				.put(DatabaseStaticField.KEY_FIELD_BORAX, field.getBorax());
		initialValues.put(DatabaseStaticField.KEY_FIELD_GYPSUM,
				field.getGypsum());
		initialValues.put(DatabaseStaticField.KEY_FIELD_GOVTSUBSIDY,
				field.getGovtsubsidy());
		initialValues.put(DatabaseStaticField.KEY_FIELD_FIELDIMAGE,
				field.getFieldimage());

		rowNo = myDataBase.insert(DatabaseStaticField.DATABASE_TABLE_FIELD,
				null, initialValues);
		Log.i("Record Inserted", "Record Inserted");
		return rowNo;
	}

	public ArrayList<Field> getFields(String sql) {
		Cursor c = myDataBase.rawQuery(sql, null);
		Field field;
		ArrayList<Field> fieldList = new ArrayList<Field>();
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			do {
				field = new Field();
				field.setFarmerid(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMER_ID)));
				field.setFarm_ID(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_FARM_ID)));
				field.setFarm_Name(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_FARM_NAME)));
				field.setFields(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_FIELDS)));
				field.setFarm_Area(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_FARM_AREA)));
				field.setCult_Area(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_CULT_AREA)));

				field.setCrop_Name(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_CROP_NAME)));
				field.setCrop_Variety(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_CROP_VARIETY)));
				field.setFarm_Village(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_FARM_VILLAGE)));
				field.setCreated_Date(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_CREATED_DATE)));
				field.setLastModified_Date(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_LASTMODIFIED_DATE)));

				field.setSync_Status(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_SYNC_STATUS)));

				field.setLatitude(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_LATITUDE)));
				field.setLongitude(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_LONGITUDE)));

				field.setYear(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_YEAR)));
				field.setSeason(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_SEASON)));
				field.setSurveyno(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_SURVEYNO)));
				field.setSowingdate(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_SOWINGDATE)));
				field.setSeedrate(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_SEEDRATE)));
				field.setSeedsource(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_SEEDSOURCE)));

				field.setUrea(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_UREA)));
				field.setDap(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_DAP)));
				field.setPotash(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_POTASH)));
				field.setComplex(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_COMPLEX)));
				field.setZincsulphate(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_ZINCSULPHATE)));
				field.setBorax(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_BORAX)));
				field.setGypsum(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_GYPSUM)));
				field.setGovtsubsidy(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_GOVTSUBSIDY)));
				field.setFieldimage(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELD_FIELDIMAGE)));

				fieldList.add(field);
			} while (c.moveToNext());

		}
		c.close();
		return fieldList;
	}

	// ////////////// Filed Visit //////////
	public ArrayList<FieldVisit> getFieldVisit(String sql) {
		Cursor c = myDataBase.rawQuery(sql, null);
		FieldVisit fieldVisit;
		ArrayList<FieldVisit> fieldVisitArray = new ArrayList<FieldVisit>();
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			do {
				fieldVisit = new FieldVisit();
				fieldVisit
						.setFieldVisit_ID(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_ID)));
				fieldVisit
						.setUser_ID(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_UserID)));

				fieldVisit
						.setUser_name(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_User_Name)));
				fieldVisit.setFarmerId(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FARMERID)));
				fieldVisit.setFieldId(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_FIELDID)));
				fieldVisit
						.setVisitType(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_TYPE)));
				fieldVisit
						.setState(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_State)));

				fieldVisit
						.setDistrict(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_District)));
				fieldVisit
						.setTaluk(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_Taluk)));
				fieldVisit
						.setHobli(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_Hobli)));
				fieldVisit
						.setVillage(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_Village)));
				fieldVisit
						.setPurpose(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_Purpose)));
				fieldVisit
						.setRecommendation(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_RECOMMENDATION)));
				fieldVisit
						.setObservation(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_OBSERVATION)));
				fieldVisit
						.setSync_Status(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_SyncStatus)));
				fieldVisit
						.setDate(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_Date)));

				fieldVisit
						.setLatitude(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_Latitude)));
				fieldVisit
						.setLongitude(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_Longitude)));
				fieldVisit
						.setVisitorImage(c.getString(c
								.getColumnIndex(DatabaseStaticField.KEY_FIELDVISIT_VisitorImage)));

				fieldVisitArray.add(fieldVisit);
			} while (c.moveToNext());

		}
		c.close();
		return fieldVisitArray;
	}

	// //// Training Record Data
	public ArrayList<Training> getTrainingRecords(String sql) {
		Cursor c = myDataBase.rawQuery(sql, null);
		Training training;
		ArrayList<Training> trainingArray = new ArrayList<Training>();
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			do {
				training = new Training();
				training.setTrainingrecord_ID(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_ID)));
				training.setUser_ID(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_UserID)));
				
// [TrainingLevel] ([Id] TEXT, [USERID] TEXT, [User_Name]
				//TEXT, [Male] TEXT, [Female] TEXT, [District] TEXT, [Taluk] TEXT, [Hobli] TEXT,
	//			[Village] TEXT, [Date] TEXT, [Latitude] TEXT, [Longitude] TEXT,[Traininglevel] TEXT, 
	//			[TrainingTopic] TEXT, [TrainingFeedback] TEXT, [Sync_Status] INTEGER,[Trainer_Image] TEXT)";

				training.setUser_name(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_User_Name)));
				training.setMale(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_Male)));
				
				training.setFemale(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_Female)));
				training.setDistrict(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_District)));
				
			
				training.setTaluk(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_Taluk)));
				training.setHobli(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_Hobli)));
				training.setVillage(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_Village)));
				training.setDate(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_Date)));

				training.setLatitude(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_Latitude)));
				training.setLongitude(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_Longitude)));
				training.setTraininglevel(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_Training)));
				training.setTraining_topic(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_TOPIC)));
				training.setTraining_feedback(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_FEEDBACK)));
				training.setState(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_State)));

				
				
				
				training.setSync_Status(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_SyncStatus)));
			
				
				training.setTrainerImage(c.getString(c
						.getColumnIndex(DatabaseStaticField.KEY_TRAININGLEVEL_TrainerImage)));

				trainingArray.add(training);
			} while (c.moveToNext());

		}
		c.close();
		return trainingArray;
	}

	// ////////////////////////////////////////

	public FieldRecommendation getRecommendation(String district, String taluk,
			String CropType) {
		String getRecommendationSQL = "select * from Recommendation where District=\'"
				+ district
				+ "\' AND Taluk=\'"
				+ taluk
				+ "\' And Croptype=\'"
				+ CropType + "\'";
		Cursor c = myDataBase.rawQuery(getRecommendationSQL, null);
		FieldRecommendation recommendation = null;
		if (c != null && c.getCount() > 0) {
			recommendation = new FieldRecommendation();
			c.moveToFirst();
			recommendation.setId(c.getString(c.getColumnIndex("Id")));
			recommendation
					.setDistrict(c.getString(c.getColumnIndex("District")));
			recommendation.setTaluk(c.getString(c.getColumnIndex("Taluk")));
			recommendation
					.setCropType(c.getString(c.getColumnIndex("Croptype")));

			recommendation.setUrea(c.getString(c.getColumnIndex("Urea")));
			recommendation.setDap(c.getString(c.getColumnIndex("DAP")));
			recommendation.setMop(c.getString(c.getColumnIndex("MOP")));
			recommendation.setGypsum(c.getString(c.getColumnIndex("Gypsum")));
			recommendation.setZinc_sulphate(c.getString(c
					.getColumnIndex("Zinc_Sulphate")));
			recommendation.setBorax(c.getString(c.getColumnIndex("Borax")));
			recommendation.setSsp_ssp(c.getString(c.getColumnIndex("Ssp_Ssp")));
			recommendation
					.setSsp_urea(c.getString(c.getColumnIndex("Ssp_Urea")));
			recommendation.setSsp_gypsum(c.getString(c
					.getColumnIndex("Ssp_Gypsum")));
			recommendation.setSsp_dap(c.getString(c.getColumnIndex("Ssp_Dap")));
		}
		if (c != null) {
			c.close();
		}
		return recommendation;
	}

	// Field Visit

	public FieldRecommendation getFiledRecommendation(String Farm_ID,
			String crop_type) {
		String getRecommendationSQL = "select * from FieldRecommendation where Farm_ID=\'"
				+ Farm_ID + "\' and Croptype=\'" + crop_type + "\'";
		Cursor c = myDataBase.rawQuery(getRecommendationSQL, null);
		FieldRecommendation recommendation = null;
		if (c != null && c.getCount() > 0) {
			recommendation = new FieldRecommendation();
			c.moveToFirst();
			recommendation.setId(c.getString(c.getColumnIndex("Id")));
			recommendation.setFarm_ID(c.getString(c.getColumnIndex("Farm_ID")));
			recommendation
					.setDistrict(c.getString(c.getColumnIndex("District")));
			recommendation.setTaluk(c.getString(c.getColumnIndex("Taluk")));
			recommendation
					.setCropType(c.getString(c.getColumnIndex("Croptype")));

			recommendation.setUrea(c.getString(c.getColumnIndex("Urea")));
			recommendation.setDap(c.getString(c.getColumnIndex("DAP")));
			recommendation.setMop(c.getString(c.getColumnIndex("MOP")));
			recommendation.setGypsum(c.getString(c.getColumnIndex("Gypsum")));
			recommendation.setZinc_sulphate(c.getString(c
					.getColumnIndex("Zinc_Sulphate")));
			recommendation.setBorax(c.getString(c.getColumnIndex("Urea")));
			recommendation.setSsp_ssp(c.getString(c.getColumnIndex("Ssp_Ssp")));
			recommendation
					.setSsp_urea(c.getString(c.getColumnIndex("Ssp_Urea")));
			recommendation.setSsp_gypsum(c.getString(c
					.getColumnIndex("Ssp_Gypsum")));
			recommendation.setSsp_dap(c.getString(c.getColumnIndex("Ssp_Dap")));
			c.close();
			return recommendation;
		} else {
			return null;
		}

	}

	private int countFiledRecommendation(String Farm_ID) {
		String getRecommendationSQL = "select * from FieldRecommendation where Farm_ID=\'"
				+ Farm_ID + "\'";
		Cursor c = myDataBase.rawQuery(getRecommendationSQL, null);
		int count;
		if (c == null) {
			count = 0;
		} else {
			count = c.getCount();
		}

		if (c != null) {
			c.close();
		}
		return count;
	}

	public void updateRecommendation(FieldRecommendation frec, String FarmID,
			String USERID) {
		int count = countFiledRecommendation(FarmID);
		ContentValues initialValues = new ContentValues();

		initialValues.put("Id", frec.getId());
		initialValues.put("Farm_ID", FarmID);
		initialValues.put("Croptype", frec.getCropType());
		initialValues.put("Urea", frec.getUrea());
		initialValues.put("DAP", frec.getDap());
		initialValues.put("MOP", frec.getMop());
		initialValues.put("Gypsum", frec.getGypsum());
		initialValues.put("Zinc_Sulphate", frec.getZinc_sulphate());
		initialValues.put("Borax", frec.getZinc_sulphate());
		initialValues.put("Ssp_ssp", frec.getSsp_ssp());
		initialValues.put("Ssp_Urea", frec.getSsp_urea());
		initialValues.put("Ssp_Gypsum", frec.getSsp_gypsum());
		initialValues.put("Ssp_Dap", frec.getSsp_dap());

		initialValues.put("USERID", USERID);

		if (count > 0) {

			int recordNo = myDataBase.update("FieldRecommendation",
					initialValues, "Farm_ID=?", new String[] { FarmID });
		} else {
			long rowNo = myDataBase.insert("FieldRecommendation", null,
					initialValues);

		}

	}

	public boolean getRecommendationAvailableStatus(String district,
			String taluk, String croptype) {
		String sql = "select * from Recommendation where district=?and taluk=?and croptype=?";
		Cursor c = myDataBase.rawQuery(sql, new String[] { district, taluk,
				croptype });
		int count = c.getCount();
		c.close();
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public long insertRecommendation(FieldRecommendation recomm, String userId) {
		long rowNo = 0;

		ContentValues initialValues = new ContentValues();

		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_ID,
				recomm.getId());
		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_District,
				recomm.getDistrict());
		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_Taluk,
				recomm.getTaluk());

		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_BORAX,
				recomm.getBorax());
		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_CROPTYPE,
				recomm.getCropType());
		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_DAP,
				recomm.getDap());
		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_GYPSUM,
				recomm.getGypsum());
		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_MOP,
				recomm.getMop());
		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_UREA,
				recomm.getUrea());
		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_ZINSULPH,
				recomm.getZinc_sulphate());
		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_SSP_SSP,
				recomm.getSsp_ssp());
		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_SSP_UREA,
				recomm.getSsp_urea());
		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_SSP_GYPSUM,
				recomm.getSsp_gypsum());
		initialValues.put(DatabaseStaticField.KEY_RECOMMENDATION_SSP_DAP,
				recomm.getSsp_dap());
		initialValues.put("USERID", userId);
		rowNo = myDataBase.insert(
				DatabaseStaticField.DATABASE_TABLE_RECOMMENDATION, null,
				initialValues);

		return rowNo;
	}

	public long insertVillage(Villages village, String userid) {
		long rowNo = 0;
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseStaticField.KEY_VILLAGE_USERID, userid);
		initialValues.put(DatabaseStaticField.KEY_VILLAGE_NAME,
				village.getVillage_Name());
		initialValues.put(DatabaseStaticField.KEY_VILLAGE_HOBLI,
				village.getHobli());
		initialValues.put(DatabaseStaticField.KEY_VILLAGE_TALUK,
				village.getTaluk());
		rowNo = myDataBase.insert(DatabaseStaticField.DATABASE_TABLE_VILLAGE,
				null, initialValues);

		return rowNo;
	}

	public void deleteVillage() {
		myDataBase.delete(DatabaseStaticField.DATABASE_TABLE_VILLAGE, null,
				null);
	}

	public long insertSoilMap(SoilMaps msoil) {

		long rowNo = 0;

		ContentValues initialValues = new ContentValues();

		initialValues.put(DatabaseStaticField.KEY_USER_ID, msoil.getUserid());

		initialValues.put(DatabaseStaticField.KEY_USER_DISTRICT,
				msoil.getDistrictName());

		initialValues
				.put(DatabaseStaticField.KEY_CROPNAME, msoil.getCropname());

		initialValues.put(DatabaseStaticField.KEY_CROP_URL, msoil.getCropUrl());

		rowNo = myDataBase.insert(
				DatabaseStaticField.DATABASE_TABLE_SoilMapping,

				null, initialValues);

		return rowNo;
	}

	public ArrayList<String> getSoilMaps(String district) {
		String sql = "Select LOCAL_MAP_IMAGE_PATH from SoilMapping where district=\'"
				+ district + "\'";
		Cursor c = myDataBase.rawQuery(sql, null);
		ArrayList<String> mapList = new ArrayList<String>();
		if (c != null & c.getCount() > 0) {
			c.moveToFirst();
			do {
				mapList.add(c.getString(c
						.getColumnIndex("LOCAL_MAP_IMAGE_PATH")));
			} while (c.moveToNext());
		}
		c.close();
		return mapList;
	}

	public ArrayList<String> getCropNames(String userId, String district,
			String taluk) {
		String sql = "Select distinct Croptype from Recommendation where USERID=? and District=? and Taluk=?  order by croptype asc";
		Cursor c = myDataBase.rawQuery(sql, new String[] { userId, district,
				taluk });
		ArrayList<String> cropArrayList = new ArrayList<String>();
		if (c != null & c.getCount() > 0) {
			c.moveToFirst();
			do {
				cropArrayList.add(c.getString(c.getColumnIndex("Croptype")));
			} while (c.moveToNext());
		}
		c.close();
		return cropArrayList;
	}

	public Set<String> getVillage(String sql, String columName) {
		Cursor c = myDataBase.rawQuery(sql, null);
		Set<String> mVillageList = new TreeSet();
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			do {
				mVillageList.add(c.getString(c.getColumnIndex(columName)));
			} while (c.moveToNext());

		}
		c.close();
		return mVillageList;
	}
}