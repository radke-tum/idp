package reqtool.graph;

import graph.model.MyEdgeType;
import graph.model.MyNode;
import gui.graph.GraphVisualization;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.spi.Resolver;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.ModelBuilder;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Model;
import reqtool.IssueResolver;
import reqtool.TestCaseCreator;
import reqtool.VersionManager;

public class IssueResolverPopup {
	private static final MyEdgeType LEADS_TO =  ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_LEADS_TO);
	private static final MyEdgeType REFERENCIAL = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_REFERENTIAL);
	
	private JPanel allPanel;
	private JPanel reqPanel;
	private JPanel solPanel;
	
	private MyNode selectedNode;
	private LinkedList<MyNode> reqs;
	private LinkedList<MyNode> sols;
	
	public IssueResolverPopup (MyNode issue){
		selectedNode = issue;
		IssueResolver resolver = new IssueResolver(issue);
		reqs = resolver.getRequirement();
		sols = resolver.getSolArtifacts();
	}
	
	public boolean showPopup() {
		JPanel allPanel = createPanel();

		int dialogResult = JOptionPane.showConfirmDialog(null, allPanel, "Resolve this issue", JOptionPane.DEFAULT_OPTION);

		return evalDialog(dialogResult);
	}

	private boolean evalDialog(int dialogResult) {
		if (dialogResult == 0) {
			List<Integer> indexes = new ArrayList<Integer>();
			Component[] reqNodes = reqPanel.getComponents();
			for (int i=0;i<reqNodes.length;i++) {
				Component tmp = reqNodes[i];
				if ((tmp instanceof JCheckBox)) {
					JCheckBox a = (JCheckBox) tmp;
					if (a.isSelected()) {
						indexes.add(i);
					}
				}
			}
			
			for(int index:indexes) {
				VersionManagerPopup.showPopup(reqs.get(index));
			}
			
			List<Integer> selectedSolNodes = new ArrayList<Integer>();
			Component[] solNodes = solPanel.getComponents();
			for (int i=0;i<solNodes.length;i++) {
				Component tmp = solNodes[i];
				if ((tmp instanceof JCheckBox)) {
					JCheckBox a = (JCheckBox) tmp;
					if (a.isSelected()) {
						selectedSolNodes.add(i);
					}
				}
			}
			
			MyNode changeProposal = ModelBuilder.addNewNodeFromGUI("ChangeProposal for Issue"+selectedNode.getName() , ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_CHANGE_PROPOSAL));
			ModelBuilder.addNewEdgeGUI(selectedNode, changeProposal, LEADS_TO, true);
			
			MyNode decision = ModelBuilder.addNewNodeFromGUI("Decision for ChangeProposal"+selectedNode.getName() , ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_DECISION));
			ModelBuilder.addNewEdgeGUI(changeProposal, decision, LEADS_TO, true);
			
			MyNode changeEvent = ModelBuilder.addNewNodeFromGUI("ChangeEvent"+selectedNode.getName(),  ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_CHANGE_EVENT));
			ModelBuilder.addNewEdgeGUI(selectedNode, changeEvent,REFERENCIAL, true);
			ModelBuilder.addNewEdgeGUI(changeProposal, changeEvent, REFERENCIAL, true);
			ModelBuilder.addNewEdgeGUI(decision, changeEvent,REFERENCIAL, true);
			
			for (int index:selectedSolNodes) {
				if (VersionManagerPopup.showPopup(sols.get(index))) {
					
					VersionManager vm = new VersionManager(sols.get(index));
					MyNode newVersionNode = vm.getMaxVersion();
					
					ModelBuilder.addNewEdgeGUI(newVersionNode, changeEvent, ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON), true);
					/*
					 * TODO Also a "Based On" edge from ChangeEvent if Requirement node is selected
					 */
					//gViz.updateGraph();
				}
			}
	
			return true;
		}
		return false;
	}

	private JPanel createPanel() {
		allPanel = new JPanel(new GridBagLayout());

		reqPanel = new JPanel(new GridLayout(0, 1));
		solPanel = new JPanel(new GridLayout(0, 1));

		for (MyNode node : reqs) {
			JCheckBox choice = new JCheckBox(node.getName());
			choice.setSelected(false);
			reqPanel.add(choice);
		}
		
		for (MyNode node : sols) {
			JCheckBox choice = new JCheckBox(node.getName());
			choice.setSelected(false);
			solPanel.add(choice);
		}

		JScrollPane reqScrollNodes = new JScrollPane(reqPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		reqScrollNodes.setPreferredSize(new Dimension(170, 100));
		
		JScrollPane solScrollNodes = new JScrollPane(solPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		solScrollNodes.setPreferredSize(new Dimension(170, 100));

		GridBagConstraints c = new GridBagConstraints();
		int ypos = 0;

		c.gridx = 0;
		c.gridy = ypos;
		allPanel.add(new JLabel("Create new versions for:"), c);
		
		c.gridx = 0;
		c.gridy = ypos+40;
		allPanel.add(new JLabel("Requirement-node"), c);
		c.gridx = 0;
		c.gridy = ypos+60;
		allPanel.add(reqScrollNodes, c);
		
		
		c.gridx = 190;
		c.gridy = ypos+40;
		allPanel.add(new JLabel("Solution artifact-node"), c);
		c.gridx = 190;
		c.gridy = ypos+60;
		allPanel.add(solScrollNodes, c);

		//c.weighty = 1;
		//c.weightx = 1;
		
		allPanel.setPreferredSize(new Dimension(400, 150));
		allPanel.setMaximumSize(new Dimension(400, 200));
		allPanel.setMinimumSize(new Dimension(400, 200));

		return allPanel;
	}
	

}
