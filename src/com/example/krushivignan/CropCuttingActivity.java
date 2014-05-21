package com.example.krushivignan;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.CropCutting;
import com.nunc.krushivignan.db.DBHelper;
import com.nunc.krushivignan.util.GPSTracker;

public class CropCuttingActivity extends Activity {

	private TextView cce_username, cce_harvestDate;
	private EditText cce_harvestArea, cce_fw_pod_fp, cce_fw_fodder_fp,
			cce_fw_pod_ip, cce_fw_fodder_ip, cce_ssfw_pod_ip, cce_ssfw_pod_fp,
			cce_ssfw_fodder_ip, cce_ssfw_fodder_fp;
	private Button save, cancel;
	private DBHelper db;
	private String farmerID, fieldID;
	private String latitude;
	private Calendar cal = Calendar.getInstance();

	private String longitude;
	private GPSTracker gps;
	private ImageView take_photo;
	static final int DATE_PICKER_ID = 1111;
	private int intyear;
	private int month;
	private int day;
	private static final int DIALOG_ALERT = 10;
	String str_harvestArea, str_fw_pod_fp, str_fw_fodder_fp, str_fw_pod_ip,
			str_fw_fodder_ip, str_ssfw_pod_ip, str_ssfw_pod_fp,
			str_ssfw_fodder_ip, str_ssfw_fodder_fp, str_username,
			str_harvestDate, str_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crop_cutting_exp_layout);
		farmerID = getIntent().getStringExtra("farmerID");
		fieldID = getIntent().getStringExtra("fieldID");
		db = ((KrushiVignanApp) getApplication()).getDatabase();
		gps = new GPSTracker(getParent());
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude() + "";
			longitude = gps.getLongitude() + "";
			Toast.makeText(
					getApplicationContext(),
					"Your Location is - \nLat: " + latitude + "\nLong: "
							+ longitude, Toast.LENGTH_LONG).show();
		} else {

			gps.showSettingsAlert();
		}

		initiate();
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
			cce_harvestDate.setText(new StringBuilder().append(intyear)
					.append("/").append(month + 1).append("/").append(day)
					.append(" "));
			// cce_harvestDate.setText(new StringBuilder().append(month + 1)
			// .append("-").append(day).append("-").append(intyear)
			// .append(" "));

		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:

			// open datepicker dialog.
			// set date picker for current date
			// add pickerListener listner to date picker
			new DatePickerDialog(getParent(), pickerListener,
					cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH)).show();
			break;
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

	private void saveclickAction() {
		str_harvestArea = cce_harvestArea.getText().toString();
		str_fw_pod_fp = cce_fw_pod_fp.getText().toString();
		str_fw_fodder_fp = cce_fw_fodder_fp.getText().toString();
		str_fw_pod_ip = cce_fw_pod_ip.getText().toString();
		str_fw_fodder_ip = cce_fw_fodder_ip.getText().toString();
		str_ssfw_pod_fp = cce_ssfw_pod_fp.getText().toString();
		str_ssfw_fodder_fp = cce_ssfw_fodder_fp.getText().toString();
		str_ssfw_pod_ip = cce_ssfw_pod_ip.getText().toString();
		str_ssfw_fodder_ip = cce_ssfw_fodder_ip.getText().toString();
		str_harvestDate = cce_harvestDate.getText().toString();
		str_image = RegisterFarmerActivity.file + "";

		if ((str_harvestArea != null && str_harvestArea.length() > 0)
				&& (str_fw_pod_fp != null && str_fw_pod_fp.length() > 0)
				&& (str_fw_fodder_fp != null && str_fw_fodder_fp.length() > 0)
				&& (str_fw_pod_ip != null && str_fw_pod_ip.length() > 0)
				&& (str_fw_fodder_ip != null && str_fw_fodder_ip.length() > 0)
				&& (str_ssfw_pod_fp != null && str_ssfw_pod_fp.length() > 0)
				&& (str_ssfw_fodder_fp != null && str_ssfw_fodder_fp.length() > 0)
				&& (str_ssfw_pod_ip != null && str_ssfw_pod_ip.length() > 0)
				&& (str_ssfw_fodder_ip != null && str_ssfw_fodder_ip.length() > 0)
				&& (str_harvestDate != null && str_harvestDate.length() > 0)
				&& (str_image != null && str_image.length() > 0)) {
			pushDatatoDB();
		} else {
			Toast.makeText(getApplicationContext(), "Please Fill All Fields",
					Toast.LENGTH_LONG).show();
		}

		// return true;

	}

	private void pushDatatoDB() {
		CropCutting crop_cutting = new CropCutting();
		crop_cutting.setCropCutting_ID(UUID.randomUUID().toString());
		crop_cutting.setCce_UserName(AppInfo.CurrentUser.getUserName() + "");
		crop_cutting.setCce_HarvestDate(str_harvestDate + "");
		crop_cutting.setCce_HarvestArea(str_harvestArea + "");
		crop_cutting.setFarmer_Id(farmerID + "");
		crop_cutting.setField_Id(fieldID + "");
		crop_cutting.setLatitude(latitude + "");
		crop_cutting.setLongitude(longitude + "");
		crop_cutting.setCce_Fw_Pod_Fp(str_fw_pod_fp + "");
		crop_cutting.setCce_Fw_Fodder_Fp(str_fw_fodder_fp + "");
		crop_cutting.setCce_Fw_Pod_Ip(str_fw_pod_ip + "");
		crop_cutting.setCce_Fw_Fodder_Ip(str_fw_fodder_ip + "");
		crop_cutting.setCce_ssfw_Pod_Fp(str_ssfw_pod_fp + "");
		crop_cutting.setCce_ssfw_Fodder_Fp(str_ssfw_fodder_fp + "");
		crop_cutting.setCce_ssfw_Pod_Ip(str_ssfw_pod_ip + "");
		crop_cutting.setCce_ssfw_Fodder_Ip(str_ssfw_fodder_ip + "");
		crop_cutting.setCce_Image(str_image + "");
		crop_cutting.setSync_status(0 + "");
		db.insertCropCuttingData(crop_cutting, AppInfo.CurrentUser.getUserId(),
				farmerID, fieldID, 0);
		RegisterFarmerActivity.file = null;
		Toast.makeText(this, "CCE Successfully Added", Toast.LENGTH_SHORT)
				.show();
		finish();
	}

	private void cancelclickAction() {
		finish();
	}

	private void initiate() {
		cce_username = (TextView) findViewById(R.id.cce_username);
		cce_username.setText(AppInfo.CurrentUser.getUserName());
		cce_harvestDate = (TextView) findViewById(R.id.cce_harvest_date);
		final Calendar c = Calendar.getInstance();
		intyear = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		cce_harvestDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_PICKER_ID);
			}
		});
		cce_harvestArea = (EditText) findViewById(R.id.cce_harvest_area);
		cce_fw_pod_fp = (EditText) findViewById(R.id.cce_fw_pod_grains);
		cce_fw_fodder_fp = (EditText) findViewById(R.id.cce_fw_fodder);
		cce_fw_pod_ip = (EditText) findViewById(R.id.cce_fw_pod_improved);
		cce_fw_fodder_ip = (EditText) findViewById(R.id.cce_fw_fodder_ip);
		cce_ssfw_pod_ip = (EditText) findViewById(R.id.cce_ss_fw_pod);
		cce_ssfw_fodder_ip = (EditText) findViewById(R.id.cce_ss_fw_improvedp);
		cce_ssfw_pod_fp = (EditText) findViewById(R.id.cce_ss_fw_pod_fp);
		cce_ssfw_fodder_fp = (EditText) findViewById(R.id.cce_ss_fw_fodder);
		take_photo = (ImageView) findViewById(R.id.takePhoto);
		save = (Button) findViewById(R.id.cce_save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveclickAction();
			}
		});
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelclickAction();
			}
		});
		RegisterFarmerActivity.takePhoto = take_photo;
		take_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_ALERT);

			}
		});
	}

}
