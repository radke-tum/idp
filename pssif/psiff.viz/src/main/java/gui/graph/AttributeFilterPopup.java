package gui.graph;

import graph.model2.MyEdge2;
import graph.model2.MyEdgeType;
import graph.model2.MyNode2;
import graph.model2.MyNodeType;
import graph.operations.AttributeOperations;
import graph.operations.GraphViewContainer;
import graph.operations.NodeAttributeFilter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import edu.uci.ics.jung.graph.Graph;
import model.ModelBuilder;

public class AttributeFilterPopup {
	
	private MyNodeType[] nodePossibilities;
	private JPanel allPanel;
	private JComboBox<String> attributeList;
	private JComboBox<String> operationList;
	private JTextField valueTextField;
	
	private HashMap<String, DataType> attributeNames;
	private Graph<MyNode2, MyEdge2> graph;
	
	public void showPopup(Graph<MyNode2, MyEdge2> graph)
	{
		this.graph = graph;
		
		JPanel panel = createPanel();
		
		int dialogResult = JOptionPane.showConfirmDialog(null, panel, "Filter by Attribute", JOptionPane.DEFAULT_OPTION);
		
		evalDialog(dialogResult);
	}
	
	private String[] getPossibleOperations(DataType type)
	{
		if (type.equals(PrimitiveDataType.BOOLEAN) || type.equals(PrimitiveDataType.STRING))
		{
			return new String[]{AttributeOperations.EQUAL.toString(),AttributeOperations.NOT_EQUAL.toString()};
		}
		else
		{
			AttributeOperations[] tmp = AttributeOperations.values();
			
			String[] res = new String[tmp.length];
			
			int i=0;
			for (AttributeOperations ao :tmp)
			{
				res[i] = ao.getName();
				i++;
						
			}
			return res;
		}
	}
	
	private JPanel createPanel()
	{
		nodePossibilities = ModelBuilder.getNodeTypes().getAllNodeTypesArray();
		
		allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
			
		attributeNames = new HashMap<String, DataType>();
		
		// get all the attributes and their type
		for (MyNodeType type : nodePossibilities)
		{
			LinkedList<Attribute> temp = new LinkedList<Attribute>(type.getType().getAttributes());
			
			for (Attribute attr :temp)
			{
				attributeNames.put(attr.getName(), attr.getType());
			}
			
		}
		
		// holds all the Attributes
		attributeList = new JComboBox<String>(attributeNames.keySet().toArray(new String[]{}));
		
		operationList = new JComboBox<String>(new String[]{});
		
		attributeList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 JComboBox<String> cb = (JComboBox<String>)e.getSource();
			     String attrName = (String)cb.getSelectedItem();
			     
			     DataType dataType = attributeNames.get(attrName);
			     
			     operationList.removeAllItems();
			     
			     for (String s : getPossibleOperations(dataType))
			     {
			    	 operationList.addItem(s);
			     }
			    
			}
		});
		
		if (attributeList.getItemCount()>0)
			attributeList.setSelectedIndex(0);
		
	    valueTextField = new JTextField(10);
	    
	    int ypos =0;
	    
		c.gridx = 0;
		c.gridy = ypos;
		allPanel.add(new JLabel("Choose an Attribute"),c);
		c.gridx = 1;
		c.gridy = ypos;
		allPanel.add(new JLabel("Choose an Operation"),c);
		c.gridx = 2;
		c.gridy = ypos++;
		allPanel.add(new JLabel("Enter a Value"),c);
		
		c.weighty = 1;
		
	    c.gridx = 0;
	    c.gridy = ypos;
	    allPanel.add(attributeList, c);
	    c.gridx = 1;
	    c.gridy = ypos;
	    allPanel.add(operationList, c);
	    c.gridx = 2;
	    c.gridy = ypos++;
	    allPanel.add(valueTextField, c);
	    
		
		allPanel.setPreferredSize(new Dimension(400,500));
		allPanel.setMaximumSize(new Dimension(400,500));
		allPanel.setMinimumSize(new Dimension(400,500));

		return allPanel;
	}
	
	private void evalDialog (int dialogResult)
	{
		if (dialogResult==0)
    	{
    		String selectedAttribute = (String)attributeList.getSelectedItem();
    		String selectedOperation = (String)operationList.getSelectedItem();
    		String refValue = valueTextField.getText();
    		
			if (refValue.length()==0)
			{
        		// not enough information
        		JPanel errorPanel = new JPanel();
        		
        		errorPanel.add(new JLabel("No name entered or no edge and node types selected"));
        		
        		JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				// all information is available
				AttributeOperations op = AttributeOperations.getValueOf(selectedOperation);
				
				try
				{
					NodeAttributeFilter.filter(graph, selectedAttribute, op, refValue);
				}
				catch (Exception e)
				{
					JPanel errorPanel = new JPanel();
	        		
	        		errorPanel.add(new JLabel("There was a problem converting the entered value into the attribute data type"));
	        		
	        		JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
				}
				
				
			}
    	}
	}
}
