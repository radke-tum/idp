package reqtool.graph;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import reqtool.TestCaseCreator;

public class TestCaseCreatorPopup {
	private JPanel NodePanel;
	private JTextField testCaseNameTextField;
	private LinkedList<MyNode> solutionArtifacts;
	private MyNode requirementNode;
	private GraphVisualization gViz;

	public TestCaseCreatorPopup(MyNode requirementNode, GraphVisualization gViz) {
		this.requirementNode = requirementNode;
		this.solutionArtifacts = TestCaseCreator.getRequirementSatisfyNodes(requirementNode);
		this.gViz = gViz;
	}

	/**
	 * Show the Popup to the user
	 * 
	 * @return
	 */
	public boolean showPopup() {
		JPanel allPanel = createPanel();

		int dialogResult = JOptionPane.showConfirmDialog(null, allPanel, "Create a new Test Case", JOptionPane.DEFAULT_OPTION);

		return evalDialog(dialogResult);
	}

	private boolean evalDialog(int dialogResult) {
		LinkedList<MyNode> selectedNodes = new LinkedList<MyNode>();
		
		List<Integer> indexes = new ArrayList<Integer>();
		Component[] nodes = NodePanel.getComponents();
		for (int i=0;i<nodes.length;i++) {
			Component tmp = nodes[i];
			if ((tmp instanceof JCheckBox)) {
				JCheckBox a = (JCheckBox) tmp;
				if (a.isSelected()) {
					indexes.add(i);
				}
			}
		}
		
		for(int index:indexes) {
			selectedNodes.add(solutionArtifacts.get(index));
		}

		TestCaseCreator.createTestCase(gViz, requirementNode, selectedNodes);
		return true;
	}

	private JPanel createPanel() {
		JPanel allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		NodePanel = new JPanel(new GridLayout(0, 1));

		for (MyNode node : solutionArtifacts) {
			JCheckBox choice = new JCheckBox(node.getName());

			choice.setSelected(false);
			NodePanel.add(choice);
		}

		JScrollPane scrollNodes = new JScrollPane(NodePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollNodes.setPreferredSize(new Dimension(200, 400));

		final JCheckBox selectAllNodes = new JCheckBox("Select all solution artifacts");

		selectAllNodes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectAllNodes.isSelected()) {
					Component[] node = NodePanel.getComponents();
					for (Component tmp : node) {
						if ((tmp instanceof JCheckBox)) {
							JCheckBox a = (JCheckBox) tmp;

							a.setSelected(true);
						}
					}
				} else {
					Component[] attr = NodePanel.getComponents();
					for (Component tmp : attr) {
						if ((tmp instanceof JCheckBox)) {
							JCheckBox a = (JCheckBox) tmp;

							a.setSelected(false);
						}
					}
				}
			}
		});

		selectAllNodes.setSelected(false);

		testCaseNameTextField = new JTextField(10);

		int ypos = 0;

		c.gridx = 0;
		c.gridy = ypos;
		allPanel.add(new JLabel("Choose Node Types"), c);
		c.gridy = ypos++;
		allPanel.add(scrollNodes, c);
		c.gridy = ypos++;
		allPanel.add(selectAllNodes, c);
		c.gridy = ypos++;

		c.weighty = 1;

		c.gridx = 0;
		c.gridy = ypos;
		allPanel.add(new JLabel("Graph View name "), c);
		c.gridx = 1;
		c.gridy = ypos++;
		allPanel.add(testCaseNameTextField, c);

		allPanel.setPreferredSize(new Dimension(400, 500));
		allPanel.setMaximumSize(new Dimension(400, 500));
		allPanel.setMinimumSize(new Dimension(400, 500));

		return allPanel;
	}

}
