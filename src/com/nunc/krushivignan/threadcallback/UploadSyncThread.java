package com.nunc.krushivignan.threadcallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.util.Xml;

import com.example.krushivignan.KrushiVignanMainTab;
import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.Farmer;
import com.nunc.krushivignan.core.Field;
import com.nunc.krushivignan.core.ServerURL;
import com.nunc.krushivignan.core.User;
import com.nunc.krushivignan.db.DBHelper;

public class UploadSyncThread {
	private static final int sync = 1;
	private DBHelper db;
	private ProgressDialog _dialog;
	private Context context;

	public UploadSyncThread(Context context) {
		this.context = context;
		try {
			db = ((KrushiVignanApp) ((Activity) context).getApplication())
					.getDatabase();

		} catch (Exception e) {

		}
	}

	public void startSync() {
		// TODO Auto-generated method stub
		_dialog = ProgressDialog.show(context, "",
				"Uploading Farmers & Farms...");

		subscribe(ServerURL.getSyncURL());
		Log.e("the sending data to server is", ServerURL.getSyncURL());
	}

	private String writeXml() {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "FarmFacilitators");
			serializer.startTag("", "FarmFacilitator");
			serializer.attribute("", "Id", AppInfo.CurrentUser.getUserId());
			serializer.attribute("", "Name", AppInfo.CurrentUser.getUserName());
			serializer.attribute("", "Taluk", AppInfo.CurrentUser.getTaluk());
			serializer.attribute("", "District",
					AppInfo.CurrentUser.getDistrict());
			serializer.attribute("", "Partial_Sync_Date",
					AppInfo.CurrentUser.getLastPartialSyncTime());
			serializer.attribute("", "Full_Sync_Date",
					AppInfo.CurrentUser.getLastFullSyncTime());
			serializer.startTag("", "Farmers");
			ArrayList<Farmer> farmerList = db
					.getFarmers("select * from Farmer where USERID=\'"
							+ AppInfo.CurrentUser.getUserId()
							+ "\' and Sync_Status=0");
			for (Farmer farmer : farmerList) {
				if (farmer.getFarmer_Image() != null
						&& farmer.getFarmer_Image().length() > 0
						&& !farmer.getFarmer_Image().equalsIgnoreCase("null")) {
					ImageUploadThread thread = new ImageUploadThread(
							"103.13.242.103", "krishigy", "*KGS2012*",
							farmer.getFarmer_Image());
					thread.run();
				}
				serializer.startTag("", "Farmer");
				serializer.attribute("", "Farmer_ID", farmer.getFarmer_ID()
						+ "");
				serializer.attribute("", "First_Name", farmer.getFirst_Name()
						+ "");
				serializer.attribute("", "Last_Name", farmer.getLast_Name()
						+ "");
				serializer.attribute("", "Father_Name", farmer.getFather_Name()
						+ "");
				serializer.attribute("", "Mobile_No", farmer.getMobile_No()
						+ "");
				serializer.attribute("", "Telephone_No",
						farmer.getTelephone_No() + "");
				serializer.attribute("", "Caste", farmer.getCaste() + "");
				serializer.attribute("", "Sex", farmer.getSex() + "");
				serializer.attribute("", "District", farmer.getDistrict() + "");
				serializer.attribute("", "Taluk", farmer.getTaluk() + "");
				serializer.attribute("", "Hobli", farmer.getHobli() + "");
				serializer.attribute("", "Village", farmer.getVillage() + "");
				serializer.attribute("", "Created_Date",
						farmer.getCreated_Date() + "");
				serializer.attribute("", "LastModified_Date",
						farmer.getLastModified_Date() + "");

				serializer.attribute("", "Farmer_Image",
						getlastToken(farmer.getFarmer_Image()) + "");
				serializer.attribute("", "Total_Acre", farmer.getTotal_Acre()
						+ "");

				serializer.attribute("", "Rain_Fed", farmer.getRain_Fed() + "");
				serializer.attribute("", "Irrigated", farmer.getIrrigation()
						+ "");
				serializer.attribute("", "Plantation", farmer.getPlantation()
						+ "");
				serializer.attribute("", "Fallow", farmer.getFallow() + "");
				serializer.attribute("", "Survey_No", farmer.getSurvey_No()
						+ "");
				serializer.attribute("", "ID_Type", farmer.getId_type() + "");
				serializer.attribute("", "ID_No", farmer.getId_no() + "");
				serializer.attribute("", "Latitude", farmer.getLatitude());
				serializer.attribute("", "Longitude", farmer.getLongitude());
				serializer.endTag("", "Farmer");
			}
			serializer.endTag("", "Farmers");
			serializer.startTag("", "Fields");
			String sql = "select * from Field where USERID=\'"
					+ AppInfo.CurrentUser.getUserId() + "\' and Sync_Status=0";
			ArrayList<Field> fieldList = db.getFields(sql);
			for (Field field : fieldList) {
				if (field.getFieldimage() != null
						&& field.getFieldimage().length() > 0
						&& !field.getFieldimage().equalsIgnoreCase("null")) {
					ImageUploadThread thread = new ImageUploadThread(
							"103.13.242.103", "krishigy", "*KGS2012*",
							field.getFieldimage());
					thread.run();
				}
				serializer.startTag("", "Field");
				serializer.attribute("", "Farmer_ID", field.getFarmerid() + "");
				serializer.attribute("", "Farm_ID", field.getFarm_ID() + "");
				serializer
						.attribute("", "Farm_Name", field.getFarm_Name() + "");
				serializer.attribute("", "Field_Image",
						getlastToken(field.getFieldimage()) + "");

				serializer.attribute("", "Year", field.getYear() + "");
				serializer.attribute("", "Season", field.getSeason() + "");
				serializer.attribute("", "Survey_No", field.getSurveyno() + "");
				serializer.attribute("", "Sowing_Date", field.getSowingdate()
						+ "");
				serializer.attribute("", "Seed_Rate", field.getSeedrate() + "");
				serializer.attribute("", "Seed_Source", field.getSeedsource()
						+ "");
				serializer.attribute("", "Govt_Subsidy", field.getGovtsubsidy()
						+ "");
				serializer.attribute("", "Urea", field.getUrea() + "");
				serializer.attribute("", "Dap", field.getDap() + "");
				serializer.attribute("", "Potash", field.getPotash() + "");
				serializer.attribute("", "Complex", field.getComplex() + "");
				serializer.attribute("", "Zinc", field.getZincsulphate() + "");
				serializer.attribute("", "Borax", field.getBorax() + "");
				serializer.attribute("", "Gypsum", field.getGypsum() + "");

				serializer.attribute("", "Fields", field.getFields() + "");
				serializer
						.attribute("", "Farm_Area", field.getFarm_Area() + "");
				serializer
						.attribute("", "Cult_Area", field.getCult_Area() + "");
				serializer
						.attribute("", "Crop_Name", field.getCrop_Name() + "");
				serializer.attribute("", "Crop_Variety",
						field.getCrop_Variety() + "");
				serializer.attribute("", "Farm_Village",
						field.getFarm_Village() + "");
				serializer.attribute("", "Created_Date",
						field.getCreated_Date() + "");
				serializer.attribute("", "LastModified_Date",
						field.getLastModified_Date() + "");
				serializer.attribute("", "Latitude", field.getLatitude() + "");
				serializer
						.attribute("", "Longitude", field.getLongitude() + "");
				serializer.endTag("", "Field");

			}
			serializer.endTag("", "Fields");

			serializer.endTag("", "FarmFacilitator");

			serializer.endTag("", "FarmFacilitators");
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			Log.e("Error:::::", e + "");
			return "";
		}
	}

	public void subscribe(String url) {
		final String _url = url;

		Thread _thread = new Thread() {

			public void run() {

				Message _message = new Message();
				_message.what = sync;
				String str = writeXml();
				Log.i("\n\n\n\n\n\n\n\n\n\n\n\n\n\nSTRRRRRRRRRRRRRRRRRRRr in XML",
						str);
				subscribeWebMethod(_url, str);
				uiCallback.sendMessage(_message);
			}
		};
		_thread.start();
	}

	private void subscribeWebMethod(String subscriptionUrl, String xmlData) {
		try {
			HttpParams params = new BasicHttpParams();
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);
			DefaultHttpClient hc = new DefaultHttpClient(params);
			ResponseHandler<String> res = new BasicResponseHandler();
			HttpPost postMethod = new HttpPost(subscriptionUrl);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

			nameValuePairs.add(new BasicNameValuePair("syncxml", xmlData));

			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			String response = hc.execute(postMethod, res);
			Log.i("RESPONSE", response + "");
			SyncParserThread thread = new SyncParserThread(response, context);
			thread.start();
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("AAAAAAAAAAAAAAAAAAAAA", e + "");
		}
	}

	Handler uiCallback = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {

			case sync:
				_dialog.dismiss();
				FieldVisitSyncThread thread1 = new FieldVisitSyncThread(context);
				thread1.startSync();
				break;
			}
		}
	};

	private void displayAlert(String Msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setTitle("Close");
		alertDialogBuilder.setMessage(Msg).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						AppInfo.CurrentUser.setLastPartialSyncTime(new Date()
								+ "");
						dialog.cancel();
						SavePreference(AppInfo.CurrentUser);
						Intent in = new Intent(context,
								KrushiVignanMainTab.class);
						in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(in);
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private String getlastToken(String strPath) {
		String[] strArray;
		String strlttoken = null;

		strArray = strPath.split("/");

		strlttoken = strArray[strArray.length - 1];
		return strlttoken;

	}

	private void SavePreference(User user) {
		SharedPreferences _prefs = context.getSharedPreferences("APP_PREF",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor _editor = _prefs.edit();
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

		ObjectOutputStream objectOutput;
		try {
			objectOutput = new ObjectOutputStream(arrayOutputStream);
			objectOutput.writeObject(user);
			byte[] data = arrayOutputStream.toByteArray();
			objectOutput.close();
			arrayOutputStream.close();

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Base64OutputStream b64 = new Base64OutputStream(out, Base64.DEFAULT);
			b64.write(data);
			b64.close();
			out.close();

			_editor.putString("Selected_User", new String(out.toByteArray()));

			_editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
