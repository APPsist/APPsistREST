/**
 * Exception thrown when a schema of a message containing machine data could not be found
 */
package de.appsist.service.middrv.rest;

public class SchemaNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;

	public SchemaNotFoundException(String msg){
		super(msg);
	}
}
