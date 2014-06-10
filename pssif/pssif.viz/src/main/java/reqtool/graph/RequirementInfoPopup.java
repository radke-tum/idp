package reqtool.graph;

import graph.model.MyNode;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import reqtool.model.RequirementNode;

public class RequirementInfoPopup {
	private MyNode reqNode;
	private JPanel allPanel;
	private GridBagConstraints constr;
	
	public RequirementInfoPopup(MyNode reqNode) {
		this.reqNode = reqNode;
	}
	
	public boolean showPopup() {
		createPanel();
		
		int dialogResult = JOptionPane.showConfirmDialog(null, allPanel, "Requirement informations", JOptionPane.DEFAULT_OPTION);

		return evalDialog(dialogResult);
	}
	
	
	
	private boolean evalDialog(int dialogResult) {
		if (dialogResult == 0) {
			return true;
		}
		return false;
	}
	
	private void createPanel() {
		getConstraints().gridx = 0;
		getConstraints().gridx = 0;
		
		addAttributes();
		addVersionsCount();
		addNodes();
	}
	
	private void addNodes() {
		for (MyNode node : RequirementNode.getRequirementSourceNodes(reqNode, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_LOGICAL_SATISFIES)) {
			addRow1Col(node.getName());
		}
	}

	private void addVersionsCount() {
		// TODO Auto-generated method stub
	}

	private void addAttributes() {
		String reqId = "";
		try {
			reqId = RequirementNode.getAttributeValue(reqNode, "id").getOne().asString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		addRow2Col("ReqID", reqId);
		
		String reqName = "";
		try {
			reqName  = RequirementNode.getAttributeValue(reqNode, "name").getOne().asString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		addRow2Col("ReqName", reqName);
		
		String reqVersion = "";
		try {
			reqVersion  = RequirementNode.getAttributeValue(reqNode, "version").getOne().asString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		addRow2Col("Version", reqVersion);
		
		String reqStatus = "";
		try {
			reqName  = RequirementNode.getAttributeValue(reqNode, "status").getOne().asString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		addRow2Col("ReqStatus", reqStatus);
		
	}
	
	private void addRow2Col(String labelText, String valueText) {
		JLabel labelTxt = new JLabel(labelText);
		getConstraints().gridx++;
		getConstraints().gridy++;
		getPanel().add(labelTxt, getConstraints());
		if (valueText != null) {
			JLabel label = new JLabel(valueText);
			getConstraints().gridx++;
			getPanel().add(label, getConstraints());
		}
	}
	
	private void addRow1Col(String valueText) {
		if (valueText != null) {
			getConstraints().gridx++;
			getConstraints().gridy++;
			JLabel label = new JLabel(valueText);
			getPanel().add(label, getConstraints());
		}
	}

	private JPanel getPanel() {
		if (allPanel == null) {
			allPanel = new JPanel(new GridBagLayout());
			/*
			mJTSpecArtifName = new JTextField(15);
	    	mJCSpecArtifTypes =  new JComboBox<MyNodeType>(getSpecArtifTypes());
	    	
	    	JLabel jLnodeTypes = new JLabel("Choose Specification node Type:");
			JLabel jLnodeName = new JLabel("Specification Node name:");
			
			mJCheckBoxImportFile = new JCheckBox("Contains file");
			
			newReqCheckBox = new JCheckBox("Create Requirement from SpecArtifact");
			
			c.gridx = 0;
			c.gridy = 0;
			allPanel.add(jLnodeName, c);
			
			c.gridx = 1;
			c.gridy = 0;
			c.weightx = 0.5;
			c.insets = new Insets(0,10,0,0);
			allPanel.add(mJTSpecArtifName, c);
			
			c.gridx = 0;
			c.gridy = 1;
			allPanel.add(jLnodeTypes, c);
			
			c.gridx = 1;
			c.gridy = 1;
			c.weightx = 0.5;
			c.insets = new Insets(0,10,0,0);
			allPanel.add(mJCSpecArtifTypes, c);
			
			c.gridx = 0;
			c.gridy = 2;
			allPanel.add(mJCheckBoxImportFile, c);
			
			c.gridx = 0;
			c.gridy = 3;
			allPanel.add(newReqCheckBox, c);
			*/
			
		}		
		return allPanel;
	}
	
	public GridBagConstraints getConstraints() {
		if (constr == null) {
			constr = new GridBagConstraints();
		}
		return constr;
	}
	
	public void resetPopup() {
		reqNode = null;
		constr = null;
		allPanel = null;
	}
	

}
