package gui;


import graph.model2.MyEdgeType;
import graph.model2.MyNodeType;
import graph.operations.GraphViewContainer;
import gui.graph.MyListColorRenderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.ModelBuilder;

public class Main {
	
	private static MatrixView matrixView;
	private static JFrame frame;
	private static GraphView graphView;
	private static Dimension frameSize;

	
	private static JMenuItem resetGraph;
	private static JMenuItem resetMatrix;
	private static JMenuItem colorNodes;
	private static JMenuItem createView;
	private static JMenu applyView;
	private static JMenu deleteView;
	
	public static void main(String[] args) {
		
		ModelBuilder m = new ModelBuilder();



		frame = new JFrame("Product Service Systems - Integration Framework ---- Visualisation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth()/4;
		int height = gd.getDisplayMode().getHeight()/4;
		
		width= width*3;
		height = height*3;
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setState(Frame.MAXIMIZED_BOTH);
		
		frameSize = frame.getSize();
		
		frame.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {}
			
			@Override
			public void componentResized(ComponentEvent e) {
				frameSize = e.getComponent().getSize();
				
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {}
			
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
		
		frame.pack();
		
		matrixView = new MatrixView();
		graphView = new GraphView(/*frame.getSize()*/);
		
		// Standart start with Graph
		frame.getContentPane().add(graphView.getGraphPanel());
		graphView.setActive(true);
		matrixView.setActive(false);
		
		frame.setJMenuBar(createMenu());
		adjustButtons();
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private static JMenuBar createMenu()
	{
		// PICKING or TRANSFORMING Mode
		JMenuBar menuBar = new JMenuBar();
		JMenu modeMenu = graphView.getGraph().getAbstractModalGraphMouse().getModeMenu(); // Obtain mode menu from the mouse
		modeMenu.setText("Mouse Mode");
		modeMenu.setIcon(null); 
		modeMenu.setPreferredSize(new Dimension(80,20));
		
		// Which Visualization
		JMenu visualisationMenu = new JMenu("Visualisation Mode");
		JMenuItem graphItem = new JMenuItem("Graph");
		graphItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				Dimension d = frame.getSize();
				System.out.println("current Size "+d);
				frame.getContentPane().add(graphView.getGraphPanel());
				graphView.setActive(true);
				matrixView.setActive(false);
				adjustButtons();
				frame.setPreferredSize(d);
				
				frame.pack();
				//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
				frame.repaint();
			}
		});
		visualisationMenu.add(graphItem);
		visualisationMenu.setIcon(null);
		
		
		JMenuItem matrixItem = new JMenuItem("Matrix");
		matrixItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				Dimension d = frame.getSize();
				System.out.println("current Size "+d);
				frame.getContentPane().add(matrixView.getVisualization());
				graphView.setActive(false);
				matrixView.setActive(true);
				adjustButtons();
				frame.setPreferredSize(d);
				
				frame.pack();
				//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
				frame.repaint();
			}
		});
		visualisationMenu.add(matrixItem);
		visualisationMenu.setIcon(null);
		
		
		// Reset the Graph or Matrix
		JMenu resetMenu = new JMenu("Reset");
		resetGraph = new JMenuItem("Reset Graph");
		resetGraph.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphView.resetGraph();
			}
		});

		
		resetMenu.add(resetGraph);
		
		resetMatrix = new JMenuItem("Reset Matrix");
		resetMatrix.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean res = matrixView.chooseNodes();
				
				if (res)
				{
					frame.getContentPane().removeAll();
					frame.getContentPane().add(matrixView.getVisualization());
					
					
					frame.pack();
					//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
					frame.repaint();
				}
			}
		});
		resetMenu.add(resetMatrix);
		resetMenu.setIcon(null);
		
		// Color Options
		JMenu visualizationEffects = new JMenu("Visualization Effects");
		colorNodes = new JMenuItem("Choose Node colors");
		colorNodes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pickNodeColor();
			}
		});
		visualizationEffects.add(colorNodes);
		
		createView = new JMenuItem("Create new GraphView");
		createView.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewGraphView ();
				resetReadGraphViews();
			}
		});
		visualizationEffects.add(createView);
		
		applyView = new JMenu("Apply GraphView");
		
		visualizationEffects.add(applyView);
		// create the SubMenus
		readGraphViews();
		
		deleteView = new JMenu("Delete GraphView");
		
		visualizationEffects.add(deleteView);
		// create the SubMenus
		deleteGraphView();
		
		// Add all to the menuBar
		menuBar.add(modeMenu);
		menuBar.add(visualisationMenu);
		menuBar.add(resetMenu);
		menuBar.add(visualizationEffects);
		
		return menuBar;
	}
	
	
	private static void pickNodeColor()
	{
		JPanel bannerPanel = new JPanel(new GridLayout());;
 
       final JList<MyNodeType> list = new JList<MyNodeType>( ModelBuilder.getNodeTypes().getAllNodeTypesArray() );  
        
       final MyListColorRenderer colorlistener = new MyListColorRenderer();
       colorlistener.setColors(graphView.getGraph().getNodeColorMapping());
       
        list.setCellRenderer(colorlistener);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane sp = new JScrollPane( list ); 
        bannerPanel.add(sp);
		
        //Set up color chooser for setting text color
        final JColorChooser tcc = new JColorChooser();
        tcc.getSelectionModel().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				MyNodeType type = list.getSelectedValue();
				Color newColor = tcc.getColor();
				 
				if (type!=null)
				{
					colorlistener.setColor(type, newColor);
					list.repaint();
				}
			}
		});
        
        bannerPanel.add(tcc);
        
        int dialogResult = JOptionPane.showConfirmDialog(null, bannerPanel, "Choose Nodes Colors", JOptionPane.DEFAULT_OPTION);
    	
    	if (dialogResult==0)
    	{
    		HashMap<MyNodeType, Color > colors = colorlistener.getColorMapping();
    		graphView.getGraph().setNodeColorMapping(colors);
    		
    	}
	}
	
	private static void adjustButtons()
	{
		if (graphView.isActive())
		{
			resetGraph.setEnabled(true);
			resetMatrix.setEnabled(false);
			colorNodes.setEnabled(true);
			createView.setEnabled(true);
		}
		
		if (matrixView.isActive())
		{
			resetGraph.setEnabled(false);
			resetMatrix.setEnabled(true);
			colorNodes.setEnabled(false);
			createView.setEnabled(false);
		}
			
	}
	
	private static void createNewGraphView ()
	{
		MyEdgeType[] edgePossibilities = ModelBuilder.getEdgeTypes().getAllEdgeTypesArray();
		MyNodeType[] nodePossibilities = ModelBuilder.getNodeTypes().getAllNodeTypesArray();
		
		
		JPanel allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		final JPanel NodePanel = new JPanel(new GridLayout(0, 1));
		
		for (MyNodeType attr : nodePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			
			choice.setSelected(false);
			NodePanel.add(choice);
		}
		
		final JPanel EdgePanel = new JPanel(new GridLayout(0, 1));
		
		for (MyEdgeType attr : edgePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			choice.setSelected(false);
			
			EdgePanel.add(choice);
		}
		JScrollPane scrollNodes = new JScrollPane(NodePanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollNodes.setPreferredSize(new Dimension(200, 400));
			    
	    JScrollPane scrollEdges = new JScrollPane(EdgePanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollEdges.setPreferredSize(new Dimension(200, 400));
		
		final JCheckBox selectAllNodes = new JCheckBox("Select all Node Types");
	    
	    selectAllNodes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
		      {
		        if (selectAllNodes.isSelected())
		        {
		          Component[] attr = NodePanel.getComponents();
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
		          Component[] attr = NodePanel.getComponents();
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
	    final JCheckBox selectAllEdges = new JCheckBox("Select all Edge Types");
	    
	    selectAllEdges.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
	      {
	        if (selectAllEdges.isSelected())
	        {
	          Component[] attr = EdgePanel.getComponents();
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
	          Component[] attr = EdgePanel.getComponents();
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
		
	    selectAllNodes.setSelected(false);
	    
	    selectAllEdges.setSelected(false);
	    
	    JTextField viewNameTextField = new JTextField(10);
	    
	    int ypos =0;
	    
		c.gridx = 0;
		c.gridy = ypos;
		allPanel.add(new JLabel("Choose Node Types"),c);
		c.gridx = 1;
		c.gridy = ypos++;
		allPanel.add(new JLabel("Choose Edge Types"),c);
	    c.gridx = 0;
	    c.gridy = ypos;
	    allPanel.add(scrollNodes, c);
	    c.gridx = 1;
	    c.gridy = ypos++;
	    allPanel.add(scrollEdges, c);
	    c.gridx = 0;
	    c.gridy = ypos;
	    allPanel.add(selectAllNodes, c);
	    c.gridx = 1;
	    c.gridy = ypos++;
	    allPanel.add(selectAllEdges, c);
	    
	    c.weighty = 1;
	    
		c.gridx = 0;
		c.gridy = ypos;
		allPanel.add(new JLabel("Graph View name "),c);
		c.gridx = 1;
		c.gridy = ypos++;
		allPanel.add(viewNameTextField,c);
	    
		
		
		allPanel.setPreferredSize(new Dimension(400,500));
		allPanel.setMaximumSize(new Dimension(400,500));
		allPanel.setMinimumSize(new Dimension(400,500));
		
		int dialogResult = JOptionPane.showConfirmDialog(null, allPanel, "Create a new Graph View", JOptionPane.DEFAULT_OPTION);
    	
    	if (dialogResult==0)
    	{
    		LinkedList<MyNodeType> selectedNodes = new LinkedList<MyNodeType>();
    		// get all the values of the Nodes
        	Component[] attr = NodePanel.getComponents();       	
        	for (Component tmp :attr)
        	{
        		if ((tmp instanceof JCheckBox))
        		{
        			JCheckBox a = (JCheckBox) tmp;
        			
        			// compare which ones where selected
        			 if (a.isSelected())
        			 {
        				 MyNodeType b = ModelBuilder.getNodeTypes().getValue(a.getText());
        				 selectedNodes.add(b);
        			 }
        				
        		}	
        	}
        	
        	LinkedList<MyEdgeType> selectedEdges = new LinkedList<MyEdgeType>();
    		// get all the values of the edges
        	attr = EdgePanel.getComponents();       	
        	for (Component tmp :attr)
        	{
        		if ((tmp instanceof JCheckBox))
        		{
        			JCheckBox a = (JCheckBox) tmp;
        			
        			// compare which ones where selected
        			 if (a.isSelected())
        			 {
        				 MyEdgeType b = ModelBuilder.getEdgeTypes().getValue(a.getText());
        				 selectedEdges.add(b);
        			 }
        				
        		}	
        	}
        	
        	
        	String viewName = viewNameTextField.getText();
        	
        	if (viewName.length()==0 || (selectedNodes.size()==0 && selectedEdges.size()==0))
        	{
        		// not enough information
        		JPanel errorPanel = new JPanel();
        		
        		errorPanel.add(new JLabel("No name entered or no edge and node types selected"));
        		
        		JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
        		
        		
        	}
        	else
        	{
        		// write to config
        		GraphViewContainer container = new GraphViewContainer(selectedNodes,selectedEdges,viewName);
	        	graphView.getGraph().createNewGraphView(container);
	        	
	        	// apply the view
	        	graphView.getGraph().applyNodeAndEdgeFilter(container.getSelectedNodeTypes(), container.getSelectedEdgeTypes());
	        	
	        	//update the menus
	        	resetDeleteGraphViews();
	        	resetReadGraphViews();
        	}
        }
	}

	private static void readGraphViews()
	{
		final HashMap<String, GraphViewContainer> views = graphView.getGraph().getAllGraphViews();
		
		for (final String name : views.keySet())
		{
			JMenuItem menuItem = new JMenuItem(name);
			
			menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				graphView.getGraph().applyNodeAndEdgeFilter(ModelBuilder.getNodeTypes().getAllNodeTypes(), ModelBuilder.getEdgeTypes().getAllEdgeTypes());
				
				GraphViewContainer view = views.get(name);
				
				System.out.println("Number of Node Types: "+view.getSelectedNodeTypes().size());
				System.out.println("Number of Edge Types: "+view.getSelectedEdgeTypes().size());
				graphView.getGraph().applyNodeAndEdgeFilter(view.getSelectedNodeTypes(), view.getSelectedEdgeTypes());
			}
			});
			
			applyView.add(menuItem);
		}	
	}
	
	private static void resetReadGraphViews()
	{
		applyView.removeAll();
		readGraphViews();
	}
	
	private static void deleteGraphView()
	{
		final HashMap<String, GraphViewContainer> views = graphView.getGraph().getAllGraphViews();
		
		for (final String name : views.keySet())
		{
			JMenuItem menuItem = new JMenuItem(name);
			
			menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				graphView.getGraph().deleteGraphView(views.get(name));
				graphView.getGraph().applyNodeAndEdgeFilter(ModelBuilder.getNodeTypes().getAllNodeTypes(), ModelBuilder.getEdgeTypes().getAllEdgeTypes());
				resetReadGraphViews();
				resetDeleteGraphViews();
			}
			});
			
			deleteView.add(menuItem);
		}	
	}
	
	private static void resetDeleteGraphViews()
	{
		deleteView.removeAll();
		deleteGraphView();
	}
}



  
