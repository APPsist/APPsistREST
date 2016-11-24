/**
 * Entity class representing a machine value.
 * 
 * @author Marian
 */
package de.appsist.service.middrv.entity;

import org.json.JSONObject;

public class MachineValueImpl<T extends Comparable<T>> implements MachineValue {
	private T value;
	private MachineValueType type;
	
	/**
	 * @return The MachineValueType of this MachineValue
	 */
	public MachineValueType getType(){
		return type;
	}
	
	/**
	 * @return The actual value
	 */
	public T getValue(){
		return value;
	}
	
	/**
	 * @param value The new value
	 */
	public void setValue(T value){
		this.value = value;
	}
	
	/**
	 * @return  A human readable String representation of this MachineSchma
	 */
	@Override
	public String toString(){
		return value.toString();
	}
	
	/**
	 * @return <code>true</code> The given Object is a MachineSchema instance and equal to this one
	 *         <code>false</code> The given Object is either not a MachineSchema instance or differs from this one
	 */
	// Explicitly checking for correct type and parameterization, so suppression warnings: 
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o){
		MachineValue genericMachineValue;
		MachineValueImpl<T> correctMachineValue;
		
		if (!(o instanceof MachineValue))
			return false;
		
		genericMachineValue = (MachineValue) o;
		
		if (!genericMachineValue.getType().equals(getType()))
			return false;
		
		correctMachineValue = (MachineValueImpl<T>) genericMachineValue;
		
		if (!correctMachineValue.getValue().equals(this.getValue()))
			return false;
		
		return true;
	}
	
	/**
	 * Creates a new MachineValue - use the static MachineValue.create() functions instead
	 * @param type Type of the machine value
	 * @param value The actual value
	 */
	protected MachineValueImpl(MachineValueType type, T value){
		this.type = type;
		this.value = value;
	}

	@Override
	public String toJson() {
		if (getType() == MachineValueType.STRING)
			return JSONObject.quote((String) getValue());
		
		return getValue().toString();
	}

	@Override
	public String toXml() {
		return toXml(false);
	}

	@Override
	public String toXml(boolean withOuterTags) {
		String result = "";
		
		if (withOuterTags)
			result += "<" + getType().toString() + ">";
		
		if (getType() == MachineValueType.STRING)
			result += "<![CDATA[" + getValue().toString() + "]]>";
		else
			result += getValue().toString();
		
		if (withOuterTags)
			result += "</" + getType().toString() + ">";
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(MachineValue o) {
		if (!getType().equals(o.getType()))
			throw new IllegalArgumentException("Cannot compare MachineValues of different types");
		
		return getValue().compareTo(((MachineValueImpl<T>) o).getValue());
	}
}