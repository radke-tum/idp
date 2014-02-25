package gui.graph;

import de.tum.pssif.core.PSSIFConstants;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import graph.listener.ConfigWriterReader;
import graph.listener.MiddleMousePlugin;
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import model.ModelBuilder;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ChainedTransformer;

public class GraphVisualization
{
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
    
    this.g = this.gb.createGraph(this.detailedNodes);
    

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
          Rectangle2D rec = new Rectangle2D.Double(-75.0D, -75.0D, 150.0D, 150.0D);
          if (node.getSize() != 0) {
            return 
              AffineTransform.getScaleInstance(node.getSize() + 1, node.getSize() + 1).createTransformedShape(rec);
          }
          return rec;
        }
        Rectangle2D rec = new Rectangle2D.Double(-75.0D, -25.0D, 150.0D, 50.0D);
        return rec;
      }
    };
    
    Transformer<MyNode, String> vertexLabelTransformer =  new Transformer<MyNode, String>()
    	    {
        public String transform(MyNode node)
        {
        	return "<html>" + node.getNodeInformations();
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
		        	return "<html>" + edge.getEdgeInformations();
		        }
    	    };
    	    
    Transformer<MyEdge, Paint> edgePaint = new Transformer<MyEdge, Paint>()
    {
      public Paint transform(MyEdge edge)
      {
        int res = edge.getEdgeType().getLineType() % 13;
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
        case 4: 
          return Color.DARK_GRAY;
        case 5: 
          return Color.GRAY;
        case 6: 
          return Color.GREEN;
        case 7: 
          return Color.LIGHT_GRAY;
        case 8: 
          return Color.MAGENTA;
        case 9: 
          return Color.ORANGE;
        case 10: 
          return Color.PINK;
        case 11: 
          return Color.YELLOW;
        case 12: 
          return Color.WHITE;
        }
        return Color.BLACK;
      }
    };
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
    this.gm.add(new MyPopupGraphMousePlugin());
    this.gm.add(new MiddleMousePlugin());
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
    	  this.g = this.gb.changeNodeDetails(details, g);
      else
    	  this.g = this.gb.createGraph(details);
      
      this.vv.repaint();
    }
  }
  
  public void setHighlightNodes(LinkedList<MyEdgeType> types)
  {
	setHighlightNodes(types,vsh.getSearchDepth());
  }
  
  public void setHighlightNodes(LinkedList<MyEdgeType> types, int depth)
  {
	 this.vsh = new VertexStrokeHighlight<MyNode, MyEdge>(g, this.vv.getPickedVertexState());
	 this.vsh.setHighlight(true, depth, types);
	 this.vv.getRenderContext().setVertexStrokeTransformer(this.vsh);
	 this.vv.repaint();
  }
  //TODO Strange
  public void setHighlightNodes(int depth)
  {
	 setHighlightNodes(vsh.getFollowEdges(), depth);
  }
//TODO Strange
  public LinkedList<MyEdgeType> getHighlightNodes()
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



public void applyNodeAndEdgeFilter(LinkedList<MyNodeType> nodes, LinkedList<MyEdgeType> edges)
{
	NodeAndEdgeTypeFilter.filter(nodes, edges);
	
	updateGraph();
}

public void updateGraph()
{
	g = gb.updateGraph(detailedNodes);
	
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
}

