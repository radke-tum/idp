package model;

import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.tum.pssif.transform.Mapper;
import de.tum.pssif.transform.MapperFactory;


public class FileExporter {

  private JComboBox<String>       filetype;
  private TreeMap<String, String> comboBoxValues;

  public FileExporter() {
    comboBoxValues = new TreeMap<String, String>();

    comboBoxValues.put("Umsatzorientierte Funktionsplanung", MapperFactory.UOFP);
    comboBoxValues.put("BPMN", MapperFactory.BPMN);
    comboBoxValues.put("EPK", MapperFactory.EPK);
    comboBoxValues.put("SysML", MapperFactory.SYSML);
    comboBoxValues.put("PSS-IF", MapperFactory.PSSIF);
  }

  /**
   * Read the selected file. Select the correct exporter. Load the Nodes and Edges with the ModelBuilder
   * @param file the selected file which should be imported
   * @return true if no errors occured, false otherwise
   */
  private boolean exportFile(File file) {
    // Check if the OutputFile exists
	
	if (!file.isDirectory())  
	{
		try {
				boolean fileCreation = true;
				if (!file.exists())
						fileCreation = file.createNewFile();
				else
				{
					createErrorPopup("Filename already exists");
					System.out.println("filename exists");
					return false;
				}
				
				if (!fileCreation)
				{
					createErrorPopup("Filename already exists");
					System.out.println("filename exists");
					return false;
				}
				
			    OutputStream out = new FileOutputStream(file);
			
			    String selectedFileType = String.valueOf(filetype.getSelectedItem());
			
			    Mapper exporter = MapperFactory.getMapper(comboBoxValues.get(selectedFileType));
			     
			    exporter.write(ModelBuilder.getMetamodel(), ModelBuilder.getModel(), out);
			
			    return true;
		    	  
		     } 
			catch (FileNotFoundException e) {
		  		e.printStackTrace();
		  		
		  		createErrorPopup("There was a problem exporting the selected file.");
		    }
			catch (IOException e) {
				e.printStackTrace();
				createErrorPopup("An error occured while creating the new file");
			}	
		      
		    catch (Exception e) {
			     e.printStackTrace();
			     createErrorPopup("There was a problem transforming the Model.");
		      }
		return false;
	}
	else
	{
		System.out.println("File is no file test" +file.getAbsolutePath());
		return false;
	}
    
  }

  /**
   * Display the popup to the User
   * @param caller the component which called to display the popup
   * @return true if the entire export went fine, otherwise false
   */
  public boolean showPopup(Component caller) {
    JFileChooser saveFile = new JFileChooser();

    JPanel panel1 = (JPanel) saveFile.getComponent(3);
    JPanel panel2 = (JPanel) panel1.getComponent(2);

    JPanel importeType = new JPanel(new FlowLayout());
    JLabel label1 = new JLabel("Select Export File Format:");
    importeType.add(label1);
    filetype = new JComboBox<String>(comboBoxValues.keySet().toArray(new String[0]));
    filetype.setSelectedIndex(0);
    importeType.add(filetype);

    panel2.add(importeType);
    
    saveFile.setSelectedFile(new File("PSS-IF_export.xml"));

    int returnVal = saveFile.showSaveDialog(caller);

    if (returnVal == JFileChooser.APPROVE_OPTION) {

      File file = saveFile.getSelectedFile();

      return exportFile(file);

    }
    else {
      return false;
    }
  }
  
  
  private void createErrorPopup (String text)
  {
	  JPanel errorPanel = new JPanel();
		
      errorPanel.add(new JLabel(text));

      JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
  }
}
