package de.tum.pssif.core.model.operation;

import com.google.common.collect.Multimap;

import de.tum.pssif.core.metamodel.impl.EdgeEndImpl;
import de.tum.pssif.core.metamodel.impl.EdgeTypeImpl;
import de.tum.pssif.core.model.Node;


public final class CreateEdgeOperation {

  private final EdgeTypeImpl          edgeTypeImpl;
  private Multimap<EdgeEndImpl, Node> connections;

  public CreateEdgeOperation(EdgeTypeImpl edgeTypeImpl, Multimap<EdgeEndImpl, Node> connections) {
    this.edgeTypeImpl = edgeTypeImpl;
    this.connections = connections;
  }

  public EdgeTypeImpl getEdgeTypeImpl() {
    return this.edgeTypeImpl;
  }

  public Multimap<EdgeEndImpl, Node> getConnections() {
    return this.connections;
  }

}
