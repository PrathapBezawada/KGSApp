package com.example.krushivignan;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.core.ServerURL;
import com.nunc.krushivignan.core.User;
import com.nunc.krushivignan.threadcallback.UserLoginParserThread;
import com.nunc.krushivignan.util.CustomizeDialog;

public class LoginActivity extends Activity {
	private Spinner language;
	private TextView user_name_label, password_label, language_label;
	private Button login, cancel;
	private EditText user_name, password;
	private SharedPreferences _prefs;
	private SharedPreferences.Editor _editor;
	private String language_str;
	final String appName = "com.paninikeypad.kannada";
	private User user = new User();
	private ProgressDialog progress;
private TextView hello;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		
		hello=(TextView)findViewById(R.id.txtHello);
		hello.setSelected(true);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
		_editor = _prefs.edit();
		HomeActivity.mQuery=false;
		user = GetPreference();
		if (user != null && user.getUserLoginStatus().equalsIgnoreCase("true")) {
			AppInfo.CurrentUser = user;
			Intent intent = new Intent(LoginActivity.this,
					KrushiVignanMainTab.class);
			startActivity(intent);
			finish();
		} else {
			language_str = _prefs.getString("SelectedLanguage", "");
			getComponentValues();
			language.setSelection(getIndex(language, language_str));
			UpdateLanguage();
			user_name = (EditText) findViewById(R.id.user_name);
			password = (EditText) findViewById(R.id.password);
			boolean dialogDisplayStatus = _prefs.getBoolean("DialogStatus",
					false);
			if (!dialogDisplayStatus) {
				if (!appInstalledOrNot(appName))
					displayDialog(); 
			}
		}
	}

	private void displayAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Error");

		// set dialog message
		alertDialogBuilder
				.setMessage("Invalid Username and password")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
						
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private User GetPreference() {
		try {
			String StrObj = _prefs.getString("Selected_User", null);
			if (StrObj != null) {
				byte[] bytes = StrObj.getBytes();
				if (bytes.length == 0) {
					return null;
				}
				ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
				Base64InputStream base64InputStream = new Base64InputStream(
						byteArray, Base64.DEFAULT);
				ObjectInputStream in;
				in = new ObjectInputStream(base64InputStream);
				User myObject = (User) in.readObject();
				return myObject;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	private void SavePreference(User user) {
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

	public void displayDialog() {
		CustomizeDialog customizeDialog = new CustomizeDialog(this);
		customizeDialog.setTitle("Kanada KeyPad App Not Found");
		customizeDialog
				.setMessage("For Kanada language, please Install the App From Play Store. And For using the Corresponding Keypad, Please Select from Settings menu.");
		customizeDialog.setCanceledOnTouchOutside(false);
		customizeDialog.show();
	}

	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	public void installKanadaKeypad() {
		final String appName = "com.paninikeypad.kannada";
		try {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=" + appName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ appName)));
		}
	}

	public void login(View v) {
		user = new User();

		String username = user_name.getText().toString().trim();
		String pass = password.getText().toString().trim();
		
		if (username.length() > 0 && pass.length() > 0) {
			if(isOnline()){
				UserAuthentication(username, pass);
}
			else{
				
				Toast.makeText(LoginActivity.this, "Please Check  Your Internet Connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "User Name & Password Can't be Blank",
					Toast.LENGTH_SHORT).show();
		}
		}
		

	

	public boolean isOnline(){
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
   

	private void UpdateLanguage() {
		language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				language_str = (String) parent.getItemAtPosition(pos);
				setLanguage(language_str);
				_editor.putString("SelectedLanguage", language_str);
				_editor.commit();
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	public void getComponentValues() {
		language = (Spinner) findViewById(R.id.language);
		//
		user_name_label = (TextView) findViewById(R.id.user_name_label);
		password_label = (TextView) findViewById(R.id.password_label);
		language_label = (TextView) findViewById(R.id.language_label);
		login = (Button) findViewById(R.id.login);
		cancel = (Button) findViewById(R.id.cancel);
	}

	private void setLanguage(String language) {
		if (language.startsWith("English")) {
			user_name_label
					.setText(R.string.loginscreen_user_name_text_english);
			password_label.setText(R.string.loginscreen_password_text_english);
			language_label.setText(R.string.loginscreen_language_text_english);
			login.setText(R.string.loginscreen_login_button_english);
			cancel.setText(R.string.loginscreen_cancel_button_english);
		} else if (language.startsWith("Kanada")) {
			user_name_label.setText(R.string.loginscreen_user_name_text_kanada);
			password_label.setText(R.string.loginscreen_password_text_kanada);
			language_label.setText(R.string.loginscreen_language_text_kanada);
			login.setText(R.string.loginscreen_login_button_kanada);
			cancel.setText(R.string.loginscreen_cancel_button_kanada);
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

	private void UserAuthentication(String UserName, String password) {
		String URL = ServerURL.getLoginURL(UserName, password);

		UserLoginParserThread thread = new UserLoginParserThread(user,
				URL) {
			@Override
			public void OnComplete() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						progress.dismiss();
						if (user!=null &&user.getUserLoginStatus()!=null &&user.getUserLoginStatus().equalsIgnoreCase("true")) {
							SavePreference(user);
							AppInfo.CurrentUser = user;
							Intent intent = new Intent(LoginActivity.this,
									KrushiVignanMainTab.class);
							startActivity(intent);
							finish();
						} else {

							displayAlert();
						}
					}
				});
			}

			@Override
			public void OnError(final String msg) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						progress.dismiss();
						Toast.makeText(LoginActivity.this, msg + "",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		};
		thread.start();
		progress = ProgressDialog.show(LoginActivity.this, "", "Loggingin...",
				false, true);
	}
}
