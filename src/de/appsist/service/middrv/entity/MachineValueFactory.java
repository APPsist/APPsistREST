package de.appsist.service.middrv.entity;

public class MachineValueFactory {
	/**
	 * @return A new MachineValue of type BOOL
	 */
	public static MachineValue create(Boolean value){
		return new MachineValueImpl<>(MachineValueType.BOOL, value);
	}
	
	/**
	 * @return A new MachineValue of type String
	 */
	public static MachineValue create(String value){
		return new MachineValueImpl<>(MachineValueType.STRING, value);
	}
	
	/**
	 * @return A new MachineValue of type LONG
	 */
	public static MachineValue create(Integer value){
		return new MachineValueImpl<>(MachineValueType.LONG, new Long(value));
	}
	
	/**
	 * @return A new MachineValue of type LONG
	 */
	public static MachineValue create(Long value){
		return new MachineValueImpl<>(MachineValueType.LONG, value);
	}
	
	/**
	 * @return A new MachineValue of type DOUBLE
	 */
	public static MachineValue create(Double value){
		return new MachineValueImpl<>(MachineValueType.DOUBLE, value);
	}
}
