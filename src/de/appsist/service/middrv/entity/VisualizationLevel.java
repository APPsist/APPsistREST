package de.appsist.service.middrv.entity;

import org.json.JSONObject;

public enum VisualizationLevel implements Serializeable {
	OVERVIEW("overview", 0),
	DETAIL("detail", 10),
	NEVER("never", 20);
	
	private final String name;
	private final int level;
	
	private VisualizationLevel(String name, int level){
		this.name = name;
		this.level = level;
	}
	
	@Override
	public String toString(){
		return getName();
	}
	
	public String getName(){
		return name;
	}
	
	public int getLevel(){
		return level;
	}
	
	@Override
	public String toJson() {
		return JSONObject.quote(getName());
	}

	@Override
	public String toXml() {
		return toXml(true);
	}

	@Override
	public String toXml(boolean withOuterTags) {
		String result = "";
		
		if (withOuterTags)
			result += "<visualization_level>";
		
		result +=  "<![CDATA[" + getName() + "]]>"; 
		
		if (withOuterTags)
			result += "<visualization_level>";
		
		return result;
	}
	
	public static VisualizationLevel byName(String name){
		switch (name){
		case "overview":
			return VisualizationLevel.OVERVIEW;
		case "detail":
			return VisualizationLevel.DETAIL;
		case "never":
			return VisualizationLevel.NEVER;
		default:
			throw new IllegalArgumentException("No VisualizationLevel is identified by \"" + name + "\"");
		}
	}
}
