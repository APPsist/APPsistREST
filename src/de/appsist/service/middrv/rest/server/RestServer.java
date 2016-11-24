/**
 * A reference implementation of the APPsist REST interface on top of vert.x
 * 
 * This implementation supports:
 * - Receiving machine data
 * - Receiving error messages
 * - Receiving schema
 */

package de.appsist.service.middrv.rest.server;

import java.util.concurrent.ConcurrentHashMap;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServer;

import de.appsist.service.middrv.entity.Machine;
import de.appsist.service.middrv.rest.Constants;
import de.appsist.service.middrv.rest.ContentType;
import de.appsist.service.middrv.rest.ServerInformation;

import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

public class RestServer {
	private static final String name = "APPsist REST Server based on vert.x";
	private static final String version = "0.2";
	
	private Vertx vertx;
	private Logger logger = LoggerFactory.getLogger(RestServer.class);
	private WebRequestHandler requestHandler;

	/**
	 * Create a new RestServer instance
	 * @param dataMessageHandler Handler to call when new a data message comes in
	 * @param schemaHandler Handler to call when a schema message comes in
	 */
	public RestServer(Vertx vertx, DataMessageHandler dataMessageHandler, SchemaHandler schemaHandler, HeartBeatHandler heartBeatHandler, String basePath, long heartBeatInterval)
	{
		this(vertx, dataMessageHandler, schemaHandler, heartBeatHandler, new ConcurrentHashMap<Machine,
				SchemaTimePair>(), basePath, heartBeatInterval);
	}
	
	
	/**
	 * Construct a APPsist REST service
	 * @param dataMessageHandler Handler to call when a data message was received
	 * @param schemaHandler Handler to call when a SchemaMessage was received
	 * @param schemas Map to look up schemas of machines from. This map will be passed to the schema
	 *                handler, when a new schema message is received, so the handler can extend it
	 *                with new entries
	 */
	private RestServer(Vertx vertx, DataMessageHandler dataMessageHandler,
					  SchemaHandler schemaHandler, HeartBeatHandler heartBeatHandler,
					  ConcurrentHashMap<Machine, SchemaTimePair> schemas, String basePath, long heartBeatInterval)
	{
		this.vertx = vertx;

		ServerInformation info = new ServerInformation(name + " " + version, heartBeatInterval);
		info.addContentType(ContentType.JSON);
		info.addContentType(ContentType.XML);

		// ReqestHandler
		requestHandler = new WebRequestHandler();
		// Handler for data messages
		requestHandler.putParser("POST", basePath + Constants.RES_MSG_DATA, 
		                         new DataMessageParser(schemas, dataMessageHandler));
		// Handler for schemas
		requestHandler.putParser("POST", basePath + Constants.RES_MSG_SCHEMA, 
		                         new SchemaParser(schemas, schemaHandler));
		// Handler for ServerInformation
		requestHandler.putParser("GET", basePath + Constants.RES_SERVER_INFO, new ServerInfoParser(info));
		// Handler for heart beat signal of clients
		requestHandler.putParser("GET", basePath + Constants.RES_CLIENT_HEART_BEAT, new HeartBeatParser(heartBeatInterval, schemas, heartBeatHandler));
		
		logger.info("Registered REST handler with basepath = " + basePath);
	}
	
	/**
	 * Start listening at the default port on the Host specified in the constructor.
	 */
	public void listenHttp(){
		listenHttp(Constants.HTTP_DEFAULT_PORT);
	}
	
	/**
	 * Start to listen at the specified IP/Host and port using the HTTP transport
	 * @param port Port to start to listen at
	 */
	public void listenHttp(final int port){
		HttpServer server;
		server = vertx.createHttpServer();
		server.requestHandler(requestHandler).listen(port, new Handler<AsyncResult<HttpServer>>(){
				public void handle(AsyncResult<HttpServer> result){
					if (result.succeeded())
						logger.info("APPsist REST Server: HTTP-Transport started on \"" + "localhost"
						                   + "\" port " + port);
					else
						logger.error("Failed to start HTTP-Transport: " + result.cause());
				}
			});
	}
}