package graph.listener;
import java.awt.Event;
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
import reqtool.RequirementToolbox;
import reqtool.RequirementTracer;
import reqtool.RequirementVersionManager;
import reqtool.TestCaseVerifier;
import reqtool.event.ReqInfoEvent;
import reqtool.event.ResolveIssueEvent;
import reqtool.event.bus.ReqToolReqistry;
import reqtool.event.menu.CreateReqMenuEvent;
import reqtool.event.menu.TraceReqMenuEvent;
import reqtool.event.menu.VersionVisibilityMenuEvent;
import reqtool.graph.IssueResolverPopup;
import reqtool.graph.TestCaseCreatorPopup;
import reqtool.graph.VersionManagerPopup;
import reqtool.handler.menu.VersionsVisibilityMenuHandler;
import model.ModelBuilder;
import model.MyModelContainer;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import graph.model.MyEdge;
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
            MyEdge edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
            // check where did the user click
			if (node != null) {
				// if the user made a right click on a Node
				JPopupMenu popup = new JPopupMenu();
				
				JMenu submenu = createEdge(e, node);
				JMenuItem submenuRemove = removeNode(e, node);
				popup.add(submenu);
				popup.add(submenuRemove);
				// if the user made a right click on a Reqtool Node
				ReqToolReqistry.getInstance().post(new CreateReqMenuEvent(node, popup, gViz));

				popup.show(vv, e.getX(), e.getY());
			} else if (edge != null) {
				JPopupMenu popup = new JPopupMenu();
				
				JMenuItem submenu = removeEdge(e, edge);
				popup.add(submenu);
				
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
     * provide the SubMenu options to remove a node
     * @param e The MouseEvent which triggered the action
     * @param selectedNode The Node which was selected when the user pushed the right mouse button
     * @return a menu with all the option to trace a requirement
     */
   
    private JMenuItem removeNode(MouseEvent e, final MyNode selectedNode) {
    	JMenuItem submenu = new JMenuItem("Remove node");
    	submenu.addActionListener(new ActionListener() {
    		@Override
        	public void actionPerformed(ActionEvent e) {
    			ModelBuilder.removeNodeFromGUI(selectedNode);
    			ModelBuilder.printVisibleStuff();
        		gViz.updateGraph();
        	}
		});
 		return submenu;
    }
    
    private JMenuItem removeEdge(MouseEvent e, final MyEdge selectedEdge) {
    	JMenuItem submenu = new JMenuItem("Remove edge");
    	submenu.addActionListener(new ActionListener() {
    		@Override
        	public void actionPerformed(ActionEvent e) {
    			ModelBuilder.removeEdgeFromGUI(selectedEdge);
    			ModelBuilder.printVisibleStuff();
        		gViz.updateGraph();
        	}
		});
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