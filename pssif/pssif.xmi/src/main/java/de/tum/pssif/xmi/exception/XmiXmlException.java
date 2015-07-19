package de.tum.pssif.xmi.exception;

public class XmiXmlException extends XmiException {

	private static final long serialVersionUID = 3672618870081202384L;

	public XmiXmlException(String msg) {
	    super(msg);
	  }

	  public XmiXmlException(String msg, Throwable rootCause) {
	    super(msg, rootCause);
	  }

	  public XmiXmlException(Throwable rootCause) {
	    super(rootCause);
	  }
}
