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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
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

import org.apache.commons.collections15.map.HashedMap;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.subLayout.TreeCollapser;
import graph.model.MyEdge;
import graph.model.MyNode;
import graph.operations.MyCollapser;

public class MiddleMousePlugin implements MouseListener, GraphMousePlugin {
	 
	private MyCollapser collapser;
	
	    public MiddleMousePlugin() {
	    	collapser = new MyCollapser();
	    }
	   public MiddleMousePlugin(int button2) {
			super();
			collapser = new MyCollapser();
		}
	    
		@Override
		public void mouseClicked(final MouseEvent me) {
			
			if (me.getButton()==MouseEvent.BUTTON2)
			{
			VisualizationViewer<MyNode,MyEdge> vv = (VisualizationViewer<MyNode,MyEdge>)me.getSource();
		        Point2D p = me.getPoint();
		        //System.out.println("Call");
		        GraphElementAccessor<MyNode,MyEdge> pickSupport = vv.getPickSupport();
		        if(pickSupport != null) {
		            //System.out.println("Middle MOUSE Popup!");
		            MyNode node = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
		            if(node != null) {
		            	JPopupMenu popup = new JPopupMenu();
	       	         	
		            	 popup.add(new AbstractAction("Collapse") {
		                        public void actionPerformed(ActionEvent e) {
		                        	VisualizationViewer<MyNode,MyEdge> vv = (VisualizationViewer<MyNode,MyEdge>)me.getSource();
		                        	Layout<MyNode, MyEdge> l = vv.getGraphLayout();
		                        	Collection picked =new HashSet(vv.getPickedVertexState().getPicked());
					                if(picked.size() == 1) {
					                	MyNode root = (MyNode)picked.iterator().next();
					                	
					                	Graph<MyNode, MyEdge> graph = l.getGraph();
					                	Graph<MyNode, MyEdge> res = collapser.collapseGraph(graph, root);
					                	
					                	//l.setGraph(res);
				                    	//vv.setGraphLayout(l);
				                    	
				                    	vv.getPickedVertexState().clear();
				                        vv.repaint();
					                }
		                        }});
		            	
		            	 popup.add(new AbstractAction("Expand") {
		                        public void actionPerformed(ActionEvent e) {
		                        	VisualizationViewer<MyNode,MyEdge> vv = (VisualizationViewer<MyNode,MyEdge>)me.getSource();
		                        	Layout<MyNode, MyEdge> l = vv.getGraphLayout();
		                        	Collection picked =new HashSet(vv.getPickedVertexState().getPicked());
					                if(picked.size() == 1) {
					                	MyNode root = (MyNode)picked.iterator().next();
					                	
					                	Graph<MyNode, MyEdge> graph = l.getGraph();
					                	Graph<MyNode, MyEdge> res = collapser.expandNode(graph, root);
					                	
					                	//l.setGraph(res);
				                    	//vv.setGraphLayout(l);
				                    	
				                    	vv.getPickedVertexState().clear();
				                        vv.repaint();
					                }
		                        }});
		            	 
		                popup.show(vv, me.getX(), me.getY());
		                System.out.println("Middle MOUSE on Node!");
		            } 
		            else {
		            	System.out.println("Middle MOUSE on Canvas!");
		            }
		               
		        }
			}
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			 // System.out.println("Middle MOUSE is not NULL!");
		}
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			 // System.out.println("Middle MOUSE is not NULL!");
		}
		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			 // System.out.println("Middle MOUSE is not NULL!");
		}//
		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//  System.out.println("Middle MOUSE is not NULL!");
		}
		@Override
		public boolean checkModifiers(MouseEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public int getModifiers() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void setModifiers(int arg0) {
			// TODO Auto-generated method stub
			
		}
}

