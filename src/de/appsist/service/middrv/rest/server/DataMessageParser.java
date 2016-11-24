/**
 * The Parser for DataMessages
 */

package de.appsist.service.middrv.rest.server;

import java.util.concurrent.ConcurrentHashMap;

import de.appsist.service.middrv.entity.MachineData;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;

import de.appsist.service.middrv.entity.Machine;
import de.appsist.service.middrv.rest.ContentType;
import de.appsist.service.middrv.rest.DataMessage;

class DataMessageParser implements ContentParser{
	private DataMessageHandler handler;
	private ConcurrentHashMap<Machine, SchemaTimePair> schemas;

	
	public DataMessageParser(ConcurrentHashMap<Machine, SchemaTimePair> schemas, DataMessageHandler handler){
		this.handler = handler;
		this.schemas = schemas;
	}

	@Override
	public void parseContent(byte[] content, ContentType contentType, HttpServerResponse response,
	                         ContentType responseContentType, HttpServerRequest request)
	     throws Exception 
	{
		DataMessage msg = null;

		
		switch(contentType){
		case JSON:
			msg = DataMessage.createFromJson(schemas, new String(content));
			break;
		case XML:
			msg = DataMessage.createFromXml(schemas, new String(content));
			break;
		case TEXT_PLAIN:
				break;
		default:
			throw new IllegalArgumentException("Parser for this content type is not implemented");
		}
        // update timers of machines since they did something
        for (MachineData data : msg.getContent()) {
            schemas.get(data.getMachine()).updateTimeOfLastUpdate();
        }
        // call interface callback
		if (handler!=null){
			handler.handleDataMessage(msg, response, responseContentType);
		}
	}

	@Override
	public boolean allowEmptyContent() {
		return false;
	}

	@Override
	public boolean allowResponseContentTypeEmpty() {
		return false;
	}
}
