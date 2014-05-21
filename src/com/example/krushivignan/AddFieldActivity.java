package com.example.krushivignan;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.Farmer;
import com.nunc.krushivignan.core.Field;
import com.nunc.krushivignan.db.DBHelper;
import com.nunc.krushivignan.db.DatabaseStaticField;
import com.nunc.krushivignan.util.GPSTracker;

public class AddFieldActivity extends Activity {
	private TextView label0, label1, label2, label3, label4, label5, label6,
			label7, label8, label9, label10, label11, label12, label13,
			label14, label15, label16, label17, label18, label19, label20,
			label21, label22, label23;
	private Button save, cancel;
	private SharedPreferences _prefs;
	private String language_str;
	private DBHelper db;
	private EditText farm_name, field_id, farm_area, cult_area, crop_variety;
	private Spinner spinn_cropType;
	private Spinner farm_village;
	private Farmer farmer;
	private String latitude;
	private String longitude;

	private String village, cropname;
	private GPSTracker gps;

	private EditText surveyNo, seedRate, govtsubsidy, urea, dap, potash,
			complex, zincsulphate, borax, gypsum;
	private Spinner season, SeedSource, year;

	private String txtSeason, txtSeedSource, txtYear;

	private ImageView takePhoto;
	static final int DATE_PICKER_ID = 1111;
	static final int DATE_PICKER_ID_Year = 2222;

	private int intyear;
	private int month;
	private int day;
	private TextView sowingDate;
	private static final int DIALOG_ALERT = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.field_add_layout);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		farm_village = (Spinner) findViewById(R.id.Farm_Village);
		farm_village.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				village = (String) arg0.getAdapter().getItem(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		SeedSource = (Spinner) findViewById(R.id.SeedSource);
		SeedSource.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				txtSeedSource = (String) arg0.getAdapter().getItem(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		year = (Spinner) findViewById(R.id.year);
		year.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				txtYear = (String) parent.getAdapter().getItem(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		season = (Spinner) findViewById(R.id.season);
		season.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				txtSeason = (String) arg0.getAdapter().getItem(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		farmer = (Farmer) getIntent().getSerializableExtra("SelectedFarmer");
		db = ((KrushiVignanApp) getApplication()).getDatabase();
		ArrayList<String> mListVillage = new ArrayList<String>(db.getVillage(
				"select * from Village where userId=\'"
						+ AppInfo.CurrentUser.getUserId() + "\'",
				DatabaseStaticField.KEY_VILLAGE_NAME));

		mListVillage.add(0, "Select Village");
		if (mListVillage.size() > 1) {
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					getParent(), android.R.layout.simple_spinner_item,
					mListVillage);
			farm_village.setAdapter(spinnerArrayAdapter);
		}

		_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
		language_str = _prefs.getString("SelectedLanguage", "");
		getComponentValues();
		spinn_cropType = (Spinner) findViewById(R.id.spinn);
		ArrayList<String> cropList = db.getCropNames(
				AppInfo.CurrentUser.getUserId(),
				AppInfo.CurrentUser.getDistrict(),
				AppInfo.CurrentUser.getTaluk());
		cropList.add(0, "Select Crop");
		spinn_cropType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				cropname = (String) arg0.getAdapter().getItem(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, cropList);

		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinn_cropType.setAdapter(aa);

		setLanguage(language_str);
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
		final Calendar c = Calendar.getInstance();
		intyear = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		// sowingDate.setText( sdf.format( new Date() ));

		sowingDate.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {

				// On button click show datepicker dialog
				showDialog(DATE_PICKER_ID);

			}

		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		gps.stopUsingGPS();
	}

	private void setLanguage(String language) {
		if (language.startsWith("English")) {

			label0.setText(R.string.field_add_label0_english);
			label1.setText(R.string.field_add_label1_english);
			label2.setText(R.string.field_add_label2_english);
			label3.setText(R.string.field_add_label3_english);
			label4.setText(R.string.field_add_label4_english);
			label5.setText(R.string.field_add_label5_english);
			label6.setText(R.string.field_add_label6_english);
			label7.setText(R.string.field_add_label7_english);
			label8.setText(R.string.field_add_label8_english);
			label9.setText(R.string.field_add_label9_english);
			label10.setText(R.string.field_add_label10_english);
			label11.setText(R.string.field_add_label11_english);
			label12.setText(R.string.field_add_label12_english);
			label13.setText(R.string.field_add_label13_english);
			label14.setText(R.string.field_add_label14_english);
			label15.setText(R.string.field_add_label15_english);
			label16.setText(R.string.field_add_label16_english);
			label17.setText(R.string.field_add_label17_english);
			label18.setText(R.string.field_add_label18_english);
			label19.setText(R.string.field_add_label19_english);
			label20.setText(R.string.field_add_label20_english);
			label21.setText(R.string.field_add_label21_english);
			label22.setText(R.string.field_add_label22_english);
			label23.setText(R.string.field_add_label23_english);

			save.setText(R.string.save_text_english);
			cancel.setText(R.string.cancel_text_english);

		} else if (language.startsWith("Kanada")) {

			save.setText(R.string.save_text_kanada);
			cancel.setText(R.string.cancel_text_kanada);
			label0.setText(R.string.field_add_label0_kanada);
			label1.setText(R.string.field_add_label1_kanada);
			label2.setText(R.string.field_add_label2_kanada);
			label3.setText(R.string.field_add_label3_kanada);
			label4.setText(R.string.field_add_label4_kanada);
			label5.setText(R.string.field_add_label5_kanada);
			label6.setText(R.string.field_add_label6_kanada);
			label7.setText(R.string.field_add_label7_kanada);
			label8.setText(R.string.field_add_label8_kanada);
			label9.setText(R.string.field_add_label9_kanada);
			label10.setText(R.string.field_add_label10_kanada);
			label11.setText(R.string.field_add_label11_kanada);
			label12.setText(R.string.field_add_label12_kanada);
			label13.setText(R.string.field_add_label13_kanada);
			label14.setText(R.string.field_add_label14_kanada);
			label15.setText(R.string.field_add_label15_kanada);
			label16.setText(R.string.field_add_label16_kanada);
			label17.setText(R.string.field_add_label17_kanada);
			label18.setText(R.string.field_add_label18_kanada);
			label19.setText(R.string.field_add_label19_kanada);
			label20.setText(R.string.field_add_label20_kanada);
			label21.setText(R.string.field_add_label21_kanada);
			label22.setText(R.string.field_add_label22_kanada);
			label23.setText(R.string.field_add_label23_kanada);
		}

	}

	private void getComponentValues() {
		label0 = (TextView) findViewById(R.id.label0);
		save = (Button) findViewById(R.id.save);
		cancel = (Button) findViewById(R.id.cancel);
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
		label20 = (TextView) findViewById(R.id.label20);
		label21 = (TextView) findViewById(R.id.label21);
		label22 = (TextView) findViewById(R.id.label22);
		label23 = (TextView) findViewById(R.id.label23);

		farm_name = (EditText) findViewById(R.id.farm_name);
		field_id = (EditText) findViewById(R.id.field_id);
		farm_area = (EditText) findViewById(R.id.farm_area);
		cult_area = (EditText) findViewById(R.id.cult_area);
		crop_variety = (EditText) findViewById(R.id.crop_variety);

		// year.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// showDialog(DATE_PICKER_ID_Year);
		// }
		// });
		surveyNo = (EditText) findViewById(R.id.surveyNo);
		sowingDate = (TextView) findViewById(R.id.sowingDate);
		seedRate = (EditText) findViewById(R.id.seedRate);
		govtsubsidy = (EditText) findViewById(R.id.govtsubsidy);
		urea = (EditText) findViewById(R.id.urea);
		dap = (EditText) findViewById(R.id.dap);
		potash = (EditText) findViewById(R.id.potash);
		complex = (EditText) findViewById(R.id.complex);
		zincsulphate = (EditText) findViewById(R.id.zincsulphate);
		borax = (EditText) findViewById(R.id.borax);
		gypsum = (EditText) findViewById(R.id.gypsum);
		takePhoto = (ImageView) findViewById(R.id.takePhoto);
		RegisterFarmerActivity.takePhoto = takePhoto;
		takePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_ALERT);

			}
		});

	}

	public void saveRecord(View v) {
		if (farm_name.getText().toString().length() > 0
				&& (!(txtYear.equalsIgnoreCase("Select Year")) && txtYear != null)
				&& (!(txtSeason.equalsIgnoreCase("Select Season")) && txtSeason != null)
				&& (!(cropname.equalsIgnoreCase("Select Crop")) && cropname != null)
				&& (sowingDate.getText().toString().length() > 0 && sowingDate
						.getText().toString() != null)

				&& farm_area.getText().toString().length() > 0
				&& cult_area.getText().toString().length() > 0
				&& !(farm_village.getSelectedItem().toString()
						.equals("Select Village"))) {
			if (validateField(farm_area.getText().toString(), cult_area
					.getText().toString())) {
				pushDatatoDB();
				// clearField();
				finish();
			} else {
				Toast.makeText(getParent(),
						"Farm Area Shlouldn't be less than Cultivation Area",
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(getParent(), "Mandatory fields Can't be blank",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void clearField() {
		farm_name.setText("");
		field_id.setText("");
		farm_area.setText("");
		cult_area.setText("");
		crop_variety.setText("");
	}

	public void cancelSave(View v) {
		finish();
	}

	public boolean pushDatatoDB() {

		Field field = new Field();
		field.setCreated_Date(new Date() + "");
		field.setLastModified_Date(new Date() + "");
		field.setCrop_Name(cropname);
		field.setCrop_Variety(crop_variety.getText().toString());
		field.setFields(field_id.getText().toString());
		field.setCult_Area(cult_area.getText().toString());
		field.setFarm_Area(farm_area.getText().toString());
		field.setFarm_ID(UUID.randomUUID().toString());
		field.setFarm_Name(farm_name.getText().toString());
		field.setFarm_Village(village);
		field.setLatitude(latitude);
		field.setLongitude(longitude);

		field.setYear(txtYear);
		field.setSurveyno(surveyNo.getText().toString());
		field.setSowingdate(sowingDate.getText().toString());
		field.setSeedrate(seedRate.getText().toString());
		field.setGovtsubsidy(govtsubsidy.getText().toString());
		if (urea.getText().toString().equalsIgnoreCase("")) {
			field.setUrea("0");

		} else {
			field.setUrea(urea.getText().toString());
		}
		if (dap.getText().toString().equalsIgnoreCase("")) {
			field.setDap("0");

		} else {
			field.setDap(dap.getText().toString());

		}
		if (potash.getText().toString().equalsIgnoreCase("")) {
			field.setPotash("0");

		} else {
			field.setPotash(potash.getText().toString());

		}
		if (complex.getText().toString().equalsIgnoreCase("")) {
			field.setComplex("0");

		} else {
			field.setComplex(complex.getText().toString());

		}
		if (zincsulphate.getText().toString().equalsIgnoreCase("")) {
			field.setZincsulphate("0");

		} else {
			field.setZincsulphate(zincsulphate.getText().toString());

		}
		if (borax.getText().toString().equalsIgnoreCase("")) {
			field.setBorax("0");

		} else {
			field.setBorax(borax.getText().toString());

		}
		if (gypsum.getText().toString().equalsIgnoreCase("")) {
			field.setGypsum("0");

		} else {
			field.setGypsum(gypsum.getText().toString());

		}

		field.setSeason(txtSeason);
		field.setSeedsource(txtSeedSource);
		field.setFieldimage(RegisterFarmerActivity.file + "");
		db.insertField(field, farmer.getFarmer_ID(), "0",
				AppInfo.CurrentUser.getUserId());
		RegisterFarmerActivity.file = null;
		Toast.makeText(this, "Field Successfully Added", Toast.LENGTH_SHORT)
				.show();
		return true;

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:

			// open datepicker dialog.
			// set date picker for current date
			// add pickerListener listner to date picker
			new DatePickerDialog(getParent(), pickerListener, intyear, month,
					day).show();
			break;
		// case DATE_PICKER_ID_Year:
		//
		// // open datepicker dialog.
		// // set date picker for current date
		// // add pickerListener listner to date picker
		// new DatePickerDialog(getParent(), pickerListener_year, intyear,
		// month, day).show();
		// break;
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

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			LunchGalleryImage();
		}

	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			LunchCameraImage();
		}
	}

	private void LunchCameraImage() {
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		RegisterFarmerActivity.file = new File(getExternalCacheDir() + "/"
				+ System.currentTimeMillis() + ".jpg");
		Uri outputFileUri = Uri.fromFile(RegisterFarmerActivity.file);

		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		getParent().startActivityForResult(cameraIntent,
				RegisterFarmerActivity.CAMERA_REQUEST);
	}

	private void LunchGalleryImage() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		getParent().startActivityForResult(photoPickerIntent,
				RegisterFarmerActivity.SELECT_PHOTO);
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			intyear = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// Show selected date
			sowingDate.setText(new StringBuilder().append(intyear).append("-")
					.append(month + 1).append("-").append(day).append(""));
			

		}
	};

	

	private boolean validateField(String farmArea, String cultivationArea) {
		if (farmArea.length() > 0 && cultivationArea.length() > 0) {
			float farm = Float.parseFloat(farmArea);
			float cult = Float.parseFloat(cultivationArea);
			if (cult <= farm) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

}