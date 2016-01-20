package graph.listener;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;


public class EmptyLayout<V, E> extends AbstractLayout<V, E> implements IterativeContext {

	protected EmptyLayout(Graph<V, E> graph, Transformer<V, Point2D> initializer, Dimension size) {
		super(graph, initializer, size);
	}

	@Override
	public void initialize() {
		
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void step() {
		
	}

	@Override
	public boolean done() {
		return true;
	}

}
