package gui.graph;

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
import graph.model2.MyEdge2;
import graph.model2.MyEdgeType;
import graph.model2.MyNode2;
import graph.model2.MyNodeType;
import graph.operations.GraphViewContainer;
import graph.operations.MyCollapser;
import graph.operations.NodeAndEdgeFilter;
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
  private Graph<MyNode2, MyEdge2> g;
  static int Nodecount;
  private AbstractModalGraphMouse gm;
  private VisualizationViewer<MyNode2, MyEdge2> vv;
  private Layout<MyNode2, MyEdge2> layout;
  private VertexStrokeHighlight<MyNode2, MyEdge2> vsh;
  private GraphBuilder gb;
  private boolean detailedNodes;
  private MyCollapser collapser;
  private LinkedList<MyNodeType> vizNodes;
  private LinkedList<MyEdgeType> vizEdges;
  private HashMap<MyNodeType,Color> nodeColorMapping;
  private ConfigWriterReader configWriterReader;
  
  public void reset()
  {
    this.g = this.gb.createGraph(this.detailedNodes);
    
    this.vv.repaint();
  }
  
  public GraphVisualization(Dimension d, boolean details)
  {
    int i = 1000;
    this.detailedNodes = details;
    this.gb = new GraphBuilder();
    this.collapser = new MyCollapser();
    configWriterReader = new ConfigWriterReader();
    setVizEdges(ModelBuilder.getEdgeTypes().getAllEdgeTypes());
    setVizNodes(ModelBuilder.getNodeTypes().getAllNodeTypes());
    this.nodeColorMapping = configWriterReader.readColors();
    
    this.g = this.gb.createGraph(this.detailedNodes);
    

    this.layout = new FRLayout<MyNode2, MyEdge2>(this.g);
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

    this.vv = new VisualizationViewer<MyNode2, MyEdge2>(this.layout);

    if (d != null) 
    {
      this.vv.setPreferredSize(d);
    }
    else 
    {
      this.vv.setPreferredSize(new Dimension(i, i));
    }
    
    Transformer<MyNode2, Paint> vertexColor = new Transformer<MyNode2, Paint>()
    {
      public Paint transform(MyNode2 i)
      {
      /*  if (i.getNodeType() == NodeType.FUNCTION) {
          return Color.GREEN;
        }
        return Color.RED;*/
    	
    	if (nodeColorMapping!=null)
    	{
	    	Color c = nodeColorMapping.get(i.getNodeType());
	    	if (c!=null)
	    		return c;
    	}
    	
    	return Color.LIGHT_GRAY;
      }
    };
    
    Transformer<MyNode2, Shape> vertexSize = new Transformer<MyNode2, Shape>()
    {
      public Shape transform(MyNode2 node)
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
    
    Transformer<MyNode2, String> vertexLabelTransformer = new ChainedTransformer<MyNode2, String>(new Transformer[] {
      new ToStringLabeller<String>(), 
      new Transformer<String,String>()
      {
        public String transform(String input)
        {
          return "<html>" + input;
        }
      } });
    
	  // Set up a new stroke Transformer for the edges
    Transformer<MyEdge2, Stroke> edgeTransformer = new Transformer<MyEdge2, Stroke>()
    {
      public Stroke transform(MyEdge2 edge)
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
    Transformer<MyEdge2, String> edgeLabelTransformer = new ChainedTransformer<MyEdge2, String>(new Transformer[] {
      new ToStringLabeller<String>(), 
      new Transformer<String,String>()
      {
        public String transform(String input)
        {
          return "<html>" + input;
        }
      } });
    Transformer<MyEdge2, Paint> edgePaint = new Transformer<MyEdge2, Paint>()
    {
      public Paint transform(MyEdge2 edge)
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
    this.vsh = new VertexStrokeHighlight<MyNode2,MyEdge2>(this.g, this.vv.getPickedVertexState());
    
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
    //vv.getRenderContext().setE
	//vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.CubicCurve<MyNode,MyEdge>());



    this.gm = new DefaultModalGraphMouse<MyNode2, MyEdge2>();
    this.vv.setGraphMouse(this.gm);
    this.gm.add(new MyPopupGraphMousePlugin());
    this.gm.add(new MiddleMousePlugin());
  }
  
  public Graph<MyNode2, MyEdge2> getGraph()
  {
    return this.g;
  }
  
  public VisualizationViewer<MyNode2, MyEdge2> getVisualisationViewer()
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
	 this.vsh = new VertexStrokeHighlight<MyNode2, MyEdge2>(g, this.vv.getPickedVertexState());
	 this.vsh.setHighlight(true, depth, types);
	 this.vv.getRenderContext().setVertexStrokeTransformer(this.vsh);
	 this.vv.repaint();
  }
  
  public void setHighlightNodes(int depth)
  {
	 setHighlightNodes(vsh.getFollowEdges(), depth);
  }
  
  public LinkedList<MyEdgeType> getHighlightNodes()
  {
    return this.vsh.getFollowEdges();
  }
  
  public void collapseNode ()
  {
	  Collection<MyNode2> picked =new HashSet<MyNode2>(vv.getPickedVertexState().getPicked());
      if(picked.size() == 1) {
      	MyNode2 root = picked.iterator().next();
      	
      	if (collapser.isCollapsable(root, g))
      	{
	      	collapser.collapseGraph(g, root);
	      	
	      	vv.getPickedVertexState().clear();
	        vv.repaint();
      	}
      }
  }
  
  public void ExpandNode(boolean nodeDetails)
  {
	  Collection<MyNode2> picked =new HashSet<MyNode2>(vv.getPickedVertexState().getPicked());
      if(picked.size() == 1) {
      	MyNode2 root = picked.iterator().next();
      	
      	if (collapser.isExpandable(root))
      	{
	      	collapser.expandNode(g, root, nodeDetails);
	      	
	      	vv.getPickedVertexState().clear();
	        vv.repaint();
      	}
      }
  }
  
  public boolean isExpandable ()
  {
	  Collection<MyNode2> picked =new HashSet<MyNode2>(vv.getPickedVertexState().getPicked());
      if(picked.size() == 1) {
      	MyNode2 root = picked.iterator().next();
      	
      	return collapser.isExpandable(root);
      }
      return false;
  }
  
  public boolean isCollapsable()
  {
	  Collection<MyNode2> picked =new HashSet<MyNode2>(vv.getPickedVertexState().getPicked());
      if(picked.size() == 1) {
      	MyNode2 root = picked.iterator().next();
      	
      	return collapser.isCollapsable(root, g);
      }
      return false;
  }

public LinkedList<MyNodeType> getVizNodes() {
	return vizNodes;
}

private void setVizNodes(LinkedList<MyNodeType> vizNodes) {
	this.vizNodes = vizNodes;
}

private void setVizNodes(MyNodeType[] vizNodes) {
	
	this.vizNodes = new LinkedList<MyNodeType>();
	for (MyNodeType n : vizNodes)
	{
		this.vizNodes.add(n);
	}
}

public LinkedList<MyEdgeType> getVizEdges() {
	return vizEdges;
}

private void setVizEdges(LinkedList<MyEdgeType> vizEdges) {
	this.vizEdges = vizEdges;
}

private void setVizEdges(MyEdgeType[] vizEdges) {
	
	this.vizEdges = new LinkedList<MyEdgeType>();
	for (MyEdgeType n : vizEdges)
	{
		this.vizEdges.add(n);
	}
}

public void applyNodeAndEdgeFilter(LinkedList<MyNodeType> nodes, LinkedList<MyEdgeType> edges)
{
	NodeAndEdgeFilter.filter(g, nodes, edges);
	setVizEdges(edges);
	setVizNodes(nodes);
	
	collapser.reset();
	
	vv.getPickedVertexState().clear();
    vv.repaint();
}

public void setNodeColorMapping(HashMap<MyNodeType, Color> nodeColorMapping) {
	this.nodeColorMapping.putAll(nodeColorMapping);
	this.configWriterReader.setColors(nodeColorMapping);
	
	vv.getPickedVertexState().clear();
    vv.repaint();
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
  
  /*	  private class MutableDirectionalEdgeValue extends ConstantDirectionalEdgeValueTransformer<MyNode,MyEdge> {
	        BoundedRangeModel undirectedModel = new DefaultBoundedRangeModel(5,0,0,10);
	        BoundedRangeModel directedModel = new DefaultBoundedRangeModel(7,0,0,10);
	        
	        public MutableDirectionalEdgeValue(double undirected, double directed) {
	            super(undirected, directed);
	            undirectedModel.setValue((int)(undirected*10));
	            directedModel.setValue((int)(directed*10));
	            
	            undirectedModel.addChangeListener(new ChangeListener(){
	                public void stateChanged(ChangeEvent e) {
	                    setUndirectedValue(new Double(undirectedModel.getValue()/10f));
	                    vv.repaint();
	                }
	            });
	            directedModel.addChangeListener(new ChangeListener(){
	                public void stateChanged(ChangeEvent e) {
	                    setDirectedValue(new Double(directedModel.getValue()/10f));
	                    vv.repaint();
	                }
	            });
	        }
	        /**
	         * @return Returns the directedModel.
	         */
	/*        public BoundedRangeModel getDirectedModel() {
	            return directedModel;
	        }

	        /**
	         * @return Returns the undirectedModel.
	         */
/*	        public BoundedRangeModel getUndirectedModel() {
	            return undirectedModel;
	        }
	    }*/
}

