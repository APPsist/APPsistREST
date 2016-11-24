/**
 * Entity class representing a machine data schema.
 * 
 * @author Marian
 */
package de.appsist.service.middrv.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import de.appsist.service.middrv.rest.DataSchemaMismatchException;

public class MachineSchema implements Serializeable {
	private Machine machine;
	private Map<String, MachineValueSpecification> schema;
	private String stationID;
	private String siteID;
	private String siteUUID;
	
	/**
	 * Create an empty Schema
	 * 
	 * @param machine Machine to create schema for
	 */
	public MachineSchema(Machine machine, String stationID, String siteID){
		this(machine, stationID, siteID, "", new HashMap<String, MachineValueSpecification>());
	}
	
	/**
	 * Create an empty Schema
	 * 
	 * @param machine Machine to create schema for
	 */
	public MachineSchema(Machine machine, String stationID, String siteID, String siteUUID){
		this(machine, stationID, siteID, siteUUID, new HashMap<String, MachineValueSpecification>());
	}
	
	/**
	 * Create a new Schema from a machine and a mapping: name --> type
	 * 
	 * @param machine Machine to create schema for
	 * @param schema A mapping name --> type (the actual schema)
	 */
	public MachineSchema(Machine machine, String stationID, String siteID, String siteUUID, Map<String, MachineValueSpecification> schema){
		this.machine = machine;
		this.schema = schema;
		this.stationID = stationID;
		this.siteUUID = siteUUID;
		this.siteID = siteID;
	}
	
	/**
	 * @return Machine belonging to this schema
	 */
	public Machine getMachine(){
		return machine;
	}
	
	/**
	 * @return The ID of the station this machine belongs to
	 */
	public String getStationID(){
		return stationID;
	}
	
	/**
	 * @return The ID of the site this machine belongs to
	 */
	public String getSiteID(){
		return siteID;
	}
	
	/**
	 * @return The UUID of the site this machine belongs to, can be looked up in ontology
	 */
	public String getSiteUUID(){
		return siteUUID;
	}
	
	/**
	 * @param key Key identifying the machine value
	 * @return Specification of the machine value identified by the given name
	 */
	public MachineValueSpecification getSpecification(String key){
		return schema.get(key);
	}
	
	/**
	 * @param key Key identifying a machine value specification
	 * @return Machine value type identified by the given key
	 */
	public MachineValueType getType(String key){
		return schema.get(key).getType();
	}
	
	/**
	 * @return Keys in the schema of this machine
	 */
	public Set<String> getSchemaKeys(){
		return schema.keySet();
	}
	
	/**
	 * @return Number of entrys in this schema
	 */
	public int getSchemaCount(){
		return schema.size();
	}
	
	/**
	 * Add an entry to the schema
	 */
	public void addField(MachineValueSpecification specification){
		schema.put(specification.getName(), specification);
	}
	
	/**
	 * Add an entry to the schema
	 * @param key Identifier of this entry (existing entry with same identifier will be overwritten)
	 * @param type Type of the machine value
	 * @param unit Unit of the machine value
	 * @throws IllegalArgumentException The given string representation does not belong to a valid type
	 */
	public void addField(String key, MachineValueType type, Unit unit) throws IllegalArgumentException{
		schema.put(key, new MachineValueSpecification(key, type, unit));
	}
	
	/**
	 * Add an entry to the schema
	 * @param key Identifier of this entry (existing entry with same identifier will be overwritten)
	 * @param type Type of the machine value
	 * @param unit Unit of the machine value
	 * @throws IllegalArgumentException The given string representation does not belong to a valid type
	 */
	public void addField(String key, MachineValueType type, Unit unit, VisualizationType visualizationType,
	                     VisualizationLevel visualizationLevel)
	     throws IllegalArgumentException
	{
		schema.put(key, new MachineValueSpecification(key, type, unit, visualizationType, visualizationLevel));
	}
	
	/**
	 * Checks if the given MachineData instance belongs to this schema and if its values are valid 
	 * @param data MachineData instance to check
	 * @return <code>false</code> If at least one of these conditions apply:
	 *                               a) data belongs not to the same machine as this schema
	 *                               b) data contains at least one entry not listed in this schema
	 *                               c) at least one entry in data has a different type as mentioned in the schema 
	 */
	public boolean isValid(MachineData data){
		try {
			checkValid(data);
		} catch (DataSchemaMismatchException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * A convenience function for checking if a MachineData instance matches its schema. 
	 * @param data MachineData instance to check
	 * @throws DataSchemaMismatchException if the MachineData instances does not match its schema
	 */
	public void checkValid(MachineData data) throws DataSchemaMismatchException{
		if (!data.getMachine().equals(getMachine()))
			throw new DataSchemaMismatchException("Machine of MachineData instance and MachineSchema instance differ");
		
		for (String key : data.getDataKeys()){
			if (!schema.containsKey(key))
				throw new DataSchemaMismatchException("MachineData instance contains extra key \"" + key + "\"");
			
			if (!data.getValue(key).getType().equals(getType(key)))
				throw new DataSchemaMismatchException("Value for \"" + key + "\" is of type "
				                                      + data.getValue(key).getType().identifier
				                                      + " but type " + getType(key).identifier
				                                      + " was expected");
		}
	}
	
	/**
	 * Checks if the given MachineData instance is valid *and* complete
	 */
	public boolean isValidAndComplete(MachineData data){
		if (data.getDataCount() != schema.size())
			return false;
		
		return isValid(data);
	}
	
	/**
	 * @return A XML representation of this schema
	 */
	@Override
	public String toXml() {
		return toXml(true);
	}
	
	/**
	 * @return A XML representation of this schema
	 * @param withMachineTags Encapsulate the xml in "<machine>"..."</machine>" tags
	 */
	@Override
	public String toXml(boolean withMachineTags) {
		String xml = "";
		if(withMachineTags)
			xml += "<machine>";
		
		xml +=  machine.toXml(false);
		xml += "<station_id>" + stationID + "</station_id>";
		xml += "<site_id>" + siteID + "</site_id>";
		xml += "<site_uuid>" + siteUUID + "</site_uuid>";
		
		xml += "<schema>";
		
		for (MachineValueSpecification item : schema.values()){
			xml += item.toXml();
		}
		
		xml += "</schema>";
		
		if (withMachineTags)
			xml +="</machine>";
		
		return xml;
	}
	
	/**
	 * @return A JSON representation of this schema
	 */
	@Override
	public String toJson() {
		String json = machine.toJson();
		json = json.substring(0, json.length() - "}".length());
		json += ",\"station_id\":" + JSONObject.quote(stationID) + ",\"site_id\":" + JSONObject.quote(siteID) + ",\"site_uuid\":" + JSONObject.quote(siteUUID) + ",\"schema\":[";
		
		{
			boolean isFirst = true;
			for (MachineValueSpecification item : schema.values()){
				if (isFirst)
					isFirst = false;
				else
					json += ",";
				
				json += item.toJson();
			}
		}
		
		json += "]}";
		return json;
	}
	
	/**
	 * @return A new schema created from the given JSON representation of the schema
	 * @throws JSONException The given JSON does not represent a valid Schema
	 * @throws IllegalArgumentException The given representation contains an invalid type
	 */
	public static MachineSchema createFromJson(String json) throws JSONException, IllegalArgumentException{
		return createFromJson(new JSONObject(json));
	}
	
	/**
	 * @return A new schema created from the given JSON representation of the schema
	 * @throws JSONException The given JSON does not represent a valid Schema
	 * @throws IllegalArgumentException The given representation contains an invalid type
	 */
	public static MachineSchema createFromJson(JSONObject json) throws JSONException, IllegalArgumentException{
		Machine machine = Machine.createFromJson(json);
		String stationID = json.getString("station_id");
		String siteID = json.getString("site_id");
		String siteUUID = "";
		try {
			siteUUID = json.getString("site_uuid");
		} catch (JSONException e) {
			// do nothing and use default of ""
		}
		MachineSchema result = new MachineSchema(machine, stationID, siteID, siteUUID);
		
		// Unwrap if needed
		if (json.has("machine"))
			json = json.getJSONObject("machine");
		
		JSONArray schema = json.getJSONArray("schema");
		int schemaLength = schema.length();
		
		for (int i = 0; i < schemaLength; i++){
			MachineValueSpecification specification = MachineValueSpecification.createFromJson(schema.getJSONObject(i));
			result.addField(specification);
		}
		
		return result;
	}
	
	/**
	 * @return A new schema created from the given XML representation of the schema
	 * @throws JSONException The given XML does not represent a valid Schema
	 * @throws IllegalArgumentException The given representation contains an invalid type
	 */
	public static MachineSchema createFromXml(String xml) throws JSONException, IllegalArgumentException{
		return createFromXml(XML.toJSONObject(xml));
	}
	
	/**
	 * @return A new schema created from the given XML representation of the schema
	 * @throws JSONException The given XML does not represent a valid Schema
	 * @throws IllegalArgumentException The given representation contains an invalid type
	 */
	public static MachineSchema createFromXml(JSONObject xml) throws JSONException, IllegalArgumentException{
		// Unwrap if needed
		if (xml.has("machine"))
			xml = xml.getJSONObject("machine");
		
		Machine machine = Machine.createFromXml(xml);
		String stationID = xml.getString("station_id");
		String siteID = xml.getString("site_id");
		String siteUUID = "";
		try {
			siteUUID = xml.getString("site_uuid");
		} catch (JSONException e) {
			// do nothing and use default of ""
		}
		MachineSchema result = new MachineSchema(machine, stationID, siteID, siteUUID);
		
		
		
		JSONObject schemas = xml.getJSONObject("schema");
		JSONArray data = schemas.optJSONArray("specification");
		
		if (data == null){
			// Only one entry in Schema
			result.addField(MachineValueSpecification.createFromXml(schemas.getJSONObject("specification")));
		} else {
			int dataLen = data.length();
			for (int i = 0; i < dataLen; i++){
				result.addField(MachineValueSpecification.createFromXml(data.getJSONObject(i)));
			}
		}
		
		return result;
	}
	
	/**
	 * @return  A human readable String representation of this MachineSchma
	 */
	@Override
	public String toString(){
		String result = machine.toString();
		
		result += ", station = " + stationID + ", site = " + siteID + ", siteUUID = " + siteUUID;
		
		result += "\nschema:\n";
		for (MachineValueSpecification specification : schema.values()){
			result += "\t" + specification + "\n";
		}
		
		return result;
	}
	
	/**
	 * @return A hash code of this schema
	 * 
	 * As the hash code of the Machine is passed through, this hash code is very fast and has a good
	 * distribution, as long as not multiple schemas for the same machine exist
	 */
	@Override
	public int hashCode(){
		return machine.hashCode();
	}
	
	/**
	 * @return <code>true</code> The given Object is a MachineSchema instance and equal to this one
	 *         <code>false</code> The given Object is either not a MachineSchema instance or differs from this one
	 */
	@Override
	public boolean equals(Object o){
		if (!(o instanceof MachineSchema))
			return false;
		
		MachineSchema schema = (MachineSchema) o;
		
		if (!schema.getMachine().equals(machine))
			return false;
		
		if (schema.getSchemaCount() != getSchemaCount())
			return false;
		
		for (String key : schema.getSchemaKeys()){
			if (!schema.getType(key).equals(getType(key)))
				return false;
		}
		
		return true;
	}
}
