package com.niwe.erp.common.exception;
 

public class ResourceNotFoundException extends RuntimeException {
    /**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}

