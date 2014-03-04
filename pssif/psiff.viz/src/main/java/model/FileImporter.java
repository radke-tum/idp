package model;

import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.util.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.transform.Mapper;
import de.tum.pssif.transform.MapperFactory;
import de.tum.pssif.transform.mapper.BpmnMapper;
import de.tum.pssif.transform.mapper.EpkMapper;
import de.tum.pssif.transform.mapper.SysMlMapper;
import de.tum.pssif.transform.mapper.graphml.GraphMLMapper;
import de.tum.pssif.transform.mapper.graphml.GraphMlViewCreator;

public class FileImporter {
	
	private JComboBox<String> filetype;
	private HashMap<String, String> comboBoxValues;
	
	
	public FileImporter()
	{
		comboBoxValues = new HashMap<String, String>();

		comboBoxValues.put("Umsatzorientierte Funktionsplanung", MapperFactory.UOFP);
		comboBoxValues.put("BPMN", MapperFactory.BPMN);
		comboBoxValues.put("EPK", MapperFactory.EPK);
		comboBoxValues.put("SysML", MapperFactory.SYSML);
		
	}
	/**
	 * Read the selected file. Select the correct importer. Load the Nodes and Edges with the ModelBuilder
	 * @param file the selected file which should be imported
	 * @return true if no errors occured, false otherwise
	 */
	private boolean importFile(File file)
	{
		// Read the Inputfile 
		InputStream in;
		try {
			in = new FileInputStream(file);

			
	        Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();

	        Metamodel view =null;;
	        
	        String selectedFileType = String.valueOf(filetype.getSelectedItem());
	        
	        Mapper importer = MapperFactory.getMapper(comboBoxValues.get(selectedFileType));
	        
	        if (importer instanceof GraphMLMapper)
	        {
	        	view = GraphMlViewCreator.createGraphMlView(metamodel);
	        }
	        
	        if (importer instanceof SysMlMapper)
	        {
	        	// TODO Needs to be changed	
	        	//view = ((SysMlMapper) importer).
	        	view = metamodel;
	        }
	        
	        if (importer instanceof EpkMapper)
	        {
	        	view = ((EpkMapper) importer).createEpkView(metamodel);
	        }
	        
	        if (importer instanceof BpmnMapper)
	        {
	        	// TODO Needs to be changed	
	        	//view = ((BpmnMapper) importer).
	        	view = metamodel;
	        }
	        
	        Model model;
	        try {
	        	model = importer.read(view, in);
	        	
		        // Create the Viz Model
		        new ModelBuilder(metamodel,model);
		        
		        return true;
	        }
	        
	        catch (Exception e)
	        {
	        	//e.printStackTrace();
	        	JPanel errorPanel = new JPanel();
	    		
	    		errorPanel.add(new JLabel("There was a problem transforming the selected file. Was the file type chosen correctly?"));
	    		
	    		JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
				return false;
	        }
		} catch (FileNotFoundException e) {
			JPanel errorPanel = new JPanel();
    		
    		errorPanel.add(new JLabel("There was a problem importing the selected file."));
    		
    		JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	/**
	 * Display the popup to the User
	 * @param caller the component which called to display the popup
	 * @return true if the entire import went fine, otherwise false
	 */
	public boolean showPopup(Component caller)
	{
		JFileChooser openFile = new JFileChooser();
		
		openFile.setCurrentDirectory(new File("C:\\Users\\Luc\\Desktop\\Uni Dropbox\\Dropbox\\IDP-PSS-IF-Shared\\Modelle PE"));
		
		JPanel panel1 = (JPanel) openFile.getComponent(3);
		JPanel panel2 = (JPanel) panel1.getComponent(2);
		
	    JPanel importeType = new JPanel(new FlowLayout());
	    JLabel label1 = new JLabel("Select Import File:");
	    importeType.add(label1);
	    filetype = new JComboBox<String>(comboBoxValues.keySet().toArray(new String[0]));
	    filetype.setSelectedIndex(0);
	    importeType.add(filetype);
	    
		panel2.add(importeType);
		
		int returnVal = openFile.showOpenDialog(caller);
		
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	      
	    	File file = openFile.getSelectedFile();
	    	
	    	return importFile(file);

	    }
	    else
	    	return false;
	}
	        
}
