/**
 * An interface used for the callback when a DataMessage was received
 */
package de.appsist.service.middrv.rest.server;

import org.vertx.java.core.http.HttpServerResponse;

import de.appsist.service.middrv.rest.ContentType;
import de.appsist.service.middrv.rest.DataMessage;

public interface DataMessageHandler {
	void handleDataMessage(DataMessage msg, HttpServerResponse response,
	                              ContentType responseContentType);
}
