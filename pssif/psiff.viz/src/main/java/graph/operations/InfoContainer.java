package graph.operations;

import graph.model.MyEdge;
import graph.model.MyNode;
import graph.model2.MyEdge2;
import graph.model2.MyNode2;

public class InfoContainer {
    	private MyEdge2 edge;
    	private MyNode2 source;
    	private MyNode2 destintation;
    	
    	public InfoContainer(MyEdge2 edge, MyNode2 source, MyNode2 destintation)
    	{
    		this.edge=edge;
    		this.source=source;
    		this.destintation=destintation;
    	}

		public MyEdge2 getEdge() {
			return edge;
		}

		public MyNode2 getEdgeSource() {
			return source;
		}

		public MyNode2 getEdgeDestintation() {
			return destintation;
		}
}
