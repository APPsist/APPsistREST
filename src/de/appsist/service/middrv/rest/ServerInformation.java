/**
 * Entity Class representing Server information
 * 
 * @author Marian
 */

package de.appsist.service.middrv.rest;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import de.appsist.service.middrv.entity.Serializeable;

public class ServerInformation implements Serializeable {
	private Set<ContentType> contentTypes;
	private String serverName;
	private long heartBeatInterval;
	
	public ServerInformation(String serverName, long heartBeatInterval){
		this(serverName, new HashSet<ContentType>(), heartBeatInterval);
	}
	
	/**
	 * Create a new ServerInformation instance
	 * @param serverName Name of the Server (some human readable information, like Name of the 
	 *                   APPsist REST Server implementation and its version)
	 */
	public ServerInformation(String serverName, Set<ContentType> contentTypes, long heartBeatInterval){
		this.serverName = serverName;
		this.contentTypes = contentTypes;
		this.heartBeatInterval = heartBeatInterval;
	}
	
	/**
	 * Add a content type to the set of supported content types
	 * @param contentType Content type to add
	 */
	public void addContentType(ContentType contentType){
		contentTypes.add(contentType);
	}
	
	/**
	 * Check if the given content type is understood by the server
	 */
	public boolean isSupported(ContentType contentType){
		return contentTypes.contains(contentType);
	}

	/**
	 * @return A human readable String representation of this ServerInformation instance
	 */
	@Override
	public String toString() {
		String result = "Server Information: Name = \"" + serverName + "\"" +
				"\nHeartBeatInterval: " + heartBeatInterval +
				"\nSupported content types: ";
		
		boolean isFirst = true;
		for (ContentType c : contentTypes){
			if (isFirst)
				isFirst = false;
			else
				result += ", ";
			
			result += c.toString();
		}
		
		return result;
	}
	
	/**
	 * @return A JSON representation of this ServerInformation instance
	 */
	@Override
	public String toJson() {
		String result = "{\"name\":" + JSONObject.quote(serverName) + ", " +
				"\"hearBeatInterval\":" + heartBeatInterval + ", " +
				"\"content_types\":[";
		
		boolean isFirst = true;
		for (ContentType c : contentTypes){
			if (isFirst)
				isFirst = false;
			else
				result += ", ";
			
			result += JSONObject.quote(c.toString());
		}
		
		result += "]}";
		return result;
	}
	
	/**
	 * @return A XML representation of this ServerInformation instance
	 */
	@Override
	public String toXml() {
		return toXml(true);
	}
	
	/**
	 * @return A XML representation of this ServerInformation instance
	 */
	@Override
	public String toXml(boolean withOuterTags) {
		String result = "";
		
		if (withOuterTags)
			result += "<server_info>";
		
		result +="<name><![CDATA[" + serverName + "]]></name>" +
				"<hearBeatInterval>" + heartBeatInterval + "</hearBeatInterval>" +
				"<content_types>";
		
		for (ContentType c : contentTypes){
			result += "<entry><![CDATA[" + c.toString() + "]]></entry>";
		}
		
		result += "</content_types>";
		
		if (withOuterTags)
			result += "</server_info>";
		
		return result;
	}
	
	/**
	 * Create a ServerInformation instance from its XML representation
	 * @param xml XML representation of the ServerInformation instance to create
	 * @throws JSONException The given XML does not contain valid server information
	 * @return The new ServerInformation instance created from the given XML representation
	 */
	public static ServerInformation createFromXml(String xml) throws JSONException {
		return createFromXml(XML.toJSONObject(xml));
	}
	
	/**
	 * Create a ServerInformation instance from its XML representation
	 * @param xml XML representation of the ServerInformation instance to create
	 * @throws JSONException The given XML does not contain valid server information
	 * @return The new ServerInformation instance created from the given XML representation
	 */
	public static ServerInformation createFromXml(JSONObject xml) throws JSONException {
		// Unwrap if needed
		if (xml.has("server_info"))
			xml = xml.getJSONObject("server_info");
		
		ServerInformation result = new ServerInformation(xml.getString("name"), xml.getInt("heartBeatInterval"));
		JSONObject contentTypes = xml.getJSONObject("content_types");
		JSONArray entries = contentTypes.optJSONArray("entry");
		
		if (entries == null){
			result.addContentType(ContentType.byString(contentTypes.getString("enry")));
		} else {
			int entriesLength = entries.length();
			for (int i = 0; i < entriesLength; i++){
				result.addContentType(ContentType.byString(entries.getString(i)));
			}
		}
		
		return result;
	}
	
	/**
	 * Create a ServerInformation instance from its JSON representation
	 * @param json JSON representation of the ServerInformation instance to create
	 * @throws JSONException The given JSON does not contain valid server information
	 * @return The new ServerInformation instance created from the given JSON representation
	 */
	public static ServerInformation createFromJson(String json) throws JSONException {
		return createFromJson(new JSONObject(json));
	}
	
	/**
	 * Create a ServerInformation instance from its JSON representation
	 * @param json JSON representation of the ServerInformation instance to create
	 * @throws JSONException The given JSON does not contain valid server information
	 * @return The new ServerInformation instance created from the given JSON representation
	 */
	public static ServerInformation createFromJson(JSONObject json) throws JSONException {
		ServerInformation result = new ServerInformation(json.getString("name"), json.getInt("heartBeatInterval"));
		
		JSONArray contentTypes = json.getJSONArray("content_types");
		int contentTypesLength = contentTypes.length();
		
		if (contentTypesLength == 0)
			throw new JSONException("ServerInformation must at least contain one content type");
		
		for (int i = 0; i < contentTypesLength; i++){
			result.addContentType(ContentType.byString(contentTypes.getString(i)));
		}
		
		return result;
	}
}
