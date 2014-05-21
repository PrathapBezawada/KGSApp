package com.example.krushivignan;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.Farmer;
import com.nunc.krushivignan.db.DBHelper;
import com.nunc.krushivignan.db.DatabaseStaticField;
import com.nunc.krushivignan.util.GPSTracker;

public class RegisterFarmerActivity extends Activity implements
		AdapterView.OnItemSelectedListener {
	private EditText first_name, last_name, father_name, mobile, telephone,
			district, Total_Acre, Rain_Fed, Irrigation, Plantation, Survey_No,
			txtid_no, Follow;
private String role;
	private Spinner caste, sex, village, select_IdType, hobli_dd, taluk_dd;
	private DBHelper db;
	// private Farmer selectedFarmer;
	private String casteStr, sexStr, hobliStr, talukStr;
	private TextView label0, label1, label2, label3, label4, label5, label6,
			label7, label8, label9, label10, label11, label12, label13,
			label14, label15, label16, label17, label18, label19;
	private SharedPreferences _prefs;
	private String language_str;
	private Button save, cancel, clear;
	public static ImageView takePhoto;
	public static final int CAMERA_REQUEST = 1888;
	public static File file=null;
	public static final int SELECT_PHOTO = 100;
	private static final int DIALOG_ALERT = 10;
	private String latitude, longitude, id_type;
	private GPSTracker gps;
	String[] items = { "Select Id Type", "PANCard", "Passport",
			"DrivingLicense", "VoterCard", "RationCard", "AdharCard" };
	public static boolean ImageUpdateStatus = false;
	private String txtrainfed = "", txtirrigation = "", txtplantation = "",
			txtfollow = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.farmer_add_layout);

		db = ((KrushiVignanApp) getApplication()).getDatabase();
		getComponent();
		_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
		language_str = _prefs.getString("SelectedLanguage", "");
		getComponentValues();
		setLanguage(language_str);

		select_IdType.setOnItemSelectedListener(this);
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_IdType.setAdapter(aa);

		gps = new GPSTracker(getParent());
		if (gps.canGetLocation()) {

			latitude = gps.getLatitude() + "";
			longitude = gps.getLongitude() + "";

			// \n is for new line
			Toast.makeText(
					getApplicationContext(),
					"Your Location is - \nLat: " + latitude + "\nLong: "
							+ longitude, Toast.LENGTH_LONG).show();
		} else {
			
			gps.showSettingsAlert();
		}

	}

	private void getComponent() {
		first_name = (EditText) findViewById(R.id.first_name);
		last_name = (EditText) findViewById(R.id.last_name);
		father_name = (EditText) findViewById(R.id.father_name);
		mobile = (EditText) findViewById(R.id.mobile);
		telephone = (EditText) findViewById(R.id.telephone);
		district = (EditText) findViewById(R.id.district);
		village = (Spinner) findViewById(R.id.village);
		district.setText(AppInfo.CurrentUser.getDistrict());
		role=AppInfo.CurrentUser.getRole();

		caste = (Spinner) findViewById(R.id.caste);
		caste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				casteStr = (String) parent.getItemAtPosition(pos);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		hobli_dd = (Spinner) findViewById(R.id.hobli);

		// /////// author prathap
		taluk_dd = (Spinner) findViewById(R.id.taluk);
		
		ArrayList<String> mListVillage = new ArrayList<String>(db.getVillage(
				"select * from Village where userId=\'"
						+ AppInfo.CurrentUser.getUserId() + "\'",
				DatabaseStaticField.KEY_VILLAGE_TALUK));

		mListVillage.add(0, "Select Taluk");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				getParent(), android.R.layout.simple_spinner_item, mListVillage);
		taluk_dd.setAdapter(spinnerArrayAdapter);

		taluk_dd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				talukStr = (String) parent.getItemAtPosition(pos);

				ArrayList<String> mListVillage = new ArrayList<String>(db
						.getVillage("select * from Village where userId=\'"
								+ AppInfo.CurrentUser.getUserId()
								+ "\' AND Taluk=\'" + talukStr + "\'",
								DatabaseStaticField.KEY_VILLAGE_HOBLI));

				mListVillage.add(0, "Select Hobli");
				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
						getParent(), android.R.layout.simple_spinner_item,
						mListVillage);
				hobli_dd.setAdapter(spinnerArrayAdapter);

				hobli_dd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						hobliStr = (String) parent.getItemAtPosition(pos);

						ArrayList<String> mListVillage = new ArrayList<String>(
								db.getVillage(
										"select * from Village where userId=\'"
												+ AppInfo.CurrentUser
														.getUserId()
												+ "\' AND Taluk=\'" + talukStr + "\' AND Hobli=\'" + hobliStr
												+ "\'",
										DatabaseStaticField.KEY_VILLAGE_NAME));

						mListVillage.add(0, "Select Village");
						ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
								getParent(),
								android.R.layout.simple_spinner_item,
								mListVillage);
						village.setAdapter(spinnerArrayAdapter);

					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		sex = (Spinner) findViewById(R.id.sex);
		sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				sexStr = (String) parent.getItemAtPosition(pos);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		takePhoto = (ImageView) findViewById(R.id.takePhoto);
		takePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_ALERT);
			}
		});

		Total_Acre = (EditText) findViewById(R.id.total_area);
		Rain_Fed = (EditText) findViewById(R.id.rain_fed);
		Rain_Fed.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				txtrainfed = Rain_Fed.getText().toString();
				Total_Acre.setText(calculateTotal(txtrainfed, txtirrigation,
						txtplantation, txtfollow) + "");
			}
		});
		Irrigation = (EditText) findViewById(R.id.irrigated);
		Irrigation.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				txtirrigation = Irrigation.getText().toString();
				Total_Acre.setText(calculateTotal(txtrainfed, txtirrigation,
						txtplantation, txtfollow) + "");
			}
		});
		Plantation = (EditText) findViewById(R.id.plantation);
		Plantation.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				txtplantation = Plantation.getText().toString();
				Total_Acre.setText(calculateTotal(txtrainfed, txtirrigation,
						txtplantation, txtfollow) + "");
			}
		});
		Follow = (EditText) findViewById(R.id.fallow);
		Follow.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				txtfollow = Follow.getText().toString();
				Total_Acre.setText(calculateTotal(txtrainfed, txtirrigation,
						txtplantation, txtfollow) + "");
			}
		});

		Survey_No = (EditText) findViewById(R.id.survey_no);

	

	}

	private float calculateTotal(String rainfed, String irrigated,
			String plantation, String follow) {
		if (rainfed.length() == 0) {
			rainfed = "0.0";
		}
		if (irrigated.length() == 0) {
			irrigated = "0.0";
		}
		if (plantation.length() == 0) {
			plantation = "0.0";
		}
		if (follow.length() == 0) {
			follow = "0.0";
		}
		float plant = Float.parseFloat(plantation);
		float rain = Float.parseFloat(rainfed);
		float irri = Float.parseFloat(irrigated);
		float foll = Float.parseFloat(follow);
		return plant + rain + irri + foll;
	}

	private void LunchCameraImage() {
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		file = new File(getExternalCacheDir() + "/"
				+ System.currentTimeMillis() + ".jpg");
		Uri outputFileUri = Uri.fromFile(file);

		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		getParent().startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}

	private void LunchGalleryImage() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		getParent().startActivityForResult(photoPickerIntent, SELECT_PHOTO);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ALERT:
			// Create out AlterDialog
			Builder builder = new AlertDialog.Builder(getParent());
			builder.setMessage("Choose Farmer Photo");
			builder.setCancelable(true);
			builder.setPositiveButton("Choose From Gallery",
					new OkOnClickListener());
			builder.setNegativeButton("Capture From Camera",
					new CancelOnClickListener());
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		return super.onCreateDialog(id);
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			LunchCameraImage();
		}
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			LunchGalleryImage();
		}
	}

//	
	public boolean pushDatatoDB() {
        boolean validateFields = validateFields();
        if (validateFields) {
                Farmer farmer = new Farmer();
                farmer.setFarmer_ID(UUID.randomUUID().toString());
                farmer.setFirst_Name(first_name.getText().toString());
                farmer.setLast_Name(last_name.getText().toString());
                farmer.setFather_Name(father_name.getText().toString());
                farmer.setDistrict(district.getText().toString());
                farmer.setHobli(hobliStr);
                farmer.setMobile_No(mobile.getText().toString());
                farmer.setTelephone_No(telephone.getText().toString());
                farmer.setVillage(village.getSelectedItem().toString());
                farmer.setTaluk(taluk_dd.getSelectedItem().toString());
                farmer.setCaste(casteStr);
                farmer.setSex(sexStr);
                farmer.setSync_Status("0");
                farmer.setFarmer_Image(file + "");
                farmer.setCreated_Date(new Date() + "");
                farmer.setLastModified_Date(new Date() + "");

                farmer.setTotal_Acre(Total_Acre.getText().toString());
                farmer.setRain_Fed(Rain_Fed.getText().toString());
                farmer.setIrrigation(Irrigation.getText().toString());
                farmer.setPlantation(Plantation.getText().toString());
                farmer.setFallow(Follow.getText().toString());
                farmer.setSurvey_No(Survey_No.getText().toString());
                farmer.setLatitude(latitude);
                farmer.setLongitude(longitude);
                farmer.setId_type(id_type);
                farmer.setId_no(txtid_no.getText().toString());
                db.insertFarmer(farmer, AppInfo.CurrentUser.getUserId(), "0");
                file=null;
                Toast.makeText(this, "Farmer Registered Successfully",
                                Toast.LENGTH_SHORT).show();
                return true;
        } else {
                Toast.makeText(this, "Mandatory Fields Can't be blank",
                                Toast.LENGTH_SHORT).show();
                return false;
        }

}
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		gps.stopUsingGPS();
	}

	private boolean validateFields() {
		if (first_name.getText().toString().length() == 0
				|| last_name.getText().toString().length() == 0
				|| mobile.getText().toString().length() == 0
				|| village.getSelectedItem().toString()
						.equals("Select Village")
				|| hobli_dd.getSelectedItem().toString().equals("Select Hobli")) {
			return false;
		} else {
			return true;
		}
	}


	
	public void saveRecord(View v) {
		if (pushDatatoDB()) {
			clearFildsOnPostSave();
			finish();
		}
	}

	public void cancelSave(View v) {

		clearFildsOnPostSave();
	}

	public void closeScreen(View v) {
		finish();
	}

	private void clearFildsOnPostSave() {
		first_name.setText("");
		last_name.setText("");
		father_name.setText("");
		mobile.setText("");
		telephone.setText("");

		caste.setSelection(0);
		sex.setSelection(0);
		village.setSelection(0);
		select_IdType.setSelection(0);
		hobli_dd.setSelection(0);
		Total_Acre.setText("");
		Rain_Fed.setText("");
		Irrigation.setText("");
		Plantation.setText("");
		Follow.setText("");
		Survey_No.setText("");
		txtid_no.setText("");

	}

	private void setLanguage(String language) {
		if (language.startsWith("English")) {

			label0.setText(R.string.farmer_detail_label0_add_english);
			label1.setText(R.string.farmer_detail_label1_english);
			label2.setText(R.string.farmer_detail_label2_english);
			label3.setText(R.string.farmer_detail_label3_english);
			label4.setText(R.string.farmer_detail_label4_english);
			label5.setText(R.string.farmer_detail_label5_english);
			label6.setText(R.string.farmer_detail_label6_english);
			label7.setText(R.string.farmer_detail_label7_english);
			label8.setText(R.string.farmer_detail_label8_english);
			label9.setText(R.string.farmer_detail_label9_english);
			label10.setText(R.string.farmer_detail_label10_english);
			label11.setText(R.string.farmer_detail_label11_english);
			save.setText(R.string.save_text_english);
			cancel.setText(R.string.cancel_text_english);
			clear.setText(R.string.clear_text_english);
			label12.setText(R.string.farmer_detail_label12_english);
			label13.setText(R.string.farmer_detail_label13_english);
			label14.setText(R.string.farmer_detail_label14_english);
			label15.setText(R.string.farmer_detail_label15_english);
			label16.setText(R.string.farmer_detail_label16_english);
			label17.setText(R.string.farmer_detail_label17_english);
			label18.setText(R.string.farmer_detail_label18_english);
			label19.setText(R.string.farmer_detail_label19_english);

		} else if (language.startsWith("Kanada")) {
			label0.setText(R.string.farmer_detail_label0_add_kanada);
			save.setText(R.string.save_text_kanada);
			cancel.setText(R.string.cancel_text_kanada);
			clear.setText(R.string.clear_text_kanada);
			label1.setText(R.string.farmer_detail_label1_kanada);
			label2.setText(R.string.farmer_detail_label2_kanada);
			label3.setText(R.string.farmer_detail_label3_kanada);
			label4.setText(R.string.farmer_detail_label4_kanada);
			label5.setText(R.string.farmer_detail_label5_kanada);
			label6.setText(R.string.farmer_detail_label6_kanada);
			label7.setText(R.string.farmer_detail_label7_kanada);
			label8.setText(R.string.farmer_detail_label8_kanada);
			label9.setText(R.string.farmer_detail_label9_kanada);
			label10.setText(R.string.farmer_detail_label10_kanada);
			label11.setText(R.string.farmer_detail_label11_kanada);

			label12.setText(R.string.farmer_detail_label12_kanada);
			label13.setText(R.string.farmer_detail_label13_kanada);
			label14.setText(R.string.farmer_detail_label14_kanada);
			label15.setText(R.string.farmer_detail_label15_kanada);
			label16.setText(R.string.farmer_detail_label16_kanada);
			label17.setText(R.string.farmer_detail_label17_kanada);
			label18.setText(R.string.farmer_detail_label18_kanada);
			label19.setText(R.string.farmer_detail_label19_kanada);
		}

	}

	private void getComponentValues() {
		label0 = (TextView) findViewById(R.id.label0);
		save = (Button) findViewById(R.id.save);
		cancel = (Button) findViewById(R.id.cancel);
		clear = (Button) findViewById(R.id.clear);
		label1 = (TextView) findViewById(R.id.label1);
		label2 = (TextView) findViewById(R.id.label2);
		label3 = (TextView) findViewById(R.id.label3);
		label4 = (TextView) findViewById(R.id.label4);
		label5 = (TextView) findViewById(R.id.label5);
		label6 = (TextView) findViewById(R.id.label6);
		label7 = (TextView) findViewById(R.id.label7);
		label8 = (TextView) findViewById(R.id.label8);
		label9 = (TextView) findViewById(R.id.label9);
		label10 = (TextView) findViewById(R.id.label10);
		label11 = (TextView) findViewById(R.id.label11);

		label12 = (TextView) findViewById(R.id.label12);
		label13 = (TextView) findViewById(R.id.label13);
		label14 = (TextView) findViewById(R.id.label14);
		label15 = (TextView) findViewById(R.id.label15);
		label16 = (TextView) findViewById(R.id.label16);
		label17 = (TextView) findViewById(R.id.label17);
		label18 = (TextView) findViewById(R.id.label18);
		label19 = (TextView) findViewById(R.id.label19);

		txtid_no = (EditText) findViewById(R.id.id_no);
		select_IdType = (Spinner) findViewById(R.id.select_id_type);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		id_type = (String) arg0.getAdapter().getItem(arg2);
		if(id_type.equalsIgnoreCase("Select Id Type")){
            txtid_no.setEnabled(false);
            txtid_no.setText("");
    }
    else{
            txtid_no.setEnabled(true);
    }
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
