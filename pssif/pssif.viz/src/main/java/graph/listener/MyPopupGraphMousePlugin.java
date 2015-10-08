package graph.listener;


import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
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

import model.ModelBuilder;
import reqtool.bus.ReqToolReqistry;
import reqtool.event.menu.CreateReqMenuEvent;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import graph.model.IMyNode;
import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;
import gui.graph.GraphVisualization;
import gui.graph.IconSizePopup;

/**
 * Creates the right click popups
 * @author Luc
 *
 */
public class MyPopupGraphMousePlugin extends AbstractPopupGraphMousePlugin implements MouseMotionListener {
	
	private GraphVisualization gViz;
	private MyNode sourceNode = null;
	private int p1x,p1y, p2x, p2y;

    public MyPopupGraphMousePlugin(GraphVisualization gViz) {
        this(MouseEvent.BUTTON3_MASK);
        this.gViz = gViz;
        p1x = p1y = p2x = p2y = 0;
    }
    public MyPopupGraphMousePlugin(int modifiers) {
        super(modifiers);
    }
    
    
    public void mousePressed(MouseEvent e) {
    	
    	if(e.isPopupTrigger()) {
            handlePopup(e);
            e.consume();
        }
    	
    	if(e.getButton() == MouseEvent.BUTTON1 && this.gViz.getAbstractModalGraphMode() == Mode.EDITING)
    	{
    		sourceNode = getClickedNode(e);
    		p1x = e.getX();
    		p1y = e.getY();
    		
    	}
    	
    }
    
    
    public void mouseReleased(MouseEvent e) {
    	if(e.isPopupTrigger()) {
            handlePopup(e);
            e.consume();
        }
    	
    	if(e.getButton() == MouseEvent.BUTTON1 && this.gViz.getAbstractModalGraphMode() == Mode.EDITING)
    	{
    		MyNode destn = getClickedNode(e);
    		if (destn != null && sourceNode != null)
    		{
    			CreateEdgePopup popup = new CreateEdgePopup(sourceNode, destn, gViz);
    			sourceNode = null;
    			popup.showPopup();
    			gViz.setAbstractModalGraphMode(Mode.PICKING);
    		}
    		
    		gViz.getVisualisationViewer().repaint();
    	}
    }
    
	/**
     * if the user clicked somewhere on the graph canvas. What should be done
     */
    protected void handlePopup(MouseEvent e) {
        try{
			@SuppressWarnings("unchecked")
			VisualizationViewer<MyNode,MyEdge> vv = ((VisualizationViewer<MyNode,MyEdge>)e.getSource());
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
					JMenuItem customizeSize = customizeSize(e, node);
					JMenuItem changeNodeType = changeNodeType(e, node);
					popup.add(submenu);
					popup.add(submenuRemove);
					popup.add(customizeSize);
					popup.add(changeNodeType);
	
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
	            	createNodePopUp(e);
	            }
	        }
        } catch (Exception exception) {}
    }
    
    
	private JMenuItem changeNodeType(MouseEvent e, final MyNode selectedNode) {
		JMenuItem submenu = new JMenuItem("Change Node Type");
    	submenu.addActionListener(new ActionListener() {
    		@Override
        	public void actionPerformed(ActionEvent e) {
    			changeNodeTypeFunction(selectedNode);
        	}
		});
 		return submenu;
	}
	
	private void changeNodeTypeFunction(MyNode node)
	{
    	MyNodeType[] possibilities = ModelBuilder.getNodeTypes().getAllNodeTypesArray();
    	JComboBox<MyNodeType> nodeType = new JComboBox<MyNodeType>(possibilities);
    	
    	JComponent[] inputs = new JComponent[] {
    			//new JLabel("Node Name"),
    			//NodeName,
    			new JLabel("Nodetype"),
    			nodeType
    	};						
    	
    	JOptionPane.showMessageDialog(null, inputs, "Change Node Type Dialog", JOptionPane.PLAIN_MESSAGE);
    	node.setNodeType((MyNodeType) nodeType.getSelectedItem());
    	
    	//updating edges from and to this node based on the new type
    	ArrayList<MyEdge> edges = new ArrayList<MyEdge>();
    	for (MyEdge edge : ModelBuilder.getAllEdges())
    		if ((edge.getSourceNode() == node) || (edge.getDestinationNode() == node))
    			edges.add(edge);
    	for (MyEdge edge : edges)
	    	if (!isEdgePossible(edge))
	    		edge.setEdgeType(ModelBuilder.getEdgeTypes().getValue("Edge"));
    	
    	gViz.updateGraph();      

	}
	
	private boolean isEdgePossible(MyEdge edge)
	{
		try{
			MyNode srcNode = (MyNode) edge.getSourceNode();
			MyNode dstNode = (MyNode) edge.getDestinationNode();
			LinkedList<MyEdgeType> edgeTypes = 
					ModelBuilder.getPossibleEdges(srcNode.getNodeType().getType(), dstNode.getNodeType().getType());
			return (edgeTypes != null && edgeTypes.contains(edge.getEdgeType()));
		} catch(Exception e){return false;}
	}
	
	/**
    * Create the popup which provides the user the possibility to add a Node
    * @param e The MouseEvent which triggered the action
    */
    @SuppressWarnings("serial")
	private void createNodePopUp( final MouseEvent e )
    {
        @SuppressWarnings("unchecked")
		VisualizationViewer<MyNode,MyEdge> vv = (VisualizationViewer<MyNode,MyEdge>) e.getSource();
    	JPopupMenu popup = new JPopupMenu();
        popup.add(new AbstractAction("Create Node") {
            public void actionPerformed(ActionEvent e2) {
            	createNode(e.getPoint());
            }

        });
        popup.show(vv, e.getX(), e.getY());
    }
    
    private void createNode(Point2D p)
    {
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
    		IMyNode mynode = ModelBuilder.addNewNodeFromGUI(NodeName.getText(), (MyNodeType) Nodetype.getSelectedItem());
    		ModelBuilder.printVisibleStuff();
    		gViz.getVisualisationViewer().getGraphLayout().setLocation(mynode, p);
    		gViz.updateGraph();
    	}       
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
        		gViz.updateGraph();
        	}
		});
 		return submenu;
    }

    private JMenuItem customizeSize(MouseEvent e, final MyNode selectedNode) {
    	JMenuItem submenu = new JMenuItem("Customize Node Size");
    	submenu.addActionListener(new ActionListener() {
    		@Override
        	public void actionPerformed(ActionEvent e) {
    			IconSizePopup popup= new IconSizePopup(selectedNode);
    			popup.showPopup();
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
    
    private MyNode getClickedNode(MouseEvent e)
    {
    	@SuppressWarnings("unchecked")
		VisualizationViewer<MyNode,MyEdge> vv = (VisualizationViewer<MyNode,MyEdge>)e.getSource();
        Point2D p = e.getPoint();
        GraphElementAccessor<MyNode,MyEdge> pickSupport = vv.getPickSupport();
        if(pickSupport != null) {
            MyNode node = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
			return node;
        }
        return null;
    }
    
	@Override
	public void mouseDragged(MouseEvent e) {
		Graphics2D g2d = (Graphics2D) gViz.getVisualisationViewer().getGraphics();
		p2x = e.getX();
		p2y = e.getY();
		//new BasicStroke(1.0F, 0, 2, 0.0F, new float[] { 20.0F, 12.0F }, 0.0F);
		//g2d.setStroke(new BasicStroke(1.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(6.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0F, null, 0.0F));
		g2d.drawLine(p1x,p1y,p2x,p2y);
		//Path2D.Double pd = new Path2D.Double();
		//pd.curveTo(p1x, p1y, 5, 10, p2x, p2y);
		p1x = p2x;
		p1y = p2y;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
	}
}