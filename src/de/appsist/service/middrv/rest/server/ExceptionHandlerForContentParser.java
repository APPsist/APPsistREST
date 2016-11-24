/**
 * This Class handles Exceptions that can occur when parsing content. Because of
 * this separation of Parsing and Exception handling the code for exception handling
 * can be shared between all content parsers
 */
package de.appsist.service.middrv.rest.server;

import org.json.JSONException;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;

import de.appsist.service.middrv.entity.Serializer;
import de.appsist.service.middrv.entity.Status;
import de.appsist.service.middrv.rest.Constants;
import de.appsist.service.middrv.rest.ContentType;
import de.appsist.service.middrv.rest.DataSchemaMismatchException;
import de.appsist.service.middrv.rest.SchemaNotFoundException;

import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

class ExceptionHandlerForContentParser implements Handler<Buffer>{
	private ContentParser parser;
	private HttpServerResponse response;
	private HttpServerRequest request;
	private Logger logger = LoggerFactory.getLogger(ExceptionHandlerForContentParser.class);
	
	public ExceptionHandlerForContentParser(HttpServerRequest request, HttpServerResponse response, ContentParser parser){
		this.request = request;
		this.response = response;
		this.parser = parser;
	}
	
	private void putMessage(HttpServerResponse dest, String message){
		putMessage(dest, message, 400);
	}
	
	private void putMessage(HttpServerResponse dest, String message, int httpCode){
		dest.setStatusCode(httpCode);
		dest.setStatusMessage(message);
		dest.putHeader("Content-Type", ContentType.TEXT_PLAIN.toString());
		dest.end(message);
	}
	
	@Override
	public void handle(Buffer body)	{
		byte[] content = body.getBytes();
		ContentType contentType;
		ContentType responseContentType;

		if (!parser.allowEmptyContent() && ((content == null) || (content.length == 0))){
			putMessage(response, "Request contained no content, but content was expected");
			logger.error("Request contained no content, but content was expected");
            return;
		}
		
		try{
			contentType = ContentType.byString(request.headers().get("Content-Type"));
		} catch (Exception e){
			if (parser.allowEmptyContent()){
				contentType = null;
			} else {
				putMessage(response, "Request contained no (valid) content type. " +
				                     "Content type was \"" + request.headers().get("Content-Type") + "\"");
               logger.error("Request contained no (valid) content type. " +
                        "Content type was \"" + request.headers().get("Content-Type") + "\"");
                return;
			}
		}
			
		try {
			responseContentType = ContentType.byString(request.headers().get("Accept").split(",")[0]);
		} catch (Exception e){
			responseContentType = contentType;
		}
		
		if ((responseContentType == null) && !parser.allowResponseContentTypeEmpty()){
			putMessage(response, "No content type for response specified (HTTP-Header: Accept)");
            logger.error("No content type for response specified (HTTP-Header: Accept)");
            return;
		}

        try{
            if (responseContentType != null)
                response.putHeader("Content-Type", responseContentType.toString());

            parser.parseContent(content, contentType, response, responseContentType, request);

        } catch(JSONException | IllegalArgumentException e){
            response.setStatusCode(400);
            Status error = new Status(Constants.STATUS_ERR_INVALID_MSG,
                                                  "Could not parse message: \"" + e.getMessage() + "\"");
            response.end(Serializer.serializeToBuffer(error, responseContentType));
            logger.error("Received an invalid message!");
            logger.error("Invalid message is:" + content.toString());
        } catch (SchemaNotFoundException e){
            response.setStatusCode(400);
            Status error = new Status(Constants.STATUS_ERR_SCHEMA_NEEDED, e.getMessage());
            response.end(Serializer.serializeToBuffer(error, responseContentType));
            logger.debug("Received data without a schema --> requested it from client!");
        } catch (DataSchemaMismatchException e){
            response.setStatusCode(400);
            Status error = new Status(Constants.STATUS_ERR_DATA_DOES_NOT_MATCH_SCHEMA, e.getMessage());
            response.end(Serializer.serializeToBuffer(error, responseContentType));
            logger.error("Data did not match known schema!");
        } catch (Exception e){
            response.setStatusCode(500);
            Status error = new Status(Constants.STATUS_ERR_INTERNAL_SERVER,
                                                  "Error: \"" + e.toString() + "\"");
            response.end(Serializer.serializeToBuffer(error, responseContentType));
            logger.error("Internal Server Error:");
            e.printStackTrace();
        }
	}
}