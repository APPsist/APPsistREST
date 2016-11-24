/**
 * If the server signals missing schema, the missing schema will be send and the message will be resent
 */

package de.appsist.service.middrv.rest.client;

import de.appsist.service.middrv.entity.Status;
import de.appsist.service.middrv.rest.Constants;
import de.appsist.service.middrv.rest.ContentType;
import de.appsist.service.middrv.rest.DataMessage;
import de.appsist.service.middrv.rest.SchemaMessage;

import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

public class RHSchemaResender implements ResponseHandler {
	private RestClient restClient;
	private DataMessage msg;
	private Logger logger = LoggerFactory.getLogger(RHSchemaResender.class);
	
	/**
	 * @param restClient Client used to (re-)send Schema and DataMessage
	 * @param msg Message to resend, if Server signals missing Schema
	 */
	public RHSchemaResender(RestClient restClient, DataMessage msg){
		this.restClient = restClient;
		this.msg = msg;
	}
	
	@Override
	public void handleResponse(ContentType contentType, byte[] content, int statusCode, String statusMessage) {
		try {
			Status status = RHStatusPrinter.decodeStatusMessage(contentType, content);
			if (status.getCode() == Constants.STATUS_ERR_SCHEMA_NEEDED){
				logger.debug("Server signaled missing schema ==> Sending Schema and resending message");
				restClient.send(contentType, new SchemaMessage(msg.getSchemas()));
				logger.debug("(Re)sent Schema: " + msg.getSchemas());
				Thread.sleep(500);
				restClient.send(contentType, msg, false);
				logger.debug("Resent Message: " + msg);
			} else {
				logger.debug("Server returned " + status.toString() + " for previously sent message!");
			}
		} catch (Exception e) {
			logger.error("Failed to decode Server-Response: " + e.getMessage());
		}		
	}

}
