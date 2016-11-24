/**
 * An entity class representing a machine
 * 
 * @author Marian
 */
package de.appsist.service.middrv.entity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class Machine implements Serializeable {
	private String vendorID;
	private String machineID;
	private String serialNumber;
	private String machineUUID;

	
	/**
	 * Construct a machine
	 * @param vendorID A unique vendor identifier
	 * @param machineID Machine type identifier which must be unique for this vendor and machine type 
	 * @param serialNumber A unique machine identifier. (Must be at least unique for machines of this
	 *                     type from this vendor)
	 */
	public Machine(String vendorID, String machineID, String serialNumber){
		this.vendorID = vendorID;
		this.machineID = machineID;
		this.serialNumber = serialNumber;
		this.machineUUID = "";
		
	}
	
	/**
	 * Construct a machine
	 * @param vendorID A unique vendor identifier
	 * @param machineID Machine type identifier which must be unique for this vendor and machine type 
	 * @param serialNumber A unique machine identifier. (Must be at least unique for machines of this
	 *                     type from this vendor)
	 */
	public Machine(String vendorID, String machineID, String serialNumber, String machineUUID){
		this.vendorID = vendorID;
		this.machineID = machineID;
		this.serialNumber = serialNumber;
		this.machineUUID = machineUUID;
	}
	
	/**
	 * @return The vendor ID, which uniquely identifies the vendor of this machine
	 */
	public String getVendorID(){
		return vendorID;
	}
	
	/**
	 * @return The ID of this machine, which uniquely identifies this machine type among the
	 *         machines of this vendor
	 */
	public String getMachineID(){
		return machineID;
	}
	
	/**
	 * @return The serial number of this machine, which uniquely identifies this machine among
	 *         all machine of this type 
	 */
	public String getSerialNumber(){
		return serialNumber;
	}
	
	/**
	 * @return The UUID of this machine, which uniquely identifies this machine among
	 *         all machine of this type and can be looked up in the ontology
	 */
	public String getMachineUUID(){
		return machineUUID;
	}
	
	/**
	 * @return The JSON representation of this machine
	 */
	@Override
	public String toJson(){
		return "{\"vendor\":" + JSONObject.quote(getVendorID()) + ",\"id\":" + JSONObject.quote(getMachineID()) + ",\"serial_number\":"
		            + JSONObject.quote(getSerialNumber()) + ",\"uuid\":" + JSONObject.quote(getMachineUUID()) + "}";
	}
	
	/**
	 * @return The XML representation of this machine
	 */
	@Override
	public String toXml(){
		return toXml(true);
	}
	
	/**
	 * @param withMachineTags Encapsulate the XML representation in "<machine>" ... "</machine>" tags
	 * @return The XML representation of this machine
	 */
	@Override
	public String toXml(boolean withMachineTags){
		String xml = "";
		
		if (withMachineTags)
			xml += "<machine>";
		
		xml += "<vendor>" + getVendorID() + "</vendor><id>" + getMachineID() + "</id>"
		     + "<serial_number><![CDATA[" + getSerialNumber() + "]]></serial_number>"
		     +"<uuid><![CDATA[" + getMachineUUID() + "]]></uuid>";
		
		if (withMachineTags)
			xml += "</machine>";
		
		return xml;
	}
	
	/**
	 * Create a new machine instance from its JSON representation
	 * 
	 * @param json JSON representation of a machine to create a instance from
	 * @return The new instance created from JSON
	 * @throws JSONException If the given JSON does not contain a valid machine
	 */
	public static Machine createFromJson(String json) throws JSONException{
		return createFromJson(new JSONObject(json));
	}
	
	/**
	 * Create a new machine instance from its JSON representation
	 * 
	 * @param json JSON representation of a machine to create a instance from
	 * @return The new instance created from JSON
	 * @throws JSONException If the given JSON does not contain a valid machine
	 */
	public static Machine createFromJson(JSONObject json) throws JSONException{
		// unwrap if needed
		if (json.has("machine"))
			json = json.getJSONObject("machine");
		
		String id = json.getString("id");
		String vendor = json.getString("vendor");
		String serial = json.getString("serial_number");
		String uuid = "";
		try {
			uuid = json.getString("uuid");
		} catch (JSONException e) {
			// it was not defined --> use the default "";
		}
		Machine result = new Machine(vendor, id, serial, uuid);
		return result;
	}
	
	/**
	 * Create a new machine instance from its XML representation
	 * 
	 * @param xml XML representation of a machine to create a instance from
	 * @return The new instance created from XML
	 * @throws JSONException If the given XML does not contain a valid machine
	 */
	public static Machine createFromXml(String xml) throws JSONException{
		return createFromXml(XML.toJSONObject(xml));
	}
	
	/**
	 * Create a new machine instance from its XML representation
	 * 
	 * @param xml XML representation of a machine to create a instance from
	 * @return The new instance created from XML
	 * @throws JSONException If the given XML does not contain a valid machine
	 */
	public static Machine createFromXml(JSONObject xml) throws JSONException{
		return createFromJson(xml);
	}
	
	/**
	 * A human readable String representation of this machine
	 */
	@Override
	public String toString(){
		return "Machine: vendor=" + vendorID + ", machine=" + machineID + ", serial number=\""
	           + serialNumber + "\", uuid=\"" + machineUUID + "\"";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((machineID == null) ? 0 : machineID.hashCode());
		result = prime * result + ((serialNumber == null) ? 0 : serialNumber.hashCode());
		result = prime * result + ((vendorID == null) ? 0 : vendorID.hashCode());
		result = prime * result + ((machineUUID == null) ? 0 : machineUUID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Machine other = (Machine) obj;
		if (machineID == null) {
			if (other.machineID != null)
				return false;
		} else if (!machineID.equals(other.machineID))
			return false;
		if (serialNumber == null) {
			if (other.serialNumber != null)
				return false;
		} else if (!serialNumber.equals(other.serialNumber))
			return false;
		if (vendorID == null) {
			if (other.vendorID != null)
				return false;
		} else if (!vendorID.equals(other.vendorID))
			return false;
		if (machineUUID == null) {
			if (other.machineUUID != null)
				return false;
		} else if (!machineUUID.equals(other.machineUUID))
			return false;
		return true;
	}
	

}
