package reqtool.graph;

import graph.model.MyNode;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import reqtool.model.RequirementNode;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;

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
		
		getConstraints().gridx= 0 ;
		getConstraints().gridy = 3;
		addVersionsCount();
		
		getConstraints().gridy = 10;
		getConstraints().insets = new Insets(50, 50, 0, 0);
		getConstraints().gridx = 0 ;
		addSolArtifNodes();
		
		getConstraints().gridy = 10;
		getConstraints().insets = new Insets(50, 50, 0, 0);
		addSpecArtifNodes();
		
		getConstraints().gridy = 10;
		getConstraints().insets = new Insets(50, 50, 0, 0);
		addAuthorNodes();
		
		getConstraints().gridy = 10;
		getConstraints().insets = new Insets(50, 50, 0, 0);
		addPrincNodes();
		
		getConstraints().gridy = 10;
		getConstraints().insets = new Insets(50, 50, 0, 0);
		List<MyNode> tcNodes = addTestCaseNodes();
		
		getConstraints().gridy = 10;
		getConstraints().insets = new Insets(50, 50, 0, 0);
		addIssueNodes(tcNodes);
		
		getConstraints().gridy = 10;
		getConstraints().insets = new Insets(50, 50, 0, 0);
		addChangeEventNodes();
	}
	
	private void addVersionsCount() {
		// TODO Auto-generated method stub
	}
	
	private void addSolArtifNodes() {
		JLabel labelTxt = new JLabel("<html><B> Solution Artifacts </B></html>");
		getConstraints().gridx++;
		getPanel().add(labelTxt, getConstraints());
		int i=0;
		getConstraints().insets = new Insets(0, 50, 0, 0);
		for (MyNode node : RequirementNode.getRequirementSourceNodes(reqNode, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_LOGICAL_SATISFIES, PSSIFCanonicMetamodelCreator.N_SOL_ARTIFACT)) {
			getConstraints().gridy+=i;
			addMultRows(node.getName());
			i++;
		}
	}
	
	private void addSpecArtifNodes() {
		JLabel labelTxt = new JLabel("<html><B> Specification Artifacts </B></html>");
		getConstraints().gridx++;
		getPanel().add(labelTxt, getConstraints());
		int i=0;
		getConstraints().insets = new Insets(0, 50, 0, 0);
		for (MyNode node : RequirementNode.getRequirementSourceNodes(reqNode, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_REFERENTIAL_DEFINES, PSSIFCanonicMetamodelCreator.N_SPEC_ARTIFACT)) {
			getConstraints().gridy+=i;
			addMultRows(node.getName());
			i++;
		}
	}
	
	private void addAuthorNodes() {
		JLabel labelTxt = new JLabel("<html><B> Author </B></html>");
		getConstraints().gridx++;
		getPanel().add(labelTxt, getConstraints());
		getConstraints().insets = new Insets(0, 50, 0, 0);
		int i=0;
		for (MyNode node : RequirementNode.getRequirementSourceNodes(reqNode, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CAUSAL_CREATES, PSSIFCanonicMetamodelCreator.N_AUTHOR)) {
			getConstraints().gridy+=i;
			addMultRows(node.getName());
			i++;
		}
	}
	
	private void addPrincNodes() {
		JLabel labelTxt = new JLabel("<html><B> Principal </B></html>");
		getConstraints().gridx++;
		getPanel().add(labelTxt, getConstraints());
		getConstraints().insets = new Insets(0, 50, 0, 0);
		int i=0;
		for (MyNode node : RequirementNode.getRequirementSourceNodes(reqNode, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CAUSAL_REQUESTS, PSSIFCanonicMetamodelCreator.N_PRINCIPAL)) {
			getConstraints().gridy+=i;
			addMultRows(node.getName());
			i++;
		}
	}
	
	private List<MyNode> addTestCaseNodes() {
		JLabel labelTxt = new JLabel("<html><B> Test case </B></html>");
		getConstraints().gridx++;
		getPanel().add(labelTxt, getConstraints());
		List<MyNode> nodes = new ArrayList<MyNode>();
		getConstraints().insets = new Insets(0, 50, 0, 0);
		int i=0;
		for (MyNode node : RequirementNode.getRequirementRelNodes(reqNode, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON, PSSIFCanonicMetamodelCreator.N_TEST_CASE)) {
			getConstraints().gridy+=i;
			addMultRows(node.getName());
			nodes.add(node);
			i++;
		}
		return nodes;
	}
	
	private void addIssueNodes(List<MyNode> testCases) {
		JLabel labelTxt = new JLabel("<html><B> Issue </B></html>");
		getConstraints().gridx++;
		getPanel().add(labelTxt, getConstraints());
		getConstraints().insets = new Insets(0, 50, 0, 0);
		int i=0;
		for (MyNode testCaseNode: testCases) {
			for (MyNode node : RequirementNode.getRequirementDestNodes(testCaseNode, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_LEADS_TO, PSSIFCanonicMetamodelCreator.N_ISSUE)) {
				getConstraints().gridy+=i;
				addMultRows(node.getName());
				i++;
			}
		}
	}
	
	private void addChangeEventNodes() {
		JLabel labelTxt = new JLabel("<html><B> Change event </B></html>");
		getConstraints().gridx++;
		getPanel().add(labelTxt, getConstraints());
		getConstraints().insets = new Insets(0, 50, 0, 0);
		int i=0;
		for (MyNode node : RequirementNode.getRequirementDestNodes(reqNode, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON, PSSIFCanonicMetamodelCreator.N_CHANGE_EVENT)) {
			getConstraints().gridy+=i;
			addMultRows(node.getName());
			i++;
		}
	}
	
	/*
	private void addNodesView(String labelText, MyNode node) {
		JLabel labelTxt = new JLabel("<html><B> "+ labelText +" </B></html>");
		getConstraints().gridx++;
		getPanel().add(labelTxt, getConstraints());
		for (MyNode node : RequirementNode.getRequirementDestNodes(node, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON, PSSIFCanonicMetamodelCreator.N_CHANGE_EVENT)) {
			getConstraints().gridy+=1;
			addMultRows(node.getName());
		}
	}
	*/
	
	private void addAttrView(String labelText, String valueText) {
		JLabel labelTxt = new JLabel("<html><B>"+labelText+"</B></html>");
		getConstraints().gridx++;
		getConstraints().gridy = 0;
		getConstraints().insets = new Insets(0, 50, 0, 0);
		getPanel().add(labelTxt, getConstraints());
		if (valueText != null) {
			JLabel label = new JLabel(valueText);
			getConstraints().gridy = 1;
			getPanel().add(label, getConstraints());
		}
	}
	
	private void addMultRows(String valueText) {
		if (valueText != null) {
			getConstraints().gridy++;
			JLabel label = new JLabel(valueText);
			getPanel().add(label, getConstraints());
		}
	}
	
	private void addAttributes() {
		String reqId = "";
		try {
			reqId = RequirementNode.getAttributeValue(reqNode, "id").getOne().asString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		addAttrView("Id", reqId);
		
		String reqName = "";
		try {
			reqName  = RequirementNode.getAttributeValue(reqNode, "name").getOne().asString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		addAttrView("Name", reqName);
		
		String reqVersion = "";
		try {
			reqVersion  = RequirementNode.getAttributeValue(reqNode, "version").getOne().asString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		addAttrView("Version", reqVersion);
		
		String reqStatus = "";
		try {
			reqName  = RequirementNode.getAttributeValue(reqNode, "status").getOne().asString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		addAttrView("Status", reqStatus);
		
		String reqAbsLevel = "";
		try {
			reqName  = RequirementNode.getAttributeValue(reqNode, PSSIFCanonicMetamodelCreator.A_REQUIREMENT_ABS_LEVEL).getOne().asString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		addAttrView("Absraction level", reqAbsLevel);
		
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
