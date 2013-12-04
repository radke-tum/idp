package graph.listener;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ChainedTransformer;

import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.FRLayout ;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.RotatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import graph.model.ConnectionType;
import graph.model.GraphBuilder;
import graph.model.MyEdge;
import graph.model.MyNode;
import graph.model.NodeType;
import graph.operations.VertexStrokeHighlight;

public class GraphVisualization {
	
	private Graph<MyNode, MyEdge> g;
	static int Nodecount;
	private AbstractModalGraphMouse gm ;
	private VisualizationViewer<MyNode, MyEdge> vv;
	private Layout<MyNode, MyEdge> layout;
	private VertexStrokeHighlight<MyNode,MyEdge> vsh;

	private GraphBuilder gb;
	private boolean detailedNodes;
	
	
	public void reset()
	{
		g = gb.createGraph(detailedNodes);
		
		vv.repaint();
														
	}
	

	
	public GraphVisualization(Dimension d, boolean details) {
		
		int i =1000;
		detailedNodes = details;
		gb = new GraphBuilder();
		
		g = gb.createGraph(detailedNodes);
		
		// Layout implements the graph drawing logic
		layout = new FRLayout<MyNode, MyEdge>(g);
		if (d!=null)
		{
			int x = (int) d.getWidth()-50;
			int y = (int) d.getHeight()-50;
			layout.setSize(new Dimension(x,y));
		
		}
		else
			layout.setSize(new Dimension(i, i));
		
		
		// VisualizationServer actually displays the graph
		vv = new VisualizationViewer<MyNode, MyEdge>(layout);
		if (d!=null)
		{
			/*int x = (int) d.getWidth()-100;
			int y = (int) d.getHeight()-100;*/
			vv.setPreferredSize(d);
		}
		else
			vv.setPreferredSize(new Dimension(i, i)); // Sets the viewing area size
		// Transformer maps the vertex number to a vertex property
		
		Transformer<MyNode, Paint> vertexColor = new Transformer<MyNode, Paint>() {
			public Paint transform(MyNode i) {
				if (i.getNodeType() == NodeType.FUNCTION)
					return Color.GREEN;
				else
					return Color.RED;
			}
		};
		Transformer<MyNode, Shape> vertexSize = new Transformer<MyNode, Shape>() {
			public Shape transform(MyNode node) {
				
				if (node.isDetailedOutput())
				{
					Rectangle2D rec = new Rectangle2D.Double(-75, -75, 150, 150);
					if (node.getSize() !=0 )
						return AffineTransform.getScaleInstance(node.getSize()+1, node.getSize()+1)
								.createTransformedShape(rec);
					else
						return rec;
				}
				else
				{
					Rectangle2D rec = new Rectangle2D.Double(-75, -25, 150, 50);
					return rec;
				}

				
			}
		};
		
		Transformer<MyNode, String> vertexLabelTransformer = new ChainedTransformer<MyNode,String>(new Transformer[]{
	            new ToStringLabeller<String>(),
	            new Transformer<String,String>() {
	            public String transform(String input) {
	              //  return "<html><font color=\"yellow\">"+input;
	                return "<html>"+input;
	            }}});
		
		
		// Set up a new stroke Transformer for the edges		
		Transformer<MyEdge, Stroke> edgeTransformer = new Transformer<MyEdge, Stroke>() {
			public Stroke transform(MyEdge edge) {
				
				BasicStroke b;
				
		    	int res = edge.getConnectionType().getLineType() % 4;
		    	switch (res)				
				{
					case 0 :return RenderContext.DASHED;
				
					case 1: return RenderContext.DOTTED; 
							
					case 2:
						b = new BasicStroke(1,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,new float[] { 20, 12 }, 0);
						return b;
					
					case 3: 
						b = new BasicStroke(5,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,new float[] { 1, 1 }, 0);
						return b;
						
					default : 
						b = new BasicStroke(1,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,new float[] { 1, 1 }, 0);
						return b;
				}
				
			}
		};
		
		Transformer<MyEdge, String> edgeLabelTransformer = new ChainedTransformer<MyEdge,String>(new Transformer[]{
	            new ToStringLabeller<String>(),
	            new Transformer<String,String>() {
	            public String transform(String input) {
	              //  return "<html><font color=\"yellow\">"+input;
	                return "<html>"+input;
	            }}});
		
		
		Transformer<MyEdge, Paint> edgePaint = new Transformer<MyEdge, Paint>() {
		    public Paint transform(MyEdge edge) {
		    	
		    	int res = edge.getConnectionType().getLineType() % 13;
		    	switch (res)
				{
					case 0: return Color.BLACK;
					case 1: return Color.YELLOW;
					case 2: return Color.BLUE;				
					case 3: return Color.CYAN;				
					case 4: return Color.DARK_GRAY;				
					case 5: return Color.GRAY;				
					case 6: return Color.GREEN;					
					case 7: return Color.LIGHT_GRAY;
					case 8: return Color.MAGENTA;
					case 9: return Color.ORANGE;
					case 10: return Color.PINK;
					case 11: return Color.RED;
					case 12: return Color.WHITE;					
					default : return Color.BLACK;
				}
		    }
		};
		

		
		vsh = new VertexStrokeHighlight<MyNode,MyEdge>(g, vv.getPickedVertexState());
		
		vsh.setHighlight(true,1, new LinkedList<ConnectionType>());

		//Vertex
		vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
		vv.getRenderContext().setVertexShapeTransformer(vertexSize);
		vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
		vv.getRenderContext().setVertexStrokeTransformer(vsh);
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		
		//Edge
		vv.getRenderContext().setEdgeStrokeTransformer(edgeTransformer);
		//vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<MyEdge>());
		vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
		// vv.getRenderContext().setEdgeLabelClosenessTransformer(new MutableDirectionalEdgeValue(.3, .3));
		vv.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
		//vv.getRenderContext().setE
		//vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.CubicCurve<MyNode,MyEdge>());
		
		gm= new DefaultModalGraphMouse<MyNode, MyEdge>();
		vv.setGraphMouse(gm);
		gm.add(new MyPopupGraphMousePlugin());
		gm.add(new MiddleMousePlugin());
		
	
	}

	public Graph<MyNode, MyEdge> getGraph() {
		return g;
	}

	public VisualizationViewer<MyNode, MyEdge> getVisualisationViewer() {
		return vv;
	}

	public AbstractModalGraphMouse getAbstractModalGraphMouse() {
		return gm;
	}
	
	
	public void setNodeVisualisation(boolean details)
	{
		if (details!=detailedNodes)
		{
			detailedNodes= details;
			g = gb.createGraph(details);
			
			vv.repaint();
		}
	}
	
	public void setHighlightNodes(LinkedList<ConnectionType> types)
	{
		vsh.setHighlight(true,1, types);
	}
	
	public LinkedList<ConnectionType> getHighlightNodes()
	{
		return vsh.getFollowEdges();
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


