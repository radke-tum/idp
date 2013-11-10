package de.tum.pssif.core.metamodel;

import java.util.Collection;


public interface Metamodel {

  NodeType createNode(String name);

  EdgeType createEdge(String name);

  EdgeType createEdge(String name, String oppositeName);

  Collection<ElementType> getElementTypes();

  Collection<NodeType> getNodeTypes();

  Collection<EdgeType> getEdgeTypes();

  ElementType findElementType(String name);

  NodeType findNodeType(String name);

  EdgeType findEdgeType(String name);

  void delete(ElementType elementType);

}
