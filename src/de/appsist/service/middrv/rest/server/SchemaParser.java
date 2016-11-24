package de.appsist.service.middrv.rest.server;

import java.util.concurrent.ConcurrentHashMap;

import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;

import de.appsist.service.middrv.entity.Machine;
import de.appsist.service.middrv.entity.MachineSchema;
import de.appsist.service.middrv.rest.ContentType;
import de.appsist.service.middrv.rest.SchemaMessage;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

class SchemaParser implements ContentParser {
	private SchemaHandler handler;
	private ConcurrentHashMap<Machine, SchemaTimePair> schemas;
	private static final Logger logger = LoggerFactory.getLogger(SchemaParser.class);

	
	public SchemaParser(ConcurrentHashMap<Machine, SchemaTimePair> schemas, SchemaHandler handler){
		this.handler = handler;
		this.schemas = schemas;
	}
	
	@Override
	public void parseContent(byte[] content, ContentType contentType, HttpServerResponse response, 
	                         ContentType responseContentType, HttpServerRequest request)
	     throws Exception
	{
		SchemaMessage msg;

		switch(contentType){
		case JSON:
			msg = SchemaMessage.createFromJson(new String(content));
			break;
		case XML:
			msg = SchemaMessage.createFromXml(new String(content));
			break;
		default:
			throw new IllegalArgumentException("Parser for this content type is not implemented");
		}

		logger.debug("Got new schema entries: " + msg.getSchemas().size());
		for (MachineSchema schema : msg.getSchemas()){
			logger.debug("Schema: " + schema);
			schemas.put(schema.getMachine(), new SchemaTimePair(schema));
		}
		handler.handleNewSchema(msg, response, responseContentType);
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
