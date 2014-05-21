package com.nunc.krushivignan.threadcallback;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.Context;

import com.nunc.krushivignan.services.SyncParser;

public class SyncParserThread {
	private String content;
	private Context context;

	public SyncParserThread(String content,Context context) {
		// TODO Auto-generated constructor stub
		this.content = content;
		this.context=context;
	}

	public void start() {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp;
			sp = spf.newSAXParser();
			InputStream is = new ByteArrayInputStream(content.getBytes());

			SyncParser parser = new SyncParser(context);
			sp.parse(is, parser);
		} catch (Exception e) {

		}
	}
}
