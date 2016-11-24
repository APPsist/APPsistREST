/**
 * Enum over the different visualization modes of machine values.
 */
package de.appsist.service.middrv.entity;

import org.json.JSONObject;

public enum VisualizationType implements Serializeable{
	TEXT_FIELD ("text_field"),
	PRECENT_BAR ("percent_bar"),
	ON_OFF_LIGHT ("on_off");
	
	final String name;
	
	private VisualizationType(String name){
		this.name = name;
	}

	@Override
	public String toJson() {
		return JSONObject.quote(name);
	}

	@Override
	public String toXml() {
		return toXml(true);
	}

	@Override
	public String toXml(boolean withOuterTags) {
		String result = "";
		if (withOuterTags)
			result += "<visualization_type>";
		
		result +=  "<![CDATA[" + name + "]]>"; 
		
		if (withOuterTags)
			result += "</visualization_type>";
		
		return result;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	/**
	 * Get a VisualizationType by its name
	 * @param name Name identifying the VisualizationType to get
	 * @return The VisualizationType identified by the given name
	 * @throws IllegalArgumentException No VisualizationType has the given name 
	 */
	public static VisualizationType byName(String name) throws IllegalArgumentException{
		switch (name){
		case "text_field":
			return VisualizationType.TEXT_FIELD;
		case "percent_bar":
			return VisualizationType.PRECENT_BAR;
		case "on_off":
			return VisualizationType.ON_OFF_LIGHT;
		default:
			throw new IllegalArgumentException("No VisualizationType is identified by \"" + name + "\"");
		}
	}
}
