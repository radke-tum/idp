package de.tum.pssif.xmi.impl;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

import de.tum.pssif.xmi.XmiGraph;
import de.tum.pssif.xmi.XmiDocumentLoader;
import de.tum.pssif.xmi.XmiParser;
import de.tum.pssif.xmi.exception.XmiException;

public class XmiDocumentLoaderImpl implements XmiDocumentLoader {
	
	@Override
	public XmiGraph loadDocument(InputStream inputStream) throws XmiException {
		XmiGraph document = null;
		
		try {
			document = XmiParser.parse(inputStream);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
			throw new XmiException("Parsing File Exception");
		}
		return document;
	}
}
