package reqtool.graph;

import graph.model.MyNodeType;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RequirementFromSpecPopup {
	
	private JPanel allPanel;
	private JTextField reqName;
	private JTextField authorName;
	private JTextField principalName;
	private JTextField abstractionLevelName;
	private JCheckBox hasAuthorCheckBox;
	private JCheckBox hasPrincipalCheckBox;
	private JCheckBox hasAbsLevelCheckBox;
	
	
	public boolean showPopup() {
		JPanel allPanel = createPanel();
		
		int dialogResult = JOptionPane.showConfirmDialog(null, allPanel, "Create Requirement from specification artifact", JOptionPane.DEFAULT_OPTION);

		return evalDialog(dialogResult);
	}
	
	private boolean evalDialog(int dialogResult) {
		if (dialogResult == 0) {
			return true;
		}
		return false;
	}
	
	private JPanel createPanel() {
		allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		reqName = new JTextField(15);
		authorName = new JTextField(15);
		principalName = new JTextField(15);
		abstractionLevelName = new JTextField(15);
    	//mJCSpecArtifTypes =  new JComboBox<MyNodeType>(getSpecArtifTypes());
    	
    	//JLabel jLnodeTypes = new JLabel("Choose Specification node Type:");
		JLabel jLnodeName = new JLabel("Requirement name:");
		
		hasAuthorCheckBox = new JCheckBox("Add Author");
		hasPrincipalCheckBox = new JCheckBox("Add Principal");
		hasAbsLevelCheckBox = new JCheckBox("Add Abstraction Level");
		
		c.gridx = 0;
		c.gridy = 0;
		allPanel.add(jLnodeName, c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.5;
		c.insets = new Insets(0,10,0,0);
		allPanel.add(reqName, c);
		
		c.gridx = 0;
		c.gridy = 1;
		allPanel.add(hasAuthorCheckBox, c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.5;
		c.insets = new Insets(0,10,0,0);
		allPanel.add(authorName, c);
		
		c.gridx = 0;
		c.gridy = 2;
		allPanel.add(hasPrincipalCheckBox, c);
		
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 0.5;
		c.insets = new Insets(0,10,0,0);
		allPanel.add(principalName, c);
		
		c.gridx = 0;
		c.gridy = 3;
		allPanel.add(hasAbsLevelCheckBox, c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 0.5;
		c.insets = new Insets(0,10,0,0);
		allPanel.add(abstractionLevelName, c);
		
		
		return allPanel;
	}
	
	public String getReqName() {
		if (reqName != null) {
			return reqName.getText();
		}
		return "";
	}
	
	public String getAuthorName() {
		if (authorName != null) {
			return authorName.getText();
		}
		return "";
	}
	
	public String getPrincipalName() {
		if (principalName != null) {
			return principalName.getText();
		}
		return "";
	}
	
	public String getAbstractionLevelName() {
		if (abstractionLevelName != null) {
			return abstractionLevelName.getText();
		}
		return "";
		
	}
	
	public boolean selectedAuthorCheckBox() {
		if ( hasAuthorCheckBox != null ) {
			return hasAuthorCheckBox.isSelected();
		}
		return false;
	}
	
	public boolean selectedPrincipalCheckBox() {
		if ( hasPrincipalCheckBox != null ) {
			return hasPrincipalCheckBox.isSelected();
		}
		return false;
	}
	
	public boolean selectedAbsLevelCheckBox() {
		if ( hasAbsLevelCheckBox != null ) {
			return hasAbsLevelCheckBox.isSelected();
		}
		return false;
	}
}


