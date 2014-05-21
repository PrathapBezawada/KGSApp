package com.example.krushivignan;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.nunc.krushivignan.core.Field;
import com.nunc.krushivignan.db.DBHelper;
import com.nunc.krushivignan.db.DatabaseStaticField;

public class FieldDetailsActivity extends Activity implements
		OnItemSelectedListener {
	private TextView label0, label1, label2, label3, label4, label5, label6,
			label7, label8, label9, label10, label11, label12, label13,
			label14, label15, label16, label17, label18, label19, label20,
			label21, label22, label23;
	private Map<String, String> linksMap;
	private Button save, cancel, editField, editPhoto;
	private SharedPreferences _prefs;
	public static boolean EditButtonClicked = false;

	private String language_str, crop_txt, farmVill_txt;
	private DBHelper db;
	private EditText farm_name, field_id, farm_area, cult_area, crop_Name,
			crop_variety;
	private Spinner crop_type, farm_village;
	private Field SelectedFied;
	ArrayAdapter<String> aa, aa1;

	private EditText surveyNo, seedRate, govtsubsidy, urea, dap, potash,
			complex, zincsulphate, borax, gypsum;
	private Spinner season, SeedSource, year;

	private String txtSeason, txtSeedSource, txtYear;

	// private ImageView takePhoto;
	private TextView sowingDate;
	static final int DATE_PICKER_ID = 1111;
	static final int DATE_PICKER_ID_YEAR = 2222;
	private int intyear;
	private int month;
	private int day;
	private static final int DIALOG_ALERT = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.field_details_layout);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		db = ((KrushiVignanApp) getApplication()).getDatabase();
		_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
		language_str = _prefs.getString("SelectedLanguage", "");

		RegisterFarmerActivity.takePhoto = (ImageView) findViewById(R.id.takePhoto);
		RegisterFarmerActivity.takePhoto
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Bitmap bitmap = ((BitmapDrawable) RegisterFarmerActivity.takePhoto
								.getDrawable()).getBitmap();
						displayImageDialog(bitmap);
					}
				});
		getComponentValues();

		// for crop type
		crop_type = (Spinner) findViewById(R.id.spinn);
		ArrayList<String> item = db.getCropNames(
				AppInfo.CurrentUser.getUserId(),
				AppInfo.CurrentUser.getDistrict(),
				AppInfo.CurrentUser.getTaluk());

		crop_type.setOnItemSelectedListener(this);

		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, item);

		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		crop_type.setAdapter(aa);
		// / for farm village

		farm_village = (Spinner) findViewById(R.id.Farm_Village);
		ArrayList<String> village_item = new ArrayList<String>(db.getVillage(
				"select * from Village where userId=\'"
						+ AppInfo.CurrentUser.getUserId() + "\'",
				DatabaseStaticField.KEY_VILLAGE_NAME));

		farm_village
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						farmVill_txt = (String) parent.getItemAtPosition(pos);
					}

					public void onNothingSelected(AdapterView<?> parent) {
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
		aa1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, village_item);

		aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		farm_village.setAdapter(aa1);

		setLanguage(language_str);
		save.setVisibility(View.INVISIBLE);
		editPhoto.setVisibility(View.INVISIBLE);

		cancel.setVisibility(View.INVISIBLE);
		SelectedFied = (Field) getIntent()
				.getSerializableExtra("SelectedField");
		populateDataInScreen();

		final Calendar c = Calendar.getInstance();
		intyear = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		sowingDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// On button click show datepicker dialog
				showDialog(DATE_PICKER_ID);

			}

		});
		toggleComponentEnabling(false);
	}

	private void populateDataInScreen() {

		farm_name.setText(SelectedFied.getFarm_Name());
		field_id.setText(SelectedFied.getFields());
		farm_area.setText(SelectedFied.getFarm_Area());
		cult_area.setText(SelectedFied.getCult_Area());
		crop_variety.setText(SelectedFied.getCrop_Variety());
		// FarmVillage.setText(SelectedFied.getFarm_Village());
		ArrayAdapter adp = (ArrayAdapter) crop_type.getAdapter();
		int pos = adp.getPosition(SelectedFied.getCrop_Name());
		crop_type.setSelection(pos);

		ArrayAdapter adp1 = (ArrayAdapter) farm_village.getAdapter();
		int pos1 = adp1.getPosition(SelectedFied.getFarm_Village());
		farm_village.setSelection(pos1);
		Bitmap bitmap = BitmapFactory.decodeFile(SelectedFied.getFieldimage());
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.no_image);
		}
		RegisterFarmerActivity.takePhoto.setImageBitmap(bitmap);
		// year.setsel(SelectedFied.getYear());
		surveyNo.setText(SelectedFied.getSurveyno());
		sowingDate.setText(SelectedFied.getSowingdate());
		seedRate.setText(SelectedFied.getSeedrate());
		govtsubsidy.setText(SelectedFied.getGovtsubsidy());
		urea.setText(SelectedFied.getUrea());
		dap.setText(SelectedFied.getDap());
		potash.setText(SelectedFied.getPotash());
		complex.setText(SelectedFied.getComplex());
		zincsulphate.setText(SelectedFied.getZincsulphate());
		borax.setText(SelectedFied.getBorax());
		gypsum.setText(SelectedFied.getGypsum());

		ArrayAdapter adp2 = (ArrayAdapter) season.getAdapter();
		int pos2 = adp2.getPosition(SelectedFied.getSeason());
		season.setSelection(pos2);

		ArrayAdapter adp3 = (ArrayAdapter) SeedSource.getAdapter();
		int pos3 = adp3.getPosition(SelectedFied.getSeedsource());
		SeedSource.setSelection(pos3);
		ArrayAdapter adp4 = (ArrayAdapter) year.getAdapter();
		int pos4 = adp4.getPosition(SelectedFied.getYear());
		year.setSelection(pos4);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:

			// open datepicker dialog.
			// set date picker for current date
			// add pickerListener listner to date picker
			new DatePickerDialog(getParent().getParent(), pickerListener,
					intyear, month, day).show();
			break;
		case DATE_PICKER_ID_YEAR:

			// open datepicker dialog.
			// set date picker for current date
			// add pickerListener listner to date picker
			// new DatePickerDialog(getParent().getParent(),
			// pickerListener_year,
			// intyear, month, day).show();
			break;
		case DIALOG_ALERT:
			// Create out AlterDialog
			Builder builder = new AlertDialog.Builder(getParent());
			builder.setMessage("Choose Farmer Photo");
			builder.setCancelable(true);
			FarmerDetailsActivity.EditButtonClicked = true;

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

	// private DatePickerDialog.OnDateSetListener pickerListener_year = new
	// DatePickerDialog.OnDateSetListener() {
	//
	// // when dialog box is closed, below method will be called.
	// @Override
	// public void onDateSet(DatePicker view, int selectedYear,
	// int selectedMonth, int selectedDay) {
	//
	// intyear = selectedYear;
	// month = selectedMonth;
	// day = selectedDay;
	//
	// // Show selected date
	// year.setText(new StringBuilder().append(intyear).append("-")
	// .append(month + 1).append("-").append(day).append(""));
	// }
	// };

	public void newFieldVisit(View v) {
		Intent edit = new Intent(getParent(), AddFieldVisitActivity.class);
		edit.putExtra("isFromList", true);
		edit.putExtra("farmerId", getIntent().getExtras().getString("farmerID"));
		edit.putExtra("fieldId", getIntent().getExtras().getString("fieldID"));

		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		parentActivity.startChildActivity("FieldVisitActivity", edit);
	}

	public void editField(View v) {
		if (AppInfo.CurrentUser.getRole().equalsIgnoreCase("FF")) {
			editPhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					showDialog(DIALOG_ALERT);
				}

			});
			toggleComponentEnabling(true);
			save.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
			editPhoto.setVisibility(View.VISIBLE);

		} else {
			AlertDialog.Builder mDialog = new AlertDialog.Builder(getParent());
			mDialog.setMessage("Please Check Your Access ");
			mDialog.setCancelable(false);
			mDialog.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).show();
		}

	}

	public void saveRecord(View v) {
		if (farm_name.getText().toString().length() > 0
				&& farm_area.getText().toString().length() > 0
				&& cult_area.getText().toString().length() > 0
				&& !(farm_village.getSelectedItem().toString()
						.equals("Select Village"))) {
			if (validateField(farm_area.getText().toString(), cult_area
					.getText().toString())) {
				pushDatatoDB();
				EditButtonClicked = false;
				toggleComponentEnabling(false);
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

	private void displayImageDialog(Bitmap bitmap) {
		Dialog settingsDialog = new Dialog(getParent());
		settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View v = getLayoutInflater()
				.inflate(R.layout.farmer_image_layout, null);

		settingsDialog.setContentView(v);
		ImageView image = (ImageView) v.findViewById(R.id.farmerImage);
		image.setImageBitmap(bitmap);
		settingsDialog.setCancelable(true);
		settingsDialog.setCanceledOnTouchOutside(true);
		settingsDialog.show();
	}

	private void setLanguage(String language) {
		if (language.startsWith("English")) {

			label0.setText(R.string.field_detail_label0_english);
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
			editField.setText(R.string.field_detail_edit_field_english);
		} else if (language.startsWith("Kanada")) {

			save.setText(R.string.save_text_kanada);
			cancel.setText(R.string.cancel_text_kanada);
			label0.setText(R.string.field_detail_label0_kanada);
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
			editField.setText(R.string.field_detail_edit_field_kanada);
		}

	}

	private void getComponentValues() {
		label0 = (TextView) findViewById(R.id.label0);
		save = (Button) findViewById(R.id.save);
		cancel = (Button) findViewById(R.id.cancel);
		editField = (Button) findViewById(R.id.editField);
		label1 = (TextView) findViewById(R.id.label1);
		label2 = (TextView) findViewById(R.id.label2);
		label3 = (TextView) findViewById(R.id.label3);
		label4 = (TextView) findViewById(R.id.label4);
		label5 = (TextView) findViewById(R.id.label5);
		label6 = (TextView) findViewById(R.id.label6);
		label7 = (TextView) findViewById(R.id.label7);
		editPhoto = (Button) findViewById(R.id.editPhoto);

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
		// showDialog(DATE_PICKER_ID_YEAR);
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

	}

	private void toggleComponentEnabling(boolean status) {
		farm_name.setEnabled(status);
		field_id.setEnabled(status);
		farm_area.setEnabled(status);
		cult_area.setEnabled(status);
		crop_type.setEnabled(status);
		crop_variety.setEnabled(status);
		farm_village.setEnabled(status);
		crop_type.setClickable(status);
		farm_village.setClickable(status);

		year.setEnabled(status);
		surveyNo.setEnabled(status);
		sowingDate.setEnabled(status);
		seedRate.setEnabled(status);
		govtsubsidy.setEnabled(status);
		urea.setEnabled(status);
		dap.setEnabled(status);
		potash.setEnabled(status);
		complex.setEnabled(status);
		zincsulphate.setEnabled(status);
		borax.setEnabled(status);
		gypsum.setEnabled(status);
		season.setClickable(status);
		SeedSource.setClickable(status);

	}

	public void clearField() {
		farm_name.setText("");
		field_id.setText("");
		farm_area.setText("");
		cult_area.setText("");
		crop_variety.setText("");

		// year.setText("");
		surveyNo.setText("");
		sowingDate.setText("");
		seedRate.setText("");
		govtsubsidy.setText("");
		urea.setText("");
		dap.setText("");
		potash.setText("");
		complex.setText("");
		zincsulphate.setText("");
		borax.setText("");
		gypsum.setText("");
		// takePhoto.setImageResource(R.drawable.no_image);

	}

	public void cancelSave(View v) {
		finish();
	}

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

	public boolean pushDatatoDB() {
		Field field = new Field();
		field.setLastModified_Date(new Date() + "");
		field.setFarm_ID(SelectedFied.getFarm_ID());
		field.setCrop_Name(crop_txt);
		field.setFarm_Village(farmVill_txt);
		field.setCrop_Variety(crop_variety.getText().toString());
		field.setCult_Area(cult_area.getText().toString());
		field.setFarm_Area(farm_area.getText().toString());
		field.setFarm_Name(farm_name.getText().toString());
		field.setFields(field_id.getText().toString());

		field.setYear(txtYear);
		field.setSurveyno(surveyNo.getText().toString());
		field.setSowingdate(sowingDate.getText().toString());
		field.setSeedrate(seedRate.getText().toString());
		field.setGovtsubsidy(govtsubsidy.getText().toString());
		field.setUrea(urea.getText().toString());
		field.setDap(dap.getText().toString());
		field.setPotash(potash.getText().toString());
		field.setComplex(complex.getText().toString());
		field.setZincsulphate(zincsulphate.getText().toString());
		field.setBorax(borax.getText().toString());
		field.setGypsum(gypsum.getText().toString());
		field.setFieldimage(RegisterFarmerActivity.file + "");
		field.setSeason(txtSeason);
		field.setSeedsource(txtSeedSource);

		db.updateField(field);

		Toast.makeText(this, "Field Updated Successfully", Toast.LENGTH_SHORT)
				.show();
		RegisterFarmerActivity.file = null;
		return true;

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		crop_txt = (String) parent.getItemAtPosition(position);

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
