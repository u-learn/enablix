package com.enablix.uri.embed;

import com.enablix.commons.exception.AppError;
import com.enablix.commons.exception.BaseAppException;
import com.enablix.commons.exception.ErrorCodes;

public class EmbedException extends BaseAppException {

	private static final long serialVersionUID = 1L;

	public EmbedException(String message) {
		super(new AppError(ErrorCodes.OEMBED_PROVIDER_ERROR, message));
	}
	
	public EmbedException(String message, int httpErrorCode) {
		super(new AppError(ErrorCodes.OEMBED_PROVIDER_ERROR, message), httpErrorCode);
	}
	
	public EmbedException(String message, int httpErrorCode, Throwable t) {
		super(new AppError(ErrorCodes.OEMBED_PROVIDER_ERROR, message), httpErrorCode, t);
	}
}
