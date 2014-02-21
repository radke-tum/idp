package graph.listener;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

import model.ModelBuilder;

import org.apache.commons.collections15.map.HashedMap;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.FRLayout ;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;

public class MyPopupGraphMousePlugin extends AbstractPopupGraphMousePlugin implements MouseListener {

	    public MyPopupGraphMousePlugin() {
	        this(MouseEvent.BUTTON3_MASK);
	    }
	    public MyPopupGraphMousePlugin(int modifiers) {
	        super(modifiers);
	    }
	    
	    
	    protected void handlePopup(MouseEvent e) {
	    /*    final VisualizationViewer<MyNode2,MyEdge2> vv = (VisualizationViewer<MyNode2,MyEdge2>)e.getSource();
	        Point2D p = e.getPoint();

	        GraphElementAccessor<MyNode2,MyEdge2> pickSupport = vv.getPickSupport();
	        if(pickSupport != null) {
	           // System.out.println("POPUP MOUSE is not NULL!");
	            final MyNode2 node = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
	            if(node != null) {
	                JPopupMenu popup = new JPopupMenu();
	                	         
	                JMenu submenu = new JMenu("Add Edge");

	                final Layout<MyNode2, MyEdge2> l = vv.getGraphLayout();
                	
                	final Graph<MyNode2, MyEdge2> g = l.getGraph();
                	
                	LinkedList<MyNode2> col = new LinkedList<MyNode2>();
                	col.addAll(g.getVertices());
                	
                	col.remove(node);
                	
                	
                	for (final MyNode2 cur : col)
                	{
                		JMenuItem menuItem = new JMenuItem("To : "+cur.getName());
                		
                		menuItem.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								
								MyEdgeType[] possibilities = ModelBuilder.getEdgeTypes().getAllEdgeTypesArray();

								MyEdgeType res = (MyEdgeType)JOptionPane.showInputDialog(
								                    vv,"",
								                    "Choose Edge Type",
								                    JOptionPane.PLAIN_MESSAGE,
								                    null,
								                    possibilities,
								                    "implements");
								
								if (res!=null)
								{
									MyEdge2 newEdge = new MyEdge2(res, node, cur);
									g.addEdge(newEdge, node, cur ,EdgeType.DIRECTED);
									
									
									
								//	Layout<MyNode, MyEdge> l = new FRLayout   <MyNode, MyEdge>(g);
			                    	
			                    	//vv.setGraphLayout(l);
			                    	vv.repaint();
								}	
							} 
						});
                		
    	                submenu.add(menuItem);
                	}
	                
     
	                popup.add(submenu);
	                
	                ////////////////////////////////////////////////////////
	                  
	                popup.add(new AbstractAction("Delete Node"){
	                    public void actionPerformed(ActionEvent e) {
	                    	Layout<MyNode2, MyEdge2> l = vv.getGraphLayout();
	                      	
	                      	Graph<MyNode2, MyEdge2> g = l.getGraph();
	                    	
	                    	
	                    	Collection<MyEdge2> col = g.getIncidentEdges(node);
	                    	
	                    	for (MyEdge2 edge :col)
	                    	{
	                    		g.removeEdge(edge);
	                    	}
	                    	
	                    	g.removeVertex(node);
	                    	
	                    	//l.setGraph(g);
	                    	//vv.setGraphLayout(l);
	                    	
	                        vv.repaint();
	                    }
	                });
	                
	                
	                /////////////////////////////////////////////////////////////
	                
	                JMenu submenu2 = new JMenu("Merge Node ");

	                //l = vv.getGraphLayout();
                	
                	//final Graph<MyNode, MyEdge> graph = l.getGraph();
                	
                	col = new LinkedList<MyNode2>();
                	col.addAll(g.getVertices());
                	
                	col.remove(node);
                	
                	
                	for (final MyNode2 cur : col)
                	{
                		JMenuItem menuItem = new JMenuItem("with: "+cur.getName());
                		
                		menuItem.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								
								int dialogResult =0;
								
								//name
								String oldNodeName = node.getName();
								String newNodeName = cur.getName();
								
								JRadioButton  oldname = new JRadioButton (node.getName());
								oldname.setSelected(true);
								JRadioButton  newname = new JRadioButton (cur.getName());
								
								ButtonGroup group = new ButtonGroup();
								group.add(oldname);
								group.add(newname);
								
								JComponent[] inputs = new JComponent[] {
										oldname,
										newname
	                        	};
								dialogResult = JOptionPane.showConfirmDialog(null, inputs, "Choose the Node name", JOptionPane.OK_CANCEL_OPTION);
								
								if (dialogResult==0)
								{
									if (newname.isSelected())
										node.setRealName(cur.getRealName());
									
									//attributes
									List<String> oldattr = node.getAttributes();
									List<String> newattr = cur.getAttributes();
									
									JPanel allPanel = new JPanel(new GridLayout(0, 2));
									
									JPanel checkPanel = new JPanel(new GridLayout(0, 1));
									
									for (String attr : oldattr)
									{
										JCheckBox choice = new JCheckBox(attr);
										choice.setSelected(true);
										checkPanel.add(choice);
									}
									
									JPanel checkPanel2 = new JPanel(new GridLayout(0, 1));
									
									for (String attr : newattr)
									{
										JCheckBox choice = new JCheckBox(attr);
										choice.setSelected(false);
										checkPanel2.add(choice);
									}
									
									allPanel.add(new JLabel(oldNodeName));
									allPanel.add(new JLabel(newNodeName));
									allPanel.add(checkPanel);
									allPanel.add(checkPanel2);
									
									inputs = new JComponent[] {
											allPanel
		                        	};
									dialogResult = JOptionPane.showConfirmDialog(null, inputs, "Choose the attributes", JOptionPane.OK_CANCEL_OPTION);
		                        	
		                        	if (dialogResult==0)
		                        	{
			                        	List<String> selected = new LinkedList<String>();
			                        	Component[] attr = checkPanel.getComponents();
			                        	
			                        	for (Component tmp :attr)
			                        	{
			                        		if ((tmp instanceof JCheckBox))
			                        		{
			                        			JCheckBox a = (JCheckBox) tmp;
			                        			
			                        			 if (a.isSelected())
			                        				 selected.add(a.getText());
			                        		}
			                        			
			                        	}
			                        	
			                        	attr = checkPanel2.getComponents();
			                        	
			                        	for (Component tmp :attr)
			                        	{
			                        		if ((tmp instanceof JCheckBox))
			                        		{
			                        			JCheckBox a = (JCheckBox) tmp;
			                        			
			                        			 if (a.isSelected())
			                        				 selected.add(a.getText());
			                        		}
			                        			
			                        	}
										
			                        	node.setAttributes(selected);
										
										//edges 
			                        	
			                        	//old Node
			                        	
			                        	
			                        	
			                        	HashMap<String, InfoContainer> oldmapping = new HashMap<String,InfoContainer>();
			                        	
										Collection<MyEdge> oldOutgoing_edges = g.getOutEdges(node);
										Collection<MyEdge> oldIncoming_edges = g.getInEdges(node);
										
										checkPanel = new JPanel(new GridLayout(0, 2));
										checkPanel2 = new JPanel(new GridLayout(0, 2));
										
										checkPanel.add(new JLabel("Outgoing Edges"));
										checkPanel.add(new JLabel("InComing Edges"));
										
										JPanel oldOutgoingPanel = new JPanel(new GridLayout(0, 1));
										JPanel oldIncomingPanel = new JPanel(new GridLayout(0, 1));
										
										for (MyEdge2 edge: oldOutgoing_edges)
										{
											if (g.getDest(edge)!=cur)
											{
												String s = edge.getConnectionType().getName()+" To : "+g.getDest(edge).getName();
												oldmapping.put(s, new InfoContainer(edge, node, g.getDest(edge)));
												JCheckBox choice = new JCheckBox(s);
												choice.setSelected(true);
												oldOutgoingPanel.add(choice);
											}
										}
										
										for (MyEdge2 edge: oldIncoming_edges)
										{
											if (g.getSource(edge)!=cur)
											{
												String s = edge.getConnectionType().getName()+" From : "+g.getSource(edge).getName();
												JCheckBox choice = new JCheckBox(s);
												oldmapping.put(s, new InfoContainer(edge, g.getSource(edge) , node));
												choice.setSelected(true);
												oldIncomingPanel.add(choice);
											}
										}
										
										checkPanel.add(oldOutgoingPanel);
										checkPanel.add(oldIncomingPanel);
										
										
										//New Node
										HashMap<String, InfoContainer> newmapping = new HashMap<String,InfoContainer>();
										Collection<MyEdge2> newOutgoing_edges = g.getOutEdges(cur);
										Collection<MyEdge2> newIncoming_edges = g.getInEdges(cur);
										
										checkPanel2.add(new JLabel("Outgoing Edges"));
										checkPanel2.add(new JLabel("InComing Edges"));
										
										JPanel newOutgoingPanel = new JPanel(new GridLayout(0, 1));
										JPanel newIncomingPanel = new JPanel(new GridLayout(0, 1));
										
										for (MyEdge2 edge: newOutgoing_edges)
										{
											if (g.getDest(edge)!=node)
											{
												String s = edge.getConnectionType().getName()+" To : "+g.getDest(edge).getName();
												JCheckBox choice = new JCheckBox(s);
												newmapping.put(s, new InfoContainer(edge, cur , g.getDest(edge)));
												choice.setSelected(false);
												newOutgoingPanel.add(choice);
											}
										}
										
										for (MyEdge2 edge: newIncoming_edges)
										{
											if (g.getSource(edge)!=node)
											{
												String s = edge.getConnectionType().getName()+" From : "+g.getSource(edge).getName();
												JCheckBox choice = new JCheckBox(s);
												newmapping.put(s, new InfoContainer(edge, g.getSource(edge) , cur));
												choice.setSelected(false);
												newIncomingPanel.add(choice);
											}
										}
										
										checkPanel2.add(newOutgoingPanel);
										checkPanel2.add(newIncomingPanel);
										
										
										///
										allPanel.removeAll();
										allPanel.add(new JLabel(oldNodeName));
										allPanel.add(new JLabel(newNodeName));
										allPanel.add(checkPanel);
										allPanel.add(checkPanel2);
										
										inputs = new JComponent[] {
												allPanel
			                        	};
										dialogResult = JOptionPane.showConfirmDialog(null, inputs, "Choose the connections", JOptionPane.OK_CANCEL_OPTION);
										
										if (dialogResult==0)
										{
				                        	//connection eval
				                        	//old Node
				                        	Component[] connections = oldOutgoingPanel.getComponents();
				                        	
				                        	for (Component tmp :connections)
				                        	{
				                        		if ((tmp instanceof JCheckBox))
				                        		{
				                        			JCheckBox a = (JCheckBox) tmp;
				                        			
				                        			 if (a.isSelected())
				                        			 {
				                        				 // ok edge can stay
				                        			 }
				                        			 else
				                        			 {
				                        				 InfoContainer info = oldmapping.get(a.getText());
				                        				 g.removeEdge(info.getEdge());
				                        			 }
				                        		}
				                        			
				                        	}
				                        	
				                        	connections = oldIncomingPanel.getComponents();
				                        	
				                        	for (Component tmp :connections)
				                        	{
				                        		if ((tmp instanceof JCheckBox))
				                        		{
				                        			JCheckBox a = (JCheckBox) tmp;
				                        			
				                        			 if (a.isSelected())
				                        			 {
				                        				 // ok edge can stay
				                        			 }
				                        			 else
				                        			 {
				                        				 InfoContainer info = oldmapping.get(a.getText());
				                        				 g.removeEdge(info.getEdge());
				                        			 }
				                        		}
				                        			
				                        	}
				                        	
				                        	// new Node
				                        	connections = newOutgoingPanel.getComponents();
				                        	
				                        	for (Component tmp :connections)
				                        	{
				                        		if ((tmp instanceof JCheckBox))
				                        		{
				                        			JCheckBox a = (JCheckBox) tmp;
				                        			
				                        			 if (a.isSelected())
				                        			 {
				                        				 InfoContainer info = newmapping.get(a.getText());
				                        				 g.removeEdge(info.getEdge());
				                        				 
				                        				 g.addEdge(info.getEdge(), node, info.getEdgeDestintation());
				                        			 }
				                        			 else
				                        			 {
				                        				 InfoContainer info = newmapping.get(a.getText());
				                        				 
				                        				 g.removeEdge(info.getEdge());
				                        			 }
				                        		}
				                        			
				                        	}
				                        	
				                        	connections = newIncomingPanel.getComponents();
				                        	
				                        	for (Component tmp :connections)
				                        	{
				                        		if ((tmp instanceof JCheckBox))
				                        		{
				                        			JCheckBox a = (JCheckBox) tmp;
				                        			
				                        			 if (a.isSelected())
				                        			 {
				                        				 InfoContainer info = newmapping.get(a.getText());
				                        				 g.removeEdge(info.getEdge());
				                        				 
				                        				 g.addEdge(info.getEdge(), info.getEdgeSource(), node);
				                        			 }
				                        			 else
				                        			 {
				                        				 InfoContainer info = newmapping.get(a.getText());
				                        				 g.removeEdge(info.getEdge());
				                        			 }
				                        		}
				                        			
				                        	}
				                        	
				                        	
				                        	// remove both nodes from graph an insert the updated node
				                        	
				                        	//g.removeVertex(node);
				                        	g.removeVertex(cur);
				                        	
				                        	node.update();
				                        	//g.addVertex(node);
				                        	
				                        	//l.setGraph(g);
					                    	//vv.setGraphLayout(l);
					                    	vv.repaint();
										}
		                        	}
								}
							} 
						});
                		
    	                submenu2.add(menuItem);
                	}
	                
     
	                popup.add(submenu2);
	                
	                popup.show(vv, e.getX(), e.getY());
	            } 
	            else {
	                final MyEdge2 edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
	                if(edge != null) {
	                    JPopupMenu popup = new JPopupMenu();
	                    popup.add(new AbstractAction("Delete \""+edge.toString()+"\" Edge") {
	                        public void actionPerformed(ActionEvent e) {
	                        	Layout<MyNode2, MyEdge2> l = vv.getGraphLayout();
		                    	
		                    	Graph<MyNode2, MyEdge2> g = l.getGraph();
		                    	g.removeEdge(edge);
		                    	
		                    	//l.setGraph(g);
		                    	//vv.setGraphLayout(l);
		                    	vv.repaint();
	                        	
	                        }

	                    });//popup.add
	                    popup.show(vv, e.getX(), e.getY());

	                }//if edge != null)
	                else
	                {
	                	//rightclicked on Canvas 
	                	System.out.println("On Canvas!");
	                	
	                	JPopupMenu popup = new JPopupMenu();
	                    popup.add(new AbstractAction("Create Node") {
	                        public void actionPerformed(ActionEvent e) {
	                        	
	                        	JTextField NodeName = new JTextField();
	                        	JTextField NodeAttr = new JTextField();
	                        	MyNodeType[] possibilities = ModelBuilder.getNodeTypes().getAllNodeTypesArray();
	                        	JComboBox<MyNodeType> Nodetype = new JComboBox<MyNodeType>(possibilities);
	                        	final JComponent[] inputs = new JComponent[] {
	                        			new JLabel("Node Name"),
	                        			NodeName,
	                        			new JLabel("Nodetype"),
	                        			Nodetype,
	                        			new JLabel("Node Attributes (split with ; )"),
	                        			NodeAttr
	                        	};						
	                        	
	                        	JOptionPane.showMessageDialog(null, inputs, "Create new Node Dialog", JOptionPane.PLAIN_MESSAGE);

	                        	if (NodeName.getText()!=null && NodeName.getText().length()>0
	                        			&& NodeAttr.getText()!=null && NodeAttr.getText().length()>0)
	                        	{
	                        		String s = NodeAttr.getText();
	                        		List<String> attributes = new LinkedList<String>();
	                        		
	                        		String[] temp = s.split(";");
	                        		
	                        		for (String a :temp)
	                        		{
	                        			attributes.add(a);
	                        		}
	                        	
	                        		MyNode2 newNode = new MyNode2(NodeName.getText(), attributes,(MyNodeType)Nodetype.getSelectedItem());
	                        		
	                        		Layout<MyNode2, MyEdge2> l = vv.getGraphLayout();
			                    	
			                    	Graph<MyNode2, MyEdge2> g = l.getGraph();
			                    	
			                    	g.addVertex(newNode);
			                    	
			                    	//l = new FRLayout   <MyNode, MyEdge>(g);
			                    	
			                    	//vv.setGraphLayout(l);
			                    	vv.repaint();
			                    	
	                        	}                                       	
	                        	
	                        }

	                    });//popup.add
	                    popup.show(vv, e.getX(), e.getY());
	                	
	                }
	            }



	        }// if(pickSupport != null)
	*/
	    }
}
