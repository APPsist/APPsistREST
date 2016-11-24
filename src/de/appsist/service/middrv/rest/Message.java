package de.appsist.service.middrv.rest;

import java.util.Iterator;
import java.util.List;

import de.appsist.service.middrv.entity.Serializeable;

public abstract class Message<T extends Serializeable> implements Serializeable {
	private long time;
	private List<T> content;
	private String outerTagName;
	private String innerTagName;

	
	/**
	 * Create a new SchemaMessage instance
	 * @param content List of content elements
	 */
	public Message(String outerTagName, String innerTagName, List<T> content){
		this(outerTagName, innerTagName, content, System.currentTimeMillis());
	}
	
	/**
	 * Create a new SMessage instance
	 * @param content List of content elements
	 * @param time Time of the message in number of milli seconds since 1. 1. 1970 0:00:00 UTC
	 */
	public Message(String outerTagName, String innerTagName, List<T> content, long time){
		this.outerTagName = outerTagName;
		this.innerTagName = innerTagName;
		this.content = content;
		this.time = time;
	}
	
	/**
	 * @return The list of MachineSchema instances this message contains
	 */
	public List<T> getContent(){
		return content;
	}
	
	/**
	 * @return The number of content items
	 */
	public int getCount(){
		return content.size();
	}
	
	/**
	 * Add a schema to this message
	 * @param item The MachineSchema instance to add
	 */
	protected void addToMessage(T item){
		content.add(item);
	}
	
	public long getTime(){
		return time;
	}
	
	/**
	 * @return A JSON representation of this SchemaMessage
	 */
	@Override
	public String toJson() {
		String result = "{\"time\":" + Long.toString(time) + ",\"" + innerTagName + "\":[";
		
		boolean isFirst = true;
		for (Serializeable item : content){
			if (isFirst)
				isFirst = false;
			else
				result += ",";
			
			result += item.toJson();
		}
		
		result += "]}";
		
		return result;
	}
	
	/**
	 * @return A XML representation of this Message
	 */
	@Override
	public String toXml() {
		return toXml(true);
	}
	
	/**
	 * @return A XML representation of this Message
	 */
	@Override
	public String toXml(boolean withOuterTags) {
		String result = "";
		if (withOuterTags)
			result += "<" + outerTagName + ">";
		
		result += "<time>" + Long.toString(time) + "</time><" + innerTagName + ">";
		
		for (Serializeable item : content){
			result += item.toXml();
		}
		
		result += "</" + innerTagName + ">";
		
		if (withOuterTags)
			result += "</" + outerTagName + ">";
		
		return result;
	}
	
	/**
	 * @return A human readable String representation of this Message
	 */
	@Override
	public String toString(){
		String result = "Message:\nTime: " + Long.toString(time) + "\n";
		for (Serializeable item : content){
			result += item.toString() + "\n";
		}
		
		return result;
	}
	
	/**
	 * returns <code>true</code>, if all of these conditions apply:
	 * - o is a SchemaMessage
	 * - o contains exactly the same schema list (same order, same number of elements, same content)
	 */
	@Override
	public boolean equals(Object o){
		if (!(o instanceof Message))
			return false;

		if (!(o.getClass().equals( this.getClass())))
			return false;

		Message msgGeneric = (Message) o;
		Message<T> msg = (Message<T>) msgGeneric;
		
		if (msg.getCount() != getCount())
			return false;
		
		// Check if content is equal *and* in same *order*
		Iterator<T> i1 = content.iterator();
		Iterator<T> i2 = msg.getContent().iterator();
		while(i1.hasNext()){
			if (!i1.next().equals(i2.next()))
				return false;
		}
		
		return true;
	}
}
