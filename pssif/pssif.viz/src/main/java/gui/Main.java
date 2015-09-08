package gui;

import graph.listener.NodeLocationWriterReader;


/**
 * The main class of the project. Execute this class to start the Visualization
 * Software
 * 
 * @author Luc
 *
 */
public class Main {

	public static void main(String[] args) {

		final MainFrame mf = new MainFrame();
		//running code on exit
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	            NodeLocationWriterReader nlwr = new NodeLocationWriterReader();
	            nlwr.saveNodesToFile("nodelocations.txt", mf.getFileCommands().graphView.getGraphVisualization().layout);
	        	System.out.println("Terminated!");
	        }
	    }, "Shutdown-thread"));
	}	
	
	
}
