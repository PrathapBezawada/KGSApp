package com.nunc.krushivignan.threadcallback;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.nunc.krushivignan.core.KGSCore;

public class FieldVisitResponseParser extends DefaultHandler {
	private String value = "";
	private KGSCore kGSCore;
	private ArrayList<KGSCore> list;
	private String currentElement;

	public FieldVisitResponseParser(ArrayList<KGSCore> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		value = value.concat(new String(ch, start, length).trim());
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
		if (currentElement.equalsIgnoreCase("Row")) {
			list.add(kGSCore);
		}
		value = "";
		currentElement = "";
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentElement = qName;
		// TODO Auto-generated method stub
		if (qName.equalsIgnoreCase("Row")) {
			kGSCore = new KGSCore();
			kGSCore.setRsk_id(attributes.getValue("A"));
			kGSCore.setDist_id(attributes.getValue("B"));
			kGSCore.setTaluk_id(attributes.getValue("C"));
			kGSCore.setRsk_name(attributes.getValue("D"));
			kGSCore.setDist_name(attributes.getValue("E"));
			kGSCore.setTaluk_name(attributes.getValue("F"));

		}
	}

}
