package com.example.krushivignan;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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

public class FarmerDetailsActivity extends Activity {
	private EditText first_name, last_name, father_name, mobile, telephone,
			district, hobli_txt, Total_Acre, Rain_Fed, Irrigation, Plantation,
			Fallow, Survey_No, txtid_no;
	private Spinner caste, sex, village, select_IdType, hobli_dd, taluk_dd;
	private DBHelper db;
	private String casteStr, sexStr, idString, hoblistring, talukString;
	private Button save, cancel, editPhoto;
	private Farmer SelectedFarmer;
	private TextView label0, label1, label2, label3, label4, label5, label6,
			label7, label8, label9, label10, label11, label12, label13,
			label14, label15, label16, label17, label18, label19;
	private SharedPreferences _prefs;
	private String language_str;
	String[] items = { "Select IdType", "PANCard", "Passport",
			"DrivingLicense", "VoterCard", "RationCard", "AdharCard" };
	private Button editFarmer, fields, addField;
	public static boolean EditButtonClicked=false;
	public static final int CAMERA_REQUEST = 1888;
	public static final int SELECT_PHOTO = 100;
	private static final int DIALOG_ALERT = 10;
	private ArrayAdapter<String> aa;
	private String txtrainfed = "", txtirrigation = "", txtplantation = "",
			txtfollow = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farmer_detail_layout);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		db = ((KrushiVignanApp) getApplication()).getDatabase();
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
		getComponent();

		populateDataInScreen();
		_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
		language_str = _prefs.getString("SelectedLanguage", "");
		getComponentValues();
		setLanguage(language_str);

	}

	private void LunchCameraImage() {
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		RegisterFarmerActivity.file = new File(getExternalCacheDir() + "/"
				+ System.currentTimeMillis() + ".jpg");
		Uri outputFileUri = Uri.fromFile(RegisterFarmerActivity.file);

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
			FarmerDetailsActivity.EditButtonClicked=true;
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

	public void displayFields(View v) {
		Intent edit = new Intent(getParent(), FieldListActivity.class);
		edit.putExtra("SelectedFarmer", SelectedFarmer);
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		parentActivity.startChildActivity("Field_List", edit);
	}

	public void addField(View v) {
		if(AppInfo.CurrentUser.getRole().equalsIgnoreCase("FF")){
			Intent edit = new Intent(getParent(), AddFieldActivity.class);
			edit.putExtra("SelectedFarmer", SelectedFarmer);
			TabGroupActivity parentActivity = (TabGroupActivity) getParent();
			parentActivity.startChildActivity("Add_Field", edit);
			}
			else{
				AlertDialog.Builder mDialog = new AlertDialog.Builder(
						getParent());
				mDialog.setMessage("Please Check Your Access ");
				mDialog.setCancelable(false);
				mDialog.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).show();		
				}
		
	}

	private int getIndex(Spinner spinner, String myString) {

		int index = 0;

		for (int i = 0; i < spinner.getCount(); i++) {
			if (spinner.getItemAtPosition(i).equals(myString)) {
				index = i;
			}
		}
		return index;
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

	private void populateDataInScreen() {

		first_name.setText(SelectedFarmer.getFirst_Name());
		last_name.setText(SelectedFarmer.getLast_Name());
		father_name.setText(SelectedFarmer.getFather_Name());
		mobile.setText(SelectedFarmer.getMobile_No());
		telephone.setText(SelectedFarmer.getTelephone_No());
		district.setText(SelectedFarmer.getDistrict());
		
		Bitmap bitmap = BitmapFactory.decodeFile(SelectedFarmer
				.getFarmer_Image());
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.no_image);
		}
		RegisterFarmerActivity.takePhoto.setImageBitmap(bitmap);
		caste.setSelection(getIndex(caste, SelectedFarmer.getCaste()));
		sex.setSelection(getIndex(sex, SelectedFarmer.getSex()));
		select_IdType.setSelection(getIndex(select_IdType,SelectedFarmer.getId_type()));

		Total_Acre.setText(SelectedFarmer.getTotal_Acre());
		Rain_Fed.setText(SelectedFarmer.getRain_Fed());
		Irrigation.setText(SelectedFarmer.getIrrigation());
		Plantation.setText(SelectedFarmer.getPlantation());
		Fallow.setText(SelectedFarmer.getFallow());
		Survey_No.setText(SelectedFarmer.getSurvey_No());

		txtid_no.setText(SelectedFarmer.getId_no());

	

	}

	private void toggleEnablingViews(boolean enabledStatus) {
		first_name.setEnabled(enabledStatus);
		last_name.setEnabled(enabledStatus);
		father_name.setEnabled(enabledStatus);
		mobile.setEnabled(enabledStatus);
		telephone.setEnabled(enabledStatus);
		district.setEnabled(false);
		// hobli_txt.setEnabled(false);
		village.setEnabled(enabledStatus);
		select_IdType.setEnabled(enabledStatus);
		// Total_Acre.setEnabled(enabledStatus);
		Rain_Fed.setEnabled(enabledStatus);
		Irrigation.setEnabled(enabledStatus);
		Plantation.setEnabled(enabledStatus);
		Fallow.setEnabled(enabledStatus);
		Survey_No.setEnabled(enabledStatus);
		txtid_no.setEnabled(enabledStatus);
		hobli_dd.setClickable(enabledStatus);
		caste.setClickable(enabledStatus);
		sex.setClickable(enabledStatus);
		village.setClickable(enabledStatus);
		select_IdType.setClickable(enabledStatus);
		taluk_dd.setClickable(enabledStatus);

	}

	private void getComponent() {
		SelectedFarmer = (Farmer) getIntent().getSerializableExtra(
				"SelectedFarmer");
		editPhoto = (Button) findViewById(R.id.editPhoto);
		first_name = (EditText) findViewById(R.id.first_name);
		last_name = (EditText) findViewById(R.id.last_name);
		father_name = (EditText) findViewById(R.id.father_name);
		mobile = (EditText) findViewById(R.id.mobile);
		telephone = (EditText) findViewById(R.id.telephone);
		district = (EditText) findViewById(R.id.district);
		village = (Spinner) findViewById(R.id.village);
		txtid_no = (EditText) findViewById(R.id.id_no);
		caste = (Spinner) findViewById(R.id.caste);
		caste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				casteStr = (String) parent.getItemAtPosition(pos);
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
		hobli_dd = (Spinner) findViewById(R.id.hobli);
		taluk_dd = (Spinner) findViewById(R.id.taluk);

		ArrayList<String> mListVillage = new ArrayList<String>(db.getVillage(
				"select * from Village where userId=\'"
						+ AppInfo.CurrentUser.getUserId() + "\'",
				DatabaseStaticField.KEY_VILLAGE_TALUK));

		mListVillage.add(0, "Select Taluk");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				getParent(), android.R.layout.simple_spinner_item, mListVillage);
		taluk_dd.setAdapter(spinnerArrayAdapter);
		taluk_dd.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				taluk_dd.setSelection(getIndex(taluk_dd,
						SelectedFarmer.getTaluk()));
			}
		}, 100);
		taluk_dd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				talukString = (String) parent.getItemAtPosition(pos);

				ArrayList<String> mListVillage = new ArrayList<String>(db
						.getVillage("select * from Village where userId=\'"
								+ AppInfo.CurrentUser.getUserId()
								+ "\' AND Taluk=\'" + talukString + "\'",
								DatabaseStaticField.KEY_VILLAGE_HOBLI));

				mListVillage.add(0, "Select Hobli");
				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
						getParent(), android.R.layout.simple_spinner_item,
						mListVillage);
				hobli_dd.setAdapter(spinnerArrayAdapter);
				hobli_dd.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						hobli_dd.setSelection(getIndex(hobli_dd,
								SelectedFarmer.getHobli()));
					}
				}, 100);
				hobli_dd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						hoblistring = (String) parent.getItemAtPosition(pos);

						ArrayList<String> mListVillage = new ArrayList<String>(
								db.getVillage(
										"select * from Village where userId=\'"
												+ AppInfo.CurrentUser
														.getUserId()
												+ "\' AND Taluk=\'"
												+ talukString
												+ "\'AND Hobli=\'"
												+ hoblistring + "\'",
										DatabaseStaticField.KEY_VILLAGE_NAME));

						mListVillage.add(0, "Select Village");
						ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
								getParent(),
								android.R.layout.simple_spinner_item,
								mListVillage);
						village.setAdapter(spinnerArrayAdapter);
						village.postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								village.setSelection(getIndex(village,
										SelectedFarmer.getVillage()));
							}
						}, 100);

					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});


		select_IdType = (Spinner) findViewById(R.id.select_id_type);
		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_IdType.setAdapter(aa);
		select_IdType
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						idString = (String) parent.getItemAtPosition(pos);
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		save = (Button) findViewById(R.id.save);
		cancel = (Button) findViewById(R.id.cancel);
		save.setVisibility(View.INVISIBLE);
		cancel.setVisibility(View.INVISIBLE);
		editPhoto.setVisibility(View.INVISIBLE);

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
		Fallow = (EditText) findViewById(R.id.fallow);
		Fallow.addTextChangedListener(new TextWatcher() {

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
				txtfollow = Fallow.getText().toString();
				Total_Acre.setText(calculateTotal(txtrainfed, txtirrigation,
						txtplantation, txtfollow) + "");
			}
		});
		Survey_No = (EditText) findViewById(R.id.survey_no);
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

	public void EditFarmer(View v) {
		select_IdType
		.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent,
					View view, int pos, long id) {
				idString = (String) parent.getItemAtPosition(pos);
				if(idString.equalsIgnoreCase("Select Id Type")){
                    txtid_no.setEnabled(false);
                    txtid_no.setText("");
            }
            else{
                    txtid_no.setEnabled(true);
            }

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		if(AppInfo.CurrentUser.getRole().equalsIgnoreCase("FF")){
			editPhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					showDialog(DIALOG_ALERT);
				}

			});
			toggleEnablingViews(true);
			save.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
			editPhoto.setVisibility(View.VISIBLE);
			}
			else{
				AlertDialog.Builder mDialog = new AlertDialog.Builder(
						getParent());
				mDialog.setMessage("Please Check Your Access");
				mDialog.setCancelable(false);
				mDialog.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).show();	
			}	
	}

	public void EditSave(View v) {
		select_IdType.setOnItemSelectedListener(null);
		pushDatatoDB();
		toggleEnablingViews(false);
		save.setVisibility(View.INVISIBLE);
		cancel.setVisibility(View.INVISIBLE);
		editPhoto.setVisibility(View.INVISIBLE);
		EditButtonClicked=false;

	}

	public void EditCancel(View v) {
		select_IdType.setOnItemSelectedListener(null);
		toggleEnablingViews(false);
		save.setVisibility(View.INVISIBLE);
		cancel.setVisibility(View.INVISIBLE);
		editPhoto.setVisibility(View.INVISIBLE);
		EditButtonClicked=false;
	}

	public void pushDatatoDB() {
		boolean validateFields = validateFields();
		if (validateFields) {
			Farmer farmer = new Farmer();
			farmer.setFarmer_ID(SelectedFarmer.getFarmer_ID());
			farmer.setFirst_Name(first_name.getText().toString());
			farmer.setLast_Name(last_name.getText().toString());
			farmer.setFather_Name(father_name.getText().toString());
			farmer.setDistrict(district.getText().toString());
			farmer.setHobli(hoblistring);
			farmer.setMobile_No(mobile.getText().toString());
			farmer.setTelephone_No(telephone.getText().toString());
			farmer.setVillage(village.getSelectedItem().toString());
			farmer.setTaluk(talukString);
			farmer.setCaste(casteStr);
			farmer.setSex(sexStr);

			farmer.setLastModified_Date(new Date() + "");
			if (RegisterFarmerActivity.ImageUpdateStatus) {
				farmer.setFarmer_Image(RegisterFarmerActivity.file + "");
			} else {
				farmer.setFarmer_Image(SelectedFarmer.getFarmer_Image());
			}
			farmer.setTotal_Acre(Total_Acre.getText().toString());
			farmer.setRain_Fed(Rain_Fed.getText().toString());
			farmer.setIrrigation(Irrigation.getText().toString());
			farmer.setPlantation(Plantation.getText().toString());
			farmer.setFallow(Fallow.getText().toString());
			farmer.setSurvey_No(Survey_No.getText().toString());

			farmer.setId_no(txtid_no.getText().toString());
			farmer.setId_type(idString);

			db.updateFarmer(farmer);
			SelectedFarmer = farmer;
		RegisterFarmerActivity.ImageUpdateStatus = false;
			RegisterFarmerActivity.file=null;
			finish();

			Toast.makeText(this, "Farmer Updated Successfully",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Mandatory Fields Can't be blank",
					Toast.LENGTH_SHORT).show();
		}

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

	private void setLanguage(String language) {
		if (language.startsWith("English")) {
			editFarmer
					.setText(R.string.farmer_detail_button_editfarmer_english);
			fields.setText(R.string.farmer_detail_button_fields_english);
			addField.setText(R.string.farmer_detail_button_AddField_english);

			label0.setText(R.string.farmer_detail_label0_detail_english);
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

			label12.setText(R.string.farmer_detail_label12_english);
			label13.setText(R.string.farmer_detail_label13_english);
			label14.setText(R.string.farmer_detail_label14_english);
			label15.setText(R.string.farmer_detail_label15_english);
			label16.setText(R.string.farmer_detail_label16_english);
			label17.setText(R.string.farmer_detail_label17_english);
			label18.setText(R.string.farmer_detail_label18_english);
			label19.setText(R.string.farmer_detail_label19_english);

		} else if (language.startsWith("Kanada")) {
			save.setText(R.string.save_text_kanada);
			cancel.setText(R.string.cancel_text_kanada);
			editFarmer.setText(R.string.farmer_detail_button_editfarmer_kanada);
			fields.setText(R.string.farmer_detail_button_fields_kanada);
			addField.setText(R.string.farmer_detail_button_AddField_kanada);
			label0.setText(R.string.farmer_detail_label0_detail_kanada);
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
		editFarmer = (Button) findViewById(R.id.editFarmer);
		fields = (Button) findViewById(R.id.fields);
		addField = (Button) findViewById(R.id.add_field);
		label0 = (TextView) findViewById(R.id.label0);
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
		select_IdType = (Spinner) findViewById(R.id.select_id_type);

	}
}
