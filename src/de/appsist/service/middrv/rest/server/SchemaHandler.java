/**
 * An interface used for the callback when a MachineSchema was received
 */

package de.appsist.service.middrv.rest.server;

import org.vertx.java.core.http.HttpServerResponse;

import de.appsist.service.middrv.rest.ContentType;
import de.appsist.service.middrv.rest.SchemaMessage;

public interface SchemaHandler {
	void  handleNewSchema(SchemaMessage msg, HttpServerResponse response, ContentType responseContentType);
}
