package com.example.krushivignan;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.KGSCore;
import com.nunc.krushivignan.core.Training;
import com.nunc.krushivignan.db.DBHelper;
import com.nunc.krushivignan.threadcallback.FieldVisitParserThread;
import com.nunc.krushivignan.util.GPSTracker;

public class NewTrainingActivity extends Activity {
	private Button save, cancel;
	private Calendar cal = Calendar.getInstance();
	private EditText state, topics, feedbacks, editText_village, male, female;
	private String str_user, str_state, str_district, str_taluk, str_hobli,
			str_village, str_topic, str_farmerID, str_FieldID, str_feedback,
			str_trainingLevel, str_training, str_male, str_female, str_date;
	private ImageView take_photo;
	private TextView user, date;
	private static final int DIALOG_ALERT = 10;
	public String data = null;
	private DBHelper db;
	static final int DATE_PICKER_ID = 1111;
	private int intyear;
	private int month;
	private int day;
	private String farmerID, fieldID;
	private Boolean isFromList;
	private String latitude;
	private String longitude;
	private GPSTracker gps;
	private Spinner taluk, hobli, district, training_level;

	private ArrayList<KGSCore> list = new ArrayList<KGSCore>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.traininglevel_activity);
		db = ((KrushiVignanApp) getApplication()).getDatabase();
		gps = new GPSTracker(NewTrainingActivity.this);
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

		intilization();
	}

	private void intilization() {

		cancel = (Button) findViewById(R.id.cancel);
		topics = (EditText) findViewById(R.id.select_topic);
		feedbacks = (EditText) findViewById(R.id.select_feedback);
		editText_village = (EditText) findViewById(R.id.edit_village);
		user = (TextView) findViewById(R.id.editText_User);
		male = (EditText) findViewById(R.id.edit_male);
		female = (EditText) findViewById(R.id.edit_female);
		user.setText(AppInfo.CurrentUser.getUserName() + "");
		state = (EditText) findViewById(R.id.edittext_state);
		district = (Spinner) findViewById(R.id.spinner_district);
		taluk = (Spinner) findViewById(R.id.spinner_taluk);
		hobli = (Spinner) findViewById(R.id.spinner_hobli);
		date = (TextView) findViewById(R.id.select_date);
		training_level = (Spinner) findViewById(R.id.select_traninglevel);
		date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_PICKER_ID);
			}
		});

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
								NewTrainingActivity.this,
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
										NewTrainingActivity.this,
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
												NewTrainingActivity.this,
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
		take_photo = (ImageView) findViewById(R.id.takePhoto);
		RegisterFarmerActivity.takePhoto = take_photo;
		take_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_ALERT);

			}
		});
		save = (Button) findViewById(R.id.upload);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				str_user = user.getText().toString();
				str_state = state.getText().toString();
				str_taluk = taluk.getSelectedItem().toString();
				str_hobli = hobli.getSelectedItem().toString();
				str_district = district.getSelectedItem().toString();
				str_topic = topics.getText().toString();
				str_feedback = feedbacks.getText().toString();
				str_village = editText_village.getText().toString();
				str_male = male.getText().toString();
				str_trainingLevel = training_level.getSelectedItem().toString();
				str_female = female.getText().toString();
				str_date = date.getText().toString();
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
			break;
		case DATE_PICKER_ID:

			new DatePickerDialog(getParent(), pickerListener,
					cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH)).show();
			break;
		}
		return super.onCreateDialog(id);
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
			date.setText(new StringBuilder().append(intyear).append("/")
					.append(month + 1).append("/").append(day).append(" "));

		}
	};

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

	private void validate() {
		if ((str_date != null && !(str_date.equalsIgnoreCase("Select Date")))
				&& (str_user != null && str_user.length() > 0)
				&& (str_state != null && str_state.length() > 0)
				&& (str_male != null && str_male.length() > 0)
				&& (str_female != null && str_female.length() > 0)
				&& (str_village != null && str_village.length() > 0)
				&& (str_trainingLevel != null && !(str_trainingLevel
						.equalsIgnoreCase("Select Training level")))
				&& (str_district != null && !(str_district
						.equalsIgnoreCase("Select District")))
				&& (str_taluk != null && !(str_taluk
						.equalsIgnoreCase("Select Taluk")))
				&& (str_hobli != null && !(str_hobli
						.equalsIgnoreCase("Select Hobli")))
				&& (str_topic != null && str_topic.length() > 0)
				&& (str_feedback != null && str_feedback.length() > 0)
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

		Training triningLevel = new Training();

		triningLevel.setTrainingrecord_ID(UUID.randomUUID().toString());
		triningLevel.setUser_ID(AppInfo.CurrentUser.getUserId());
		triningLevel.setUser_name(str_user);
		triningLevel.setState(str_state);
		triningLevel.setDistrict(str_district);
		triningLevel.setTaluk(str_taluk);
		triningLevel.setHobli(str_hobli);
		triningLevel.setVillage(str_village);
		triningLevel.setMale(str_male);
		triningLevel.setFemale(str_female);
		triningLevel.setTraininglevel(str_trainingLevel);
		triningLevel.setDate(str_date);
		triningLevel.setTraining_topic(str_topic);
		triningLevel.setTraining_feedback(str_feedback);
		triningLevel.setLatitude(latitude);
		triningLevel.setLongitude(longitude);
		triningLevel.setTrainerImage(RegisterFarmerActivity.file + "");
		db.insertTrainingRecord(triningLevel, AppInfo.CurrentUser.getUserId(),
				"0");
		RegisterFarmerActivity.file = null;
	}

}
