package de.appsist.service.middrv.rest;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import de.appsist.service.middrv.entity.MachineSchema;
import de.appsist.service.middrv.entity.Serializeable;

public class SchemaMessage extends Message<MachineSchema> implements Serializeable {
	/**
	 * Create a new SchemaMessage instance
	 */
	public SchemaMessage(){
		super("schema_message", "schemas", new LinkedList<MachineSchema>());
	}
	
	/**
	 * Create a new SchemaMessage instance
	 * @param time Time of the message in number of milli seconds since 1. 1. 1970 0:00:00 UTC
	 */
	public SchemaMessage(long time){
		super("schema_message", "schemas", new LinkedList<MachineSchema>(), time);
	}
	
	/**
	 * Create a new SchemaMessage instance
	 * @param schemas MachineSchema instances the new SchemaMessage shall contain
	 */
	public SchemaMessage(List<MachineSchema> schemas){
		super("schema_message", "schemas", schemas);
	}
	
	/**
	 * @return The list of MachineSchema instances this message contains
	 */
	public List<MachineSchema> getSchemas(){
		return getContent();
	}
	
	/**
	 * Add a schema to this message
	 * @param schema The MachineSchema instance to add
	 */
	public void addSchema(MachineSchema schema){
		this.addToMessage(schema);
	}
	
	/**
	 * Create a SchemaMessage instance from its XML representation
	 * @param xml XML representation of the SchemaMessage to create
	 * @return The new SchemaMessage created from its XML representation
	 * @throws JSONException If the given XML does not contain a valid SchemaMessage
	 */
	public static SchemaMessage createFromXml(String xml) throws JSONException{
		return createFromXml(XML.toJSONObject(xml));
	}
	
	/**
	 * Create a SchemaMessage instance from its XML representation
	 * @param xml XML representation of the SchemaMessage to create
	 * @return The new SchemaMessage created from its XML representation
	 * @throws JSONException If the given XML does not contain a valid SchemaMessage
	 */
	public static SchemaMessage createFromXml(JSONObject xml) throws JSONException{
		if (xml.has("schema_message"))
			xml = xml.getJSONObject("schema_message");
		
		SchemaMessage result = new SchemaMessage(xml.getLong("time"));
		
		xml = xml.getJSONObject("schemas");
		
		JSONArray schemas = xml.optJSONArray("machine");
		
		if (schemas == null){
			// Only one Schema in message
			result.addSchema(MachineSchema.createFromXml(xml.getJSONObject("machine")));
		} else {
			int schemasLength = schemas.length();
			for (int i = 0; i < schemasLength; i++){
				result.addSchema(MachineSchema.createFromXml(schemas.getJSONObject(i)));
			}
		}
		
		return result;
	}
	
	/**
	 * Create a SchemaMessage instance from its JSON representation
	 * @param json JSON representation of the SchemaMessage to create
	 * @return The new SchemaMessage created from its JSON representation
	 * @throws JSONException If the given JSON does not contain a valid SchemaMessage
	 */
	public static SchemaMessage createFromJson(String json) throws JSONException{
		return createFromJson(new JSONObject(json));
	}
	
	/**
	 * Create a SchemaMessage instance from its JSON representation
	 * @param json JSON representation of the SchemaMessage to create
	 * @return The new SchemaMessage created from its JSON representation
	 * @throws JSONException If the given JSON does not contain a valid SchemaMessage
	 */
	public static SchemaMessage createFromJson(JSONObject json) throws JSONException{
		SchemaMessage result = new SchemaMessage(json.getLong("time"));
		
		JSONArray schemas = json.getJSONArray("schemas");
		
		int schemasLength = schemas.length();
		for (int i = 0; i < schemasLength; i++){
			result.addSchema(MachineSchema.createFromJson(schemas.getJSONObject(i)));
		}
		
		return result;
	}
}
