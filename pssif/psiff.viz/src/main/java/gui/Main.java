package gui;


import graph.model.MyNodeType;
import graph.operations.GraphViewContainer;
import gui.graph.AttributeFilterPopup;
import gui.graph.CreateNewGraphViewPopup;
import gui.graph.GraphVisualization;
import gui.graph.MyListColorRenderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.util.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.transform.Mapper;
import de.tum.pssif.transform.MapperFactory;
import de.tum.pssif.transform.mapper.graphml.GraphMLMapper;
import de.tum.pssif.transform.mapper.graphml.GraphMlViewCreator;
import model.ModelBuilder;

public class Main {
	
	private static MatrixView matrixView;
	private static JFrame frame;
	private static GraphView graphView;
	//private static Dimension frameSize;

	
	private static JMenuItem resetGraph;
	private static JMenuItem resetMatrix;
	private static JMenuItem colorNodes;
	private static JMenuItem createView;
	private static JMenuItem attributFilter;
	private static JMenuItem graphVizualistation;
	private static JMenuItem matrixVizualistation;
	private static JMenuItem importFile;
	private static JMenuItem exportFile;
	private static JCheckBoxMenuItem KKLayout;
	private static JCheckBoxMenuItem FRLayout;
	private static JCheckBoxMenuItem SpringLayout;
	private static JCheckBoxMenuItem ISOMLayout;
	private static JCheckBoxMenuItem CircleLayout;
	private static JMenu applyView;
	private static JMenu deleteView;
	private static JMenu graphLayout;
	private static JMenu visualizationEffects;
	
	
	public static void main(String[] args) {
		
		//ModelBuilder m = new ModelBuilder();

		frame = new JFrame("Product Service Systems - Integration Framework ---- Visualisation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth()/4;
		int height = gd.getDisplayMode().getHeight()/4;
		
		width= width*3;
		height = height*3;
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setState(Frame.MAXIMIZED_BOTH);
		
		/*frameSize = frame.getSize();
		
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
		});*/
		
		//frame.pack();
		
		frame.setJMenuBar(createFileMenu());
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private static JMenuBar createFileMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		// File Import Export
		JMenu fileMenu = new JMenu("File");
		importFile = new JMenuItem("Import File");
		importFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser openFile = new JFileChooser();
				
				openFile.setCurrentDirectory(new File("C:\\Users\\Luc\\Desktop\\Uni Dropbox\\Dropbox\\IDP-PSS-IF-Shared\\Modelle PE"));
				int returnVal = openFile.showOpenDialog(frame);
				
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
			      
			    	File file = openFile.getSelectedFile();
			        InputStream in;
					try {
						in = new FileInputStream(file);
						
						String path = file.getAbsolutePath();
						String fileEnding = path.substring(path.lastIndexOf('.')+1);
						
						Mapper importer = MapperFactory.getMapper(fileEnding);
						// TODO Needs to be changed	
				        Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
				        Metamodel view = GraphMlViewCreator.createGraphMlView(metamodel);

				        Model model = importer.read(view, in);
				        
				        new ModelBuilder(metamodel,model);
				        
				        matrixView = new MatrixView();
						graphView = new GraphView();
						
						Dimension d = frame.getSize();
						
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
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}			       
			    }
			}
		});
		
		fileMenu.add(importFile);
		
		menuBar.add(fileMenu);
		
		return menuBar;
	}
	
	private static JMenuBar createMenu()
	{
		// contains already the file Menu
		JMenuBar menuBar = createFileMenu();
		
		exportFile = new JMenuItem("Export File");
		
		exportFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO has to be implemented
				
			}
		});
		
		menuBar.getMenu(0).add(exportFile);
		
		
		// PICKING or TRANSFORMING Mode
		
		JMenu modeMenu = graphView.getGraph().getAbstractModalGraphMouse().getModeMenu(); // Obtain mode menu from the mouse
		modeMenu.setText("Mouse Mode");
		modeMenu.setIcon(null); 
		modeMenu.setPreferredSize(new Dimension(80,20));
		modeMenu.getItem(1).setSelected(true);
		
		// Which GraphLayout
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
		menuBar.add(graphLayout);
		
		// Which Visualization
		JMenu visualisationMenu = new JMenu("Visualisation Mode");
		graphVizualistation = new JMenuItem("Graph");
		graphVizualistation.addActionListener(new ActionListener() {
			
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
		visualizationEffects = new JMenu("Visualization Effects");
		colorNodes = new JMenuItem("Choose Node colors");
		colorNodes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pickNodeColor();
			}
		});
		
		visualizationEffects.add(colorNodes);
		
		attributFilter = new JMenuItem("Filter by attribut");
		attributFilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AttributeFilterPopup filter = new AttributeFilterPopup();
		
				filter.showPopup();

				graphView.getGraph().updateGraph();
			}
		});
		
		visualizationEffects.add(attributFilter);
		
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
			//colorNodes.setEnabled(true);
			//createView.setEnabled(true);
			graphVizualistation.setEnabled(false);
			matrixVizualistation.setEnabled(true);
			//attributFilter.setEnabled(true);
			graphLayout.setEnabled(true);
			visualizationEffects.setEnabled(true);
			
		}
		
		if (matrixView.isActive())
		{
			resetGraph.setEnabled(false);
			resetMatrix.setEnabled(true);
			//colorNodes.setEnabled(false);
			//createView.setEnabled(false);
			graphVizualistation.setEnabled(true);
			matrixVizualistation.setEnabled(false);
			//attributFilter.setEnabled(false);
			graphLayout.setEnabled(false);
			visualizationEffects.setEnabled(false);
		}
			
	}
	
	private static void createNewGraphView ()
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

	private static void readGraphViews()
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
					//graphView.getGraph().applyNodeAndEdgeFilter(ModelBuilder.getNodeTypes().getAllNodeTypes(), ModelBuilder.getEdgeTypes().getAllEdgeTypes());
					
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
					//System.out.println("Number of Node Types: "+view.getSelectedNodeTypes().size());
					//System.out.println("Number of Edge Types: "+view.getSelectedEdgeTypes().size());
					
				}
				});
				
				applyView.add(menuItem);
			}
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
	
	private static void resetDeleteGraphViews()
	{
		deleteView.removeAll();
		deleteGraphView();
	}
}



  
