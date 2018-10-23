/*
 * 
 * 
 * 
 * 
 * 
 */
package io.mosip.kernel.logger.logback.exception;

import io.mosip.kernel.core.exception.BaseUncheckedException;
import io.mosip.kernel.logger.logback.constant.LogExeptionCodeConstant;

/**
 * {@link Exception} to be thrown when a class name is not found
 * 
 * @author Urvil Joshi
 * @since 1.0.0
 */
public class ClassNameNotFoundException extends BaseUncheckedException {

	/**
	 * Unique id for serialization
	 */
	private static final long serialVersionUID = 105555532L;

	/**
	 * @param errorCode
	 *            unique exception code
	 * @param errorMessage
	 *            exception message
	 */
	public ClassNameNotFoundException(LogExeptionCodeConstant errorCode,
			LogExeptionCodeConstant errorMessage) {
		super(errorCode.getValue(), errorMessage.getValue());
	}

}
