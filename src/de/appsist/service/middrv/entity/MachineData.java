/**
 * An entity class representing a machine state. It contains a machine and the
 * state vector of the machine (which consists of a set of labeled machine values)
 * 
 * @author Marian
 */

package de.appsist.service.middrv.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class MachineData implements Serializeable {
	private Map<String, MachineValue> data;
	private Machine machine;
	
	//state given from the machine controller
	private Status status;
	
	public String getRuleState() {
		return ruleState;
	}

	public void setRuleState(String ruleState) {
		this.ruleState = ruleState;
	}

	//state according to our rule system
	private String ruleState="RUNNING";
	
	public MachineData(Machine machine, Map<String, MachineValue> data, Status status){
		this.machine = machine;
		this.data = data;
		this.status = status;
	}
	
	public MachineData(Machine machine, int statusCode, String statusDescription){
		this(machine, new HashMap<String, MachineValue>(), new Status(statusCode, statusDescription));
	}
	
	public MachineData(Machine machine){
		this(machine, new HashMap<String, MachineValue>(), new Status(0, null));
	}
	
	/**
	 * Add/replace a value to the state vector of this machine
	 * @param name Unique identifier of the state variable
	 * @param value Value to add/replace
	 */
	public void put(String name, String value){
		data.put(name, MachineValueFactory.create(value));
	}
	
	/**
	 * Add/replace a value to the state vector of this machine
	 * @param name Unique identifier of the state variable
	 * @param value Value to add/replace
	 */
	public void put(String name, boolean value){
		data.put(name, MachineValueFactory.create(value));
	}
	
	public Set<Entry<String, MachineValue>> entrySet() {
		return data.entrySet();
	}
	
	/**
	 * Add/replace a value to the state vector of this machine
	 * @param name Unique identifier of the state variable
	 * @param value Value to add/replace
	 */
	public void put(String name, int value){
		data.put(name, MachineValueFactory.create(value));
	}
	
	/**
	 * Add/replace a value to the state vector of this machine
	 * @param name Unique identifier of the state variable
	 * @param value Value to add/replace
	 */
	public void put(String name, long value){
		data.put(name, MachineValueFactory.create(value));
	}
	
	/**
	 * Add/replace a value to the state vector of this machine
	 * @param name Unique identifier of the state variable
	 * @param value Value to add/replace
	 */
	public void put(String name, double value){
		data.put(name, MachineValueFactory.create(value));
	}
	
	/**
	 * Add/replace a value to the state vector of this machine
	 * @param name Unique identifier of the state variable
	 * @param value Value to add/replace
	 * 
	 * This method autodetects the type of the object (if type is valid)
	 * 
	 * @throws IllegalArgumentException The given value is of unhandled type
	 */
	public void putAutodetectType(String name, Object value){
		if (value instanceof Double)
			put(name, (Double) value);
		else if (value instanceof Integer)
			put(name, (Integer) value);
		else if (value instanceof Long)
			put(name, (Long) value);
		else if (value instanceof Boolean)
			put(name, (Boolean) value);
		else if (value instanceof String)
			put(name, (String) value);
		else
			throw new IllegalArgumentException("Unhandled type \"" + value.getClass().toString()
			                                   + "\" in MachineData.put(String name, Object value)");
	}
	
	public Machine getMachine(){
		return machine;
	}
	
	public MachineValue getValue(String name){
		return data.get(name);
	}
	
	/**
	 * Remove a value from the state vector of this machine
	 * @param name Identifier of the value to remove
	 */
	public void remove(String name){
		data.remove(name);
	}
	
	public Status getStatus(){
		return status;
	}
	
	/**
	 * Sets the current status of the machine
	 */
	public void setStatus(Status status){
		this.status = status;
	}
	
	/**
	 * Sets the current status of the machine
	 */
	public void setStatus(int code, String description){
		status.setCode(code);
		status.setDescription(description);
	}
	
	/**
	 * @return A JSON representation of a MachineData instance
	 */
	@Override
	public String toJson(){
		String json = machine.toJson();
		json = json.substring(0, json.length() - 1);
		json += ",\"data\":{";
		
		boolean isFirst = true;
		for(Entry<String, MachineValue> e : data.entrySet()){
			if (isFirst)
				isFirst = false;
			else
				json += ",";
			
			json += JSONObject.quote(e.getKey()) + ":" + e.getValue().toJson();
		}
		
		json += "},\"status\":" + status.toJson() + "}";
		
		return json;
	}
	
	/**
	 * @return A XML representation of a MachineData instance
	 */
	@Override
	public String toXml(){
		return toXml(true);
	}
	
	/**
	 * @return A XML representation of a MachineData instance
	 */
	@Override
	public String toXml(boolean withOuterTags){
		String xml = "";
		if (withOuterTags)
			xml += "<machine>";
		
		xml += machine.toXml(false) + "<data>";
		
		for(Entry<String, MachineValue> e : data.entrySet()){
			xml += "<entry><name><![CDATA[" + e.getKey() + "]]></name><value>"
			     + e.getValue().toXml() +  "</value></entry>";
		}
		
		xml += "</data>";
		
		xml += status.toXml();
		
		if (withOuterTags)
			xml += "</machine>";
		
		return xml;
	}
	
	/**
	 * @return Get the dimension of the machine state vector (this is the number of values it contains)
	 */
	public int getDataCount(){
		return data.size();
	}
	
	/**
	 * @return Get the identifies of the machine state values
	 */
	public Set<String> getDataKeys(){
		return data.keySet();
	}
	
	/**
	 * @param json A JSON representation of a MachineData instance
	 * @return A new MachineData instance created from its JSON representation
	 */
	public static MachineData createFromJson(String json){
		return createFromJson(new JSONObject(json));
	}
	
	/**
	 * @param json A JSON representation of a MachineData instance
	 * @return A new MachineData instance created from its JSON representation
	 */
	public static MachineData createFromJson(JSONObject json){
		Machine machine = Machine.createFromJson(json);
		MachineData result = new MachineData(machine);
		
		// Unwrap if needed
		if (json.has("machine"))
			json = json.getJSONObject("machine");
		
		result.setStatus(Status.createFromJson(json));
		
		JSONObject d = json.getJSONObject("data");
		for (String name : d.keySet()){
			result.putAutodetectType(name, d.get(name));
		}
		
		return result;
	}
	
	/**
	 * @param xml A XML representation of a MachineData instance
	 * @return A new MachineData instance created from its XML representation
	 */
	public static MachineData createFromXml(String xml){
		return createFromXml(XML.toJSONObject(xml));
	}
	
	/**
	 * Parses the given data entry and adds it into the MachineData dest
	 */
	private static void parseXmlEntry(MachineData dest, JSONObject entry){
		String name = entry.getString("name");
		Object value = entry.get("value");
		dest.putAutodetectType(name, value);
	}
	
	/**
	 * @param xml A XML representation of a MachineData instance
	 * @return A new MachineData instance created from its XML representation
	 * 
	 * Please be aware that a JSONObject created from a XML representation might have a
	 * different structure than the JSONObject created from a JSON representation
	 */
	public static MachineData createFromXml(JSONObject xml){
		Machine machine = Machine.createFromXml(xml);
		MachineData result = new MachineData(machine);
		
		// Unwrap if needed
		if (xml.has("machine"))
			xml = xml.getJSONObject("machine");
		
		JSONObject data = xml.getJSONObject("data");
		JSONArray entries = data.optJSONArray("entry");
		
		if (entries == null){
			// Only one item in XML
			parseXmlEntry(result,  data.getJSONObject("entry"));
		} else {
			int entriesLength = entries.length();
			for (int i = 0; i < entriesLength; i++){
				parseXmlEntry(result, entries.getJSONObject(i));
			}
		}
		
		result.setStatus(Status.createFromXml(xml));
		
		return result;
	}
	
	/**
	 * @return <code>true</code> The given Object is an MachineData instance which is equals to this one
	 *         <code>false</code> The given Object is either not a MachineData instance or differs from this one
	 */
	@Override
	public boolean equals(Object o){
		if (!(o instanceof MachineData))
			return false;
		
		MachineData m = (MachineData) o;
		
		if (!m.getMachine().equals(getMachine()))
			return false;
		
		if (m.getDataCount() != getDataCount())
			return false;
		
		for (String key : data.keySet()){
			if (!m.getValue(key).equals(getValue(key)))
				return false;
		}
		
		return true;
	}
	
	/**
	 * @return A human readable String representation of this MachineData
	 */
	@Override
	public String toString(){
		String result = machine.toString();
		result += "\nData:\n";
		
		for (String key : data.keySet()){
			result += "\t\"" + key + "\" = \"" + getValue(key).toString() + "\"\n"; 
		}
		
		if (status != null){
			result += status.toString();
		}
		
		return result;
	}
}
