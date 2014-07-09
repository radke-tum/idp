package org.pssif.exception;

public class MatchMethodException extends ConsistencyException {

	public MatchMethodException(String message, Throwable reason) {
		super(message, reason);
	}
	
	public MatchMethodException(String message){
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6683130474170484776L;

}
