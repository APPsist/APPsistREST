/**
 * An example Server to illustrate how to run the APPsist REST Server
 */
package example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.JULLogDelegate;
import org.vertx.java.core.logging.impl.Log4jLogDelegate;
import org.vertx.java.core.logging.impl.LogDelegate;
import org.vertx.java.core.logging.impl.LoggerFactory;

import de.appsist.service.middrv.rest.server.RestServer;


public class Server {
	private static Logger logger = LoggerFactory.getLogger(Server.class);
	
	private static void waitForButtonClick(){
		JDialog dialogQuit = (new JOptionPane("REST-Server läuft. Mit OK Beenden")).createDialog("APPsist REST");
		dialogQuit.setAlwaysOnTop(true);
		dialogQuit.setModal(true);
		dialogQuit.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialogQuit.setVisible(true);
	}
	
	private static void waitForKeyPress(){
		logger.info("Press Enter to quit server...");
		try {
			(new BufferedReader(new InputStreamReader(System.in))).readLine();
		} catch (IOException e) { e.printStackTrace();}
	}
	
	public static void main(String[] args) throws Exception{
		boolean useGui = true;

		for (String arg : args) {
			switch (arg) {
				case "-c":
				case "--console":
				case "--no-gui":
					useGui = false;
					break;
				case "-h":
				case "--help":
					logger.info(
							"Usage: java -jar \"APPsist REST Server.jar\" [options]\n" +
									"\n" +
									"Parameters:\n" +
									"\n" +
									"  -c|--console|--no-gui\n" +
									"    Disable GUI, wait for pressing key on terminal instead of waiting for pressing button on GUI widget\n" +
									"\n" +
									"  -h|--help\n" +
									"    Show this help"
					);
					System.exit(0);
					break;
				default:
					logger.info("Parameter \"" + arg + "\" not understood. See \"--help\" for help");
					System.exit(-1);
			}
		}

		
		ExampleHandler handler = new ExampleHandler();
		
		RestServer server = new RestServer(VertxFactory.newVertx(), handler, handler, handler, "/services/mid", 30000);
		server.listenHttp();

		
		if (useGui)
			waitForButtonClick();
		else
			waitForKeyPress();
		
		logger.info("Server quit.");
		System.exit(0);
	}

}
