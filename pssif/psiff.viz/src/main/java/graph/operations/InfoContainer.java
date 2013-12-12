package graph.operations;

import graph.model.MyEdge;
import graph.model.MyNode;

public class InfoContainer {
    	private MyEdge edge;
    	private MyNode source;
    	private MyNode destintation;
    	
    	public InfoContainer(MyEdge edge, MyNode source, MyNode destintation)
    	{
    		this.edge=edge;
    		this.source=source;
    		this.destintation=destintation;
    	}

		public MyEdge getEdge() {
			return edge;
		}

		public MyNode getEdgeSource() {
			return source;
		}

		public MyNode getEdgeDestintation() {
			return destintation;
		}
}
