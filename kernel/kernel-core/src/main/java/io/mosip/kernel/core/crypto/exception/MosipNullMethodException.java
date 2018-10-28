/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
package io.mosip.kernel.core.crypto.exception;

import io.mosip.kernel.core.exception.BaseUncheckedException;

/**
 * {@link Exception} to be thrown when key is null
 * 
 * @author Urvil Joshi
 * @since 1.0.0
 */
public class MosipNullMethodException extends BaseUncheckedException {

	/**
	 * Unique id for serialization
	 */
	private static final long serialVersionUID = -7652091398077351394L;

	/**
	 * Constructor with errorCode and errorMessage
	 * @param errorCode
	 * @param errorMessage
	 */

	public MosipNullMethodException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

}