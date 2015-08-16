package de.tum.pssif.xmi;

import java.io.InputStream;

import de.tum.pssif.xmi.exception.XmiException;

public interface XmiDocumentLoader {

	XmiGraph loadDocument(InputStream inputStream) throws XmiException;
	
}
