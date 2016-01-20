package gui.enhancement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import edu.uci.ics.jung.visualization.util.Caching;
import graph.model.IMyNode;
import graph.model.MyEdge;
import gui.graph.GraphVisualization;
import gui.mouse.MouseCommands;

@SuppressWarnings("serial")
public class EnhancedVisualizationViewer extends VisualizationViewer<IMyNode, MyEdge> implements MouseWheelListener, DropTargetListener, KeyListener{
	
	private GraphVisualization gViz;
	public int subdivision_size = 30; 
	
	
	public void setGraphVisualization(GraphVisualization g)
	{
		this.gViz = g;
	}
	
	public EnhancedVisualizationViewer(Layout<IMyNode, MyEdge> layout) {
		super(layout);
		setBackground(Color.WHITE);
	    setForeground(Color.BLACK);	
	    setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    this.addMouseWheelListener(this);
	    this.addKeyListener(this);
	    new DropTarget(this, this);
	}

	public void paintGrid(Graphics2D g2)
	{
		if (subdivision_size == 0)
			subdivision_size = 10;
		int SUBDIVISIONS_WIDTH = getSize().width / subdivision_size;
        int SUBDIVISIONS_HEIGHT = getSize().height / subdivision_size;
      
        for (int i = 1; i < SUBDIVISIONS_WIDTH; i++) {
        	g2.setColor(Color.LIGHT_GRAY);
        	if ((i-(i/4)*4) == 0)
        		g2.setPaint(Color.DARK_GRAY);
           int x = i * subdivision_size;
           g2.drawLine(x, 0, x, getSize().height);
        }
        for (int i = 1; i < SUBDIVISIONS_HEIGHT; i++) {
        	g2.setColor(Color.LIGHT_GRAY);
        	if ((i-(i/4)*4) == 0)
        		g2.setPaint(Color.DARK_GRAY);
           int y = i * subdivision_size;
           g2.drawLine(0, y, getSize().width, y);
           
        }   
  
	}
	
	public void mouseWheelMoved(int notches) {
		if (notches < 0)
		{
			if (subdivision_size > 10)		
				subdivision_size = subdivision_size + ((notches)*10);
		}
		else
		{
			if (subdivision_size < 140)
				subdivision_size = subdivision_size + ((notches)*10);
		}
		this.repaint();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
    	
    	int notches = e.getWheelRotation();
    	if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
    		mouseWheelMoved(notches);
    }

	@Override
	protected void renderGraph(Graphics2D g2d) {
	    if(renderContext.getGraphicsContext() == null) {
	        renderContext.setGraphicsContext(new GraphicsDecorator(g2d));
        } else {
        	renderContext.getGraphicsContext().setDelegate(g2d);
        }
        renderContext.setScreenDevice(this);
	    Layout<IMyNode,MyEdge> layout = model.getGraphLayout();

		g2d.setRenderingHints(renderingHints);
		
		// the size of the VisualizationViewer
		Dimension d = getSize();
		
		// clear the offscreen image
		g2d.setColor(getBackground());
		g2d.fillRect(0,0,d.width,d.height);
		
		
		// painting the background grid area
		if (gViz.isGridEnabled())
			paintGrid(g2d);

		AffineTransform oldXform = g2d.getTransform();
        AffineTransform newXform = new AffineTransform(oldXform);
        newXform.concatenate(
        		renderContext.getMultiLayerTransformer().getTransformer(Layer.VIEW).getTransform());
//        		viewTransformer.getTransform());
		
        g2d.setTransform(newXform);

		// if there are  preRenderers set, paint them
		for(Paintable paintable : preRenderers) {

		    if(paintable.useTransform()) {
		        paintable.paint(g2d);
		    } else {
		        g2d.setTransform(oldXform);
		        paintable.paint(g2d);
                g2d.setTransform(newXform);
		    }
		}
		
		
        if(layout instanceof Caching) {
        	((Caching)layout).clear();
        }
        
        /*boolean ok = true;
        for (final IMyNode imn : gViz.getGraph().getVertices())
        {
        	Point2D pt = layout.transform(imn);
        	if (pt == null)
        	{
        		layout.setInitializer(new Transformer<IMyNode, Point2D>() {
					
					@Override
					public Point2D transform(IMyNode arg0) {
						Point2D pp;
						if (arg0 == imn)
							return new Point2D(0,0);
					}
				});
        		//ok = false;
        		//break;
        	}
        }
        
        if (ok)*/
        	renderer.render(renderContext, layout);
		
		// if there are postRenderers set, do it
		for(Paintable paintable : postRenderers) {

		    if(paintable.useTransform()) {
		        paintable.paint(g2d);
		    } else {
		        g2d.setTransform(oldXform);
		        paintable.paint(g2d);
                g2d.setTransform(newXform);
		    }
		}
		g2d.setTransform(oldXform);
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		MouseCommands mcommands = new MouseCommands(gViz);
		mcommands.createNode(dtde.getLocation());
	}
	

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == '+' || e.getKeyChar() == '=')
		{
			int x = (int) this.getCenter().getX();
			int y = (int) this.getCenter().getY();
			this.getGraphMouse().mouseWheelMoved(new MouseWheelEvent(this, 0, 0, 0, 0, 0, x, y, MouseWheelEvent.WHEEL_UNIT_SCROLL
			, true, 1, 1, 1));
			this.mouseWheelMoved(1);
		} else if (e.getKeyChar() == '-' || e.getKeyChar() == '_')
		{
			int x = (int) this.getCenter().getX();
			int y = (int) this.getCenter().getY();
			this.getGraphMouse().mouseWheelMoved(new MouseWheelEvent(this, 0, 0, 0, 0, 0, x, y, MouseWheelEvent.WHEEL_UNIT_SCROLL
			, true, 1, -1, -1));
			this.mouseWheelMoved(-1);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	

}
