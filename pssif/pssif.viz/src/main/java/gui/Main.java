package gui;

import de.tum.pssif.core.common.PSSIFConstants;
import graph.listener.NodeLocationWriterReader;

/**
 * The main class of the project. Execute this class to start the Visualization
 * Software
 * 
 * @author Basirati
 *
 */
public class Main {

	public static void main(String[] args) {

		final MainFrame mf = new MainFrame();
	
		//running code on exit
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	        	try{
	        		NodeLocationWriterReader nlwr = new NodeLocationWriterReader();
	        		String nodePositionFile = mf.getFileCommands().getImportPath();
	        		if (nodePositionFile == null || nodePositionFile.isEmpty())
	        			nodePositionFile = PSSIFConstants.NODE_POSITION_LAST_PATH;
	        		nlwr.saveNodesToFile(nodePositionFile, mf.getFileCommands().graphView.getGraphVisualization().layout);
	        	} catch(Exception e){System.out.println("Could Not Save Node Positions!");}
	        	
	        	System.out.println("Terminated!");
	        }
	    }, "Shutdown-thread"));
	}	
	
	
}
