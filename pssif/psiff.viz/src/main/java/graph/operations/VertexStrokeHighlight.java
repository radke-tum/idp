package graph.operations;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import graph.model.ConnectionType;
import graph.model.MyEdge;
import graph.model.MyNode;

public class VertexStrokeHighlight<V,E> implements
	    Transformer<V,Stroke>
	    {
	        protected boolean highlight = false;
	        protected Stroke heavy = new BasicStroke(10);
	        protected Stroke medium = new BasicStroke(5);
	        protected Stroke light = new BasicStroke(1);
	        protected PickedInfo<V> pi;
	        protected Graph<V,E> graph;
	        private LinkedList<ConnectionType> followEdges;
	        private int dept;
	        private boolean specialSearch;
	        
	        
	        public VertexStrokeHighlight(Graph<V,E> graph, PickedInfo<V> pi)
	        {
	        	this.graph = graph;
	            this.pi = pi;
	            this.dept = 1;
	            this.followEdges = new LinkedList<ConnectionType>();
	            this.specialSearch=false;
	            
	        }
	        
	        public void setHighlight(boolean highlight, int searchDept, LinkedList<ConnectionType> followEdges)
	        {
	            this.highlight = highlight;
	            this.dept = searchDept;
	            this.followEdges = followEdges;
	            
	            if (this.dept!=1 || this.followEdges.size()!=0)
	            	this.specialSearch=true;
	            else
	            	this.specialSearch=false;
	        }
	        
	        public Stroke transform(V currentNode)
	        {
	            
	        	if (highlight)
	            {
	        	//	System.out.println("-----------------------");
	        	//	System.out.println(((MyNode) currentNode).getRealName());
	               // System.out.println("specialSearch "+specialSearch);
	            	if (specialSearch)
	                {
	                	if (pi.isPicked(currentNode))
	                	{
		                    System.out.println("Heavy");
	                		return heavy;
	                	}
		                else
		                {	                
		                	Collection<V>  predecessors = graph.getPredecessors(currentNode);
		                	if (predecessors!=null)
		                	{
		                		for (V v: predecessors)
		                		{
		                			if (pi.isPicked(v))
		                			{
		                				if(searchOutEdges(v, currentNode))
		                				{
		                					System.out.println("medium");
		                					return medium;
		                				}
		                				else
		                				{
		                					System.out.println("light");
		                					return light;
		                				}
		                			}
		                		}
		                	}
		                	System.out.println("outer light");
		                	return light;
		                }
	                	
	                }
	                else
	                {
		            	if (pi.isPicked(currentNode))
		                    return heavy;
		                else
		                {	                
		                	Collection<V>  col = graph.getPredecessors(currentNode);
		                	if (col!=null)
		                	{
			                	for(V w : col)
			                	{
			                        if (pi.isPicked(w))
			                            return medium;
			                    }
		                	}
		                    return light;
		                }
		            }
	            }
	            else
	                return light; 
	        }
	        
	        private boolean searchOutEdges(V source, V dest)
	        {
	        	@SuppressWarnings("unchecked")
				Collection<MyEdge> eout = ( Collection<MyEdge>) graph.getOutEdges(source);
	        	 
	        	 for (MyEdge e : eout)
	        	 {
	        		/* System.out.println(e.getConnectionType()+" dest "+e.getDestinationNode().getRealName()+
 							" source "+e.getSourceNode().getRealName());*/
	        		 if (this.followEdges.contains(e.getConnectionType()) && e.getDestinationNode().equals(((MyNode) dest)))
	        				 return true;
	        	 }
	        	 return false;
	        }
	        
	        public LinkedList<ConnectionType> getFollowEdges()
	        {
	        	return this.followEdges;
	        }
	        
}
