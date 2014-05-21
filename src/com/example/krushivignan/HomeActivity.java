package com.example.krushivignan;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.Farmer;
import com.nunc.krushivignan.db.DBHelper;
import com.nunc.krushivignan.db.DatabaseStaticField;

public class HomeActivity extends Activity {
	private DBHelper db;
	private Spinner sp_Village;
	private EditText ed_FristName;
	public static ArrayList<Farmer> mData;
	public static boolean mQuery;

	private TextView label1, label2, label4;
	private Button addFarmer, search, refresh, new_training;
	private SharedPreferences _prefs;
	private String language_str;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initialization();
		_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
		language_str = _prefs.getString("SelectedLanguage", "");
		getComponentValues();
		setLanguage(language_str);

		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_FristName.setText("");
			}
		});
	}

	private void initialization() {
		// TODO Auto-generated method stub
		ed_FristName = (EditText) findViewById(R.id.ed_fristname);
		sp_Village = (Spinner) findViewById(R.id.sp_village);
		db = ((KrushiVignanApp) getApplication()).getDatabase();

	}

	public void newFieldVisit(View v) {
		Intent edit = new Intent(getParent(), AddFieldVisitActivity.class);
		edit.putExtra("isFromList", false);
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		parentActivity.startChildActivity("FieldVisitActivity", edit);
	}

	public void cropCutting(View v) {
		Intent edit = new Intent(getParent(), CropCuttingActivity.class);
		edit.putExtra("isFromList", false);
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		parentActivity.startChildActivity("CropCuttingActivity", edit);
	}

	public void addFarmer(View v) {
		if (AppInfo.CurrentUser.getRole().equalsIgnoreCase("FF")) {
			Intent edit = new Intent(getParent(), RegisterFarmerActivity.class);
			TabGroupActivity parentActivity = (TabGroupActivity) getParent();
			parentActivity.startChildActivity("Register_Farmer", edit);
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

	public void onSearch(View v) {

		String query = null;
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ed_FristName.getWindowToken(), 0);
		} catch (Exception e) {
		}

		if (!(ed_FristName.getText().toString().length() == 0 && (sp_Village
				.getSelectedItem() == null || sp_Village.getSelectedItem()
				.toString().equalsIgnoreCase("Choose Village")))) {

			String[] splitarray = ed_FristName.getText().toString().split(" ");
			if (splitarray.length >= 2) {
				query = "select *from Farmer where ("
						+ DatabaseStaticField.KEY_FARMER_FIRST_NAME + " like "
						+ "'%" + splitarray[0] + "%'  OR "
						+ DatabaseStaticField.KEY_FARMER_LAST_NAME + " like "
						+ "'%" + splitarray[1] + "%' ) And USERID=\'"
						+ AppInfo.CurrentUser.getUserId()
						+ "\' ORDER BY First_Name ASC";
			} else {

				if (ed_FristName.getText().toString().length() > 0
						&& (sp_Village.getSelectedItem() == null || sp_Village
								.getSelectedItem().toString()
								.equalsIgnoreCase("Choose Village"))) {
					query = "select *from Farmer where ("
							+ DatabaseStaticField.KEY_FARMER_FIRST_NAME
							+ " like " + "'%"
							+ ed_FristName.getText().toString() + "%'  OR "
							+ DatabaseStaticField.KEY_FARMER_LAST_NAME
							+ " like " + "'%"
							+ ed_FristName.getText().toString()
							+ "%' ) And USERID=\'"
							+ AppInfo.CurrentUser.getUserId()
							+ "\' ORDER BY First_Name ASC";
				} else if (ed_FristName.getText().toString().length() > 0
						&& !(sp_Village.getSelectedItem() == null || sp_Village
								.getSelectedItem().toString()
								.equalsIgnoreCase("Choose Village"))) {
					query = "select *from Farmer where ("
							+ DatabaseStaticField.KEY_FARMER_FIRST_NAME
							+ " like " + "'%"
							+ ed_FristName.getText().toString() + "%'" + " OR "
							+ DatabaseStaticField.KEY_FARMER_LAST_NAME
							+ " like " + "'%"
							+ ed_FristName.getText().toString() + "%' ) And "
							+ DatabaseStaticField.KEY_FARMER_VILLAGE + " like "
							+ "'%" + sp_Village.getSelectedItem().toString()
							+ "%' And USERID=\'"
							+ AppInfo.CurrentUser.getUserId()
							+ "\' ORDER BY First_Name ASC";

				} else if (ed_FristName.getText().toString().length() == 0

						&& !(sp_Village.getSelectedItem() == null || sp_Village
								.getSelectedItem().toString()
								.equalsIgnoreCase("Choose Village"))) {
					query = "select *from Farmer where "
							+ DatabaseStaticField.KEY_FARMER_VILLAGE + " like "
							+ "'%" + sp_Village.getSelectedItem().toString()
							+ "%' And USERID=\'"
							+ AppInfo.CurrentUser.getUserId()
							+ "\' ORDER BY First_Name ASC";
				}
			}

			Log.e("Query", "" + splitarray.length);
			ArrayList<Farmer> list = db.getFarmers(query);
			if (!list.isEmpty()) {
				mQuery = true;
				try {
					mData.clear();
				} catch (Exception e) {
				}
				mData = list;
				TabActivity tab = (TabActivity) ((TabGroupActivity) getParent())
						.getParent();
				tab.getTabHost().setCurrentTab(1);
				LocalActivityManager manager = tab.getLocalActivityManager();
				String currentTag = tab.getTabHost().getCurrentTabTag();
				Class<? extends Activity> currentClass = manager
						.getCurrentActivity().getClass();
				manager.startActivity(currentTag, new Intent(this,
						FarmerGroupActivity.class));
			} else {
				AlertDialog.Builder mDialog = new AlertDialog.Builder(
						getParent());
				mDialog.setMessage("No Result Found ");
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
		} else {
			AlertDialog.Builder mDialog = new AlertDialog.Builder(getParent());
			mDialog.setMessage("Name and Village Name Not Selected");
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

	private void setLanguage(String language) {
		if (language.startsWith("English")) {
			label1.setText(R.string.search_farmer_label1_english);
			label2.setText(R.string.search_farmer_label2_english);
			label4.setText(R.string.search_farmer_label4_english);
			addFarmer.setText(R.string.search_farmer_add_english);
			search.setText(R.string.search_farmer_search_english);
			refresh.setText(R.string.farmer_list_button_refresh_enlish);

		} else if (language.startsWith("Kanada")) {
			label1.setText(R.string.search_farmer_label1_kanada);
			label2.setText(R.string.search_farmer_label2_kanada);
			label4.setText(R.string.search_farmer_label4_kanada);
			addFarmer.setText(R.string.search_farmer_add_kanada);
			search.setText(R.string.search_farmer_search_kanada);
			refresh.setText(R.string.farmer_list_button_refresh_kanada);
		}

	}

	private void getComponentValues() {

		label1 = (TextView) findViewById(R.id.label1);
		label2 = (TextView) findViewById(R.id.label2);
		label4 = (TextView) findViewById(R.id.label4);
		addFarmer = (Button) findViewById(R.id.addFarmer);
		search = (Button) findViewById(R.id.search);
		refresh = (Button) findViewById(R.id.refreshFarmer);
		new_training = (Button) findViewById(R.id.new_training);
		new_training.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent edit = new Intent(getParent(), NewTrainingActivity.class);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("NewTrainingActivity", edit);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		ArrayList<String> mListVillage = new ArrayList<String>(db.getVillage(
				"select * from Village where userId=\'"
						+ AppInfo.CurrentUser.getUserId() + "\'",
				DatabaseStaticField.KEY_VILLAGE_NAME));

		mListVillage.add(0, "Choose Village");
		if (mListVillage.size() > 1) {
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					getParent(), android.R.layout.simple_spinner_item,
					mListVillage);
			sp_Village.setAdapter(spinnerArrayAdapter);
		}

	}

}
