package com.example.krushivignan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.demo.imageapp.imagezoom.ImageViewTouch;
import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.db.DBHelper;

public class SoilMapActivity extends Activity {

	private ImageViewTouch oldImageView;

	public static ViewFlipper imageContainerLayout;
	private Animation inLeftAnimation;
	private Animation outRightAnimation;
	private Animation outLeftAnimation;
	private Animation inRightAnimation;
	private DBHelper db;
	private TextView screen_title;
	private SharedPreferences _prefs;
	private String language_str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.soilmap_layout);
		initAnimations();
		db = ((KrushiVignanApp) getApplication()).getDatabase();
		_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AppInfo.photoDetailsList = db.getSoilMaps(AppInfo.CurrentUser
				.getDistrict());
		try {
			getComponent();
		} catch (Exception e) {

		}
		language_str = _prefs.getString("SelectedLanguage", "");
		setLanguage(language_str);
	}

	private void getComponent() {
		screen_title = (TextView) findViewById(R.id.screen_title);

		imageContainerLayout = (ViewFlipper) findViewById(R.id.imageContainer);

		oldImageView = new ImageViewTouch(this, null);
		Bitmap bitmap = BitmapFactory
				.decodeFile(AppInfo.photoDetailsList
						.get(AppInfo.position));
		oldImageView.setImageBitmap(AppInfo.getResizedBitmap(bitmap));
		imageContainerLayout.addView(oldImageView);

		AppInfo.oldImageView = oldImageView;

		((ImageButton) findViewById(R.id.nav_next))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						moveNext();
					}
				});
		((ImageButton) findViewById(R.id.nav_prev))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						movePrevious();
					}
				});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppInfo.photoDetailsList.clear();
		System.gc();
	}
	private void initAnimations() {

		// set up animations
		inLeftAnimation = (Animation) AnimationUtils.loadAnimation(this,
				R.anim.flip_in_left);
		outRightAnimation = (Animation) AnimationUtils.loadAnimation(this,
				R.anim.flip_out_right);
		inRightAnimation = (Animation) AnimationUtils.loadAnimation(this,
				R.anim.flip_in_right);
		outLeftAnimation = (Animation) AnimationUtils.loadAnimation(this,
				R.anim.flip_out_left);
	}

	private void moveNext() {
		if (AppInfo.position + 1 < AppInfo.photoDetailsList
				.size()) {

			AppInfo.position = AppInfo.position + 1;
			//
			Bitmap bitmap = BitmapFactory
					.decodeFile(AppInfo.photoDetailsList
							.get(AppInfo.position));

			ImageViewTouch _newImageView = new ImageViewTouch(
					SoilMapActivity.this, null);
			_newImageView.setImageBitmap(AppInfo.getResizedBitmap(bitmap));
			System.gc();

			SoilMapActivity.imageContainerLayout
					.setOutAnimation(outLeftAnimation);
			SoilMapActivity.imageContainerLayout
					.setInAnimation(inRightAnimation);

			SoilMapActivity.imageContainerLayout.addView(_newImageView);
			SoilMapActivity.imageContainerLayout.showNext();

			if (AppInfo.oldImageView != null) {
				SoilMapActivity.imageContainerLayout
						.removeView(AppInfo.oldImageView);
			}
			AppInfo.oldImageView = _newImageView;
		}
	}

	private void movePrevious() {

		if (AppInfo.position > 0) {

			AppInfo.position = AppInfo.position - 1;

		
			Bitmap bitmap = BitmapFactory
					.decodeFile(AppInfo.photoDetailsList
							.get(AppInfo.position));
			ImageViewTouch _newImageView = new ImageViewTouch(this, null);
			
			_newImageView.setImageBitmap(AppInfo.getResizedBitmap(bitmap));
			System.gc();

			SoilMapActivity.imageContainerLayout
					.setOutAnimation(outRightAnimation);
			SoilMapActivity.imageContainerLayout
					.setInAnimation(inLeftAnimation);

			SoilMapActivity.imageContainerLayout.addView(_newImageView);
			SoilMapActivity.imageContainerLayout.showPrevious();

			if (AppInfo.oldImageView != null)
				SoilMapActivity.imageContainerLayout
						.removeView(AppInfo.oldImageView);
			AppInfo.oldImageView = _newImageView;
		}
	}

	private void setLanguage(String language) {
		if (language.startsWith("English")) {
			String Title = getResources().getString(
					R.string.tab_item_package_of_soilmaps_text_english)
					+ "- " + AppInfo.CurrentUser.getDistrict();
			screen_title.setText(Title);

		} else if (language.startsWith("Kanada")) {
			String Title = getResources().getString(
					R.string.tab_item_package_of_soilmaps_text_kanada)
					+ "- " + AppInfo.CurrentUser.getDistrict();
			screen_title.setText(Title);
		}

	}
}
