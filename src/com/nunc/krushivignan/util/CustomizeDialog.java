package com.nunc.krushivignan.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.example.krushivignan.R;

/** Class Must extends with Dialog */
/** Implement onClickListener to dismiss dialog when OK Button is pressed */
public class CustomizeDialog extends Dialog implements OnClickListener {

	@Override
	public void onWindowAttributesChanged(LayoutParams params) {
		super.onWindowAttributesChanged(params);

		// params.height = 500;

		Log.d("cmd", "onWindowAttributesChanged Height= " + params.height);
	}

	Button okButton;
	Button remind;
	Button noThanks;
	Context mContext;

	TextView mTitle = null;
	TextView mMessage = null;

	View v = null;
	private SharedPreferences _prefs;
	private SharedPreferences.Editor _editor;
	public CustomizeDialog(Context context) {
		super(context);
		mContext = context;
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.dialog);
		v = getWindow().getDecorView();
		v.setBackgroundResource(android.R.color.transparent);

		mTitle = (TextView) findViewById(R.id.dialogTitle);
		mMessage = (TextView) findViewById(R.id.dialogMessage);
		okButton = (Button) findViewById(R.id.registerButton);
		remind = (Button) findViewById(R.id.remindButton);
		noThanks = (Button) findViewById(R.id.noThanksButton);
		okButton.setOnClickListener(this);
		remind.setOnClickListener(this);
		noThanks.setOnClickListener(this);
		_prefs = mContext.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
		_editor = _prefs.edit();

	}

	@Override
	public void onClick(View v) {
		/** When OK Button is clicked, dismiss the dialog */
		if (v == okButton) {
			dismiss();
			installKanadaKeypad(mContext);
		}
		if (v == remind) {
			dismiss();
		}
		if (v == noThanks) {
			dismiss();
			_editor.putBoolean("DialogStatus", true);
			_editor.commit();
		}
	}

	public void installKanadaKeypad(Context context) {
		final String appName = "com.paninikeypad.kannada";
		try {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("market://details?id=" + appName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("http://play.google.com/store/apps/details?id="
							+ appName)));
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		mTitle.setText(title);
	}

	@Override
	public void setTitle(int titleId) {
		super.setTitle(titleId);

		mTitle.setText(mContext.getResources().getString(titleId));

	}

	/**
	 * Set the message text for this dialog's window.
	 * 
	 * @param message
	 *            - The new message to display in the title.
	 */
	public void setMessage(CharSequence message) {
		mMessage.setText(message);

		mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	/**
	 * Set the message text for this dialog's window. The text is retrieved from
	 * the resources with the supplied identifier.
	 * 
	 * @param messageId
	 *            - the message's text resource identifier <br>
	 * @see <b>Note : if resourceID wrong application may get crash.</b><br>
	 *      Exception has not handle.
	 */
	public void setMessage(int messageId) {
		mMessage.setText(mContext.getResources().getString(messageId));
		mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

}
