package reqtool.graph;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.tum.pssif.core.metamodel.DataType;

public class IssueResolverPopup {
	private MyNode selectedNode;
	private GraphVisualization gViz;
	private JPanel allPanel;
	private JComboBox<String> attributeList;
	private JComboBox<String> operationList;
	private JTextField valueTextField;
	private Map<String, DataType> attributeNames;
	
	public IssueResolverPopup (MyNode issue, MyNode req, MyNode sol, GraphVisualization gViz){
		this.selectedNode = issue;
		this.gViz = gViz;
	}
	
	public boolean showPopup() {
		JPanel allPanel = createPanel();

		int dialogResult = JOptionPane.showConfirmDialog(null, allPanel, "Resolve this issue", JOptionPane.DEFAULT_OPTION);

		return evalDialog(dialogResult);
	}

	private boolean evalDialog(int dialogResult) {
		// TODO Auto-generated method stub
		return false;
	}

	private JPanel createPanel() {
		allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		int ypos = 0;

		attributeList = new JComboBox<String>(attributeNames.keySet().toArray(new String[] {}));

		operationList = new JComboBox<String>(new String[] {});

		attributeList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				if (cb.getSelectedItem() != null) {
					String attrName = (String) cb.getSelectedItem();

					DataType dataType = attributeNames.get(attrName);

					operationList.removeAllItems();

					
				}
			}
		});

		if (attributeList.getItemCount() > 0)
			attributeList.setSelectedIndex(0);
		if (operationList.getItemCount() > 0)
			operationList.setSelectedIndex(0);

		valueTextField = new JTextField(10);

		c.gridx = 0;
		c.gridy = ypos;
		allPanel.add(new JLabel("Requirement-node"), c);
		c.gridx = 1;
		c.gridy = ypos;
		allPanel.add(new JLabel("Condition"), c);
		c.gridx = 2;
		c.gridy = ypos;
		allPanel.add(new JLabel("solution artifact-node"), c);

		// c.weighty = 1;
		c.weightx = 1;

		c.gridx = 0;
		c.gridy = ypos++;
		allPanel.add(attributeList, c);
		c.gridx = 1;
		c.gridy = ypos;
		allPanel.add(operationList, c);
		c.gridx = 2;
		c.gridy = ypos;
		allPanel.add(valueTextField, c);
		
		c.gridx = 0;
		c.gridy = ypos;
		allPanel.add(attributeList, c);
		c.gridx = 1;
		c.gridy = ypos;
		allPanel.add(operationList, c);
		c.gridx = 2;
		c.gridy = ypos++;
		allPanel.add(valueTextField, c);

		allPanel.setPreferredSize(new Dimension(400, 200));
		allPanel.setMaximumSize(new Dimension(400, 200));
		allPanel.setMinimumSize(new Dimension(400, 200));

		return allPanel;

	}
	
	
	

}
