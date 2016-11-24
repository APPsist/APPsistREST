/**
 * ResponseHandler that dumps a Status message to the terminal
 */
package de.appsist.service.middrv.rest.client;

import java.io.IOException;

import javax.xml.transform.TransformerConfigurationException;

import org.json.JSONException;
import org.openexi.proc.common.EXIOptionsException;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import org.xml.sax.SAXException;

import de.appsist.service.middrv.entity.Status;
import de.appsist.service.middrv.rest.ContentType;

public class RHStatusPrinter implements ResponseHandler {
	private static final Logger logger = LoggerFactory.getLogger(RHStatusPrinter.class);

	public RHStatusPrinter() {
	}
	/**
	 * Generate a ErrorMessage instance from a JSON/XML representation of an ErrorMessage
	 * @param contentType Encoding of the ErrorMessage representation (JSON/XML)
	 * @param content The ErrorMessage representation to convert
	 * @return The new ErrorMessage instance create from the given representation
	 * @throws JSONException <code>content</code> does not contain a valid ErrorMessage representation
	 */
	public static Status decodeStatusMessage(ContentType contentType, byte[] content) 
	                            throws JSONException, TransformerConfigurationException,
	                            IOException, SAXException, EXIOptionsException
	{
		switch(contentType){
		case JSON:
			return Status.createFromJson(new String(content));
		case XML:
			return Status.createFromXml(new String(content));
		default:
			throw new IllegalArgumentException("Unhandled content type \"" + contentType.toString() + "\"");
		}
	}
	
	@Override
	public void handleResponse(ContentType contentType, byte[] content, int statusCode, String statusMessage) {
		try {
			Status status = decodeStatusMessage(contentType, content);
			logger.debug("Server returned " + status.toString() +  " for previously sent schema!");
		} catch (Exception e){
			logger.error("When proccessing content: \n" + new String(content) + "\nGot error:\n");
			e.printStackTrace();
		}
	}

}
