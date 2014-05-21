package com.example.krushivignan;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.FieldVisit;
import com.nunc.krushivignan.core.KGSCore;
import com.nunc.krushivignan.db.DBHelper;
import com.nunc.krushivignan.threadcallback.FieldVisitParserThread;
import com.nunc.krushivignan.util.GPSTracker;

public class AddFieldVisitActivity extends Activity {

	private Button upload, cancel;
	private EditText purpose, state, comments, Observation, editText_village;
	private String str_Purpose, str_user, str_state, str_district, str_taluk,
			str_hobli, str_village, str_comments, str_visitType, str_farmerID,
			str_FieldID, observation;
	private ImageView take_photo;

	private TextView user, visitTypeSpinner;
	private static final int DIALOG_ALERT = 10;
	public String data = null;
	private DBHelper db;
	private String farmerID, fieldID;
	private Boolean isFromList;
	private String latitude;
	private String longitude;
	private GPSTracker gps;
	private ArrayList<KGSCore> list = new ArrayList<KGSCore>();

	private Spinner purposeTypeSpinner, taluk, hobli, district;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.field_visit_other);
		db = ((KrushiVignanApp) getApplication()).getDatabase();
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

		isFromList = getIntent().getExtras().getBoolean("isFromList");

		if (isFromList) {
			farmerID = getIntent().getExtras().getString("farmerId");
			fieldID = getIntent().getExtras().getString("fieldId");

		} else {
			farmerID = "";
			fieldID = "";
		}
		intilization();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		gps.stopUsingGPS();
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

	private void intilization() {
		cancel = (Button) findViewById(R.id.cancel);
		Observation = (EditText) findViewById(R.id.observation);
		purposeTypeSpinner = (Spinner) findViewById(R.id.purposeTypeSpinner);
		visitTypeSpinner = (TextView) findViewById(R.id.visitTypeSpinner);
		editText_village = (EditText) findViewById(R.id.edit_village);
		if (isFromList) {
			visitTypeSpinner.setText("Farmer Field Visit");
		} else {
			visitTypeSpinner.setText("Other Visit");

		}

		if (visitTypeSpinner.getText().toString()
				.equalsIgnoreCase("Farmer Field Visit")) {
			purposeTypeSpinner.setAdapter(new ArrayAdapter<String>(
					AddFieldVisitActivity.this,
					android.R.layout.simple_dropdown_item_1line, getResources()
							.getStringArray(R.array.farmer_field_visit_array)));
		} else if (visitTypeSpinner.getText().toString()
				.equalsIgnoreCase("Other Visit")) {
			purposeTypeSpinner.setAdapter(new ArrayAdapter<String>(
					AddFieldVisitActivity.this,
					android.R.layout.simple_dropdown_item_1line, getResources()
							.getStringArray(R.array.other_visit_array)));
		}
		user = (TextView) findViewById(R.id.editText_User);
		user.setText(AppInfo.CurrentUser.getUserName() + "");

		state = (EditText) findViewById(R.id.edittext_state);
		district = (Spinner) findViewById(R.id.spinner_district);
		taluk = (Spinner) findViewById(R.id.spinner_taluk);
		hobli = (Spinner) findViewById(R.id.spinner_hobli);

		FieldVisitParserThread thread = new FieldVisitParserThread(this, list) {
			@Override
			public void onCompleter() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Set<String> districtSet = new TreeSet<String>();
						for (KGSCore core : list) {
							districtSet.add(core.getDist_name());
						}
						ArrayList<String> district_Data = new ArrayList<String>(
								districtSet);
						district_Data.add(0, "Select District");
						district.setAdapter(new ArrayAdapter<String>(
								AddFieldVisitActivity.this,
								android.R.layout.simple_spinner_dropdown_item,
								district_Data));
						district.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								String selectedDistrict = (String) arg0
										.getAdapter().getItem(arg2);
								Set<String> talukSet = new TreeSet<String>();
								for (KGSCore core : list) {
									if (core.getDist_name().equalsIgnoreCase(
											selectedDistrict)) {
										talukSet.add(core.getTaluk_name());
									}
								}
								ArrayList<String> Taluk_Data = new ArrayList<String>(
										talukSet);
								Taluk_Data.add(0, "Select Taluk");
								taluk.setAdapter(new ArrayAdapter<String>(
										AddFieldVisitActivity.this,
										android.R.layout.simple_spinner_dropdown_item,
										Taluk_Data));
								taluk.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										// TODO Auto-generated method stub
										String selectedTaluk = (String) arg0
												.getAdapter().getItem(arg2);
										Set<String> hubliSet = new TreeSet<String>();
										for (KGSCore core : list) {
											if (core.getTaluk_name()
													.equalsIgnoreCase(
															selectedTaluk)) {
												hubliSet.add(core.getRsk_name());
											}
										}
										ArrayList<String> Hobli_Data = new ArrayList<String>(
												hubliSet);
										Hobli_Data.add(0, "Select Hobli");
										hobli.setAdapter(new ArrayAdapter<String>(
												AddFieldVisitActivity.this,
												android.R.layout.simple_spinner_dropdown_item,
												Hobli_Data));

									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {
										// TODO Auto-generated method stub

									}
								});

							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});
					}
				});

			}
		};
		thread.start();

		comments = (EditText) findViewById(R.id.editText_comments);

		take_photo = (ImageView) findViewById(R.id.takePhoto);
		RegisterFarmerActivity.takePhoto = take_photo;
		take_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_ALERT);

			}
		});
		upload = (Button) findViewById(R.id.upload);
		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				str_visitType = visitTypeSpinner.getText().toString();
				str_Purpose = purposeTypeSpinner.getSelectedItem().toString();
				str_user = user.getText().toString();
				str_state = state.getText().toString();
				str_taluk = taluk.getSelectedItem().toString();
				str_hobli = hobli.getSelectedItem().toString();
				str_district = district.getSelectedItem().toString();
				str_comments = comments.getText().toString();
				observation = Observation.getText().toString();
				str_village = editText_village.getText().toString();
				validate();
			}
		});
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void validate() {
		if ((str_Purpose != null && !(str_Purpose.equalsIgnoreCase("Select Purpose")))
				&& (str_user != null&& str_user.length() > 0) 
				&& (str_state != null && str_state.length()>0)
				&&( str_village != null && str_village.length()>0)
				&& (str_district != null && !(str_district.equalsIgnoreCase("Select District")))
				&& (str_taluk != null && !(str_taluk.equalsIgnoreCase("Select Taluk")))
				&& (str_hobli != null && !(str_hobli.equalsIgnoreCase("Select Hobli")))
				&& (str_comments != null && str_comments.length()>0)
				&& (observation != null && Observation.length()>0)
				&& (str_visitType != null  && str_visitType.length()>0)
				&& RegisterFarmerActivity.file != null) {
			Toast.makeText(getApplicationContext(), "Data Saved Successfully",
					Toast.LENGTH_SHORT).show();
			pushDatatoDB();
			finish();
		} else {
			Toast.makeText(getApplicationContext(), "Please fill all Fields",
					Toast.LENGTH_LONG).show();
		}

	}

	private void pushDatatoDB() {
		FieldVisit fieldVisit = new FieldVisit();

		fieldVisit.setFieldVisit_ID(UUID.randomUUID().toString());
		fieldVisit.setUser_ID(AppInfo.CurrentUser.getUserId());
		fieldVisit.setVisitType(str_visitType);
		fieldVisit.setUser_name(str_user);
		fieldVisit.setPurpose(str_Purpose);
		fieldVisit.setState(str_state);
		fieldVisit.setDistrict(str_district);
		fieldVisit.setTaluk(str_taluk);
		fieldVisit.setHobli(str_hobli);
		fieldVisit.setVillage(str_village);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		fieldVisit.setDate(currentDate + "");
		fieldVisit.setLatitude(latitude);
		fieldVisit.setLongitude(longitude);
		fieldVisit.setRecommendation(str_comments);
		fieldVisit.setObservation(observation);
		if (isFromList) {
			fieldVisit.setFarmerId(farmerID);
			fieldVisit.setFieldId(fieldID);
		} else {
			fieldVisit.setFarmerId("");
			fieldVisit.setFieldId("");
		}
		fieldVisit.setIsFromList(isFromList);
		fieldVisit.setVisitorImage(RegisterFarmerActivity.file + "");
		db.insertFieldVisit(fieldVisit, AppInfo.CurrentUser.getUserId(), "0");
		RegisterFarmerActivity.file = null;
	}

}
