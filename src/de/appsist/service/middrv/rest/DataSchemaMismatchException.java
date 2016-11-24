/**
 * An exception that is thrown when a machine data representation does not match its schema
 */
package de.appsist.service.middrv.rest;

public class DataSchemaMismatchException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public DataSchemaMismatchException(String description){
		super(description);
	}
}
