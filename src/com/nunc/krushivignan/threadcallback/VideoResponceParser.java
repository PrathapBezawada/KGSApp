package com.nunc.krushivignan.threadcallback;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.nunc.krushivignan.core.VideoCore;

public class VideoResponceParser extends DefaultHandler {
	private String value = "";
	private VideoCore videoCore;
	private ArrayList<VideoCore> list;
	private String currentElement;

	public VideoResponceParser(ArrayList<VideoCore> list) {
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

		if (currentElement.equalsIgnoreCase("title")) {
			videoCore.setTitle(value);
		} else if (currentElement.equalsIgnoreCase("lenght")) {
			videoCore.setLenght(value);
		} else if (currentElement.equalsIgnoreCase("thumbnail")) {
			videoCore.setThumbnail(value);
		} else if (currentElement.equalsIgnoreCase("videourl")) {
			videoCore.setVideourl(value);
		}
		if (qName.equalsIgnoreCase("video")) {
			list.add(videoCore);
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
		if (qName.equalsIgnoreCase("video")) {
			videoCore = new VideoCore();

		}
	}

}
