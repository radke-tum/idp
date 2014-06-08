package reqtool;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.tum.pssif.transform.MapperFactory;
import model.FileImporter;

public class ReqProjectImporter extends FileImporter {
	private static final String labelText = "Select type for specification artifact:";
	
	private TreeMap<String, String> comboBoxSpecTypes;
	private JComboBox<String> specArtifType;

	public ReqProjectImporter() {
		super();
		
		JPanel panel1 = (JPanel) getFileChooser().getComponent(3);
	    JPanel panel2 = (JPanel) panel1.getComponent(0);

	    comboBoxSpecTypes = new TreeMap<String, String>();

	    comboBoxSpecTypes.put("Umsatzorientierte Funktionsplanung", MapperFactory.UOFP);
	   // comboBoxValues.put("BPMN", MapperFactory.BPMN);
	    comboBoxSpecTypes.put("EPK", MapperFactory.EPK);
	    comboBoxSpecTypes.put("SysML", MapperFactory.SYSML);
	    comboBoxSpecTypes.put("PSS-IF", MapperFactory.PSSIF);
	    comboBoxSpecTypes.put("ReqIf", MapperFactory.REQ_IF);
	    
	    JPanel importeType = new JPanel(new FlowLayout());
	    JLabel label1 = new JLabel(labelText);
	    importeType.add(label1);
	    specArtifType = new JComboBox<String>(comboBoxSpecTypes.keySet().toArray(new String[0]));
	    specArtifType.setSelectedIndex(0);
	    importeType.add(specArtifType);

	    panel2.add(importeType);
	}

	@Override
	public boolean showPopup(Component caller) {
		try {
			super.showPopup(caller);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return createSpecNode();
	}
	
	private boolean createSpecNode() {
		
		return true;
	}

	/**
	 * Get the selected Specification Artifact Type Format
	 * @return the String which the Mapper Factory needs, to create the correct Mapper
	 */
	public String getSelectedSpecArtifType() {
		 String selectedFileType = String.valueOf(specArtifType.getSelectedItem());
		 return comboBoxSpecTypes.get(selectedFileType);
	}

}
