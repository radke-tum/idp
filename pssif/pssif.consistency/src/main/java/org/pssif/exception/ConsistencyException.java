package org.pssif.exception;

public class ConsistencyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1220406763001425332L;

	public ConsistencyException(String message){
		super(message);
	}
	
	public ConsistencyException(String message, Throwable reason){
		super(message, reason);
	}
	
}
