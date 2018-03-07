package com.enablix.commons.exception;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.util.json.JsonUtil;

@ControllerAdvice
@RestController  
public class GlobalExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(value = Throwable.class)  
    public ResponseEntity<String> handleException(Throwable e) throws Throwable {
		
		LOGGER.error("Error : ", e);
		
		if (e instanceof BaseAppException) {
			
			BaseAppException be = (BaseAppException) e;
			
			String errorJson = null;
			
			if (be instanceof ErrorCollection) {
				
				ErrorCollection ec = (ErrorCollection) be;
				errorJson = JsonUtil.toJsonString(new ErrorCollectionWrapper(ec.getError(), ec.getErrors()));
				
			} else {
				errorJson = JsonUtil.toJsonString(be.getError());
			}
			
			return ResponseEntity.status(be.getHttpErrorCode() >= 400 ? 
							be.getHttpErrorCode() : HttpStatus.INTERNAL_SERVER_ERROR.value())
					.body(errorJson);
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(JsonUtil.toJsonString(new AppError("unknown", e.getMessage())));
	}
	
	public static final class ErrorCollectionWrapper {
		
		private AppError error;
		private Collection<AppError> errors;
		
		public ErrorCollectionWrapper(AppError error, Collection<AppError> errors) {
			super();
			this.error = error;
			this.errors = errors;
		}

		public AppError getError() {
			return error;
		}
		
		public void setError(AppError error) {
			this.error = error;
		}
		
		public Collection<AppError> getErrors() {
			return errors;
		}
		
		public void setErrors(Collection<AppError> errors) {
			this.errors = errors;
		}
		
	}
	
	
}
