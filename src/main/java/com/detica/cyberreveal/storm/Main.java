package com.detica.cyberreveal.storm;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;

import com.detica.cyberreveal.storm.topology.BookTopology;

/**
 * Main class. This should be used only for testing purposes.
 */

public final class Main {

	/**
	 * Private Constructor. this is a utility class and should not be
	 * instantiated.
	 */
	private Main() {
		// Do Nothing
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws AlreadyAliveException
	 *             the already alive exception
	 * @throws InvalidTopologyException
	 *             the invalid topology exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(final String[] args) throws AlreadyAliveException,
														InvalidTopologyException,
														IOException {
		String topologyName = null;

		if (args != null && args.length > 0) {
			topologyName = args[0];
		}

/************************************** New block- Start ***************************************
 Reason: Creation of an additional feature.
 		 A configuration file was implemented (App.properties) to provide location/name for
         the input and output files that are going to be used by the Storm Topology
 **********************************************************************************************/

		Properties prop = new Properties();
		String configFileName = "src/main/resources/App.properties";


		try (FileInputStream fis = new FileInputStream(configFileName)) {
			prop.load(fis);

			BookTopology topology = new BookTopology(topologyName,
											 prop.getProperty("properties.inputFile"),
										 	new File(prop.getProperty("properties.outputFile")));

			topology.run();

		} catch (FileNotFoundException ex) {System.out.println("File not found: "+configFileName);
		}

/************************************** AFTER SUBMISSION - Changed block - Start **********************/
		catch (IOException ex) {System.out.println(ex);
		}
/************************************** AFTER SUBMISSION - Changed block - End ************************/

/************************************** Previous block - Start ***********************************
  		catch (IOException ex) {System.out.println("File could not be opened: "+configFileName);}
 ************************************** Previous block - End ************************************/

/*************************************New block- End*******************************************/

	}
}
