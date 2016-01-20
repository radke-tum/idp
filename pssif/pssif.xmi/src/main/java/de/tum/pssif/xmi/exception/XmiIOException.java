package de.tum.pssif.xmi.exception;

public class XmiIOException extends XmiException {

	private static final long serialVersionUID = -5384375637309001288L;

	public XmiIOException(String msg) {
	    super(msg);
	  }

	  public XmiIOException(String msg, Throwable rootCause) {
	    super(msg, rootCause);
	  }

	  public XmiIOException(Throwable rootCause) {
	    super(rootCause);
	  }

}
