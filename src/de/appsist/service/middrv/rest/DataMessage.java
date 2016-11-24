/**
 * Entity class represention a message containing machine data
 * 
 * @author Marian
 */
package de.appsist.service.middrv.rest;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.util.Iterator;

import de.appsist.service.middrv.rest.server.SchemaTimePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import de.appsist.service.middrv.entity.Machine;
import de.appsist.service.middrv.entity.MachineData;
import de.appsist.service.middrv.entity.MachineSchema;
import de.appsist.service.middrv.entity.Serializeable;

public class DataMessage extends Message<MachineData> implements Serializeable {
	private List<MachineSchema> schemas;
	
	/**
	 * Create a new DataMessage with the current time as timestemp
	 */
	public DataMessage(){
		super("data_message", "machines", new LinkedList<MachineData>());
		schemas = new LinkedList<>();
	}
	
	/**
	 * Create a new DataMessage
	 * @param time Time of the message in number of milli seconds since 1. 1. 1970 0:00:00 UTC
	 */
	public DataMessage(long time){
		super("data_message", "machines", new LinkedList<MachineData>(), time);
		schemas = new LinkedList<>();
	}
	
	public List<MachineData> getData(){
		return getContent();
	}
	
	public List<MachineSchema> getSchemas(){
		return schemas;
	}
	
	/**
	 * Add a MachineData instance to this message
	 * @param data MachineData to add
	 * @param schema MachineSchema the given data belongs to
	 * @throws DataSchemaMismatchException If the given data is not valid to the given schema 
	 */
	public void addMachineData(MachineData data, MachineSchema schema) throws DataSchemaMismatchException{
		schema.checkValid(data);
		addToMessage(data);
		this.schemas.add(schema);
	}
	
	/**
	 * returns <code>true</code>, if all of these conditions apply:
	 * - o is a Data Message
	 * - o has the same time stamp
	 * - o contains exactly the same machine data list (same order, same number of elements, same content)
	 */
	@Override
	public boolean equals(Object o){
		if (!(o instanceof DataMessage))
			return false;
		
		if (!(super.equals(o)))
			return false;
		
		DataMessage msg = (DataMessage) o;
		
		if (msg.getTime() != getTime())
			return false;
		
		if (msg.getSchemas().size() != schemas.size())
			return false;
		
		// Check if content is equal *and* in same *order*
		Iterator<MachineSchema> i1 = schemas.iterator();
		Iterator<MachineSchema> i2 = msg.getSchemas().iterator();
		while(i1.hasNext()){
			if (!i1.next().equals(i2.next()))
				return false;
		}
		
		return true;
	}
	
	/**
	 * @param schemas Map containing schemas that will be used to look up the schema of the given machine
	 * @param xml The XML representation to create a machine data message from
	 * @return A new DataMessage instance created from the given XML representation
	 * @throws JSONException The given XML representation is not a valid DataMessage representation
	 * @throws SchemaNotFoundException The given schema map does not contain a schema for the machine
	 *         represented by the given XML
	 * @throws DataSchemaMismatchException A schema for the machine in the XML was found, but the data
	 *         in the XML does not match this schema
	 */
	public static DataMessage createFromXml(Map<Machine, SchemaTimePair> schemas, String xml)
	                          throws JSONException, SchemaNotFoundException, DataSchemaMismatchException
	{
		return createFromXml(schemas, XML.toJSONObject(xml));
	}
	
	/**
	 * @param schemas Map containing schemas that will be used to look up the schema of the given machine
	 * @param xml The XML representation to create a machine data message from
	 * @return A new DataMessage instance created from the given XML representation
	 * @throws JSONException The given XML representation is not a valid DataMessage representation
	 * @throws SchemaNotFoundException The given schema map does not contain a schema for the machine
	 *         represented by the given XML
	 * @throws DataSchemaMismatchException A schema for the machine in the XML was found, but the data
	 *         in the XML does not match this schema
	 * 
	 * Please be ware that a JSONObject created from an XML representation differs in structure from a JSONObject
	 * created from a JSON representation.
	 */
	public static DataMessage createFromXml(Map<Machine, SchemaTimePair> schemas, JSONObject xml)
	                          throws JSONException, SchemaNotFoundException, DataSchemaMismatchException
	{
		DataMessage result;
		if (xml.has("data_message"))
			xml = xml.getJSONObject("data_message");
		
		long time = xml.getBigInteger("time").longValue();
		result = new DataMessage(time);
		
		xml = xml.getJSONObject("machines");
		JSONArray machines = xml.optJSONArray("machine");
		
		if (machines == null){
			// Only one MachineData instance in message
			MachineData data = MachineData.createFromXml(xml.getJSONObject("machine"));
			SchemaTimePair schema = schemas.get(data.getMachine());
			
			if (schema == null)
				throw new SchemaNotFoundException("Schema of machine \"" + data.getMachine().toString() + "\" not found.");
			
			result.addMachineData(data, schema.getSchema());
		} else {
			// Multiple MachineData instances in this message
			int machinesLength = machines.length();
			for (int i = 0; i < machinesLength; i++){
				MachineData data = MachineData.createFromXml(machines.getJSONObject(i));
				SchemaTimePair schema = schemas.get(data.getMachine());
				
				if (schema == null)
					throw new SchemaNotFoundException("Schema of machine \"" + data.getMachine().toString() + "\" not found.");
				
				result.addMachineData(data, schema.getSchema());
			}
		}
		
	
		return result;
	}
	
	/**
	 * @param schemas Map containing schemas that will be used to look up the schema of the given machine
	 * @param json The JSON representation to create a machine data message from
	 * @return A new DataMessage instance created from the given XML representation
	 * @throws JSONException The given JSON representation is not a valid DataMessage representation
	 * @throws SchemaNotFoundException The given schema map does not contain a schema for the machine
	 *         represented by the given JSON
	 * @throws DataSchemaMismatchException A schema for the machine in the JSON was found, but the data
	 *         in the JSON does not match this schema
	 * 
	 * Please be ware that a JSONObject created from an XML representation differs in structure from a JSONObject
	 * created from a JSON representation.
	 */
	public static DataMessage createFromJson(Map<Machine, SchemaTimePair> schemas, String json)
	                          throws JSONException, SchemaNotFoundException, DataSchemaMismatchException
	{
		return createFromJson(schemas, new JSONObject(json));
	}
	
	/**
	 * @param schemas Map containing schemas that will be used to look up the schema of the given machine
	 * @param json The JSON representation to create a machine data message from
	 * @return A new DataMessage instance created from the given XML representation
	 * @throws JSONException The given JSON representation is not a valid DataMessage representation
	 * @throws SchemaNotFoundException The given schema map does not contain a schema for the machine
	 *         represented by the given JSON
	 * @throws DataSchemaMismatchException A schema for the machine in the JSON was found, but the data
	 *         in the JSON does not match this schema
	 * 
	 * Please be ware that a JSONObject created from an XML representation differs in structure from a JSONObject
	 * created from a JSON representation.
	 */
	public static DataMessage createFromJson(Map<Machine, SchemaTimePair> schemas, JSONObject json)
	                          throws JSONException, SchemaNotFoundException, DataSchemaMismatchException
	{
		long time = json.getBigInteger("time").longValue();
		DataMessage result = new DataMessage(time);
		JSONArray machines = json.getJSONArray("machines");
		int len = machines.length();
		for (int i = 0; i < len; i++){
			MachineData data = MachineData.createFromJson(machines.getJSONObject(i));
			SchemaTimePair schema = schemas.get(data.getMachine());
			
			/*System.out.println("Available machines for schema:" + data.getMachine());
			for (Machine ma:schemas.keySet()){
				System.out.println(ma);
			}
			System.out.println("-------------");*/
			if (schema == null)
				throw new SchemaNotFoundException("Schema of machine \"" + data.getMachine().toString() + "\" not found.");
			
			result.addMachineData(data, schema.getSchema());
		}
		
		return result;
	}
}
