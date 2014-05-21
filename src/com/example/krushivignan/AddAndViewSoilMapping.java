package com.example.krushivignan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AddAndViewSoilMapping extends Activity {
	private Button save, cancel,send_email;
	private TextView label1, label2, label3, label4, label5, label6, label7,
			label8;
	private SharedPreferences _prefs;
	private String language_str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addandviewsoilmapping);
		_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
		language_str = _prefs.getString("SelectedLanguage", "");
		getComponentValues();
		setLanguage(language_str);
	}

	private void setLanguage(String language) {
		if (language.startsWith("English")) {
			label1.setText(R.string.soil_mapping_label1_english);
			label2.setText(R.string.soil_mapping_label2_english);
			label3.setText(R.string.soil_mapping_label3_english);
			label4.setText(R.string.soil_mapping_label4_english);
			label5.setText(R.string.soil_mapping_label5_english);
			label6.setText(R.string.soil_mapping_label6_english);
			label7.setText(R.string.soil_mapping_label7_english);
			label8.setText(R.string.soil_mapping_label8_english);
			save.setText(R.string.save_text_english);
			cancel.setText(R.string.cancel_text_english);
			send_email.setText(R.string.soil_mapping_btn_recommendation_english);

		} else if (language.startsWith("Kanada")) {
			save.setText(R.string.save_text_kanada);
			cancel.setText(R.string.cancel_text_kanada);
			label1.setText(R.string.soil_mapping_label1_kanada);
			label2.setText(R.string.soil_mapping_label2_kanada);
			label3.setText(R.string.soil_mapping_label3_kanada);
			label4.setText(R.string.soil_mapping_label4_kanada);
			label5.setText(R.string.soil_mapping_label5_kanada);
			label6.setText(R.string.soil_mapping_label6_kanada);
			label7.setText(R.string.soil_mapping_label7_kanada);
			label8.setText(R.string.soil_mapping_label8_kanada);
			send_email.setText(R.string.soil_mapping_btn_recommendation_kanada);
		}

	}

	private void getComponentValues() {
		send_email=(Button)findViewById(R.id.sendMailRecommendation);
		save = (Button) findViewById(R.id.save);
		cancel = (Button) findViewById(R.id.cancel);
		label1 = (TextView) findViewById(R.id.label1);
		label2 = (TextView) findViewById(R.id.label2);
		label3 = (TextView) findViewById(R.id.label3);
		label4 = (TextView) findViewById(R.id.label4);
		label5 = (TextView) findViewById(R.id.label5);
		label6 = (TextView) findViewById(R.id.label6);
		label7 = (TextView) findViewById(R.id.label7);
		label8 = (TextView) findViewById(R.id.label8);

	}
}
