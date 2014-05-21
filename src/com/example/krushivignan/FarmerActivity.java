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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.Farmer;
import com.nunc.krushivignan.core.FieldRecommendation;
import com.nunc.krushivignan.core.Mapdata;
import com.nunc.krushivignan.db.DBHelper;

public class FarmerActivity extends ListActivity implements OnItemSelectedListener {
	private ArrayList<Farmer> list;
	private DBHelper db;
	private TextView label1, label2, label4, label5, label6, label7,label8;
	private Button addFarmer, refresh;
	private SharedPreferences _prefs;
	private String language_str;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farmer_list_layout);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		db = ((KrushiVignanApp) getApplication()).getDatabase();
		ListView lv = getListView();
		lv.setItemsCanFocus(false);
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Farmer farmer = (Farmer) parent.getAdapter().getItem(position);
				Intent edit = new Intent(getParent(),
						FarmerDetailsActivity.class);
				edit.putExtra("SelectedFarmer", farmer);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.startChildActivity("Farmer_Detail", edit);
			}

		});
		_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
		language_str = _prefs.getString("SelectedLanguage", "");
		getComponentValues();
		setLanguage(language_str);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (HomeActivity.mQuery) {
			list = HomeActivity.mData;
			setListAdapter(new FarmerAdapter(list));
		} else {
			setupDummyData("select *from Farmer where USERID=\'"
					+ AppInfo.CurrentUser.getUserId() + "\' ORDER BY First_Name ASC");
			setListAdapter(new FarmerAdapter(list));
		}

	}

	public void addFarmer(View v) {
		if(AppInfo.CurrentUser.getRole().equalsIgnoreCase("FF")){
			Intent edit = new Intent(getParent(), RegisterFarmerActivity.class);
			TabGroupActivity parentActivity = (TabGroupActivity) getParent();
			parentActivity.startChildActivity("Register_Farmer", edit);
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
	
	

	public void onRefresh(View v) {
		HomeActivity.mQuery = false;
		setupDummyData("select *from Farmer where USERID=\'"
				+ AppInfo.CurrentUser.getUserId() + "\'");
		setListAdapter(new FarmerAdapter(list));
	}

	private void setupDummyData(String query) {

		list = db.getFarmers(query);
	}

	static class ViewHolder {
        TextView first_name, mobile_Num, village;
        Button viewFields, viewRecom, viewMap;

}

class FarmerAdapter extends BaseAdapter {

        private ArrayList<Farmer> list;

        public FarmerAdapter(ArrayList<Farmer> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View v = convertView;

			if (v == null) {
				holder = new ViewHolder();
				LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.farmer_list_item, null);
				holder.first_name = (TextView) v.findViewById(R.id.first_name);
				holder.mobile_Num = (TextView) v.findViewById(R.id.mobile);
				holder.village = (TextView) v.findViewById(R.id.village);
				holder.viewFields = (Button) v.findViewById(R.id.viewField);
				holder.viewRecom = (Button) v.findViewById(R.id.viewRecom);
				holder.viewMap = (Button) v.findViewById(R.id.viewMap);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			final Farmer farmer = new Farmer(list.get(position));
			holder.first_name.setText(farmer.getFirst_Name().trim() + " "
					+ farmer.getLast_Name().trim());
			holder.mobile_Num.setText(farmer.getMobile_No());
			holder.village.setText(farmer.getVillage());
			if (language_str.startsWith("English")) {
				holder.viewFields
						.setText(R.string.farmer_list_item_button_viewfield_english);
				holder.viewRecom
						.setText(R.string.farmer_list_item_button_viewfield_english);
				holder.viewMap
				.setText(R.string.farmer_list_item_button_viewfield_english);
			} else if (language_str.startsWith("Kanada")) {
				holder.viewFields
						.setText(R.string.farmer_list_item_button_viewfield_kanada);
				holder.viewRecom
						.setText(R.string.farmer_list_item_button_viewfield_kanada);
				holder.viewMap
				.setText(R.string.farmer_list_item_button_viewfield_kanada);
			}
			holder.viewFields.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent edit = new Intent(getParent(),
							FieldListActivity.class);
					edit.putExtra("SelectedFarmer", farmer);
					TabGroupActivity parentActivity = (TabGroupActivity) getParent();
					parentActivity.startChildActivity("Field_List", edit);
				}
			});
			holder.viewRecom.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					recommendationDialog(farmer);
				}
			});
			holder.viewMap.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent edit = new Intent(getParent(),
							DisplayFarmOnMapActivity.class);
					Mapdata data = new Mapdata();
					data.setTitle(farmer.getVillage());
					data.setDescription(farmer.getFirst_Name());
					data.setLatitude(farmer.getLatitude());
					data.setLongitude(farmer.getLongitude());

					edit.putExtra("MapData", data);
					TabGroupActivity parentActivity = (TabGroupActivity) getParent();
					parentActivity.startChildActivity("Field_Detail", edit);
				}
			});
			return v;
		}
	}
	

	public void recommendationDialog(Farmer farmer) {
		final Dialog dialog = new Dialog(getParent());
		dialog.setContentView(R.layout.recommendation_dialog1);
		dialog.setTitle("Recommendations (Kg/ha)");
		
		getDialogComponents(farmer,dialog);

		dialog.show();
		
	}

	private void getDialogComponents(final Farmer farmer,final Dialog dialog) {

		final EditText urea = (EditText) dialog.findViewById(R.id.urea);
		final EditText dap = (EditText) dialog.findViewById(R.id.dap);
		final EditText mop = (EditText) dialog.findViewById(R.id.mop);
		final EditText gypsum = (EditText) dialog.findViewById(R.id.gypsum);
		final EditText zincsulphate = (EditText) dialog
				.findViewById(R.id.zinc_sulphate);
		final EditText borax = (EditText) dialog.findViewById(R.id.borax);
		final EditText ssp_ssp=(EditText)dialog.findViewById(R.id.ssp_ssp);
		final EditText ssp_urea=(EditText)dialog.findViewById(R.id.ssp_urea);
		final EditText ssp_gypsum=(EditText)dialog.findViewById(R.id.ssp_gypsum);
		final EditText ssp_dap=(EditText)dialog.findViewById(R.id.ssp_dap);
		urea.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		        inputManager.hideSoftInputFromWindow(urea
		                .getWindowToken(), 1);
			}
		}, 100);
		
        
		Spinner crop_type = (Spinner) dialog.findViewById(R.id.crop_type);

		
		crop_type.setAdapter(new ArrayAdapter<String>(getParent(),
				android.R.layout.simple_spinner_item, db
						.getCropNames(AppInfo.CurrentUser.getUserId(), 
								farmer.getDistrict(),
								farmer.getTaluk())));
		crop_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String cropType = (String) arg0.getAdapter().getItem(arg2);
				FieldRecommendation rec = db.getRecommendation(
						farmer.getDistrict(),
						farmer.getTaluk(), cropType);// Current
				// User
				if (rec != null) {
					urea.setText(rec.getUrea());
					dap.setText(rec.getDap());
					mop.setText(rec.getMop());
					gypsum.setText(rec.getGypsum());
					zincsulphate.setText(rec.getZinc_sulphate());
					borax.setText(rec.getBorax());
					ssp_ssp.setText(rec.getSsp_ssp());
					ssp_urea.setText(rec.getSsp_urea());
					ssp_gypsum.setText(rec.getSsp_gypsum());
					ssp_dap.setText(rec.getSsp_dap());

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		Button dialogButton = (Button) dialog.findViewById(R.id.ok);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			}
		});
	}

	private void setLanguage(String language) {
		if (language.startsWith("English")) {
			label1.setText(R.string.farmer_list_label1_english);
			label2.setText(R.string.farmer_list_label2_english);
			label4.setText(R.string.farmer_list_label4_english);
			label5.setText(R.string.farmer_list_label5_english);
			label6.setText(R.string.farmer_list_label6_english);
			label7.setText(R.string.farmer_list_label7_english);
			label8.setText(R.string.field_list_label7_english);
			addFarmer.setText(R.string.farmer_list_button_addfarmer_english);
			refresh.setText(R.string.farmer_list_button_refresh_enlish);
		} else if (language.startsWith("Kanada")) {
			label1.setText(R.string.farmer_list_label1_kanada);
			label2.setText(R.string.farmer_list_label2_kanada);
			label4.setText(R.string.farmer_list_label4_kanada);
			label5.setText(R.string.farmer_list_label5_kanada);
			label6.setText(R.string.farmer_list_label6_kanada);
			label7.setText(R.string.farmer_list_label7_kanada);
			addFarmer.setText(R.string.farmer_list_button_addfarmer_kanada);
			label8.setText(R.string.field_list_label7_kanada);
			refresh.setText(R.string.farmer_list_button_refresh_kanada);
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
		addFarmer = (Button) findViewById(R.id.addFarmer);
		refresh = (Button) findViewById(R.id.refresh);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
