/**
 * Provides static methods to serialize Objects implementing the Serializable interface
 */
package de.appsist.service.middrv.entity;

import org.vertx.java.core.buffer.Buffer;

import de.appsist.service.middrv.rest.ContentType;

public class Serializer {
	/**
	 * Serialize the given Serializable to a vert.x buffer ready for sending it
	 * @param serializeMe The Object to serialize
	 * @param contentType The content type to use for serialization
	 * @return A buffer containing the serialized version of the given Object
	 */
	public static Buffer serializeToBuffer(Serializeable serializeMe, ContentType contentType)
	{
		switch (contentType){
		case JSON:
			return new Buffer(serializeMe.toJson());
		case XML:
			return new Buffer(serializeMe.toXml());
		default:
			throw new IllegalArgumentException("Content type \"" + contentType.toString() + "\" not supported for serialization.");
		}
	}
}
