package com.nunc.krushivignan.threadcallback;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.res.AssetManager;

import com.nunc.krushivignan.core.KGSCore;

public class FieldVisitParserThread extends Thread {
	private ArrayList<KGSCore> list;
	private Context context;

	public FieldVisitParserThread(Context context, ArrayList<KGSCore> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			AssetManager assetManager = context.getAssets();
			InputStream tinstr = assetManager.open("karnataka_taluk.xml");
			FieldVisitResponseParser vehicleXMLHandler = new FieldVisitResponseParser(list);
			xr.setContentHandler(vehicleXMLHandler);
			xr.parse(new InputSource(tinstr));

			onCompleter();
		} catch (Exception e) {
			onError(e + "");
		}
	}

	public void onCompleter() {

	}

	public void onError(String msg) {

	}
}
