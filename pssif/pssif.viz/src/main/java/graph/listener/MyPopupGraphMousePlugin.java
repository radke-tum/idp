package graph.listener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import reqtool.RequirementTracer;
import reqtool.RequirementVersionManager;
import reqtool.TestCaseVerifier;
import reqtool.graph.IssueResolverPopup;
import reqtool.graph.TestCaseCreatorPopup;
import reqtool.graph.VersionManagerPopup;
import model.ModelBuilder;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import graph.model.IMyNode;
import graph.model.MyEdge;
import graph.model.MyJunctionNode;
import graph.model.MyNode;
import graph.model.MyNodeType;
import gui.graph.GraphVisualization;
/**
 * Creates the right click popups
 * @author Luc
 *
 */
public class MyPopupGraphMousePlugin extends AbstractPopupGraphMousePlugin {

	private GraphVisualization gViz;

    public MyPopupGraphMousePlugin(GraphVisualization gViz) {
        this(MouseEvent.BUTTON3_MASK);
        this.gViz = gViz;
    }
    public MyPopupGraphMousePlugin(int modifiers) {
        super(modifiers);
    }
    
    /**
     * if the user clicked somewhere on the graph canvas. What should be done
     */
    protected void handlePopup(MouseEvent e) {
        VisualizationViewer<MyNode,MyEdge> vv = (VisualizationViewer<MyNode,MyEdge>)e.getSource();
    	
        Point2D p = e.getPoint();

        GraphElementAccessor<MyNode,MyEdge> pickSupport = vv.getPickSupport();
        if(pickSupport != null) {
            MyNode node = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
            // check where did the user click
			if (node != null) {
				// if the user made a right click on a Node
				JPopupMenu popup = new JPopupMenu();
				JMenu submenu = createEdge(e, node);
				popup.add(submenu);


				if (node.getNodeType().equals((ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_REQUIREMENT)))) {

					JMenu reqMenu = new JMenu("Requirement Tool");
					JMenu versMenu = new JMenu("Version Management");

					JMenuItem subItem1 = traceRequirement(e, node);
					JMenuItem subItem2 = createTestCase(e, node);
					JMenuItem subItem3 = createNewVersion(e, node);
					JMenuItem subItem4 = showHideVersions(e, node);
					reqMenu.add(subItem1);
					reqMenu.add(subItem2);
					reqMenu.add(versMenu);
					versMenu.add(subItem3);
					versMenu.add(subItem4);
					popup.add(reqMenu);
				} else if (node.getNodeType().equals((ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_TEST_CASE)))) {
					JMenuItem subItem = verifyTestCase(e, node);
					popup.add(subItem);

				} else if (node.getNodeType().equals((ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_ISSUE)))) {
					JMenuItem subItem = resolveIssue(e, node);
					popup.add(subItem);


				}
				if (ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_SOL_ARTIFACT).getType().isAssignableFrom(node.getNodeType().getType())) {

					JMenu versMenu = new JMenu("Version Management");
					JMenuItem subItem3 = createNewVersion(e, node);
					JMenuItem subItem4 = showHideVersions(e, node);
					versMenu.add(subItem3);
					versMenu.add(subItem4);
					popup.add(versMenu);

			}


				popup.show(vv, e.getX(), e.getY());
			}
            else {
            	// not on a node, so show the new Node popup
            	createNode(e);
            }
        }
    }
    

/**
    * Create the popup which provides the user the possibility to add a Node
    * @param e The MouseEvent which triggered the action
    */
    private void createNode( MouseEvent e )
    {
        VisualizationViewer<MyNode,MyEdge> vv = (VisualizationViewer<MyNode,MyEdge>) e.getSource();
    	
    	JPopupMenu popup = new JPopupMenu();
        popup.add(new AbstractAction("Create Node") {
            public void actionPerformed(ActionEvent e) {
            	
            	JTextField NodeName = new JTextField();

            	MyNodeType[] possibilities = ModelBuilder.getNodeTypes().getAllNodeTypesArray();
            	JComboBox<MyNodeType> Nodetype = new JComboBox<MyNodeType>(possibilities);
            	
            	JComponent[] inputs = new JComponent[] {
            			new JLabel("Node Name"),
            			NodeName,
            			new JLabel("Nodetype"),
            			Nodetype
            	};						
            	
            	JOptionPane.showMessageDialog(null, inputs, "Create new Node Dialog", JOptionPane.PLAIN_MESSAGE);
            	
            	// check if the user filled all the input field
            	if (NodeName.getText()!=null && NodeName.getText().length()>0)
            	{
            		ModelBuilder.addNewNodeFromGUI(NodeName.getText(), (MyNodeType) Nodetype.getSelectedItem());
            		ModelBuilder.printVisibleStuff();
            		gViz.updateGraph();
            	}                                       	
            	
            }

        });
        popup.show(vv, e.getX(), e.getY());
    }
    /**
     * provide the SubMenu options to create a new Edge
     * @param e The MouseEvent which triggered the action
     * @param selectedNode The Node which was selected when the user pushed the right mouse button
     * @return a menu with all the options to create a new Edge
     */
    private JMenu createEdge ( MouseEvent e, MyNode selectedNode)
    {
    	JMenu submenu = new JMenu("Add Edge");

       	LinkedList<MyNode> col = new LinkedList<MyNode>();
       	
       	col.addAll(ModelBuilder.getAllNodes());
       	// self loops are allowed
       	
       	LinkedList<String> destinations = new LinkedList<String>();
       	HashMap<String, MyNode> help = new HashMap<String, MyNode>();
       	for (MyNode cur : col)
       	{
       		destinations.add("To : "+cur.getRealName());
       		help.put("To : "+cur.getRealName(), cur);
       		
       	}
       	
       	// sort the destination Nodes by Name
       	Collections.sort(destinations);
       	
       	for (String dest : destinations)
       	{
       		JMenuItem menuItem = new JMenuItem(dest);
       		// create the action listener with the specific Edge Types
       		MyAddEdgeListener el = new MyAddEdgeListener(selectedNode, help.get(dest), gViz);
       		
       		menuItem.addActionListener(el);
       		submenu.add(menuItem);
       	}
           
       	return submenu;
    }
    
    /**
     * provide the SubMenu options to trace a requirement
     * @param e The MouseEvent which triggered the action
     * @param selectedNode The Node which was selected when the user pushed the right mouse button
     * @return a menu with all the option to trace a requirement
     */
    private JMenuItem traceRequirement ( MouseEvent e, final MyNode selectedNode)
    {
    	JMenuItem submenu;
        if (RequirementTracer.isTracedNode(selectedNode)) {
         submenu = new JMenuItem("Untrace requirement");
         submenu.addActionListener(new ActionListener() {
          
       @Override
       public void actionPerformed(ActionEvent e) {
        RequirementTracer.stopTracing();
        gViz.stopTracingNodes(); 
       }
       
      });
        } else {
         submenu = new JMenuItem("Trace requirement");
         submenu.addActionListener(new ActionListener() {
       
       @Override
       public void actionPerformed(ActionEvent e) {
        RequirementTracer.traceRequirement(selectedNode);;
        gViz.traceNodes(); 
       }
      });
        }
         
           return submenu;
    }
    
    private JMenuItem createTestCase(MouseEvent e, final MyNode selectedNode) {
 		// TODO Auto-generated method stub
    	
    	JMenuItem submenu;
    	
    	submenu = new JMenuItem("Create Test Case");
    	submenu.addActionListener(new ActionListener() {

    		@Override
        	public void actionPerformed(ActionEvent e) {
    			TestCaseCreatorPopup crPopup = new TestCaseCreatorPopup(selectedNode, gViz);
    			crPopup.showPopup();
        	}
		}
    	);
    	
    	    	
 		return submenu;
 	}
    
    private JMenuItem verifyTestCase(MouseEvent e, final MyNode selectedNode) {
    	
    	JMenuItem submenu;
    	
    	submenu = new JMenuItem("Verify Test Case");
    	submenu.addActionListener(new ActionListener() {

    		@Override
        	public void actionPerformed(ActionEvent e) {
    			new TestCaseVerifier(selectedNode).verifyTestCase();
    			gViz.updateGraph();
        	}
		}
    	);
    	
    	    	
 		return submenu;
	}
    
    private JMenuItem showHideVersions(MouseEvent e, final MyNode selectedNode) {
 		// TODO Auto-generated method stub
    	
    	JMenuItem submenu;
    	
    	if (RequirementVersionManager.getMinVersion(selectedNode).isVisible()){
    	
    		submenu = new JMenuItem("Hide Versions");
        	submenu.addActionListener(new ActionListener() {
    			
        		@Override
            	public void actionPerformed(ActionEvent e){
        			RequirementVersionManager.hideVersions(gViz, selectedNode);
        			gViz.updateGraph();
            	}
    		}
        	);
    	} else {
    		submenu = new JMenuItem("Show Versions");
        	submenu.addActionListener(new ActionListener() {
    			
        		@Override
            	public void actionPerformed(ActionEvent e){
        			RequirementVersionManager.showVersions(gViz, selectedNode);
        			gViz.updateGraph();
            	}
    		}
        	);
    		
    	}
    	
    	    	
 		return submenu;
 	}
   
    
    private JMenuItem createNewVersion(final MouseEvent mouseEvent, final MyNode node) {
    	JMenuItem submenu;
    	
    	submenu = new JMenuItem("Create new version");
    	submenu.addActionListener(new ActionListener() {

    		@Override
        	public void actionPerformed(ActionEvent e) {
    			if (VersionManagerPopup.showPopup(node)) {
    				gViz.updateGraph();
    			}
            }

        });
    	    	
 		return submenu;
 	}
    
    private JMenuItem resolveIssue(MouseEvent e, final MyNode selectedNode)  {
    	
    	JMenuItem submenu;
    	
    	submenu = new JMenuItem("Resolve Issue");
    	submenu.addActionListener(new ActionListener() {

    		@Override
        	public void actionPerformed(ActionEvent e) {
    		IssueResolverPopup popup = new IssueResolverPopup(selectedNode);
    		popup.showPopup(gViz);
    		gViz.updateGraph();
    		}
		}
    	);
    	
    	    	
 		return submenu;
	}
    
    /**
     * Action listener for the Edge creation
     * @author Luc
     *
     */
    private class MyAddEdgeListener implements ActionListener
    {
    	private MyNode source;
    	private MyNode dest;
    	private GraphVisualization gViz;
    	
    	public MyAddEdgeListener (MyNode source, MyNode dest, GraphVisualization gViz)
    	{
    		this.source = source;
    		this.dest = dest;
    		this.gViz = gViz;
    	}
    	
		@Override
		public void actionPerformed(ActionEvent e) {
			CreateEdgePopup popup = new CreateEdgePopup(source, dest, gViz);

			popup.showPopup();			
		}
    }
}