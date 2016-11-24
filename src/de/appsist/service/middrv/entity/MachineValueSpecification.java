package de.appsist.service.middrv.entity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class MachineValueSpecification implements Serializeable {
	private String name;
	private MachineValueType type;
	private Unit unit;
	private VisualizationType visualizationType;
	private VisualizationLevel visualizationLevel;
	
	public MachineValueSpecification(String name, MachineValueType type, Unit unit,
	                                 VisualizationType visualizationType,
	                                 VisualizationLevel visualizationLevel)
	{
		this.name = name;
		this.type = type;
		this.unit = unit;
		this.visualizationType = visualizationType;
		this.visualizationLevel = visualizationLevel;
	}
	
	public MachineValueSpecification(String name, MachineValueType type, Unit unit){
		this(name, type, unit, VisualizationType.TEXT_FIELD, VisualizationLevel.DETAIL);
	}

	public String getName() {
		return name;
	}

	public MachineValueType getType() {
		return type;
	}

	public Unit getUnit() {
		return unit;
	}

	public VisualizationType getVisualizationType() {
		return visualizationType;
	}
	
	public VisualizationLevel getVisualizationLevel() {
		return visualizationLevel;
	}

	@Override
	public String toJson() {
		return "{\"name\":" + JSONObject.quote(name)  + ",\"type\":" + type.toJson()
		     + ",\"unit\":" + JSONObject.quote(unit.toString()) + ",\"visualization_type\":"
		     + JSONObject.quote(visualizationType.toString()) + ",\"visualization_level\":"
		     + JSONObject.quote(visualizationLevel.toString()) + "}";
	}

	@Override
	public String toXml() {
		return toXml(true);
	}

	@Override
	public String toXml(boolean withOuterTags) {
		String result = "";
		
		if (withOuterTags)
			result += "<specification>";
		
		result += "<name><![CDATA[" + name + "]]></name>"
		        + type.toXml()
		        + "<unit><![CDATA[" + unit + "]]></unit>"
		        + "<visualization_type><![CDATA[" + visualizationType + "]]></visualization_type>"
		        + "<visualization_level><![CDATA[" + visualizationLevel + "]]></visualization_level>";
		
		if (withOuterTags)
			result += "</specification>";
		
		return result;
	}
	
	/**
	 * Create a MachineValueSpecification instance by its JSON representation
	 * @param json Representation to create the MachineValueSpecification instance from
	 * @return The MachineValueSpecification instance created from its representation
	 * @throws JSONException The given representation contains no valid MachineValueSpecification
	 */
	public static MachineValueSpecification createFromJson(String json) throws JSONException {
		return createFromJson(new JSONObject(json));
	}
	
	/**
	 * Create a MachineValueSpecification instance by its JSON representation
	 * @param json Representation to create the MachineValueSpecification instance from
	 * @return The MachineValueSpecification instance created from its representation
	 * @throws JSONException The given representation contains no valid MachineValueSpecification
	 */
	public static MachineValueSpecification createFromJson(JSONObject json) throws JSONException {
		
		return new MachineValueSpecification(json.getString("name"), MachineValueType.byIdentifier(json.getString("type")), 
		                                     new Unit(json.getString("unit")),
		                                     VisualizationType.byName(json.getString("visualization_type")),
		                                     VisualizationLevel.byName(json.getString("visualization_level")));
	}
	
	/**
	 * Create a MachineValueSpecification instance by its XML representation
	 * @param xml Representation to create the MachineValueSpecification instance from
	 * @return The MachineValueSpecification instance created from its representation
	 * @throws JSONException The given representation contains no valid MachineValueSpecification
	 */
	public static MachineValueSpecification createFromXml(String xml) throws JSONException {
		return createFromXml(XML.toJSONObject(xml));
	}
	
	/**
	 * Create a MachineValueSpecification instance by its XML representation
	 * @param xml Representation to create the MachineValueSpecification instance from
	 * @return The MachineValueSpecification instance created from its representation
	 * @throws JSONException The given representation contains no valid MachineValueSpecification
	 */
	public static MachineValueSpecification createFromXml(JSONObject xml) throws JSONException {
		// Unwrap if needed:
		if (xml.has("specification"))
			xml = xml.getJSONObject("specification");
		
		return createFromJson(xml);
	}
	
	@Override
	public String toString(){
		return "Name = \"" + name + "\", type = " + type + ", unit = \"" + unit + "\", visualization: type = "
		      + visualizationType + ", level = " + visualizationLevel;
	}
}
