package com.xafero.vermilingua.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xafero.vermilingua.api.AbstractParser;
import com.xafero.vermilingua.api.IParser;
import com.xafero.vermilingua.util.MyUtils;

public class XmlParser extends AbstractParser implements IParser {

	private final String text;

	public XmlParser(String text) {
		this.text = text;
	}

	@Override
	public String parseWithError(String name, InputStream input) throws Exception {
		JsonArray array = new JsonArray();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader xml = factory.createXMLStreamReader(new HackInputStream(input));
		while (xml.hasNext()) {
			int tag = xml.next();
			switch (tag) {
			case XMLStreamConstants.START_DOCUMENT:
				break;
			case XMLStreamConstants.START_ELEMENT:
				String xName = xml.getLocalName() + "";
				if (MyUtils.contains(xName + "", text))
					array.add(xName);
				for (int i = 0; i < xml.getAttributeCount(); i++) {
					QName aname = xml.getAttributeName(i);
					String aval = xml.getAttributeValue(i);
					if (MyUtils.contains(aname + "", text) || MyUtils.contains(aval, text)) {
						JsonObject obj = new JsonObject();
						obj.addProperty(aname + "", aval);
						array.add(obj);
					}
				}
				break;
			case XMLStreamConstants.COMMENT:
			case XMLStreamConstants.CHARACTERS:
				String txt = xml.getText().trim();
				if (!txt.isEmpty() && MyUtils.contains(txt, text))
					array.add(txt);
				break;
			case XMLStreamConstants.END_ELEMENT:
				break;
			case XMLStreamConstants.END_DOCUMENT:
				break;
			default:
				throw new UnsupportedOperationException(tag + "!");
			}
		}
		return array.size() < 1 ? null : array.toString();
	}

	public static class HackInputStream extends FilterInputStream {

		protected HackInputStream(InputStream in) {
			super(in);
		}

		@Override
		public void close() throws IOException {
			// NO-OP!
		}
	}
}