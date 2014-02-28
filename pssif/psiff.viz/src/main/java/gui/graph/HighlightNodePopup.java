package gui.graph;

import graph.model.MyEdgeType;
import gui.checkboxtree.CheckBoxTree;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.ModelBuilder;

/**
 * Allows the user to define the Edge Types which should be followed during the Highlight analysis.
 * @author Luc
 *
 */
public class HighlightNodePopup extends MyPopup{
	
	private JPanel popupPanel;
	private JPanel edgePanel;
	private GraphVisualization graphViz;
	private CheckBoxTree tree;
	private  JSpinner spinner;
	
	public HighlightNodePopup(GraphVisualization graphViz)
	{
		this.graphViz = graphViz;
		this.tree = new CheckBoxTree();
	}
	/**
	 * Create the Panel(GUI) of the Popup 
	 * @return a panel with all the components
	 */
	private JPanel createPanel()
	{
		JPanel allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		edgePanel = new JPanel(new GridLayout(0, 1));
		
		LinkedList<MyEdgeType> highlightEdges = graphViz.getFollowEdgeTypes();
		
		TreeMap<String, LinkedList<MyEdgeType>> sortedEdges = sortByEdgeTypeByParentType(ModelBuilder.getEdgeTypes().getAllEdgeTypes());
		
		JTree tmpTree = tree.createTree(sortedEdges, highlightEdges);

		edgePanel.add(tmpTree);
		
		
		JScrollPane scrollEdges = new JScrollPane(edgePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollEdges.setPreferredSize(new Dimension(200, 400));
		
		int currentDepth = 1;
	    SpinnerModel depthModel = new SpinnerNumberModel(currentDepth, //initial value
	                                       1, //min
	                                       currentDepth + 100, //max
	                                       1);
	    spinner = new JSpinner(depthModel);
	    
	    /*spinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Object value = spinner.getValue();
				if (value instanceof Integer)
				{
					int depth = (Integer) value;
					grgraph.setFollowEdgeTypes(depth);
				}
				
			}
		});*/
		
		c.gridx = 1;
		c.gridy = 0;
		allPanel.add(new JLabel("Choose Edge Types"),c);

		c.gridx = 1;
		c.gridy = 1;
		allPanel.add(scrollEdges,c);
		
		c.gridx = 1;
		c.gridy = 2;
		allPanel.add(new JLabel("Search Depth"),c);
		
		c.gridx = 1;
		c.gridy = 3;
		allPanel.add(spinner,c);
		
		allPanel.setPreferredSize(new Dimension(200,500));
		allPanel.setMaximumSize(new Dimension(200,500));
		allPanel.setMinimumSize(new Dimension(200,500));

		return allPanel;
	}
	/**
	 * Evaluate the Popup after the users input
	 * @param dialogResult the result of the users interaction with the popup gui
	 */
	private void evalDialog (int dialogResult)
	{
		if (dialogResult==0)
    	{
			LinkedList<MyEdgeType> res = tree.evalTree();
			
			int depth = (Integer) spinner.getValue();
			
			graphViz.setFollowEdgeTypes(res,depth);
    	}	
	}
	
	/**
	 * Show the Popup to the user
	 * @return
	 */
	public void showPopup()
	{
		popupPanel = createPanel();
		
		int dialogResult = JOptionPane.showConfirmDialog(null, popupPanel, "Choose the highlighted Edge types", JOptionPane.DEFAULT_OPTION);
		
		evalDialog(dialogResult);
	}
}
