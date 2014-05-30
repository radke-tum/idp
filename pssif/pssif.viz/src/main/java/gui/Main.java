package gui;


import graph.operations.AttributeAggregation;
import graph.operations.AttributeFilter;
import graph.operations.GraphViewContainer;
import graph.operations.MasterFilter;
import gui.graph.AttributeFilterPopup;
import gui.graph.CreateNewGraphViewPopup;
import gui.graph.GraphVisualization;
import gui.graph.HighlightNodePopup;
import gui.graph.NodeColorPopup;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.FileExporter;
import model.FileImporter;

/**
 * The main class of the project. Execute this class to start the Visualization Software
 * @author Luc
 *
 */
public class Main {
	
	private MatrixView matrixView;
	private JFrame frame;
	private GraphView graphView;
	
	private JMenuItem resetGraph;
	private JMenuItem resetMatrix;
	private JMenuItem colorNodes;
	private JMenuItem modelStatistics;
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
//	private JCheckBoxMenuItem TestLayout;
	private JMenu applyView;
	private JMenu deleteView;
	private JMenu graphLayout;
	private JMenu graphOperations;
	private JMenu applyNodeFilter;
	private JMenu applyEdgeFilter;
	private JMenu deleteNodeFilter;
	private JMenu deleteEdgeFilter;
	
	private FileImporter importer;
	private MasterFilter masterFilter;
	
	public static void main(String[] args) {
		
		new Main();
	}
	
	/**
	 * create a new instance of the program
	 */
	public Main ()
	{
		frame = new JFrame("Product Service Systems - Integration Framework ---- Visualization");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth()/4;
		int height = gd.getDisplayMode().getHeight()/4;
		
		width= width*3;
		height = height*3;
		
		importer = new FileImporter();
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setState(Frame.MAXIMIZED_BOTH);
		
		initFrame();
	}
	
	private void initFrame()
	{	
		frame.getContentPane().removeAll();
		// create the Basic Menu Bar
		frame.setJMenuBar(createFileMenu());
		// create an information Panel
		JPanel allPanel = new JPanel(new GridBagLayout());
		allPanel.setSize(frame.getSize());
		//allPanel.setPreferredSize(frame.getPreferredSize());
		allPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JLabel label = new JLabel("Please import a file");
		label.setFont(new Font("Arial", Font.ITALIC, 25));
		allPanel.add(label);
		
		frame.add(allPanel);
		
		frame.pack();
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
				if (importer.showPopup(frame))
				{
			        // Create the Views
					matrixView = new MatrixView();	
					graphView = new GraphView();
					// instance which manages all the filters
					masterFilter = new MasterFilter(graphView);
					
					Dimension d = frame.getSize();
					
					// Setup the frame
					frame.getContentPane().removeAll();
					// Standard start with Graph
					frame.getContentPane().add(graphView.getGraphPanel());
					graphView.setActive(true);
					matrixView.setActive(false);
					
					//create the full menuBar after first import
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
	
	/**
	 * Adds the missing options to the "File" Menu to the MenuBar (Import File not included)
	 * @param menuBar where to place the menu
	 * @return the updated menuBar
	 */
	private JMenuBar addFileMenu(JMenuBar menuBar)
	{
		exportFile = new JMenuItem("Export File");
		
		exportFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileExporter exporter = new FileExporter();
				
				if (exporter.showPopup(frame))
				{
					  JPanel answerPanel = new JPanel();
						
					  answerPanel.add(new JLabel("Export successful."));

				      JOptionPane.showMessageDialog(null, answerPanel, "Success!", JOptionPane.INFORMATION_MESSAGE);
				}
				
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
		
		modelStatistics = new JMenuItem("Statistics");
		modelStatistics.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AttributeAggregation agg = new AttributeAggregation();
				
				agg.showInfoPanel();
				
			}
		});
		menuBar.getMenu(0).add(modelStatistics);
		
		return menuBar;
	}
	
	/**
	 * Adds the Mouse Operations Mode to the MenuBar (Picking and Transforming Mode)
	 * @return The updated MenuBar
	 */
	private JMenu addMouseOperationModes()
	{
		JMenu modeMenu = graphView.getGraph().getMouseOperationModes(); // Obtain mode menu from the mouse
		modeMenu.setText("Mouse Mode");
		modeMenu.setIcon(null); 
		modeMenu.setPreferredSize(new Dimension(80,20));
		modeMenu.getItem(1).setSelected(true);
		// as soon as Transforming is activated no Edge or Node should be selected anymore
		modeMenu.getItem(0).addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphView.getGraph().clearPickSupport();
			}
		});
		
		return modeMenu;
	}
	
	/**
	 * Adds the Option to change the Graph Layout to the MenuBar
	 * @return The updated MenuBar
	 */
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
				KKLayout.setSelected(true);
				//TestLayout.setSelected(false);
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
				FRLayout.setSelected(true);
				//TestLayout.setSelected(false);
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
				SpringLayout.setSelected(true);
				//TestLayout.setSelected(false);
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
				ISOMLayout.setSelected(true);
				//TestLayout.setSelected(false);
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
				CircleLayout.setSelected(true);
				//TestLayout.setSelected(false);
			}
		});
		graphLayout.add(CircleLayout);
		
/*		TestLayout = new JCheckBoxMenuItem(GraphVisualization.TestLayout);
		TestLayout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphView.getGraph().changeLayout(GraphVisualization.TestLayout);
				KKLayout.setSelected(false);
				FRLayout.setSelected(false);
				SpringLayout.setSelected(false);
				ISOMLayout.setSelected(false);
				TestLayout.setSelected(true);
			}
		});
		graphLayout.add(TestLayout);*/
		
		return graphLayout;
	}
	
	/**
	 * Adds the possibility to change to the GraphView or MatrixView to the MenuBar
	 * @return The updated MenuBar
	 */
	private JMenu addVizModes()
	{
		JMenu visualisationMenu = new JMenu("Visualization Mode");
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

				frame.getContentPane().add(matrixView.getVisualization());
				graphView.setActive(false);
				matrixView.setActive(true);
				adjustButtons();
				frame.setPreferredSize(d);
				
				frame.pack();
				frame.repaint();
			}
		});
		visualisationMenu.add(matrixVizualistation);
		visualisationMenu.setIcon(null);
		
		return visualisationMenu;
	}
	
	/**
	 * Adds the Reset Matrix and Graph Options to the MenuBar
	 * @return The updated MenuBar
	 */
	private JMenu addResetOptions()
	{
		JMenu resetMenu = new JMenu("Reset");
		resetGraph = new JMenuItem("Reset Graph");
		resetGraph.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphView.resetGraph();
				initFrame();
			}
		});

		
		resetMenu.add(resetGraph);
		
		resetMatrix = new JMenuItem("Reset Matrix Filter");
		resetMatrix.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean res = matrixView.chooseEdgesAndNodes();
				
				frame.getContentPane().removeAll();
				if (res)
				{
					// create the Matrix View
					frame.getContentPane().add(matrixView.getVisualization());
				}
				else
				{
					// create an information Panel and display no Matrix
					JPanel allPanel = new JPanel(new GridBagLayout());
					allPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
					JLabel label = new JLabel("Please select a least one EdgeType and one NodeType");
					label.setFont(new Font("Arial", Font.ITALIC, 25));
					allPanel.add(label);
					frame.add(allPanel);
				}
				frame.pack();
				frame.repaint();
				
			}
		});
		resetMenu.add(resetMatrix);
		resetMenu.setIcon(null);
		
		return resetMenu;
	}
	
	/**
	 * Adds the Graph Filters (View, AttributeFilter, FollowEdges)  to the MenuBar
	 * @return The updated MenuBar
	 */
	private JMenu addGraphOperations()
	{
		graphOperations = new JMenu("Graph Operations");
		
		attributFilter = new JMenuItem("Create a new Attribute Filter");
		attributFilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AttributeFilterPopup filter = new AttributeFilterPopup();
		
				String result = filter.showPopup();
				
				if (result !=null && result.contains(AttributeFilterPopup.newEdge))
				{
					String condition = result.substring(result.indexOf("|")+1);
					
					// Tell the MasterFilter
					masterFilter.addEdgeAttributFilter(condition, true);
					
					//update the MenuBar
					resetApplyEdgeFilters(condition);
					resetDeleteEdgeFilters();
				}
				
				if (result !=null && result.contains(AttributeFilterPopup.newNode))
				{
					String condition = result.substring(result.indexOf("|")+1);
					
					// Tell the MasterFilter
					masterFilter.addNodeAttributFilter(condition, true);
					
					//update the MenuBar
					resetApplyNodeFilters(condition);
					resetDeleteNodeFilters();
				}

				
			}
		});
		
		graphOperations.add(attributFilter);
		
		applyNodeFilter = new JMenu("Apply Node Attribute Filters");
		// create the SubMenus
		addApplyNodeFilters(null);
		graphOperations.add(applyNodeFilter);
		
		deleteNodeFilter = new JMenu("Remove Node Attribute Filters");
		// create the SubMenus
		addRemoveNodeFilters();
		graphOperations.add(deleteNodeFilter);
		
		applyEdgeFilter = new JMenu("Apply Edge Attribute Filters");
		// create the SubMenus
		addApplyEdgeFilters(null);
		graphOperations.add(applyEdgeFilter);
		
		deleteEdgeFilter = new JMenu("Remove Edge Attribute Filters");
		// create the SubMenus
		addRemoveEdgeFilters();
		graphOperations.add(deleteEdgeFilter);
		
		followEdges = new JMenuItem("Choose Follow EdgeTypes");
		followEdges.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				HighlightNodePopup popup = new HighlightNodePopup(graphView);
				
				popup.showPopup();
			}
		});
		
		graphOperations.add(followEdges);
		
		createView = new JMenuItem("Create a new GraphView");
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
	
	/**
	 * defines which Items of the MenuBar are currently visible
	 */
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
			modelStatistics.setEnabled(true);
		}
		
		if (matrixView.isActive())
		{
			resetGraph.setEnabled(false);
			resetMatrix.setEnabled(true);
			graphVizualistation.setEnabled(true);
			matrixVizualistation.setEnabled(false);
			graphLayout.setEnabled(false);
			graphOperations.setEnabled(false);
			modelStatistics.setEnabled(true);
		}
			
	}
	
	/**
	 * Displays the Popup to create a new GraphView and updates the MenuBar accordingly to the users choices
	 */
	private void createNewGraphView ()
	{
		CreateNewGraphViewPopup popup = new CreateNewGraphViewPopup(graphView, masterFilter);
		boolean res = popup.showPopup();
		
		if (res)
		{
        	//update the menus
        	resetDeleteGraphViews();
        	resetReadGraphViews();
		}
	}
	
	/**
	 * Reads all the GraphViews and display for each View a MenuItem to apply/undo the GraphView filter
	 */
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

					JCheckBoxMenuItem item = (JCheckBoxMenuItem)e.getSource();
					if (item.isSelected())
					{
						masterFilter.applyNodeAndEdgeTypeFilter(name);
					}
					else
					{
						masterFilter.undoNodeAndEdgeTypeFilter(name);
					}
				}
				});
				
				applyView.add(menuItem);
			}
		}
	}
	
	/**
	 * Removes all the apply View MenuItems and creates them from scratch
	 */
	private void resetReadGraphViews()
	{
		applyView.removeAll();
		readGraphViews();
		
	}

	/**
	 * Reads all the GraphViews and display for each View a MenuItem to delete the selected the GraphView filter
	 */
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
					
					masterFilter.removeNodeAndEdgeTypeFilter(name);
					
					resetReadGraphViews();
					resetDeleteGraphViews();
				}
				});
				
				deleteView.add(menuItem);
			}
		}
	}
	
	/**
	 * Removes all the delete View MenuItems and creates them from scratch
	 */
	private void resetDeleteGraphViews()
	{
		deleteView.removeAll();
		deleteGraphView();
	}
	
	/**
	 * Creates all the MenuItems to apply the Node Attribute Filters
	 * @param newCondition a condition which should be applied immediately ( can be null, so no condition is applied)
	 */
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
								masterFilter.applyNodeAttributeFilter(condition);
							}
							else
							{
								masterFilter.undoNodeAttributeFilter(condition);
							}
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							JPanel errorPanel = new JPanel();
			        		
			        		errorPanel.add(new JLabel("There was a problem applying the attribute filter"));
			        		
			        		JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
						}
					}
					});
				
				if ((newCondition!=null && name.equals(newCondition)) || masterFilter.NodeAttributFilterActive(name))
				{
					menuItem.setSelected(true);
				}
				applyNodeFilter.add(menuItem);
			}
		}
	}
	
	/**
	 * Creates all the MenuItems to apply the Edge Attribute Filters
	 * @param newCondition a condition which should be applied immediately ( can be null, so no condition is applied)
	 */
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
								masterFilter.applyEdgeAttributeFilter(condition);
							}
							else
							{
								masterFilter.undoEdgeAttributeFilter(condition);
							}
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							JPanel errorPanel = new JPanel();
			        		
			        		errorPanel.add(new JLabel("There was a problem applying the attribute filter"));
			        		
			        		JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
						}
					}
					});
								
				if ((newCondition!=null && name.equals(newCondition)) || masterFilter.EdgeAttributFilterActive(name))
				{
					menuItem.setSelected(true);
				}
				applyEdgeFilter.add(menuItem);
			}
		}
	}
	
	/**
	 * Creates all the MenuItems to remove the Node Attribute Filters
	 */
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
							masterFilter.removeNodeAttributFilter(name);
							
							resetApplyNodeFilters(null);
							resetDeleteNodeFilters();
						} catch (Exception ex) {							
							ex.printStackTrace();
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
	
	/**
	 * Creates all the MenuItems to remove the Edge Attribute Filters
	 */
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
							masterFilter.removeEdgeAttributFilter(name);
							
							resetApplyEdgeFilters(null);
							resetDeleteEdgeFilters();
						} catch (Exception ex) {
							ex.printStackTrace();
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

	/**
	 * Removes all the apply Node Filter MenuItems and creates them from scratch
	 * @param newCondition a condition which should be applied immediately ( can be null, so no condition is applied)
	 */
	private void resetApplyNodeFilters(String newConditon)
	{
		applyNodeFilter.removeAll();
		addApplyNodeFilters(newConditon);
	}
	
	/**
	 * Removes all the delete Node Filter MenuItems and creates them from scratch
	 */
	private void resetDeleteNodeFilters()
	{
		deleteNodeFilter.removeAll();
		addRemoveNodeFilters();
	}
	
	/**
	 * Removes all the apply Edge Filter MenuItems and creates them from scratch
	 * @param newCondition a condition which should be applied immediately ( can be null, so no condition is applied)
	 */
	private void resetApplyEdgeFilters(String newConditon)
	{
		applyEdgeFilter.removeAll();
		addApplyEdgeFilters(newConditon);
	}
	
	/**
	 * Removes all the delete Edge Filter MenuItems and creates them from scratch
	 */
	private void resetDeleteEdgeFilters()
	{
		deleteEdgeFilter.removeAll();
		addRemoveEdgeFilters();
	}
}



  
