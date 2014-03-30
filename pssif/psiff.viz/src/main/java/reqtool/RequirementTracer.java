package reqtool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Node;
import model.ModelBuilder;
import graph.model.IMyNode;
import graph.model.MyEdge;
import graph.model.MyNode;

public class RequirementTracer {
 
 private static MyNode mNode;
 private static LinkedList<MyNode> mTracedNodes = new LinkedList<MyNode>();
 
 private static List<String> traceableEdges = new ArrayList<String>();
 
 
 public static void traceRequirement(Node node) {
  mNode = getMyNode(node);
  mTracedNodes.clear();
  traceableEdges.add(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON);
  traceableEdges.add(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP);
  traceableEdges.add(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_LOGICAL_SATISFIES);
  
    System.out.println("Tracing "+mNode.getName());
  
  for (MyEdge edge : ModelBuilder.getAllEdges()){
   if (edge.getSourceNode().getNode().getId().equals(node.getId()) && (traceableEdges.contains(edge.getEdgeType().getName())))
  {
   System.out.println("Next node is: "+edge.getDestinationNode().getName());
   traceRequirementDestination(edge.getDestinationNode().getNode());
   
  }
  else if (edge.getDestinationNode().getNode().getId().equals(node.getId()) && (traceableEdges.contains(edge.getEdgeType().getName())))
  {
   traceRequirementSource(edge.getSourceNode().getNode());
   System.out.println("Previous node is: "+edge.getSourceNode().getName());
  }
 }
 }


 
 
 
 public static MyNode getMyNode(Node node) {
  for (MyNode n : ModelBuilder.getAllNodes()) {
   if (n.getNode().getId().equals(node.getId())) {
    return n;
   }
  }
  return null;
 }
 
 public static void traceRequirementDestination(Node node) {
  mTracedNodes.add(getMyNode(node));
  for (MyEdge edge : ModelBuilder.getAllEdges()){
   if (edge.getSourceNode().getNode().getId().equals(node.getId()) && (traceableEdges.contains(edge.getEdgeType().getName())))
  {
    System.out.println("Next node is: "+edge.getDestinationNode().getName());
    mTracedNodes.add((MyNode)edge.getDestinationNode());
   
  }
   }
  
 }
 
 public static void traceRequirementSource(Node node) {
  mTracedNodes.add(getMyNode(node));
  for (MyEdge edge : ModelBuilder.getAllEdges()){
   if (edge.getDestinationNode().getNode().getId().equals(node.getId()) && (traceableEdges.contains(edge.getEdgeType().getName())))
   {
    System.out.println("Previous node is: "+edge.getSourceNode().getName());
    mTracedNodes.add((MyNode)edge.getSourceNode());
   }
  }
 }
 
 public static IMyNode getTracedNode() {
  return mNode;
 }
 
 public static boolean isTracedNode(IMyNode node){
  if (mNode!= null && mNode.getNode().getId().equals(node.getNode().getId())) {
   return true;
  }
  return false;
 }
 
 public static boolean isOnTraceList(IMyNode node) {
  for (MyNode n:mTracedNodes) {
   if (n.getNode().getId().equals(node.getNode().getId())) {
    return true;
   }
  }
  return false;
 }
 
 public static LinkedList<MyNode> getTracedNodes() {
  return mTracedNodes;
 }
 
 public static void stopTracing() {
  mNode = null;
  mTracedNodes.clear();
 }

}