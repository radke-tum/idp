package de.tum.pssif.xmi.exception;

public class XmiException extends RuntimeException {

	private static final long serialVersionUID = 5082951167264277965L;

	public XmiException(String msg) {
	    super(msg);
	  }

	  public XmiException(String msg, Throwable rootCause) {
	    super(msg, rootCause);
	  }

	  public XmiException(Throwable rootCause) {
	    super(rootCause);
	  }

}
