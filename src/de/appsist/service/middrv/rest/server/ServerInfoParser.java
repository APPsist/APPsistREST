package de.appsist.service.middrv.rest.server;

import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;

import de.appsist.service.middrv.entity.Serializer;
import de.appsist.service.middrv.rest.ContentType;
import de.appsist.service.middrv.rest.ServerInformation;

public class ServerInfoParser implements ContentParser {
	private ServerInformation info;
	
	public ServerInfoParser(ServerInformation info){
		this.info = info;
	}
	
	
	@Override
	public void parseContent(byte[] content, ContentType contentType, HttpServerResponse response,
	                         ContentType responseContentType, HttpServerRequest request)
	     throws Exception 
	{
		response.setStatusCode(200);
		response.end(Serializer.serializeToBuffer(this.info, responseContentType));

	}


	@Override
	public boolean allowEmptyContent() {
		return true;
	}


	@Override
	public boolean allowResponseContentTypeEmpty() {
		return false;
	}

}
