package io.mosip.registration.processor.scanner.virusscanner.exception;

import io.mosip.kernel.core.exception.BaseUncheckedException;
import io.mosip.registration.processor.core.exception.util.PlatformErrorMessages;



public class RetryFolderNotAccessibleException extends BaseUncheckedException {

	private static final long serialVersionUID = 1L;

	public RetryFolderNotAccessibleException(String errorMessage) {
		super(PlatformErrorMessages.RPR_PSJ_RETRY_FOLDER_NOT_ACCESSIBLE.getCode(), errorMessage);
	}

	public RetryFolderNotAccessibleException(String message, Throwable cause) {
		super(PlatformErrorMessages.RPR_PSJ_RETRY_FOLDER_NOT_ACCESSIBLE.getCode()+ EMPTY_SPACE, message, cause);
	}

}
