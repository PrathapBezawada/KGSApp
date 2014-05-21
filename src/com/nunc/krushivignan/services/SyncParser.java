package com.nunc.krushivignan.services;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.content.Context;

import com.nunc.krushivignan.app.KrushiVignanApp;
import com.nunc.krushivignan.core.AppInfo;
import com.nunc.krushivignan.db.DBHelper;

public class SyncParser extends DefaultHandler {
	private Context context;
	private DBHelper db;

	public SyncParser(Context context) {
		this.context = context;
		try {
			db = ((KrushiVignanApp) ((Activity) context).getApplication())
					.getDatabase();
		} catch (Exception e) {

		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		if (qName.equalsIgnoreCase("Farmer")) {
			String oldId = attributes.getValue("oldid");
			String newId = attributes.getValue("newid");
			String syncStatus = attributes.getValue("status");
			if (syncStatus.equalsIgnoreCase("True")) {
				db.updateFarmerPostSync(oldId, newId);

			}
		} else if (qName.equalsIgnoreCase("Field")) {
			String oldId = attributes.getValue("oldid");
			String newId = attributes.getValue("newid");
			String farmerId = attributes.getValue("Farmer_ID");
			String syncStatus = attributes.getValue("status");
			if (syncStatus.equalsIgnoreCase("True")) {
				db.updateFieldPostSync(farmerId, oldId, newId);
				db.updateCropCuttingPostSync(farmerId, oldId, newId);
//				db.updateFieldVisitPostSync(farmerId, oldId, newId);
			}
		} else if (qName.equalsIgnoreCase("CCE")) {
			String oldId = attributes.getValue("oldid");
			String newId = attributes.getValue("newid");
			String syncStatus = attributes.getValue("status");
			if (syncStatus.equalsIgnoreCase("True")) {
				db.updateCropCuttingSyncStatus(oldId, newId);
			}
		}
		else if (qName.equalsIgnoreCase("TrainingRecord")) {
			String oldId = attributes.getValue("oldid");
			String newId = attributes.getValue("newid");
			String syncStatus = attributes.getValue("status");
			if (syncStatus.equalsIgnoreCase("True")) {
				db.updateTrainingReportSyncStatus(oldId, newId);
			}
		}
		 else if (qName.equalsIgnoreCase("Fieldvisitor")){
		 String syncStatus=attributes.getValue("status");
		 if(syncStatus.equalsIgnoreCase("True")){
		 db.updateFieldVisitSyncStatus((AppInfo.CurrentUser.getUserName()));
		 }
		
		 }
	}

}
