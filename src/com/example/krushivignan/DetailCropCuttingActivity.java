package com.example.krushivignan;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class DetailCropCuttingActivity extends Activity {

	private TextView cce_username, cce_harvestDate;
	private EditText cce_harvestArea, cce_fw_pod_fp, cce_fw_fodder_fp,cce_fw_pod_ip, cce_fw_fodder_ip, cce_ssfw_pod_ip,
	 cce_ssfw_pod_fp,cce_ssfw_fodder_ip, cce_ssfw_fodder_fp;
	private Button save, cancel, edit, editPhoto;
	private DBHelper db;
	private String farmerID, fieldID, cropCuttingID;
	private String latitude;
	private String longitude;
	private GPSTracker gps;
	private ImageView take_photo;
	static final int DATE_PICKER_ID = 1111;
	static final int DATE_PICKER_ID_YEAR = 2222;
	private int intyear;
	private int month;
	private int day;
	CropCutting crop_cutting;
	private static final int DIALOG_ALERT = 10;
	String str_harvestArea, str_fw_pod_fp, str_fw_fodder_fp, str_fw_pod_ip,
			str_fw_fodder_ip, str_ssfw_pod_ip, str_ssfw_pod_fp,
			str_ssfw_fodder_ip, str_ssfw_fodder_fp, str_username,
			str_harvestDate, str_image, str_image1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_crop_cutting_layout);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		db = ((KrushiVignanApp) getApplication()).getDatabase();
		getComponents();
		save.setVisibility(View.INVISIBLE);
		edit.setVisibility(View.VISIBLE);
		editPhoto.setVisibility(View.INVISIBLE);
		farmerID = getIntent().getStringExtra("farmerID");
		fieldID = getIntent().getStringExtra("fieldID");
		toggleComponentEnabling(false);
		populateDatainScreen();
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
	}

	private void populateDatainScreen() {

		String sql = "Select * From CropCutting Where Farm_ID=\'" + fieldID+ "\' AND Farmer_ID=\'" + farmerID + "\'";
		crop_cutting = db.getCropCuttingDetails(sql);
		cropCuttingID = crop_cutting.getCropCutting_ID();
		cce_harvestArea.setText(crop_cutting.getCce_HarvestArea());
		cce_harvestDate.setText(crop_cutting.getCce_HarvestDate());
		cce_fw_pod_fp.setText(crop_cutting.getCce_Fw_Pod_Fp());
		cce_fw_fodder_fp.setText(crop_cutting.getCce_Fw_Fodder_Fp());
		cce_fw_pod_ip.setText(crop_cutting.getCce_Fw_Pod_Ip());
		cce_fw_fodder_ip.setText(crop_cutting.getCce_Fw_Fodder_Ip());
		cce_ssfw_pod_ip.setText(crop_cutting.getCce_ssfw_Pod_Ip());
		cce_ssfw_fodder_ip.setText(crop_cutting.getCce_ssfw_Fodder_Ip());
		cce_ssfw_pod_fp.setText(crop_cutting.getCce_ssfw_Pod_Fp());
		cce_ssfw_fodder_fp.setText(crop_cutting.getCce_ssfw_Fodder_Fp());
		str_image = crop_cutting.getCce_Image();
		latitude = crop_cutting.getLatitude();
		longitude = crop_cutting.getLongitude();
		Bitmap bitmap = BitmapFactory.decodeFile(crop_cutting.getCce_Image());
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.no_image);
		}
		RegisterFarmerActivity.takePhoto.setImageBitmap(bitmap);
	}

	private void toggleComponentEnabling(boolean status) {
		cce_harvestArea.setEnabled(status);
		cce_harvestDate.setEnabled(status);
		cce_fw_pod_fp.setEnabled(status);
		cce_fw_fodder_fp.setEnabled(status);
		cce_fw_pod_ip.setEnabled(status);
		cce_fw_fodder_ip.setEnabled(status);
		cce_ssfw_pod_ip.setEnabled(status);
		cce_ssfw_fodder_ip.setEnabled(status);
		cce_ssfw_pod_fp.setEnabled(status);
		cce_ssfw_fodder_fp.setEnabled(status);
	}

	private void getComponents() {
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
		editPhoto = (Button) findViewById(R.id.editPhoto);
		editPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_ALERT);

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
				toggleComponentEnabling(false);

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

		edit = (Button) findViewById(R.id.cce_edit);
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				save.setVisibility(View.VISIBLE);
				edit.setVisibility(View.INVISIBLE);
				editPhoto.setVisibility(View.VISIBLE);
				toggleComponentEnabling(true);

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

	private void cancelclickAction() {
		finish();
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
		str_image1 = RegisterFarmerActivity.file + "";
		if (str_image1.equalsIgnoreCase("null")) {
			str_image1 = str_image;
		}

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
				&& (str_image1 != null && str_image1.length() > 0)) {
			pushDatatoDB();
		} else {
			Toast.makeText(getApplicationContext(), "Please Fill All Fields",
					Toast.LENGTH_LONG).show();
		}

		// return true;

	}

	private void pushDatatoDB() {
		CropCutting crop_cutting = new CropCutting();
		crop_cutting.setCropCutting_ID(cropCuttingID);
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
		crop_cutting.setCce_Image(str_image1 + "");
		crop_cutting.setSync_status(0 + "");

		db.updateCropCutting(crop_cutting);
		RegisterFarmerActivity.file = null;
		Toast.makeText(this, "CCE Successfully Updated", Toast.LENGTH_SHORT)
				.show();
		finish();
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
			cce_harvestDate.setText(new StringBuilder().append(intyear)
					.append("-").append(month + 1).append("-").append(day)
					.append(""));

		}
	};

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
}
