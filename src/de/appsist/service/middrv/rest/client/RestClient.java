/**
 * An reference implementation of a client supporting the APPsist REST interface.
 * 
 * You might notice that the event driven approach of vert.x is not really convenient
 * for clients. You might want to consider a blocking approach for the client side
 * implementation 
 */

package de.appsist.service.middrv.rest.client;

import java.util.Map;
import java.util.Map.Entry;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientResponse;

import de.appsist.service.middrv.entity.Serializeable;
import de.appsist.service.middrv.entity.Serializer;
import de.appsist.service.middrv.rest.Constants;
import de.appsist.service.middrv.rest.ContentType;
import de.appsist.service.middrv.rest.DataMessage;
import de.appsist.service.middrv.rest.SchemaMessage;

import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

public class RestClient {

	private HttpClient httpClient;
	private ResponseHandler statusPrinter;
	private String cookies;
	private String basePath;
	private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

	
	/**
	 * Create a new client for the APPsist REST interface
	 * @param host Host to connect to
	 */
	public RestClient(String host, int port, String basePath){
		cookies = "";
		this.basePath=basePath;
		Vertx vertx = VertxFactory.newVertx();
		
		httpClient = vertx.createHttpClient();
		httpClient.setHost(host);
		httpClient.setPort(port);
		
		statusPrinter = new RHStatusPrinter();
	}
	
	public RestClient(String host, String basePath){
		this(host, Constants.HTTP_DEFAULT_PORT, basePath);
	}

    public void get(String destPath, ContentType acceptType, Map<String, String> params) {
        get(destPath, acceptType, params, null);
    }
	
	/**
	 * Send a GET request with payload to the server
	 * @param destPath Path to GET from
	 * @param acceptType Content type the response is wished to be in
	 * @param params URL-parameter to add to the request (may be null) 
	 * @param responseHandler Handler to call when response is received
	 */
	public void get(String destPath, ContentType acceptType, Map<String, String> params, final ResponseHandler responseHandler) {
		String paramString = "";
		
		if (params != null){
			boolean isFirst = true;
			
			for (Entry<String, String> param : params.entrySet()){
				if (isFirst){
					isFirst = false;
					paramString += "?";
				} else {
					paramString += "&";
				}
				
				paramString += param.getKey() + "=" + param.getValue();
			}
		}

        String url = destPath + paramString;
        if (url.contains(" "))
            throw new IllegalArgumentException("Cannot make a GET request containing a space! " + url);
        logger.debug("GET request to : " + url);

		try {
			httpClient.get(url, new Handler<HttpClientResponse>(){
				public void handle(HttpClientResponse response) {
					try{
						final ContentType responseContentType = ContentType.byString(response.headers().get("Content-Type"));
						final int statusCode = response.statusCode();
						final String statusMsg = response.statusMessage();
						response.bodyHandler(new Handler<Buffer>(){
							public void handle(Buffer buffer){
								byte[] content = buffer.getBytes();
                                if (responseHandler != null)
								    responseHandler.handleResponse(responseContentType, content, statusCode, statusMsg);
							}
						});
					} catch(Exception e){
						logger.error("Got response with content Type \"" + response.headers().get("Content-Type") + "\", which is not understood.");
						logger.error("header:\n");
						for (Entry<String,String> entry: response.headers()){
							logger.error(entry.getKey() + " = " + entry.getValue());
						}
					}
				}
			}).putHeader("Accept", acceptType.toString()).putHeader("Cookie", cookies).end();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send a Serializeable in the desired encoding to the given destination path as POST-Request
	 * @param destPath Path to send the data to
	 * @param contentType Desired encoding to send the data in
	 * @param sendMe Data to send
	 */
	private void post(String destPath, ContentType contentType, Serializeable sendMe, final ResponseHandler responseHandler){
		
		try {
			httpClient.post(destPath, new Handler<HttpClientResponse>(){
				public void handle(HttpClientResponse response){
					try{
						final ContentType responseContentType = ContentType.byString(response.headers().get("Content-Type"));
						final int statusCode = response.statusCode();
						final String statusMsg = response.statusMessage();
						
						// Trivial Cookie handling:
						String setCookie = response.headers().get("Set-Cookie");
						if (setCookie != null)
							cookies += ((cookies.length() == 0) ? "" : ";") + setCookie;
						
						// Handle response
						response.bodyHandler(new Handler<Buffer>(){
							public void handle(Buffer buffer){
								byte[] content = buffer.getBytes();
								responseHandler.handleResponse(responseContentType, content, statusCode, statusMsg);
							}
						});
						} catch(Exception e){
							logger.error("Got response with content Type \"" + response.headers().get("Content-Type") + "\", which is not understood.");
							logger.error("header:\n");
							for (Entry<String,String> entry: response.headers()){
								logger.error(entry.getKey() + " = " + entry.getValue());
							}
						}
					}
				}).putHeader("Content-Type", contentType.toString())
				  .putHeader("Cookie", cookies)
				  .end(Serializer.serializeToBuffer(sendMe, contentType));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send a MachineSchema instance to the server in the desired encoding with a custom basebath
	 */
	public void send(ContentType contentType, SchemaMessage msg){
		post(basePath + Constants.RES_MSG_SCHEMA, contentType, msg, statusPrinter);
	}
	
	/**
	 * Send a DataMessage instance to the server in the desired encoding with a custom basePath
	 * @param contentType Content type to encode message in
	 * @param msg Message to send
	 */
	public void send(ContentType contentType, DataMessage msg){
		send(contentType, msg, true);
	}
	
	/**
	 * Send a DataMessage instance to the server in the desired encoding
	 * @param contentType Content type to encode message in
	 * @param msg Message to send
	 * @param retry Send Schema and resend Message on failure
	 */
	public void send(ContentType contentType, DataMessage msg, boolean retry){
		if (retry)
			post(basePath + Constants.RES_MSG_DATA, contentType, msg, new RHSchemaResender(this, msg));
		else
			post(basePath + Constants.RES_MSG_DATA, contentType, msg, statusPrinter);
	}
}
