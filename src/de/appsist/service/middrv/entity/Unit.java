/**
 * Entity to represent a Unit. 
 */
package de.appsist.service.middrv.entity;

import org.json.JSONObject;

public class Unit implements Serializeable {
	private final String unit;
	
	public Unit(String unit){
		this.unit = unit;
	}
	
	@Override
	public String toJson() {
		return JSONObject.quote(toString());
	}

	@Override
	public String toXml() {
		return toXml(true);
	}

	@Override
	public String toXml(boolean withOuterTags) {
		String result = "";
		
		if (withOuterTags)
			result += "<unit>";
		
		result +=  "<![CDATA[" + toString() + "]]>";
		
		if (withOuterTags)
			result += "</unit>";
		
		return result;
	}
	
	@Override
	public String toString(){
		return unit;
	}
	
	@Override
	public int hashCode(){
		return unit.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if (!(o instanceof Unit))
			return false;
		
		Unit u = (Unit) o;
		
		return toString().equals(u);
	}
	
	// Definitions of some most common units (mostly SI units) to promote consisted spelling of
	// units
	public static final Unit NONE = new Unit("");
	public static final Unit CELSIUS = new Unit("°C");
	public static final Unit DEGREE_CELSIUS = CELSIUS;
	public static final Unit KELVIN = new Unit("K");
	public static final Unit METRE = new Unit("m");
	public static final Unit CENTI_METRE = new Unit("cm");
	public static final Unit MILLI_METRE = new Unit("mm");
	public static final Unit MICRO_METRE = new Unit("µm");
	public static final Unit NANO_METRE = new Unit("nm");
	public static final Unit KILO_METRE = new Unit("km");
	public static final Unit METRE_PER_SECOND = new Unit("m/s");
	public static final Unit CENTI_METRE_PER_SECOND = new Unit("cm/s");
	public static final Unit MILLI_METRE_PER_SECOND = new Unit("mm/s");
	public static final Unit MICRO_MILLI_METRE_PER_SECOND = new Unit("µm/s");
	public static final Unit NANO_MILLI_METRE_PER_SECOND = new Unit("nm/s");
	public static final Unit METRE_PER_HOUR = new Unit("m/h");
	public static final Unit KILO_METRE_PER_SECOND = new Unit("km/h");
	public static final Unit KILOGRAM = new Unit("kg");
	public static final Unit GRAM = new Unit("g");
	public static final Unit MILLI_GRAM = new Unit("mg");
	public static final Unit MYCRO_GRAM = new Unit("µg");
	public static final Unit NANO_GRAM = new Unit("ng");
	public static final Unit TON = new Unit("t");
	public static final Unit YEAR = new Unit("y");
	public static final Unit DAY = new Unit("d");
	public static final Unit HOUR = new Unit("h");
	public static final Unit MINUTE = new Unit("min");
	public static final Unit SECOND = new Unit("s");
	public static final Unit MILLI_SECOND = new Unit("ms");
	public static final Unit MICRO_SECOND = new Unit("µs");
	public static final Unit NANO_SECOND = new Unit("ns");
	public static final Unit AMPERE = new Unit("A");
	public static final Unit MILLI_AMPERE = new Unit("mA");
	public static final Unit MICRO_AMPERE = new Unit("µA");
	public static final Unit NANO_AMPERE = new Unit("nA");
	public static final Unit MOLE = new Unit("mol");
	public static final Unit HERTZ = new Unit("Hz");
	public static final Unit KILO_HERTZ = new Unit("kHz");
	public static final Unit MEGA_HERTZ = new Unit("MHz");
	public static final Unit GIGA_HERTZ = new Unit("GHz");
	public static final Unit MILLI_HERTZ = new Unit("mHz");
	public static final Unit MICRO_HERTZ = new Unit("µHz");
	public static final Unit ROUNDS_PER_MINUTE = new Unit("min⁻¹");
	public static final Unit NEWTON = new Unit("N");
	public static final Unit KILO_NEWTON = new Unit("kN");
	public static final Unit MEGA_NEWTON = new Unit("MN");
	public static final Unit MILLI_NEWTON = new Unit("mN");
	public static final Unit MICRO_NEWTON = new Unit("µN");
	public static final Unit NANO_NEWTON = new Unit("nN");
	public static final Unit PASCAL = new Unit("Pa");
	public static final Unit HEKTO_PASCAL = new Unit("hPa (mbar)");
	public static final Unit MILLI_BAR = HEKTO_PASCAL;
	public static final Unit BAR = new Unit("10⁵ Pa (bar)");
	public static final Unit KILO_PASCAL = new Unit("kPa");
	public static final Unit MEGA_PASCAL = new Unit("MPa");
	public static final Unit MILLI_PASCAL = new Unit("mPa");
	public static final Unit MICRO_PASCAL = new Unit("µPa");
	public static final Unit JOULE = new Unit("J");
	public static final Unit KILO_JOULE = new Unit("kJ");
	public static final Unit MEGA_JOULE = new Unit("MJ");
	public static final Unit MILLI_JOULE = new Unit("mJ");
	public static final Unit WATT_HOURS = new Unit("Wh");
	public static final Unit KILO_WATT_HOURS = new Unit("kWh");
	public static final Unit WATT = new Unit("W");
	public static final Unit KILO_WATT = new Unit("kW");
	public static final Unit MEGA_WATT = new Unit("MW");
	public static final Unit MILLI_WATT = new Unit("mW");
	public static final Unit COULOMB = new Unit("C");
	public static final Unit KILO_COULOMB = new Unit("kC");
	public static final Unit MEGA_COULOMB = new Unit("MC");
	public static final Unit AMPERE_HOURS = new Unit("Ah");
	public static final Unit KILO_AMPERE_HOURS = new Unit("kAh");
	public static final Unit MILLI_AMPERE_HOURS = new Unit("mAh");
	public static final Unit VOLT = new Unit("V");
	public static final Unit KILO_VOLT = new Unit("kV");
	public static final Unit MEGA_VOLT = new Unit("MV");
	public static final Unit MILLI_VOLT = new Unit("mV");
	public static final Unit MICRO_VOLT = new Unit("µV");
	public static final Unit FARAD = new Unit("F");
	public static final Unit KILO_FARAD = new Unit("kF");
	public static final Unit MILLI_FARAD = new Unit("mF");
	public static final Unit MICRO_FARAD = new Unit("µF");
	public static final Unit NANO_FARAD = new Unit("nF");
	public static final Unit OHM = new Unit("Ω");
	public static final Unit KILO_OHM = new Unit("kΩ");
	public static final Unit MEGA_OHM = new Unit("MΩ");
	public static final Unit GIGA_OHM = new Unit("GΩ");
	public static final Unit MILLI_OHM = new Unit("mΩ");
	public static final Unit SIEMENS = new Unit("S");
	public static final Unit KILO_SIEMENS = new Unit("kS");
	public static final Unit MILLI_SIEMENS = new Unit("mS");
	public static final Unit MICRO_SIEMENS = new Unit("µS");
	public static final Unit NANO_SIEMENS = new Unit("nS");
	public static final Unit WEBER = new Unit("Wb");
	public static final Unit KILO_WEBER = new Unit("kWb");
	public static final Unit MEGA_WEBER = new Unit("MWb");
	public static final Unit MILLI_WEBER = new Unit("mWb");
	public static final Unit MICRO_WEBER = new Unit("µWb");
	public static final Unit TESLA = new Unit("T");
	public static final Unit KILO_TESLA = new Unit("kT");
	public static final Unit MEGA_TESLA = new Unit("MT");
	public static final Unit MILLI_TESLA = new Unit("mT");
	public static final Unit MICRO_TESLA = new Unit("µT");
	public static final Unit HENRY = new Unit("H");
	public static final Unit KILO_HENRY = new Unit("kH");
	public static final Unit MEGA_HENRY = new Unit("MH");
	public static final Unit MILLI_HENRY = new Unit("mH");
	public static final Unit MICRO_HENRY = new Unit("µH");
}
