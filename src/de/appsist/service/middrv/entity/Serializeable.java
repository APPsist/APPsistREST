/**
 * Interface providing function to serialize an instance
 * 
 * @author Marian
 */
package de.appsist.service.middrv.entity;

public interface Serializeable {
	/**
	 * @return All needed attributes to recreate this Object serialized as JSON-String
	 */
	String toJson();
	
	/**
	 * @return All needed attributes to recreate this Object serialized as XML-String
	 */
	String toXml();
	
	/**
	 * @param withOuterTags Wether to print outer tags
	 * @return All needed attributes to recreate this Object serialized as XML-String
	 */
	String toXml(boolean withOuterTags);
}
