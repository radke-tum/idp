package gui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.util.Animator;
import graph.listener.ConfigWriterReader;
import graph.listener.MyPopupGraphMousePlugin;
import graph.model.GraphBuilder;
import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;
import graph.operations.GraphViewContainer;
import graph.operations.MyCollapser;
import graph.operations.NodeAndEdgeTypeFilter;
import graph.operations.VertexStrokeHighlight;

public class GraphVisualization
{
  public static final String KKLayout ="KKLayout";
  public static final String FRLayout  ="FRLayout";
  public static final String SpringLayout  ="SpringLayout";
  public static final String ISOMLayout  ="ISOMLayout";
  public static final String CircleLayout  ="CircleLayout";
	
  private Graph<MyNode, MyEdge> g;
//  static int Nodecount;
  private AbstractModalGraphMouse gm;
  private VisualizationViewer<MyNode, MyEdge> vv;
  private Layout<MyNode, MyEdge> layout;
  private VertexStrokeHighlight<MyNode, MyEdge> vsh;
  private GraphBuilder gb;
  private boolean detailedNodes;
  private MyCollapser collapser;

  private HashMap<MyNodeType,Color> nodeColorMapping;
  private ConfigWriterReader configWriterReader;

  
  public GraphVisualization(Dimension d, boolean details)
  {
    int i = 1000;
    this.detailedNodes = details;
    this.gb = new GraphBuilder();
    this.collapser = new MyCollapser();
    configWriterReader = new ConfigWriterReader();
    this.nodeColorMapping = configWriterReader.readColors();
    
    this.g =  this.gb.createGraph(this.detailedNodes);
    

    this.layout = new FRLayout<MyNode, MyEdge>(this.g);
    if (d != null)
    {
      int x = (int)d.getWidth() - 50;
      int y = (int)d.getHeight() - 50;
      this.layout.setSize(new Dimension(x, y));
    }
    else
    {
      this.layout.setSize(new Dimension(i, i));
    }

    this.vv = new VisualizationViewer<MyNode, MyEdge>(this.layout);

    if (d != null) 
    {
      this.vv.setPreferredSize(d);
    }
    else 
    {
      this.vv.setPreferredSize(new Dimension(i, i));
    }
    
    Transformer<MyNode, Paint> vertexColor = new Transformer<MyNode, Paint>()
    {
      public Paint transform(MyNode i)
      {
    	if (nodeColorMapping!=null)
    	{
	    	Color c = nodeColorMapping.get(i.getNodeType());
	    	if (c!=null)
	    		return c;
    	}
    	
    	return Color.LIGHT_GRAY;
      }
    };
    
    Transformer<MyNode, Shape> vertexSize = new Transformer<MyNode, Shape>()
    {
      public Shape transform(MyNode node)
      {
        if (node.isDetailedOutput())
        {	
         
          if (node.getHeight() != 0 && node.getWidth() != 0) { 
        	  return new Rectangle2D.Double(-75.0D, -75.0D, node.getWidth()*55, node.getHeight()*55);
          }
          else
          {
        	  return new Rectangle2D.Double(-75.0D, -75.0D, 150.0D, 150.0D);
          }
        }
        else
        {
        	return new Rectangle2D.Double(-75.0D, -25.0D, node.getWidth()*55, 50.0D);
        }
      }
    };
    
    Transformer<MyNode, String> vertexLabelTransformer =  new Transformer<MyNode, String>()
    	    {
        public String transform(MyNode node)
        {
        	return "<html>" + node.getNodeInformations(node.isDetailedOutput());
        }
        
    };
    
	  // Set up a new stroke Transformer for the edges
    Transformer<MyEdge, Stroke> edgeTransformer = new Transformer<MyEdge, Stroke>()
    {
      public Stroke transform(MyEdge edge)
      {
        int res = edge.getEdgeType().getLineType() % 4;
        BasicStroke b = null;
        
        switch (res)
        {
        case 0: 
          return RenderContext.DASHED;
        case 1: 
          return RenderContext.DOTTED;
        case 2: 
          b = new BasicStroke(1.0F, 0, 2, 0.0F, new float[] { 20.0F, 12.0F }, 0.0F);
          return b;
        case 3: 
          b = new BasicStroke(5.0F, 0, 2, 0.0F, new float[] { 1.0F, 1.0F }, 0.0F);
          return b;
        }
        b = new BasicStroke(1.0F, 0, 2, 0.0F, new float[] { 1.0F, 1.0F }, 0.0F);
        return b;
      }
    };
    
    Transformer<MyEdge, String> edgeLabelTransformer =  new Transformer<MyEdge, String>()
    	    {
		        public String transform(MyEdge edge)
		        {
		        	return "<html>" + edge.getEdgeTypeName();
		        }
    	    };
    	    
    Transformer<MyEdge, Paint> edgePaint = new Transformer<MyEdge, Paint>()
    {
      public Paint transform(MyEdge edge)
      {
        int res = edge.getEdgeType().getLineType() % 8;
        switch (res)
        {
        case 0: 
          return Color.BLACK;
        case 1: 
          return Color.RED;
        case 2: 
          return Color.BLUE;
        case 3: 
          return Color.CYAN;
       /* case 4: 
          return Color.DARK_GRAY;
        case 5: 
          return Color.GRAY;*/
        case 4: 
          return Color.GREEN;
      /*  case 7: 
          return Color.LIGHT_GRAY;*/
        case 5: 
          return Color.MAGENTA;
        case 6: 
          return Color.ORANGE;
        case 7: 
          return Color.PINK;
      /*  case 11: 
          return Color.YELLOW;
        case 12: 
          return Color.WHITE;*/
        }
        return Color.BLACK;
      }
    };
    
    vv.setEdgeToolTipTransformer(new Transformer<MyEdge,String>(){
        public String transform(MyEdge e) {
            return "<html>" + e.getEdgeInformations();
        }
    });
    
    vv.setVertexToolTipTransformer(new Transformer<MyNode,String>(){
        public String transform(MyNode n) {
        	if (!n.isDetailedOutput())
        		return "<html>" + n.getNodeInformations(true);
        	else
        		return "";
        }
    });
    
    this.vsh = new VertexStrokeHighlight<MyNode,MyEdge>(this.g, this.vv.getPickedVertexState());
    
    this.vsh.setHighlight(true, 1, new LinkedList<MyEdgeType>());
    
	//Vertex
    this.vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
    this.vv.getRenderContext().setVertexShapeTransformer(vertexSize);
    this.vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
    this.vv.getRenderContext().setVertexStrokeTransformer(this.vsh);
    this.vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
    
	//Edge
    this.vv.getRenderContext().setEdgeStrokeTransformer(edgeTransformer);
    //vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<MyEdge>());
    this.vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
    // vv.getRenderContext().setEdgeLabelClosenessTransformer(new MutableDirectionalEdgeValue(.3, .3));
    this.vv.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);

    this.gm = new DefaultModalGraphMouse<MyNode, MyEdge>();
    this.vv.setGraphMouse(this.gm);
    this.gm.add(new MyPopupGraphMousePlugin(this));
  }
  
  public VisualizationViewer<MyNode, MyEdge> getVisualisationViewer()
  {
    return this.vv;
  }
  
  public AbstractModalGraphMouse getAbstractModalGraphMouse()
  {
    return this.gm;
  }
  
  public void setNodeVisualisation(boolean details)
  {
    if (details != this.detailedNodes)
    {
      this.detailedNodes = details;
      
      if (collapser.CollapserActive())
    	  this.g = (Graph)this.gb.changeNodeDetails(details, g);
      else
    	  this.g = (Graph)this.gb.createGraph(details);
      
      this.vv.repaint();
    }
  }
  /**
   * Which Edge Types should be followed during highlight
   * @param types List with all the Edge Types which should be followed
   */
  public void setFollowEdgeTypes(LinkedList<MyEdgeType> types)
  {
	setFollowEdgeTypes(types,vsh.getSearchDepth());
  }
  
  /**
   * Which Edge Types should be followed during highlight
   * @param types List with all the Edge Types which should be followed
   * @param depth how deep should the Edges be followed
   */
  public void setFollowEdgeTypes(LinkedList<MyEdgeType> types, int depth)
  {
	 this.vsh = new VertexStrokeHighlight<MyNode, MyEdge>(g, this.vv.getPickedVertexState());
	 this.vsh.setHighlight(true, depth, types);
	 this.vv.getRenderContext().setVertexStrokeTransformer(this.vsh);
	 this.vv.repaint();
  }

  /**
   * Which Edge Types should be followed during highlight
    * @param depth how deep should the Edges be followed
   */
  public void setFollowEdgeTypes(int depth)
  {
	 setFollowEdgeTypes(vsh.getFollowEdges(), depth);
  }
  
  /**
   * Get the Edge Types which are followed during the highlighting
   * @return List with Edge Types
   */
  public LinkedList<MyEdgeType> getFollowEdgeTypes()
  {
    return this.vsh.getFollowEdges();
  }
  
  public void collapseNode ()
  {
	  Collection<MyNode> picked =new HashSet<MyNode>(vv.getPickedVertexState().getPicked());
      if(picked.size() == 1) {
      	MyNode root = picked.iterator().next();
      	
      	if (collapser.isCollapsable(root))
      	{
	      	collapser.collapseGraph( root);
	      	
	      	this.updateGraph();
      	}
      }
  }
  
  public void ExpandNode(boolean nodeDetails)
  {
	  Collection<MyNode> picked =new HashSet<MyNode>(vv.getPickedVertexState().getPicked());
      if(picked.size() == 1) {
      	MyNode root = picked.iterator().next();
      	
      	if (collapser.isExpandable(root))
      	{
	      	collapser.expandNode( root, nodeDetails);
	      	
	      	this.updateGraph();
      	}
      }
  }
  
  public boolean isExpandable ()
  {
	  Collection<MyNode> picked =new HashSet<MyNode>(vv.getPickedVertexState().getPicked());
      if(picked.size() == 1) {
      	MyNode root = picked.iterator().next();
      	
      	return collapser.isExpandable(root);
      }
      return false;
  }
  
  public boolean isCollapsable()
  {
	  Collection<MyNode> picked =new HashSet<MyNode>(vv.getPickedVertexState().getPicked());
      if(picked.size() == 1) {
      	MyNode root = picked.iterator().next();
      	
      	return collapser.isCollapsable(root);
      }
      return false;
  }



	public void applyNodeAndEdgeFilter(LinkedList<MyNodeType> nodes, LinkedList<MyEdgeType> edges, String viewName)
	{
		NodeAndEdgeTypeFilter.filter(nodes, edges, viewName);
		
		updateGraph();
	}
	
	public void undoNodeAndEdgeFilter(String viewName)
	{
		NodeAndEdgeTypeFilter.undoFilter(viewName);
		
		updateGraph();
	}
	
	public void updateGraph()
	{
		g = (Graph) gb.updateGraph(detailedNodes);
		
		vv.getPickedVertexState().clear();
	    vv.repaint();
	}
	
	public void setNodeColorMapping(HashMap<MyNodeType, Color> nodeColorMapping) {
		this.nodeColorMapping.putAll(nodeColorMapping);
		this.configWriterReader.setColors(nodeColorMapping);
		
		updateGraph();
	}
	
	public HashMap<MyNodeType, Color> getNodeColorMapping ()
	{
		return this.configWriterReader.readColors();
	}
	
	
	public void createNewGraphView (GraphViewContainer newView)
	{
		this.configWriterReader.setGraphView(newView);
	}
	
	public HashMap<String, GraphViewContainer> getAllGraphViews()
	{
		return this.configWriterReader.readViews();
	}
	
	public void deleteGraphView (GraphViewContainer deleteView)
	{
		this.configWriterReader.deleteView(deleteView.getViewName());
	}
	
	public void changeLayout (String newLayout)
	{
		//Dimension d =layout.getSize();
		
		if (newLayout.equals(KKLayout))
		{
			this.layout = new KKLayout<MyNode, MyEdge>(g);
		}
		if (newLayout.equals(FRLayout))
		{
			this.layout = new FRLayout<MyNode, MyEdge>(g);
		}
		if (newLayout.equals(SpringLayout))
		{
			this.layout = new SpringLayout<MyNode, MyEdge>(g);
		}
		if (newLayout.equals(ISOMLayout))
		{
			this.layout = new ISOMLayout<MyNode, MyEdge>(g);
		}
		if (newLayout.equals(CircleLayout))
		{
			this.layout = new CircleLayout<MyNode, MyEdge>(g);
		}
		
		layout.setInitializer(vv.getGraphLayout());
		layout.setSize(vv.getSize());
        
		LayoutTransition<MyNode,MyEdge> lt =new LayoutTransition<MyNode,MyEdge>(vv, vv.getGraphLayout(), layout);
		Animator animator = new Animator(lt);
		animator.start();
		vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
		vv.repaint();
	}
}