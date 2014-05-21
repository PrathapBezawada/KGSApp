package com.example.krushivignan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nunc.krushivignan.core.Mapdata;

public class DisplayFarmOnMapActivity extends Activity {
	private GoogleMap map;
	// private Field field;
	private LatLng location;

	private TextView label0;
	private Button cancel;
	private SharedPreferences _prefs;
	private String language_str;

	private Mapdata mapData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		// try{
		mapData = (Mapdata) getIntent().getSerializableExtra("MapData");
		if (!(mapData.getLatitude() == null || mapData.getLongitude() == null
				|| mapData.getLatitude().equalsIgnoreCase("null")
				|| mapData.getLatitude().equalsIgnoreCase("null")
				|| mapData.getLatitude().length() == 0 || mapData
				.getLongitude().length() == 0)) {
			location = new LatLng(Double.parseDouble(mapData.getLatitude()),
					Double.parseDouble(mapData.getLongitude()));
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			if (map != null) {
				Marker marker = map.addMarker(new MarkerOptions()
						.position(location).title(mapData.getTitle())
						.snippet(mapData.getDescription()));
				// Move the camera instantly to hamburg with a zoom of 15.
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

				// Zoom in, animating the camera.
				map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			}
		} else {
			Toast.makeText(getParent(), "Location Not Available",
					Toast.LENGTH_SHORT).show();
		}
		_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
		language_str = _prefs.getString("SelectedLanguage", "");
		getComponentValues();
		setLanguage(language_str);
		// // TODO: handle exception
	}

	public void cancel(View v) {
		finish();
	}

	private void setLanguage(String language) {
		if (language.startsWith("English")) {

			label0.setText(R.string.farm_on_googlemap_label0_english);
			cancel.setText(R.string.cancel_text_english);

		} else if (language.startsWith("Kanada")) {

			cancel.setText(R.string.cancel_text_kanada);
			label0.setText(R.string.farm_on_googlemap_label0_kanada);
		}

	}

	private void getComponentValues() {
		label0 = (TextView) findViewById(R.id.label0);
		cancel = (Button) findViewById(R.id.cancel);
	}
}
