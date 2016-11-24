/**
 * Enum for checking the type of a generic MachineValue instance
 * 
 * @author Marian
 */
package de.appsist.service.middrv.entity;

public enum MachineValueType implements Serializeable {
	DOUBLE("double", Double.class),
	LONG("long", Long.class),
	BOOL("bool", Boolean.class),
	STRING("string", String.class);
	
	public final String identifier;
	@SuppressWarnings("rawtypes")
	public final Class type;
	
	@SuppressWarnings("rawtypes")
	MachineValueType(String identifier, Class type){
		this.identifier = identifier;
		this.type = type;
	}
	
	/**
	 * Get a MachineValueType by its identifier
	 * @param identifier The name (used in XML/JSON-representations) identifying the machine value type
	 * @return The MachineValueType identified by the given name
	 * @throws IllegalArgumentException If the given name does not identify any MachineValueType
	 */
	public static MachineValueType byIdentifier(String identifier){
		switch(identifier){
		case "double":
			return MachineValueType.DOUBLE;
		case "long":
			return MachineValueType.LONG;
		case "bool":
			return MachineValueType.BOOL;
		case "string":
			return MachineValueType.STRING;
		}
		
		throw new IllegalArgumentException("The given identifier does not identify any MachineValueType");
	}
	
	/**
	 * Get a MachineValueType by the Class of the underlying data type
	 * @param type The underlying data type of the MachienValueType
	 * @return The MachineValueType identified by the given Class
	 * @throws IllegalArgumentException If the given Class does not identify any MachineValueType
	 */
	@SuppressWarnings("rawtypes")
	public static MachineValueType byClass(Class type){
		switch(type.getSimpleName()){
		case "Double":
			return MachineValueType.DOUBLE;
		case "Long":
			return MachineValueType.LONG;
		case "Boolean":
			return MachineValueType.BOOL;
		case "String":
			return MachineValueType.STRING;
		}
		
		throw new IllegalArgumentException("The given Class does not identify any MachineValueType");
	}

	@Override
	public String toJson() {
		return "\"" + toString() + "\"";
	}

	@Override
	public String toXml() {
		return toXml(true);
	}

	@Override
	public String toXml(boolean withOuterTags) {
		String result = "";
		
		if (withOuterTags)
			result += "<type>";
		
		result += toString();
		
		if (withOuterTags)
			result += "</type>";
		
		return result;
	}
	
	@Override
	public String toString(){
		return identifier;
	}
}
