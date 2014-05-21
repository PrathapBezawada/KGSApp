package com.example.krushivignan;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.Farmer;
import com.nunc.krushivignan.core.Field;
import com.nunc.krushivignan.core.FieldRecommendation;
import com.nunc.krushivignan.core.Mapdata;
import com.nunc.krushivignan.db.DBHelper;

public class FieldListActivity extends ListActivity {
	private ArrayList<Field> list = new ArrayList<Field>();

	// private ListView listView;
	private TextView label1, label2, label4, label5, label6, label7, label8;
	private Button addField;
	private SharedPreferences _prefs;
	private String language_str;
	private DBHelper db;
	Farmer farmer;
	String farmerId = null;
	FieldRecommendation rec;
	private Field field;
	public String farmerID, farmID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.field_list_layout);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		db = ((KrushiVignanApp) getApplication()).getDatabase();
		farmer = (Farmer) getIntent().getSerializableExtra("SelectedFarmer");
		farmerId = farmer.getFarmer_ID();
		_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
		language_str = _prefs.getString("SelectedLanguage", "");
		getComponentValues();
		setLanguage(language_str);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setupDummyData();

		setListAdapter(new FieldAdapter(list));
		ListView lv = getListView();
		lv.setItemsCanFocus(false);
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				field = (Field) parent.getAdapter().getItem(position);
				Intent edit = new Intent(getParent(),
						FieldDetailsActivity.class);
				edit.putExtra("SelectedField", field);
				farmerID = field.getFarmerid();
				// farmID = field.getFarm_ID();
				edit.putExtra("fieldID", farmID);
				edit.putExtra("farmerID", farmerID);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("Field_Map", edit);
			}

		});

	}

	public void addField(View v) {
		if (AppInfo.CurrentUser.getRole().equalsIgnoreCase("FF")) {
			Intent edit = new Intent(getParent(), AddFieldActivity.class);
			edit.putExtra("SelectedFarmer", farmer);
			TabGroupActivity parentActivity = (TabGroupActivity) getParent();
			parentActivity.startChildActivity("Add_Field", edit);
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

	private void setupDummyData() {
		String sql = "select * from Field where FARMER_ID=\'"
				+ farmer.getFarmer_ID() + "\' AND USERID=\'"
				+ AppInfo.CurrentUser.getUserId() + "\'";
		list = db.getFields(sql);
		Log.i("List", list + "");
	}

	static class ViewHolder {
		TextView farm_name, crop_name, village_name;
		Button addandviewsoilMapping, viewMap, viewFertilizer, ccexp;

	}

	class FieldAdapter extends BaseAdapter {

		private ArrayList<Field> list;

		public FieldAdapter(ArrayList<Field> list) {
			this.list = list;
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			View v = convertView;

			if (v == null) {
				holder = new ViewHolder();
				LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.field_list_item, null);
				holder.farm_name = (TextView) v.findViewById(R.id.farm_name);
				holder.crop_name = (TextView) v.findViewById(R.id.crop_name);
				holder.village_name = (TextView) v
						.findViewById(R.id.village_name);
				holder.addandviewsoilMapping = (Button) v
						.findViewById(R.id.addandviewsoilMapping);
				holder.ccexp = (Button) v.findViewById(R.id.viewCCE);
				final Field field = new Field(list.get(position));
				if (db.getCCEStatus(field.getFarm_ID())) {
					holder.ccexp.setBackgroundResource(R.drawable.view_cceimg);
				}
				holder.ccexp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (db.getCCEStatus(field.getFarm_ID())) {
							Intent intent = new Intent(getParent(),
									DetailCropCuttingActivity.class);

							intent.putExtra("fieldID", field.getFarm_ID());
							if (farmerId != null
									&& field.getFarmerid().length() > 0) {
								intent.putExtra("farmerID", field.getFarmerid());
							}

							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity(
									"DetailCropCuttingActivity", intent);

						} else {
							Intent edit = new Intent(getParent(),
									CropCuttingActivity.class);
							edit.putExtra("fieldID", field.getFarm_ID());
							edit.putExtra("farmerID", farmerID);
							if (farmerId != null && farmerId.length() > 0) {
								edit.putExtra("farmerID", farmerId);
							}
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.startChildActivity(
									"CropCuttingActivity", edit);
						}
					}
				});
				holder.viewMap = (Button) v.findViewById(R.id.viewMap);
				holder.viewFertilizer = (Button) v
						.findViewById(R.id.viewFertilizer);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			final Field field = new Field(list.get(position));
			holder.farm_name.setText(field.getFarm_Name());
			holder.crop_name.setText(field.getCrop_Name());
			holder.village_name.setText(field.getFarm_Village());
			if (language_str.startsWith("English")) {
				holder.addandviewsoilMapping
						.setText(R.string.field_list_item_soilmapping_english);
				holder.viewMap
						.setText(R.string.field_list_item_soilmapping_english);
			} else if (language_str.startsWith("Kanada")) {
				holder.addandviewsoilMapping
						.setText(R.string.field_list_item_soilmapping_kanada);
				holder.viewMap
						.setText(R.string.field_list_item_soilmapping_kanada);
			}
			holder.addandviewsoilMapping
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							recommendationDialog(field);
						}
					});
			holder.viewMap.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent edit = new Intent(getParent(),
							DisplayFarmOnMapActivity.class);
					Mapdata data = new Mapdata();
					data.setTitle(field.getCrop_Name());
					data.setDescription(field.getCrop_Name() + ","
							+ field.getFarm_Village());
					data.setLatitude(field.getLatitude());
					data.setLongitude(field.getLongitude());

					edit.putExtra("MapData", data);
					TabGroupActivity parentActivity = (TabGroupActivity) getParent();
					parentActivity.startChildActivity(
							"DisplayFarmOnMapActivity", edit);
				}
			});
			holder.viewFertilizer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					fertilizerDialog(field);
				}
			});
			return v;
		}
	}

	private void setLanguage(String language) {
		if (language.startsWith("English")) {
			String farmername = getResources().getString(
					R.string.field_list_label1_english)
					+ "- " + farmer.getFirst_Name();
			label1.setText(farmername);
			label2.setText(R.string.field_list_label2_english);
			label4.setText(R.string.field_list_label4_english);
			label5.setText(R.string.field_list_label5_english);
			label6.setText(R.string.field_list_label6_english);
			label7.setText(R.string.field_list_label7_english);
			label8.setText(R.string.field_list_label8_english);
			addField.setText(R.string.field_list_button_addfield_english);

		} else if (language.startsWith("Kanada")) {
			String farmername = getResources().getString(
					R.string.field_list_label1_kanada)
					+ "- " + farmer.getFirst_Name();
			label1.setText(farmername);
			label2.setText(R.string.field_list_label2_kanada);
			label4.setText(R.string.field_list_label4_kanada);
			label5.setText(R.string.field_list_label5_kanada);
			label6.setText(R.string.field_list_label6_kanada);
			label7.setText(R.string.field_list_label7_kanada);
			label8.setText(R.string.field_list_label8_kanada);
			addField.setText(R.string.field_list_button_addfield_kanada);
		}

	}

	private void getComponentValues() {

		label1 = (TextView) findViewById(R.id.label1);
		label2 = (TextView) findViewById(R.id.label2);
		label4 = (TextView) findViewById(R.id.label4);
		label5 = (TextView) findViewById(R.id.label5);
		label6 = (TextView) findViewById(R.id.label6);
		label7 = (TextView) findViewById(R.id.label7);
		label8 = (TextView) findViewById(R.id.label8);
		addField = (Button) findViewById(R.id.addField);
	}

	public void fertilizerDialog(Field field) {
		final Dialog dialog = new Dialog(getParent());
		dialog.setContentView(R.layout.fertilizer_dialog);
		dialog.setTitle("Used Fertilizers (Kg) For " + field.getCrop_Name());
		getFertilizerDialogComponents(dialog, field);

		dialog.show();

	}

	private void getFertilizerDialogComponents(final Dialog dialog,
			final Field field) {

		final EditText urea = (EditText) dialog.findViewById(R.id.urea);
		EditText dap = (EditText) dialog.findViewById(R.id.dap);
		EditText potash = (EditText) dialog.findViewById(R.id.potash);
		EditText gypsum = (EditText) dialog.findViewById(R.id.gypsum);
		EditText zincsulphate = (EditText) dialog
				.findViewById(R.id.zinc_sulphate);
		EditText borax = (EditText) dialog.findViewById(R.id.borax);
		EditText complex = (EditText) dialog.findViewById(R.id.complex);
		urea.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(urea.getWindowToken(), 1);
			}
		}, 100);
		if (!field.getCult_Area().toString().equals("")) {

			// User
//			float hector = Float.parseFloat(field.getCult_Area()) / 2.5f;
			urea.setText(Float.parseFloat(field.getUrea())+"");
			dap.setText((Float.parseFloat(field.getDap())) + "");
			potash.setText((Float.parseFloat(field.getPotash())) + "");
			gypsum.setText((Float.parseFloat(field.getGypsum())) + "");
			zincsulphate
					.setText((Float.parseFloat(field.getZincsulphate()))
							+ "");
			borax.setText((Float.parseFloat(field.getBorax())) + "");
			complex.setText((Float.parseFloat(field.getComplex()))
					+ "");

		} else {
			Toast.makeText(
					getApplicationContext(),
					"Please enter the cultivation area to see the recomendations",
					3000).show();
		}
		Button dialogButton = (Button) dialog.findViewById(R.id.ok);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rec != null)
					db.updateRecommendation(rec, field.getFarm_ID(),
							AppInfo.CurrentUser.getUserId());

				dialog.dismiss();
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			}
		});

	}

	public void recommendationDialog(Field field) {
		final Dialog dialog = new Dialog(getParent());
		dialog.setContentView(R.layout.recommendation_dialog);
		dialog.setTitle("Recommendations (Kg) For " + field.getCrop_Name());
		getDialogComponents(dialog, field);

		dialog.show();

	}

	private void getDialogComponents(final Dialog dialog, final Field field) {

		final EditText urea = (EditText) dialog.findViewById(R.id.urea);
		EditText dap = (EditText) dialog.findViewById(R.id.dap);
		EditText mop = (EditText) dialog.findViewById(R.id.mop);
		EditText gypsum = (EditText) dialog.findViewById(R.id.gypsum);
		EditText zincsulphate = (EditText) dialog
				.findViewById(R.id.zinc_sulphate);
		EditText borax = (EditText) dialog.findViewById(R.id.borax);
		EditText ssp_ssp = (EditText) dialog.findViewById(R.id.ssp_ssp);
		EditText ssp_urea = (EditText) dialog.findViewById(R.id.ssp_urea);
		EditText ssp_gypsum = (EditText) dialog.findViewById(R.id.ssp_gypsum);
		EditText ssp_dap = (EditText) dialog.findViewById(R.id.ssp_dap);
		urea.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(urea.getWindowToken(), 1);
			}
		}, 100);
		if (!field.getCult_Area().toString().equals("")) {

			rec = db.getFiledRecommendation(field.getFarm_ID(),
					field.getCrop_Name());
			if (rec == null)
				rec = db.getRecommendation(farmer.getDistrict(),
						farmer.getTaluk(), field.getCrop_Name());// Current
			// User
			float hector = Float.parseFloat(field.getCult_Area()) / 2.5f;
			if (rec != null) {
				urea.setText((Float.parseFloat(rec.getUrea()) * hector) + "");
				dap.setText((Float.parseFloat(rec.getDap()) * hector) + "");
				mop.setText((Float.parseFloat(rec.getMop()) * hector) + "");
				gypsum.setText((Float.parseFloat(rec.getGypsum()) * hector)
						+ "");
				zincsulphate
						.setText((Float.parseFloat(rec.getZinc_sulphate()) * hector)
								+ "");
				borax.setText((Float.parseFloat(rec.getBorax()) * hector) + "");
				ssp_ssp.setText((Float.parseFloat(rec.getSsp_ssp()) * hector)
						+ "");
				ssp_urea.setText((Float.parseFloat(rec.getSsp_urea()) * hector)
						+ "");
				ssp_gypsum
						.setText((Float.parseFloat(rec.getSsp_gypsum()) * hector)
								+ "");
				ssp_dap.setText((Float.parseFloat(rec.getSsp_dap()) * hector)
						+ "");
			}
		} else {
			Toast.makeText(
					getApplicationContext(),
					"Please enter the cultivation area to see the recomendations",
					3000).show();
		}
		Button dialogButton = (Button) dialog.findViewById(R.id.ok);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rec != null)
					db.updateRecommendation(rec, field.getFarm_ID(),
							AppInfo.CurrentUser.getUserId());

				dialog.dismiss();
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			}
		});

	}
}
