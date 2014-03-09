package de.tum.pssif.transform.transformation.delegating;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.NodeTypeBase;


/* package */final class ConnectionMappingDelegateDescriptor {
  private final ConnectionMapping baseMapping;
  private final NodeTypeBase      from;
  private final NodeTypeBase      to;

  /*package*/ConnectionMappingDelegateDescriptor(NodeTypeBase from, NodeTypeBase to, ConnectionMapping baseMapping) {
    this.from = from;
    this.to = to;
    this.baseMapping = baseMapping;
  }

  /*package*/ConnectionMapping getBaseMapping() {
    return baseMapping;
  }

  /*package*/NodeTypeBase getFrom() {
    return from;
  }

  /*package*/NodeTypeBase getTo() {
    return to;
  }
}