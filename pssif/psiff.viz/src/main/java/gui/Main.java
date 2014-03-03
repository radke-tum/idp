package gui;


import graph.operations.AttributeFilter;
import graph.operations.GraphViewContainer;
import gui.graph.AttributeFilterPopup;
import gui.graph.CreateNewGraphViewPopup;
import gui.graph.GraphVisualization;
import gui.graph.HighlightNodePopup;
import gui.graph.NodeColorPopup;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.FileImporter;
import model.ModelBuilder;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.util.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.transform.Mapper;
import de.tum.pssif.transform.MapperFactory;
import de.tum.pssif.transform.mapper.graphml.GraphMlViewCreator;

public class Main {
	
	private MatrixView matrixView;
	private JFrame frame;
	private GraphView graphView;
	
	private JMenuItem resetGraph;
	private JMenuItem resetMatrix;
	private JMenuItem colorNodes;
	private JMenuItem createView;
	private JMenuItem attributFilter;
	private JMenuItem graphVizualistation;
	private JMenuItem matrixVizualistation;
	private JMenuItem importFile;
	private JMenuItem exportFile;
	private JMenuItem followEdges;
	private JCheckBoxMenuItem KKLayout;
	private JCheckBoxMenuItem FRLayout;
	private JCheckBoxMenuItem SpringLayout;
	private JCheckBoxMenuItem ISOMLayout;
	private JCheckBoxMenuItem CircleLayout;
	private JMenu applyView;
	private JMenu deleteView;
	private JMenu graphLayout;
	private JMenu graphOperations;
	private JMenu applyNodeFilter;
	private JMenu applyEdgeFilter;
	private JMenu deleteNodeFilter;
	private JMenu deleteEdgeFilter;
	
	
	public static void main(String[] args) {
		
		new Main();
	}
	
	public Main ()
	{
		frame = new JFrame("Product Service Systems - Integration Framework ---- Visualisation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth()/4;
		int height = gd.getDisplayMode().getHeight()/4;
		
		width= width*3;
		height = height*3;
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setState(Frame.MAXIMIZED_BOTH);
		
		// create the Basic Menu Bar
		frame.setJMenuBar(createFileMenu());
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	/**
	 * Create MenuBar with only the Import Button
	 * @return MenuBar
	 */
	private JMenuBar createFileMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		importFile = new JMenuItem("Import File");
		importFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileImporter importer = new FileImporter();
				if (importer.showPopup(frame))
				{
			        // Create the Views
			        matrixView = new MatrixView();
					graphView = new GraphView();
					
					Dimension d = frame.getSize();
					
					// Setup the frame
					frame.getContentPane().removeAll();
					// Standart start with Graph
					frame.getContentPane().add(graphView.getGraphPanel());
					graphView.setActive(true);
					matrixView.setActive(false);
					
					frame.setJMenuBar(createMenu());
					adjustButtons();
					
					frame.setPreferredSize(d);
					frame.pack();
					frame.repaint();
				}
			}
		});
		
		fileMenu.add(importFile);
		
		menuBar.add(fileMenu);
		
		return menuBar;
	}
	
	/**
	 * create A MenuBar with all the possible SubMenus
	 * @return
	 */
	private JMenuBar createMenu()
	{
		// contains already the file Menu and Import
		JMenuBar menuBar = createFileMenu();
		
		// add additional functions to the File Menu
		menuBar = addFileMenu(menuBar);
		
		// add Mouse Operation Modes
		menuBar.add(addMouseOperationModes());
		
		// Which GraphLayout was chosen
		menuBar.add(addChangeLayouts());
		
		// Which Visualization Mode
		menuBar.add(addVizModes());
		
		// Reset the Graph or Matrix
		menuBar.add(addResetOptions());
		
		// Color Options
		menuBar.add(addGraphOperations());
		
		return menuBar;
	}
	
	
	private JMenuBar addFileMenu(JMenuBar menuBar)
	{
		exportFile = new JMenuItem("Export File");
		
		exportFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO has to be implemented
				
			}
		});
		
		menuBar.getMenu(0).add(exportFile);
		
		colorNodes = new JMenuItem("Choose Node colors");
		colorNodes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NodeColorPopup popup = new NodeColorPopup(graphView);
				
				popup.showPopup();
			}
		});
		
		menuBar.getMenu(0).add(colorNodes);
		
		return menuBar;
	}
	
	private JMenu addMouseOperationModes()
	{
		JMenu modeMenu = graphView.getGraph().getAbstractModalGraphMouse().getModeMenu(); // Obtain mode menu from the mouse
		modeMenu.setText("Mouse Mode");
		modeMenu.setIcon(null); 
		modeMenu.setPreferredSize(new Dimension(80,20));
		modeMenu.getItem(1).setSelected(true);
		
		return modeMenu;
	}
	
	private JMenu addChangeLayouts()
	{
		graphLayout = new JMenu("Graph Layout");
		
		KKLayout = new JCheckBoxMenuItem(GraphVisualization.KKLayout);
		KKLayout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphView.getGraph().changeLayout(GraphVisualization.KKLayout);
				FRLayout.setSelected(false);
				SpringLayout.setSelected(false);
				ISOMLayout.setSelected(false);
				CircleLayout.setSelected(false);
			}
		});
		graphLayout.add(KKLayout);
		
		FRLayout = new JCheckBoxMenuItem(GraphVisualization.FRLayout);
		FRLayout.setSelected(true);
		FRLayout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphView.getGraph().changeLayout(GraphVisualization.FRLayout);
				KKLayout.setSelected(false);
				SpringLayout.setSelected(false);
				ISOMLayout.setSelected(false);
				CircleLayout.setSelected(false);
			}
		});
		graphLayout.add(FRLayout);
		
		SpringLayout = new JCheckBoxMenuItem(GraphVisualization.SpringLayout);
		SpringLayout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphView.getGraph().changeLayout(GraphVisualization.SpringLayout);
				KKLayout.setSelected(false);
				FRLayout.setSelected(false);
				ISOMLayout.setSelected(false);
				CircleLayout.setSelected(false);
			}
		});
		graphLayout.add(SpringLayout);
		
		ISOMLayout = new JCheckBoxMenuItem(GraphVisualization.ISOMLayout);
		ISOMLayout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphView.getGraph().changeLayout(GraphVisualization.ISOMLayout);
				KKLayout.setSelected(false);
				FRLayout.setSelected(false);
				SpringLayout.setSelected(false);
				CircleLayout.setSelected(false);
			}
		});
		graphLayout.add(ISOMLayout);
		
		CircleLayout = new JCheckBoxMenuItem(GraphVisualization.CircleLayout);
		CircleLayout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphView.getGraph().changeLayout(GraphVisualization.CircleLayout);
				KKLayout.setSelected(false);
				FRLayout.setSelected(false);
				SpringLayout.setSelected(false);
				ISOMLayout.setSelected(false);
			}
		});
		graphLayout.add(CircleLayout);
		
		return graphLayout;
	}
	
	private JMenu addVizModes()
	{
		JMenu visualisationMenu = new JMenu("Visualisation Mode");
		graphVizualistation = new JMenuItem("Graph");
		graphVizualistation.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				Dimension d = frame.getSize();
				
				frame.getContentPane().add(graphView.getGraphPanel());
				graphView.setActive(true);
				matrixView.setActive(false);
				adjustButtons();
				frame.setPreferredSize(d);
				
				frame.pack();
				frame.repaint();
			}
		});
		visualisationMenu.add(graphVizualistation);
		visualisationMenu.setIcon(null);
		
		
		matrixVizualistation = new JMenuItem("Matrix");
		matrixVizualistation.addActionListener(new ActionListener() {
			
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
		visualisationMenu.add(matrixVizualistation);
		visualisationMenu.setIcon(null);
		
		return visualisationMenu;
	}
	
	private JMenu addResetOptions()
	{
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
		
		return resetMenu;
	}
		
	private JMenu addGraphOperations()
	{
		graphOperations = new JMenu("Graph Operations");
		
		attributFilter = new JMenuItem("Create new Attribute Filter");
		attributFilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AttributeFilterPopup filter = new AttributeFilterPopup();
		
				String result = filter.showPopup();
				
				if (result !=null && result.contains(AttributeFilterPopup.newEdge))
				{
					String condition = result.substring(result.indexOf("|")+1);
					resetApplyEdgeFilters(condition);
					resetDeleteEdgeFilters();
					
					graphView.getGraph().updateGraph();
				}
				
				if (result !=null && result.contains(AttributeFilterPopup.newNode))
				{
					String condition = result.substring(result.indexOf("|")+1);
					resetApplyNodeFilters(condition);
					resetDeleteNodeFilters();
					
					graphView.getGraph().updateGraph();
				}

				
			}
		});
		
		graphOperations.add(attributFilter);
		
		applyNodeFilter = new JMenu("Apply Node Attribute Filters");
		addApplyNodeFilters(null);
		graphOperations.add(applyNodeFilter);
		
		deleteNodeFilter = new JMenu("Remove Node Attribute Filters");
		addRemoveNodeFilters();
		graphOperations.add(deleteNodeFilter);
		
		applyEdgeFilter = new JMenu("Apply Edge Attribute Filters");
		addApplyEdgeFilters(null);
		graphOperations.add(applyEdgeFilter);
		
		deleteEdgeFilter = new JMenu("Remove Edge Attribute Filters");
		addRemoveEdgeFilters();
		graphOperations.add(deleteEdgeFilter);
		
		followEdges = new JMenuItem("Choose Follow EdgeTypes");
		followEdges.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				HighlightNodePopup popup = new HighlightNodePopup(graphView.getGraph());
				
				popup.showPopup();
			}
		});
		
		graphOperations.add(followEdges);
		
		createView = new JMenuItem("Create new GraphView");
		createView.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewGraphView ();
				resetReadGraphViews();
			}
		});
		graphOperations.add(createView);
		
		applyView = new JMenu("Apply GraphView");
		
		graphOperations.add(applyView);
		// create the SubMenus
		readGraphViews();
		
		deleteView = new JMenu("Delete GraphView");
		
		graphOperations.add(deleteView);
		// create the SubMenus
		deleteGraphView();
		
		return graphOperations;
	}
	
	private void adjustButtons()
	{
		if (graphView.isActive())
		{
			resetGraph.setEnabled(true);
			resetMatrix.setEnabled(false);
			graphVizualistation.setEnabled(false);
			matrixVizualistation.setEnabled(true);
			graphLayout.setEnabled(true);
			graphOperations.setEnabled(true);	
		}
		
		if (matrixView.isActive())
		{
			resetGraph.setEnabled(false);
			resetMatrix.setEnabled(true);
			graphVizualistation.setEnabled(true);
			matrixVizualistation.setEnabled(false);
			graphLayout.setEnabled(false);
			graphOperations.setEnabled(false);
		}
			
	}
	
	private void createNewGraphView ()
	{
		CreateNewGraphViewPopup popup = new CreateNewGraphViewPopup(graphView);
		boolean res = popup.showPopup();
		
		if (res)
		{
        	//update the menus
        	resetDeleteGraphViews();
        	resetReadGraphViews();
		}
	}

	private void readGraphViews()
	{
		final HashMap<String, GraphViewContainer> views = graphView.getGraph().getAllGraphViews();
		
		if (views.size()==0)
		{
			applyView.setEnabled(false);
		}
		else
		{
			applyView.setEnabled(true);
			
			for (final String name : views.keySet())
			{
				JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(name);
				
				menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					GraphViewContainer view = views.get(name);
					
					JCheckBoxMenuItem item = (JCheckBoxMenuItem)e.getSource();
					if (item.isSelected())
					{
						//System.out.println("Selected");
						graphView.getGraph().applyNodeAndEdgeFilter(view.getSelectedNodeTypes(), view.getSelectedEdgeTypes(), name);
					}
					else
					{
					//	System.out.println("UnSelected");
						graphView.getGraph().undoNodeAndEdgeFilter(name);
					}
					
				}
				});
				
				applyView.add(menuItem);
			}
		}
	}
	
	private void resetReadGraphViews()
	{
		applyView.removeAll();
		readGraphViews();
		
	}
	
	private void deleteGraphView()
	{
		final HashMap<String, GraphViewContainer> views = graphView.getGraph().getAllGraphViews();
		
		if (views.size()==0)
		{
			deleteView.setEnabled(false);
		}
		else
		{
			deleteView.setEnabled(true);
			
			for (final String name : views.keySet())
			{
				JMenuItem menuItem = new JMenuItem(name);
				
				menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					graphView.getGraph().deleteGraphView(views.get(name));
					graphView.getGraph().undoNodeAndEdgeFilter(name);
					resetReadGraphViews();
					resetDeleteGraphViews();
				}
				});
				
				deleteView.add(menuItem);
			}
		}
	}
	
	private void resetDeleteGraphViews()
	{
		deleteView.removeAll();
		deleteGraphView();
	}
	
	private void addApplyNodeFilters(String newCondition)
	{
		LinkedList<String> conditions = AttributeFilter.getAllNodeConditions();
		
		if (conditions.size()==0)
		{
			applyNodeFilter.setEnabled(false);
		}
		else
		{
			applyNodeFilter.setEnabled(true);
			
			for (String name : conditions)
			{
				JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(name);
				
				menuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						try
						{
							JCheckBoxMenuItem item = (JCheckBoxMenuItem)e.getSource();
							String condition = item.getText();
							if (item.isSelected())
							{
								AttributeFilter.applyNodeCondition(condition);
							}
							else
							{
								AttributeFilter.undoNodeCondition(condition);
							}
							graphView.getGraph().updateGraph();
						}
						catch (Exception ex)
						{
							System.out.println(ex);
							JPanel errorPanel = new JPanel();
			        		
			        		errorPanel.add(new JLabel("There was a problem applying the attribute filter"));
			        		
			        		JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
						}
					}
					});
				
				if (newCondition!=null && name.equals(newCondition))
				{
					menuItem.setSelected(true);
				}
				applyNodeFilter.add(menuItem);
			}
		}
	}
	
	private void addApplyEdgeFilters(String newCondition)
	{
		LinkedList<String> conditions = AttributeFilter.getAllEdgeConditions();
		
		if (conditions.size()==0)
		{
			applyEdgeFilter.setEnabled(false);
		}
		else
		{
			applyEdgeFilter.setEnabled(true);
			
			for (String name : conditions)
			{
				JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(name);
				
				menuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						try
						{
							JCheckBoxMenuItem item = (JCheckBoxMenuItem)e.getSource();
							String condition = item.getText();
							if (item.isSelected())
							{
								AttributeFilter.applyEdgeCondition(condition);
							}
							else
							{
								AttributeFilter.undoEdgeCondition(condition);
							}
							graphView.getGraph().updateGraph();
						}
						catch (Exception ex)
						{
							System.out.println(ex.getMessage());
							JPanel errorPanel = new JPanel();
			        		
			        		errorPanel.add(new JLabel("There was a problem applying the attribute filter"));
			        		
			        		JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
						}
					}
					});
				
				if (newCondition!=null && name.equals(newCondition))
				{
					menuItem.setSelected(true);
				}
				applyEdgeFilter.add(menuItem);
			}
		}
	}
	
	private void addRemoveNodeFilters()
	{
		LinkedList<String> conditions = AttributeFilter.getAllNodeConditions();
		
		if (conditions.size()==0)
		{
			deleteNodeFilter.setEnabled(false);
		}
		else
		{
			deleteNodeFilter.setEnabled(true);
			
			for (final String name : conditions)
			{
				JMenuItem menuItem = new JMenuItem(name);
				
				menuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							AttributeFilter.removeNodeCondition(name);
							graphView.getGraph().updateGraph();
							
							resetApplyNodeFilters(null);
							resetDeleteNodeFilters();
						} catch (Exception ex) {
							
							System.out.println(ex.getMessage());
							JPanel errorPanel = new JPanel();
			        		
			        		errorPanel.add(new JLabel("There was a problem deleting the attribute filter"));
			        		
			        		JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				
				deleteNodeFilter.add(menuItem);
			}
		}
	}
	
	private void addRemoveEdgeFilters()
	{
		LinkedList<String> conditions = AttributeFilter.getAllEdgeConditions();
		
		if (conditions.size()==0)
		{
			deleteEdgeFilter.setEnabled(false);
		}
		else
		{
			deleteEdgeFilter.setEnabled(true);
			
			for (final String name : conditions)
			{
				JMenuItem menuItem = new JMenuItem(name);
				
				menuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							AttributeFilter.removeEdgeCondition(name);
							graphView.getGraph().updateGraph();
							
							resetApplyEdgeFilters(null);
							resetDeleteEdgeFilters();
						} catch (Exception ex) {
							System.out.println(ex.getMessage());
							JPanel errorPanel = new JPanel();
			        		
			        		errorPanel.add(new JLabel("There was a problem deleting the attribute filter"));
			        		
			        		JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				
				deleteEdgeFilter.add(menuItem);
			}
		}
	}
	
	private void resetApplyNodeFilters(String newConditon)
	{
		applyNodeFilter.removeAll();
		addApplyNodeFilters(newConditon);
	}
	
	private void resetDeleteNodeFilters()
	{
		deleteNodeFilter.removeAll();
		addRemoveNodeFilters();
	}
	
	private void resetApplyEdgeFilters(String newConditon)
	{
		applyEdgeFilter.removeAll();
		addApplyEdgeFilters(newConditon);
	}
	
	private void resetDeleteEdgeFilters()
	{
		deleteEdgeFilter.removeAll();
		addRemoveEdgeFilters();
	}
	

}



  
