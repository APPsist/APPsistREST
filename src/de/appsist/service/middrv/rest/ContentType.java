/**
 * Content types supported by the APPsist REST example implementation
 */

package de.appsist.service.middrv.rest;

public enum ContentType {
	JSON("application/json"),
	XML("application/xml"),
	TEXT_PLAIN("text/plain");
	
	final String name;
	
	ContentType(String name){
		this.name = name;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	/**
	 * @param contentType The MIME-String of the content type
	 * @return
	 */
	public static ContentType byString(String contentType){
		if (contentType == null)
			throw new IllegalArgumentException("MIME-String expected as Argument, but NULL-Pointer given");
		
		int pos;
		if ((pos = contentType.indexOf(";")) > 0)
			contentType = contentType.substring(0, pos);
		
		switch(contentType){
		case "application/json":
			return ContentType.JSON;
		case "application/xml":
			return ContentType.XML;
		case "text/plain":
			return ContentType.TEXT_PLAIN;
		}
		
		throw new IllegalArgumentException("Given MIME-String does not identify any supported content type");
	}
}
