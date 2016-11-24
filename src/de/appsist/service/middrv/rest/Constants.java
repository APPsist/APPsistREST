/**
 * Configuration of the REST interface
 * 
 * (Changing these values will most likely result in an incompatible version of the REST interface!)
 * 
 * @author Marian
 */
package de.appsist.service.middrv.rest;

public class Constants {
	
	/**
	 * Default port of the legacy HTTP transport.
	 */
	public static final int HTTP_DEFAULT_PORT = 8095;
	
	/**
	 * Path to POST machine data messages to.
	 * 
	 * Unsafe to change!
	 */
	public static final String RES_MSG_DATA = "/machine/data";
	
	/**
	 * Path to POST machine schemas to.
	 * 
	 * Unsafe to change!
	 */
	public static final String RES_MSG_SCHEMA = "/machine/schema";
	
	/**
	 * Path to GET server information from
	 * 
	 * Unsafe to change!
	 */
	public static final String RES_SERVER_INFO = "/server/info";

	/**
	 * Path to notify server that client is still alive via GET
	 *
	 * Unsafe to change!
	 */
	public static final String RES_CLIENT_HEART_BEAT = "/client_heart_beat";

	// machineId for get parameters
	public static final String GET_PARAM_MACHINE_ID = "machineId";
	
	/**
	 * Status code: Error: Received message invalid
	 */
	public static final int STATUS_ERR_INVALID_MSG = -1;
	
	/**
	 * Status code: Error: No schema for this machine type is known. Please send schema first
	 */
	public static final int STATUS_ERR_SCHEMA_NEEDED = -4;
	
	/**
	 * Status code: Error: The received data does not match the registered schema. If schema has changed,
	 * re-registration of the schema is required
	 */
	public static final int STATUS_ERR_DATA_DOES_NOT_MATCH_SCHEMA = -5;
	
	/**
	 * Status code: Error: Something went wrong on server side
	 */
	public static final int STATUS_ERR_INTERNAL_SERVER = -6;
}
