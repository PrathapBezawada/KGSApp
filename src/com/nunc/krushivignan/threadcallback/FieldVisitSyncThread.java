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
import com.nunc.krushivignan.core.FieldVisit;
import com.nunc.krushivignan.core.ServerURL;
import com.nunc.krushivignan.core.User;
import com.nunc.krushivignan.db.DBHelper;

public class FieldVisitSyncThread {
	private static final int sync = 1;
	private DBHelper db;
	private ProgressDialog _dialog;
	private Context context;

	public FieldVisitSyncThread(Context context) {
		this.context = context;
		try {
			db = ((KrushiVignanApp) ((Activity) context).getApplication())
					.getDatabase();

		} catch (Exception e) {

		}
	}

	public void startSync() {
		// TODO Auto-generated method stub
		_dialog = ProgressDialog.show(context, "", "Uploading Field Visit...");

		subscribe(ServerURL.getFieldVisitSyncURL());
		Log.e("the sending data to server is", ServerURL.getSyncURL());
	}

	private String writeXml() {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "Fieldvisits");
			// ArrayList<FieldVisit> fieldVisitDate = db
			// .getFarmers("select * from Farmer where USERID=\'"
			// + AppInfo.CurrentUser.getUserId()
			// + "\' and Sync_Status=0");
			//

			ArrayList<FieldVisit> fieldVisitData = db
					.getFieldVisit("select * from FieldVisit where Sync_Status=0");

			for (FieldVisit fieldVisit : fieldVisitData) {

				if (fieldVisit.getVisitorImage() != null
						&& fieldVisit.getVisitorImage().length() > 0
						&& !fieldVisit.getVisitorImage().equalsIgnoreCase(
								"null")) {
					ImageUploadThread thread = new ImageUploadThread(
							"103.13.242.103", "krishigy", "*KGS2012*",
							fieldVisit.getVisitorImage());
					thread.run();
				}
				serializer.startTag("", "Fieldvisitor");
				serializer.attribute("", "Id", fieldVisit.getFieldVisit_ID());
				serializer.attribute("", "UserName", fieldVisit.getUser_name());
				serializer.attribute("", "FarmerID", fieldVisit.getFarmerId());
				serializer
						.attribute("", "FarmID", fieldVisit.getFieldId() + "");
				serializer.attribute("", "Title", "");
				serializer.attribute("", "State", fieldVisit.getState());
				serializer.attribute("", "District", fieldVisit.getDistrict());
				serializer.attribute("", "Taluk", fieldVisit.getTaluk());
				serializer.attribute("", "Hobli", fieldVisit.getHobli());

				serializer.attribute("", "Village", fieldVisit.getVillage());
				serializer.attribute("", "Purpose", fieldVisit.getPurpose());
				serializer.attribute("", "Observation",
						fieldVisit.getObservation());
				serializer.attribute("", "Recommendation",
						fieldVisit.getRecommendation());
				serializer.attribute("", "Image",
						getlastToken(fieldVisit.getVisitorImage()));
				serializer.attribute("", "Date", fieldVisit.getDate());
				serializer.attribute("", "Latitude", fieldVisit.getLatitude());
				serializer
						.attribute("", "Longitude", fieldVisit.getLongitude());
				serializer.endTag("", "Fieldvisitor");

			}

			serializer.endTag("", "Fieldvisits");
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

				CropCuttingExpSyncThread thread2 = new CropCuttingExpSyncThread(
						context);
				thread2.startSync();

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
