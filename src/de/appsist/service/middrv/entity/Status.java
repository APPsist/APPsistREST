/**
 * Entity class representing an error message
 */

package de.appsist.service.middrv.entity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class Status implements Serializeable {
	private int code;
	private String description;
	
	/**
	 * Create a new error message
	 * 
	 * @param code Error code
	 * @param description Error description
	 */
	public Status(int code, String description){
		this.code = code;
		this.description = description;
	}
	
	/**
	 * Returns the code of this status
	 */
	public int getCode(){
		return code;
	}
	
	/**
	 * Sets the code of this status
	 */
	public void setCode(int code){
		this.code = code;
	}
	
	/**
	 * Returns the description of this status
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Sets the description of this status
	 */
	public void setDescription(String description){
		this.description = description;
	}
	
	/**
	 * Is status code an error code?
	 */
	public boolean isError(){
		return (code < 0);
	}
	
	/**
	 * Is status code an success code?
	 */
	public boolean isSuccess(){
		return !isError();
	}
	
	/**
	 * @return The JSON representation of the error message
	 */
	@Override
	public String toJson() {
		if (description != null)
			return "{\"code\":" + code + ",\"description\":" + JSONObject.quote(description) + "}";
		
		return  "{\"code\":" + code + "}";
	}
	
	/**
	 * @return The XML representation of the error message
	 */
	@Override
	public String toXml() {
		return toXml(true);
	}
	
	/**
	 * @return The XML representation of the error message
	 */
	@Override
	public String toXml(boolean withOuterTags) {
		String result = "";
		
		if (withOuterTags)
			result += "<status>";
		
		result += "<code>" + code + "</code>";
		
		if (description != null)
			result += "<description><![CDATA[" + description + "]]></description>";
		
		if (withOuterTags)
			result += "</status>";
		return result;
	}
	
	/**
	 * Create a ErrorMessage instance from a JSON representation
	 * @param json JSON representation of the ErrorMessage to create
	 * @return The new ErrorMessage instance created from the given representation
	 * @throws JSONException The given JSON does not represent a valid error message
	 */
	public static Status createFromJson(String json) throws JSONException{
		return createFromJson(new JSONObject(json));
	}
	
	/**
	 * Create a ErrorMessage instance from a JSON representation
	 * @param json JSON representation of the ErrorMessage to create
	 * @return The new ErrorMessage instance created from the given representation
	 * @throws JSONException The given JSON does not represent a valid error message
	 */
	public static Status createFromJson(JSONObject json) throws JSONException{
		// unwrap status if needed
		if (json.has("status"))
			json = json.getJSONObject("status");
		
		int code = json.getInt("code");
		String description = json.optString("description");
		
		return new Status(code, description);
	}
	
	/**
	 * Create a ErrorMessage instance from a XML representation
	 * @param xml XML representation of the ErrorMessage to create
	 * @return The new ErrorMessage instance created from the given representation
	 * @throws JSONException The given XML does not represent a valid error message
	 */
	public static Status createFromXml(String xml) throws JSONException{
		return createFromXml(XML.toJSONObject(xml));
	}
	
	/**
	 * Create a ErrorMessage instance from a XML representation
	 * @param xml XML representation of the ErrorMessage to create
	 * @return The new ErrorMessage instance created from the given representation
	 * @throws JSONException The given XML does not represent a valid error message
	 */
	public static Status createFromXml(JSONObject xml) throws JSONException{
		return createFromJson(xml);
	}
	
	/**
	 * @return A human readable String representation of this Status
	 */
	@Override
	public String toString(){
		return (isError() ? "Error: " : "Status: ") +  getCode()  + ": \"" + getDescription() + "\"";
	}
	
	/**
	 * @return <code>true</code>The given object is a Status instance and equal to this one
	 *         <code>false</code>Thegiven object is either not a Status instance or differs from this one
	 */
	@Override
	public boolean equals(Object o){
		if (!(o instanceof Status))
			return false;
		
		Status error = (Status) o;
		
		if (error.getCode() != getCode())
			return false;
		
		if (!error.getDescription().equals(getDescription()))
			return false;
		
		return true;
	}
}
