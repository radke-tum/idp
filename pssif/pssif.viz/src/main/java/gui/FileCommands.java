package gui;

import graph.operations.MasterFilter;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.tum.pssif.transform.mapper.db.DBToPssifMapperImpl;
import de.tum.pssif.transform.mapper.db.PssifToDBMapperImpl;
import jena.database.RDFModel;
import jena.database.URIs;
import jena.mapper.impl.DBMapperImpl;
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
	
	private DBToPssifMapperImpl fromDBMapper = null;
	private PssifToDBMapperImpl toDBMapper = null;
	
	private DBMapperImpl toDB = null;
	private MenuManager menuManager;
	private MainFrame mainFrame ;
	private PssifMapperImpl fromDB;
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
	if (fromDBMapper == null)
	fromDBMapper = new DBToPssifMapperImpl();
	fromDBMapper.DBToModel();
	} catch (Exception e1) {
	JOptionPane.showMessageDialog(
	null,
	"Error with importing!\n"
	+ e1.getLocalizedMessage(), "PSSIF",
	JOptionPane.ERROR_MESSAGE);
	}

	// Database should not be deleted
	PssifToDBMapperImpl.deleteAll = true;

	// You can just load a database if there is no other model. If a
	// new model is loaded into the tool nodes and edges are loaded
	// to lists that implies which new nodes has to be added to the
	// DB. But if you load the model from the DB all nodes/edges are
	// in the db so you can delete the list again.
	PssifToDBMapperImpl.clearAll();

	setUp();
	}
	
	public void exportDBTurtle() {
		RDFModel model = null;

		if (fromDB == null) {
			if (toDB == null) {
				toDB = new DBMapperImpl();
				model = toDB.rdfModel;
			} else
				model = toDB.rdfModel;
		} else
			model = fromDB.rdfModel;

		JFileChooser finder = new JFileChooser();
		finder.setSelectedFile(new File(System.getProperty("user.home")
				+ "\\TurtleExport.ttl"));

		int returnVal = finder.showSaveDialog(null);
		if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
			java.io.File file = finder.getSelectedFile();
			model.writeModelToTurtleFile(file);
			JOptionPane.showMessageDialog(null, "Export successful",
					"PSSIF", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void saveToDB() {
		try {
			if (toDBMapper == null){
			toDBMapper = new PssifToDBMapperImpl();
			}
			// If the model was merged then delete DB and build new
			// rdfModel from activeModel
			if (PssifToDBMapperImpl.merge == true) {
			toDBMapper.modelToDB();
			PssifToDBMapperImpl.merge = false;
			}
			// If the database could be deleted compeletely because of
			// not importing the database before importing the model...
			// Then ask the user if he wants to delete the DB or add the
			// new model to the DB
			else if (PssifToDBMapperImpl.deleteAll == true) {
			int delete = JOptionPane.showConfirmDialog(null,
			"Should the Database be deleted?");

			// If the DB should be deleted use the modelToDB Method
			// to save the activeModel
			if (delete == 0)
			toDBMapper.modelToDB();
			// If the DB should not be deleted use the saveToDB
			// Method to just save the changes
			else if (delete == 1)
			toDBMapper.saveToDB();
			else
			return;
			PssifToDBMapperImpl.deleteAll = false;
			// If there is just a minor change in the model of the
			// DB, just save the changes
			} else
			toDBMapper.saveToDB();

			JOptionPane.showMessageDialog(null, "Successfully saved!",
			"PSSIF", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Error with saving!\n"
			+ e1.getMessage(), "PSSIF",
			JOptionPane.ERROR_MESSAGE);
			}

			
		
	}
}
