package com.tmcoder.inventory.exception;

public class InventoryException extends RuntimeException {

	public InventoryException() {
		super();
	}

	public InventoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InventoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public InventoryException(String message) {
		super(message);
	}

	public InventoryException(Throwable cause) {
		super(cause);
	}

}
