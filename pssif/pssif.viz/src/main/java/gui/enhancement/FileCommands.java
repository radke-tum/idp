package gui.enhancement;

import graph.operations.MasterFilter;
import gui.GraphView;
import gui.MatrixView;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import jena.mapper.impl.PssifMapperImpl;
import reqtool.bus.ReqToolReqistry;
import reqtool.event.CreateSpecProjectEvent;
import model.FileImporter;

public class FileCommands{
	FileImporter importer = null;
	JFrame frame = null;
	GraphView graphView = null;
	MatrixView matrixView = null;
	MasterFilter masterFilter = null;
	private PssifMapperImpl fromDB = null;
	private MenuManager menuManager;
	private MainFrame mainFrame ;
	public FileCommands(MainFrame mainFrame)
	{
		importer = new FileImporter();
		this.frame = mainFrame.getFrame();
		this.menuManager = mainFrame.getMenuManager();
		this.mainFrame = mainFrame;
	}
	public void update()
	{
		this.menuManager = mainFrame.getMenuManager();
	}
	
	private void setUp()
	{
		// Create the Views
		matrixView = new MatrixView();
		graphView = new GraphView(mainFrame);
		// instance which manages all the filters
		masterFilter = graphView.getMasterFilter();
		
		int width = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDisplayMode().getWidth();
		int height = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDisplayMode().getHeight();
		Dimension d = new Dimension(width, height);
		// Setup the frame
		frame.getContentPane().removeAll();
		// Standard start with Graph
		frame.getContentPane().add(graphView.getGraphPanel());
		graphView.setActive(true);
		matrixView.setActive(false);

		menuManager.setValues(graphView, matrixView, masterFilter);

		//create the full menuBar after first import
		frame.setJMenuBar(menuManager.createMenu());
		menuManager.adjustButtons();
		
		frame.setPreferredSize(d);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.repaint();
	}
	
	
	
	//function that do the process for importing from a file
	public void importFromFile()
	{
		if (importer.showPopup(frame)) {
			setUp();
		}
	}
		
	//function for creating a new file from scratch
	public void createNewFile()
	{
		// if (reqProjImporter.open(frame))
		ReqToolReqistry.getInstance().post(new CreateSpecProjectEvent(frame));
		if (true) {
			setUp();
		}
	}
		
	//do the process for importing from DB
	public void importFromDB()
	{
		try {
			if (fromDB == null)
				fromDB = new PssifMapperImpl();
			fromDB.DBToModel();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(
					null,
					"Error with importing!\n"
							+ e1.getLocalizedMessage(), "PSSIF",
					JOptionPane.ERROR_MESSAGE);
		}
		
		
		setUp();
	}
}
