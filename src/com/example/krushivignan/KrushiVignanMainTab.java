package com.example.krushivignan;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;

import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.threadcallback.MasterUploadSyncThread;
import com.nunc.krushivignan.threadcallback.TrainingRecordSyncThread;
import com.nunc.krushivignan.threadcallback.UploadSyncThread;
import com.nunc.krushivignan.util.ConnectionDetector;

/**
 * @author Nildari Prasad
 * 
 */
@SuppressWarnings("deprecation")
public class KrushiVignanMainTab extends TabActivity {
	/** Called when the activity is first created. */

	public static final String LOG_COLLECTOR_PACKAGE_NAME = "com.xtralogic.android.logcollector";//$NON-NLS-1$
	public static final String ACTION_SEND_LOG = "com.xtralogic.logcollector.intent.action.REPORT_LOG";//$NON-NLS-1$
	public static final String EXTRA_SEND_INTENT_ACTION = "com.xtralogic.logcollector.intent.extra.SEND_INTENT_ACTION";//$NON-NLS-1$
	public static final String EXTRA_DATA = "com.xtralogic.logcollector.intent.extra.DATA";//$NON-NLS-1$
	public static final String EXTRA_ADDITIONAL_INFO = "com.xtralogic.logcollector.intent.extra.ADDITIONAL_INFO";//$NON-NLS-1$
	public static final String EXTRA_SHOW_UI = "com.xtralogic.logcollector.intent.extra.SHOW_UI";//$NON-NLS-1$
	public static final String EXTRA_FILTER_SPECS = "com.xtralogic.logcollector.intent.extra.FILTER_SPECS";//$NON-NLS-1$
	public static final String EXTRA_FORMAT = "com.xtralogic.logcollector.intent.extra.FORMAT";//$NON-NLS-1$
	public static final String EXTRA_BUFFER = "com.xtralogic.logcollector.intent.extra.BUFFER";//$NON-NLS-1$

	private SharedPreferences _prefs;
	private String language_str;
	private Button sync;
	// private DBHelper db;

	private TextView label1, label2, label3, label4, label5;

	private ImageView logout;
	private TextView user_name, district, taluk, village;
	private SharedPreferences.Editor _editor;

	private PopupWindow popup;
	private Point mPoint;
	private static final int DIALOG_ALERT = 10;
	private static final int DIALOG_USERINFO = 11;

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.krushi_gyanasagar_tab_layout);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
		_editor = _prefs.edit();
		language_str = _prefs.getString("SelectedLanguage", "");
		getComponentValues();
		setLanguage(language_str);
		setTabs(language_str);
		user_name = (TextView) findViewById(R.id.user_name);
		district = (TextView) findViewById(R.id.district);
		taluk = (TextView) findViewById(R.id.taluk);
		village = (TextView) findViewById(R.id.village);
		user_name.setText("  " + AppInfo.CurrentUser.getUserName());
		district.setText("  " + AppInfo.CurrentUser.getDistrict());
		taluk.setText("  " + AppInfo.CurrentUser.getTaluk());
		village.setText("  " + AppInfo.CurrentUser.getVillage());
		// db = ((KrushiVignanApp) getApplication()).getDatabase();
		//
		// db.getAllCropsFromRecommendation(AppInfo.CurrentUser.getDistrict(),
		// AppInfo.CurrentUser.getTaluk());
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Helping method for showPopup() method
		int[] location = new int[2];

		logout = (ImageView) findViewById(R.id.logout_img);
		logout.getLocationOnScreen(location);

		// Initialize the Point with x, and y positions
		mPoint = new Point();
		mPoint.x = location[0];
		mPoint.y = location[1];
	}

	// The method that displays the popup.
	private void showPopup(final Activity context, Point p) {

		int popupWidth = 180;
		int popupHeight = 275;

		// Inflate the popup_layout.xml
		LinearLayout viewGroup = (LinearLayout) context
				.findViewById(R.id.popup);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);
		TextView txt_Setting = (TextView) layout
				.findViewById(R.id.Resources_txt);
		TextView txt_Logout = (TextView) layout
				.findViewById(R.id.ApplicatorsRecord_txt);
		TextView send_bug = (TextView) layout.findViewById(R.id.send_bug);

		TextView txt_exit = (TextView) layout.findViewById(R.id.exit_txt);
		TextView UserInfo_txt = (TextView) layout
				.findViewById(R.id.UserInfo_txt);

		txt_Setting.setOnClickListener(new ClickEventClass());
		txt_Logout.setOnClickListener(new ClickEventClass());
		txt_exit.setOnClickListener(new ClickEventClass());
		send_bug.setOnClickListener(new ClickEventClass());
		UserInfo_txt.setOnClickListener(new ClickEventClass());
		// Creating the PopupWindow
		popup = new PopupWindow(context);
		popup.setContentView(layout);
		popup.setWidth(popupWidth);
		popup.setHeight(popupHeight);
		popup.setFocusable(true);

		// Some offset to align the popup a bit to the right, and a bit down,
		// relative to button's position.
		int OFFSET_X = 30;
		int OFFSET_Y = 30;

		popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y
				+ OFFSET_Y);

		// ClickEvent on Popup ListItem

	}

	class ClickEventClass implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.Resources_txt) {
				popup.dismiss();
				Dialog settingsDialog = new Dialog(KrushiVignanMainTab.this);
				settingsDialog.getWindow().requestFeature(
						Window.FEATURE_NO_TITLE);
				settingsDialog.setContentView(getLayoutInflater().inflate(
						R.layout.image_layout, null));
				settingsDialog.setCancelable(true);
				settingsDialog.setCanceledOnTouchOutside(true);
				settingsDialog.show();
			} else if (v.getId() == R.id.ApplicatorsRecord_txt) {
				popup.dismiss();
				_editor.remove("Selected_User");
				_editor.commit();
				AppInfo.CurrentUser = null;
				startActivity(new Intent(KrushiVignanMainTab.this,
						LoginActivity.class));
				finish();
			}

			else if (v.getId() == R.id.exit_txt) {
				popup.dismiss();
				finish();
			} else if (v.getId() == R.id.send_bug) {
				popup.dismiss();
				SendBugReport();
			} else if (v.getId() == R.id.UserInfo_txt) {
				popup.dismiss();
				showDialog(DIALOG_USERINFO);
			}
		}

	}

	private void setLanguage(String language) {
		if (language.startsWith("English")) {
			sync.setText(R.string.tab_header_button_text_sync_english);

			label1.setText(R.string.tab_header_label_text_user_english);
			label2.setText(R.string.tab_header_label_text_district_english);
			label3.setText(R.string.tab_header_label_text_taluk_english);
			label4.setText(R.string.tab_header_label_text_village_english);
		} else if (language.startsWith("Kanada")) {
			sync.setText(R.string.tab_header_button_text_sync_kanada);

			label1.setText(R.string.tab_header_label_text_user_kanada);
			label2.setText(R.string.tab_header_label_text_district_kanada);
			label3.setText(R.string.tab_header_label_text_taluk_kanada);
			label4.setText(R.string.tab_header_label_text_village_kanada);
		}

	}

	private void getComponentValues() {
		sync = (Button) findViewById(R.id.sync);
		label1 = (TextView) findViewById(R.id.label1);
		label2 = (TextView) findViewById(R.id.label2);
		label3 = (TextView) findViewById(R.id.label3);
		label4 = (TextView) findViewById(R.id.label4);
	}

	public void logout(View v) {
		showPopup(this, mPoint);

	}

	private void setTabs(String language) {
		if (language.startsWith("English")) {
			addTab(getResources()
					.getString(R.string.tab_item_home_text_english),
					R.drawable.ic_launcher, HomeGroupActivity.class);
			addTab(getResources().getString(
					R.string.tab_item_farmers_text_english),
					R.drawable.ic_launcher, FarmerGroupActivity.class);
			addTab(getResources().getString(
					R.string.tab_item_package_of_practices_text_english),
					R.drawable.ic_launcher, PackageGroupActivity.class);
			addTab(getResources().getString(
					R.string.tab_item_package_of_soilmaps_text_english),
					R.drawable.ic_launcher, SoilMapActivity.class);
//			addTab("Videos", R.drawable.ic_launcher, PlayVideoActivity.class);
		} else if (language.startsWith("Kanada")) {
			addTab(getResources().getString(R.string.tab_item_home_text_kanada),
					R.drawable.ic_launcher, HomeGroupActivity.class);
			addTab(getResources().getString(
					R.string.tab_item_farmers_text_kanada),
					R.drawable.ic_launcher, FarmerGroupActivity.class);
			addTab(getResources().getString(
					R.string.tab_item_package_of_practices_text_kanada),
					R.drawable.ic_launcher, PackageGroupActivity.class);
			addTab(getResources().getString(
					R.string.tab_item_package_of_soilmaps_text_kanada),
					R.drawable.ic_launcher, SoilMapActivity.class);
//			addTab("Videos", R.drawable.ic_launcher, PlayVideoActivity.class);
		}

		getTabHost().setCurrentTab(0);

	}

	private void addTab(String labelId, int drawableId, Class<?> c) {
		try {
			TabHost tabHost = getTabHost();
			Intent intent = new Intent(this, c);
			TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

			View tabIndicator = LayoutInflater.from(this).inflate(
					R.layout.tab_indicator, getTabWidget(), false);
			TextView title = (TextView) tabIndicator.findViewById(R.id.title);
			title.setText(labelId);

			spec.setIndicator(tabIndicator);
			spec.setContent(intent);
			tabHost.addTab(spec);

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Error", e + "");
		}
	}

	public void onSyncData(View v) {

		showDialog(DIALOG_ALERT);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ALERT:
			// Create out AlterDialog
			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Select Synchronization Method");
			builder.setCancelable(true);
			builder.setPositiveButton("Partial Synchronization",
					new OkOnClickListener());
			builder.setNegativeButton("Full Synchronization",
					new CancelOnClickListener());
			AlertDialog dialog = builder.create();
			dialog.show();
			break;
		case DIALOG_USERINFO:

			final Dialog mDialog = new Dialog(KrushiVignanMainTab.this);
			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialog.setContentView(R.layout.user_info);

			TextView txt_UserName,
			txt_Village,
			txt_Taluk,
			txt_District,
			txt_PartialSyn,
			txt_FullSyn;
			txt_UserName = (TextView) mDialog.findViewById(R.id.userId);
			txt_Village = (TextView) mDialog.findViewById(R.id.village);
			txt_Taluk = (TextView) mDialog.findViewById(R.id.taluk);
			txt_District = (TextView) mDialog.findViewById(R.id.district);
			txt_PartialSyn = (TextView) mDialog
					.findViewById(R.id.lastPartialSync);
			txt_FullSyn = (TextView) mDialog.findViewById(R.id.lastFullSync);
			txt_UserName.setText(AppInfo.CurrentUser.getUserName());
			txt_Village.setText(AppInfo.CurrentUser.getVillage());
			txt_Taluk.setText(AppInfo.CurrentUser.getTaluk());
			txt_District.setText(AppInfo.CurrentUser.getDistrict());
			txt_PartialSyn
					.setText(AppInfo.CurrentUser.getLastPartialSyncTime());
			txt_FullSyn.setText(AppInfo.CurrentUser.getLastFullSyncTime());

			mDialog.setCancelable(true);
			Button ok = (Button) mDialog.findViewById(R.id.ok);
			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mDialog.dismiss();
				}
			});
			mDialog.show();
			break;
		}
		return super.onCreateDialog(id);
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			MasterUploadSyncThread thread = new MasterUploadSyncThread(
					KrushiVignanMainTab.this);
			thread.startSync();

			

		}
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			
			
			if(new ConnectionDetector(KrushiVignanMainTab.this).isOnline()){
			UploadSyncThread thread = new UploadSyncThread(
					KrushiVignanMainTab.this);
			thread.startSync();
			
			}else{
				new ConnectionDetector(KrushiVignanMainTab.this).alertDialog("Please check your network access");
			}

		}

	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {

				// Tell the framework to start tracking this event.
				return true;

			} else if (event.getAction() == KeyEvent.ACTION_UP) {
				if (event.isTracking() && !event.isCanceled()) {

					// DO BACK ACTION HERE
					return true;

				}
			}
			return super.dispatchKeyEvent(event);
		} else {
			return super.dispatchKeyEvent(event);
		}
	}

	public void SendBugReport() {
		final PackageManager packageManager = getPackageManager();
		final Intent intent = new Intent(ACTION_SEND_LOG);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_SEND_INTENT_ACTION, Intent.ACTION_SEND);
		final String email = "";
		intent.putExtra(EXTRA_DATA, Uri.parse("mailto:" + email));
		intent.putExtra(EXTRA_ADDITIONAL_INFO,
				"Additonal info: <additional info from the device (firmware revision, etc.)>\n");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Application failure report");

		intent.putExtra(EXTRA_FORMAT, "time");

		startActivity(intent);
	}

}