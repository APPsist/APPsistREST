/**
 * An example implementation of the DataMessageHandler and SchemaHandler Interface
 */
package example;

import de.appsist.service.middrv.rest.server.HeartBeatHandler;
import org.vertx.java.core.http.HttpServerResponse;

import de.appsist.service.middrv.entity.Machine;
import de.appsist.service.middrv.entity.Serializer;
import de.appsist.service.middrv.entity.Status;
import de.appsist.service.middrv.rest.ContentType;
import de.appsist.service.middrv.rest.DataMessage;
import de.appsist.service.middrv.rest.SchemaMessage;
import de.appsist.service.middrv.rest.server.DataMessageHandler;
import de.appsist.service.middrv.rest.server.SchemaHandler;

import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

public class ExampleHandler implements DataMessageHandler, SchemaHandler, HeartBeatHandler {
	private Logger logger = LoggerFactory.getLogger(ExampleHandler.class);

	public ExampleHandler() {
	}

	@Override
	public void handleDataMessage(DataMessage msg, HttpServerResponse response,
								  ContentType responseContentType)
	{
		logger.debug("======================================================================\n"
		                 + "===                         Got DataMessage                        ===\n"
		                 + "======================================================================");

		logger.debug("Sender: <NOT AUTHENTICATED>");
		
		logger.debug(msg);
		response.setStatusCode(201);
		try {
			response.putHeader("Content-Type", responseContentType.toString());
			response.end(Serializer.serializeToBuffer(new Status(0, "Data message successfully parsed"), responseContentType));
		} catch (Exception e) {
			e.printStackTrace();
			response.putHeader("Content-Type", ContentType.TEXT_PLAIN.toString());
			response.end("Failed to generate response: " + e.getMessage());
		}
	}

	@Override
	public void handleLostMachine(Machine machine) {
		logger.warn("Lost connection to machine " + machine.getMachineID());
	}

	@Override
	public void handleNewSchema(SchemaMessage msg,
	                            HttpServerResponse response, ContentType responseContentType)
	{
		logger.debug("======================================================================\n"
		                 + "===                       Got SchemaMessage                        ===\n"
		                 + "======================================================================");

		logger.debug("Sender: <NOT AUTHENTICATED>");
		
		logger.debug(msg.toString());
		
		response.setStatusCode(201);
		
		try {
			response.putHeader("Content-Type", responseContentType.toString());
			response.end(Serializer.serializeToBuffer(new Status(0, "Schema message successfully parsed"), responseContentType));
		} catch (Exception e) {
			e.printStackTrace();
			response.putHeader("Content-Type", ContentType.TEXT_PLAIN.toString());
			response.end("Failed to generate response: " + e.getMessage());
		}
	}
}
