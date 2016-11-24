/**
 * An interface for request content parsers
 */

package de.appsist.service.middrv.rest.server;

import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;

import de.appsist.service.middrv.rest.ContentType;

interface ContentParser {
	void parseContent(byte[] content, ContentType contentType, HttpServerResponse response,
	                         ContentType responseContentType, HttpServerRequest request)
	     throws Exception;
	
	/**
	 * @return If <code>false</code>, messages with empty content will not be passed to ContentParser
	 *         but automatically rejected with an appropriate error code
	 */
	boolean allowEmptyContent();
	
	/**
	 * @return If <code>false</code>, messages with no Accept HTTP-Header will not be passed
	 *         to ContentParser but automatically rejected with an appropriate error code. If
	 *         the Accept HTTP-Header is missing, but the Content-Type HTTP-Header was set,
	 *         this content type is also assumed for the response
	 */
	boolean allowResponseContentTypeEmpty();
}
