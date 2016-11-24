/**
 * Interface for handling responses from the Server
 */

package de.appsist.service.middrv.rest.client;

import de.appsist.service.middrv.rest.ContentType;

public interface ResponseHandler{
	void handleResponse(ContentType contentType, byte[] content, int statusCode, String statusMessage);
}