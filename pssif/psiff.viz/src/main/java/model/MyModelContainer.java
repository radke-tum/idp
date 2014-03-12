package model;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyEdgeTypes;
import graph.model.MyNode;
import graph.model.MyNodeType;
import graph.model.MyNodeTypes;

import java.util.Collection;
import java.util.LinkedList;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.MutableMetamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;


/**
 * A container which holds a model and a metamodel
 * The container provides also all the information from the model and metamodel in a format which can be displayed in a Graph or Matrix
 * @author Luc
 *
 */
public class MyModelContainer {

  private Model              model;
  private Metamodel   		 meta;
  private MyNodeTypes        nodeTypes;
  private MyEdgeTypes        edgeTypes;
  private LinkedList<MyNode> nodes;
  private LinkedList<MyEdge> edges;

  public MyModelContainer(Model model, Metamodel meta) {
    if (model != null && meta != null) {
      this.model = model;
      this.meta = (MutableMetamodel) meta;

      nodes = new LinkedList<MyNode>();
      edges = new LinkedList<MyEdge>();

      createNodeTypes();
      createEdgeTypes();

      createNodes();
      createEdges();
    }
    else {
      throw new NullPointerException("Metamodel or model null!");
    }
  }

  /**
   * parse the metamodel and create all the NodeTypes
   */
  private void createNodeTypes() {
    Collection<NodeType> types = meta.getNodeTypes();

    nodeTypes = new MyNodeTypes(types);
  }

  /**
   * parse the metamodel and create all the EdgeTypes
   */
  private void createEdgeTypes() {
    Collection<EdgeType> types = meta.getEdgeTypes();

    edgeTypes = new MyEdgeTypes(types);
  }

  /**
   * parse the model and create all the Nodes
   */
  private void createNodes() {
    for (MyNodeType t : nodeTypes.getAllNodeTypes()) {
      PSSIFOption<Node> tempNodes = t.getType().apply(model, false);

      if (tempNodes.isMany()) {
        for (Node tempNode : tempNodes.getMany()) {
          nodes.add(new MyNode(tempNode, t));
        }
      }
      if (tempNodes.isOne()) {
        nodes.add(new MyNode(tempNodes.getOne(), t));
      }

    }
  }

  /**
   * create all the Edges which are contained in the Model
   */
  private void createEdges() {
    for (MyEdgeType t : edgeTypes.getAllEdgeTypes()) {
      for (ConnectionMapping mapping : t.getType().getMappings()) {
        EdgeEnd from = mapping.getFrom();
        EdgeEnd to = mapping.getTo();
        
        PSSIFOption<Edge> edges = mapping.apply(model);
        if (edges.isMany())
        {
	        for (Edge e : edges.getMany()) {
	          //TODO luc: handle hyperedges correctly
	          for (Node source : from.apply(e).getMany()) {
	            for (Node target : to.apply(e).getMany()) {
	            	addEdge(new MyEdge(e, t, findNode(source), findNode(target)));
	            }
	          }
	        }
        }
        
        if (edges.isOne())
        {
        	Edge e = edges.getOne();
        	for (Node source : from.apply(e).getMany()) {
	            for (Node target : to.apply(e).getMany()) {
	            	addEdge(new MyEdge(e, t, findNode(source), findNode(target)));
	            }
	        }
        }
      }
    }
  }

  /**
   * Add a MyNode. Should not be used for MyNodes which where created via the Gui. Is not added to the Model!!!
   * @param node the MyNode in context
   */
  public void addNode(MyNode node) {
    if (!isContained(node)) {
      nodes.add(node);
    }
  }

  /**
   * Add a MyEdge. Should not be used for MyEdges which where created via the Gui. Is not added to the Model!!!
   * @param edge the MyEdge in context
   */
  public void addEdge(MyEdge edge) {
    if (!isContained(edge)) {
      edges.add(edge);
    }
  }

  /**
   * get all Nodes from the Model
   * @return List with the Nodes
   */
  public LinkedList<MyNode> getAllNodes() {
    return nodes;
  }

  /**
   * get all Edges from the Model
   * @return List with the Edges
   */
  public LinkedList<MyEdge> getAllEdges() {
    return edges;
  }

  /**
   * check if the given MyNode is already contained in the model
   * @param node the MyNode in context
   * @return true if it is contained, false otherwise
   */
  public boolean isContained(MyNode node) {
    return nodes.contains(node);
  }

  /**
   * check if the given MyEdge is already contained in the model
   * @param node the MyEdge in context
   * @return true if it is contained, false otherwise
   */
  public boolean isContained(MyEdge edge) {
    return edges.contains(edge);
  }

  /**
   * find the MyNode object which owns the given Node
   * @param n the Node in context
   * @return the MyNode object or null if no MyNode object exists for the given Node
   */
  public MyNode findNode(Node n) {
    for (MyNode current : nodes) {
      if (current.getNode().equals(n)) {
        return current;
      }
    }

    return null;
  }

  /**
   * find the MyEdge object which owns the given Edge
   * @param e the Edge in context
   * @return the MyEdge object or null if no MyEdge object exists for the given Edge
   */
  public MyEdge findEdge(Edge e) {
    for (MyEdge current : edges) {
      if (current.getEdge().equals(e)) {
        return current;
      }
    }

    return null;
  }

  /**
   * get all Node Types from the Model
   * @return the NodeTypes object
   */
  public MyNodeTypes getNodeTypes() {
    return nodeTypes;
  }

  /**
   * get all Edge Types from the Model
   * @return the EdgeTypes object
   */
  public MyEdgeTypes getEdgeTypes() {
    return edgeTypes;
  }

  /**
   * add an Edge which is added during a collapse operation
   * @param newEdge the new Edge 
   */
  public void addCollapserEdge(MyEdge newEdge) {
    newEdge.setCollapseEdge(true);
    edges.add(newEdge);
  }

  /**
   * remove an Edge which was added during a collapse operation
   * @param edge the edge in question
   */
  public void removeCollapserEdge(MyEdge edge) {
    if (edge.isCollapseEdge()) {
      edges.remove(edge);
    }
  }

  /**
   * Add a new Node which was created through the Gui
   * @param nodeName The name of the new Node
   * @param type The type of the new Node
   */
  public void addNewNodeFromGUI(String nodeName, MyNodeType type) {
    NodeType nodeType = meta.findNodeType(type.getName());

    Node newNode = nodeType.create(model);

    nodeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(newNode, PSSIFOption.one(PSSIFValue.create(nodeName)));

    nodes.add(new MyNode(newNode, type));
  }

  /**
   * Add a new Edge which was created from the Gui
   * @param source The start Node of the Edge
   * @param destination The destination Node of the Edge
   * @param edgetype The type of the Edge
   * @param directed should the new edge be directed
   * @return true if the add operation was successful, false otherwise
   */
  public boolean addNewEdgeGUI(MyNode source, MyNode destination, MyEdgeType edgetype, Boolean directed) {
    ConnectionMapping mapping = edgetype.getType().getMapping(source.getNodeType().getType(), destination.getNodeType().getType());

    if (mapping != null) {
      Edge newEdge = mapping.create(model, source.getNode(), destination.getNode());

      PSSIFOption<PSSIFValue> value = PSSIFOption.one(PSSIFValue.create(directed));
      edgetype.getType().findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED).set(newEdge, value);


      MyEdge e = new MyEdge(newEdge, edgetype, source, destination);

      edges.add(e);
      return true;
    }
    else {
      return false;
    }
  }

  public Model getModel() {
    return model;
  }

  public Metamodel getMetamodel() {
    return meta;
  }
}
