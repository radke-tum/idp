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

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

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
		//MyEdgeType[] edgePossibilities = ModelBuilder.getEdgeTypes().getAllEdgeTypesArray();
		
		JPanel allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		edgePanel = new JPanel(new GridLayout(0, 1));
		
		LinkedList<MyEdgeType> highlightEdges = graphViz.getFollowEdgeTypes();
		
		TreeMap<String, LinkedList<MyEdgeType>> sortedEdges = sortByEdgeTypeByParentType(ModelBuilder.getEdgeTypes().getAllEdgeTypes());
		
		JTree tmpTree = tree.createTree(sortedEdges, highlightEdges);

		edgePanel.add(tmpTree);
		
		/*int allcounter =0;
		for (MyEdgeType attr : edgePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (highlightNodes!=null && highlightNodes.contains(attr))
			{
				choice.setSelected(true);
				allcounter++;
			}
			else
				choice.setSelected(false);
			edgePanel.add(choice);
		}*/
		
		
		JScrollPane scrollEdges = new JScrollPane(edgePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollEdges.setPreferredSize(new Dimension(200, 400));
		
		/*final JCheckBox selectAllEdges = new JCheckBox("Select all Edge Types");
	    

		selectAllEdges.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
	        if (selectAllEdges.isSelected())
	        {
	          Component[] attr = edgePanel.getComponents();
	          for (Component tmp : attr) {
	            if ((tmp instanceof JCheckBox))
	            {
	              JCheckBox a = (JCheckBox)tmp;
	              
	              a.setSelected(true);
	            }
	          }
	        }
	        else
	        {
	          Component[] attr = edgePanel.getComponents();
	          for (Component tmp : attr) {
	            if ((tmp instanceof JCheckBox))
	            {
	              JCheckBox a = (JCheckBox)tmp;
	              
	              a.setSelected(false);
	            }
	          }
	        }
	      }
	    });
	    
	    // set Select all edges Checkbox to true if all where selected
	    if (allcounter!=0 && allcounter ==highlightNodes.size() )
	    	selectAllEdges.setSelected(true);
		*/
		
		c.gridx = 1;
		c.gridy = 0;
		allPanel.add(new JLabel("Choose Connection Types"),c);

		c.gridx = 1;
		c.gridy = 1;
		allPanel.add(scrollEdges,c);
		
		/*c.gridx = 1;
		c.gridy = 2;
		allPanel.add(selectAllEdges,c);*/
		
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
			
			graphViz.setFollowEdgeTypes(res);
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
