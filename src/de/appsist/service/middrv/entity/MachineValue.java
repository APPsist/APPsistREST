/**
 * An interface around MachineValueImpl to get rid of "raw types" warnings 
 */
package de.appsist.service.middrv.entity;

public interface MachineValue extends Serializeable, Comparable<MachineValue>{
	/**
	 * @return The MachineValueType of this MachineValue
	 */
	MachineValueType getType();
}
