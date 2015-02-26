package de.tum.pssif.xmi.impl;

import de.tum.pssif.xmi.XmiDocumentLoader;

public class XmiDocumentLoaderFactory {

	public static XmiDocumentLoaderFactory INSTANCE = new XmiDocumentLoaderFactory();

	private XmiDocumentLoaderFactory() {
		// NOP
	}

	public XmiDocumentLoader create() {
		return new XmiDocumentLoaderImpl();
	}
}