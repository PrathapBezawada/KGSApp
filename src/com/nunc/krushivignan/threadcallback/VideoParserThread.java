package com.nunc.krushivignan.threadcallback;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.res.AssetManager;

import com.nunc.krushivignan.core.VideoCore;

public class VideoParserThread extends Thread {
	private ArrayList<VideoCore> list;
	private Context context;

	public VideoParserThread(Context context, ArrayList<VideoCore> list) {
		this.list = list;
		this.context = context;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			AssetManager assetManager = context.getAssets();
			InputStream tinstr = assetManager.open("videos_xml.xml");
			VideoResponceParser vehicleXMLHandler = new VideoResponceParser(
					list);
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