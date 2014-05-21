package com.example.krushivignan;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import pdftron.PDF.PDFDoc;
import pdftron.PDF.PDFNet;
import pdftron.PDF.PDFViewCtrl;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nunc.krushivignan.util.ResourceUtil;

public class PestControlPackages  extends Activity implements
OnItemSelectedListener {
private PDFViewCtrl content;
private ProgressBar progress;
private Spinner spinnerCrop;
private Map<String, String> linksMap;
private TextView label1;
private SharedPreferences _prefs;
private String language_str;

@Override
protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
super.onCreate(savedInstanceState);
try {
	PDFNet.initialize(this, R.raw.pdfnet);

} catch (Exception e) {
	return;
}
setContentView(R.layout.pest_control_packages_layout);
content = (PDFViewCtrl) findViewById(R.id.pdfviewctrl);

pdftron.PDF.Tools.ToolManager tm = new pdftron.PDF.Tools.ToolManager();
content.setToolManager(tm);

content.setPagePresentationMode(PDFViewCtrl.PAGE_PRESENTATION_SINGLE);

content.setupThumbnails(true, true, false, 0, "", 0);

content.setHighlightFields(true); // Turn on form fields highlighting.

spinnerCrop = (Spinner) findViewById(R.id.spinnerCrop);
linksMap = ResourceUtil.getHashMapResource(this,
		R.xml.pest_control_packages);
Object[] set = linksMap.keySet().toArray();
String[] stringArray = Arrays.copyOf(set, set.length, String[].class);
ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, stringArray);

// Drop down layout style - list view with radio button
dataAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// attaching data adapter to spinner
spinnerCrop.setAdapter(dataAdapter);
spinnerCrop.setOnItemSelectedListener(this);
_prefs = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
language_str = _prefs.getString("SelectedLanguage", "");
getComponentValues();
setLanguage(language_str);
}

@Override
public void onItemSelected(AdapterView<?> main, View view, int position,
	long Id) {
try {
	String item = main.getItemAtPosition(position).toString();
	String filname = linksMap.get(item);
	AssetManager assetManager = getParent().getAssets();
	InputStream is = assetManager.open(filname);
	PDFDoc doc = new PDFDoc(is);
	content.setDoc(doc);

} catch (Exception e) {

}
}

//
@Override
public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

}

private void setLanguage(String language) {
if (language.startsWith("English")) {
	label1.setText(R.string.package_of_practices_label0_english);

} else if (language.startsWith("Kanada")) {
	label1.setText(R.string.package_of_practices_label0_kanada);
}

}

private void getComponentValues() {
label1 = (TextView) findViewById(R.id.label1);

}
}
