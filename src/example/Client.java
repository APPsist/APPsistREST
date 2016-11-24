package example;

import de.appsist.service.middrv.entity.Machine;
import de.appsist.service.middrv.entity.MachineData;
import de.appsist.service.middrv.entity.MachineSchema;
import de.appsist.service.middrv.entity.MachineValueType;
import de.appsist.service.middrv.entity.Unit;
import de.appsist.service.middrv.entity.VisualizationLevel;
import de.appsist.service.middrv.entity.VisualizationType;
import de.appsist.service.middrv.rest.ContentType;
import de.appsist.service.middrv.rest.DataMessage;
import de.appsist.service.middrv.rest.DataSchemaMismatchException;
import de.appsist.service.middrv.rest.SchemaMessage;
import de.appsist.service.middrv.rest.client.RHStatusPrinter;
import de.appsist.service.middrv.rest.client.RestClient;

import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

public class Client {
	
	public static void main(String[] args) throws InterruptedException 
	{
		Logger logger = LoggerFactory.getLogger(Client.class);
        RestClient client = new RestClient("localhost","");

		
		// Prepare data for test messages
		Machine station1 = new Machine("0", "0", "Machine");
		MachineData station1Data1 = new MachineData(station1);
		station1Data1.put("temperature", 3.13159);
		station1Data1.put("switch", true);
		station1Data1.put("cycles", 42);
		
		MachineData station1Data2 = new MachineData(station1, -13, "Error");
		
		station1Data2.put("temperature", 2.5);
		station1Data2.put("switch", false);
		station1Data2.put("cycles", 62);
		
		MachineSchema station1Schema = new MachineSchema(station1, "1", "1");
		station1Schema.addField("temperature", MachineValueType.DOUBLE, Unit.CELSIUS, VisualizationType.TEXT_FIELD, VisualizationLevel.OVERVIEW);
		station1Schema.addField("switch", MachineValueType.BOOL, Unit.NONE, VisualizationType.ON_OFF_LIGHT, VisualizationLevel.DETAIL);
		station1Schema.addField("cycles", MachineValueType.LONG, Unit.NONE);
		station1Schema.addField("comment", MachineValueType.STRING, Unit.NONE);
		station1Schema.addField("unused value", MachineValueType.BOOL, Unit.NONE);
		
		Machine station2 = new Machine("1", "1", "First component of Machine");
		MachineSchema station2Schema = new MachineSchema(station2, "2", "1");
		station2Schema.addField("pressure", MachineValueType.DOUBLE, Unit.MILLI_BAR, VisualizationType.TEXT_FIELD, VisualizationLevel.OVERVIEW);
		MachineData station2Data = new MachineData(station2);
		station2Data.put("pressure", 1013.11);
		
		Machine station3 = new Machine("1", "42", "Second component of Machine");
		MachineSchema station3Schema = new MachineSchema(station3, "3", "1");
		station3Schema.addField("voltage", MachineValueType.DOUBLE, Unit.VOLT, VisualizationType.TEXT_FIELD, VisualizationLevel.OVERVIEW);
		MachineData station3Data = new MachineData(station3);
		station3Data.put("voltage", 23.91);
		
		
		SchemaMessage schemaMessage = new SchemaMessage();
		schemaMessage.addSchema(station1Schema);
		schemaMessage.addSchema(station2Schema);
		schemaMessage.addSchema(station3Schema);
		
		DataMessage dataMessage1 = new DataMessage();
		DataMessage dataMessage2 = new DataMessage();
		try{
			dataMessage1.addMachineData(station1Data1, station1Schema);
			dataMessage1.addMachineData(station2Data, station2Schema);
			dataMessage1.addMachineData(station3Data, station3Schema);
			dataMessage2.addMachineData(station1Data2, station1Schema);
			dataMessage2.addMachineData(station2Data, station2Schema);
			dataMessage2.addMachineData(station3Data, station3Schema);
		} catch(DataSchemaMismatchException e){
			e.printStackTrace();
			System.exit(-1);
		}

		
		// Optionally: Sending Schema. (If Server does not know Schema yet, Schema will automatically be sent)
		client.send(ContentType.JSON, schemaMessage);
		client.send(ContentType.XML, schemaMessage);
		
		// Send test messages
		
		Thread.sleep(500);
		
		station1Data1.put("comment", "json content");
		station1Data2.put("comment", "json content");
		client.send(ContentType.JSON, dataMessage1);
		client.send(ContentType.JSON, dataMessage2);
		
		Thread.sleep(500);
		
		station1Data1.put("comment", "xml content");
		station1Data2.put("comment", "xml content");
		client.send(ContentType.XML, dataMessage1);
		client.send(ContentType.XML, dataMessage2);
		
		Thread.sleep(1000);
		
		logger.info("Client quit");
		System.exit(0);
	}

}
