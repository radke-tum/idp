package de.tum.pssif.transform.transformation.joined;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.transform.transformation.viewed.ViewedConnectionMapping;


public class LeftOutgoingJoinedConnectionMapping extends ViewedConnectionMapping {
  private final ConnectionMapping joinedMapping;
  private final ConnectionMapping targetMapping;

  public LeftOutgoingJoinedConnectionMapping(ConnectionMapping baseMapping, EdgeType type, NodeTypeBase from, NodeTypeBase to,
      ConnectionMapping joinedMapping) {
    super(baseMapping, type, from, to);
    this.joinedMapping = joinedMapping;
    this.targetMapping = type.getMapping(from, to).getOne();
  }

  @Override
  public Edge create(Model model, Node from, Node to) {
    //we expect exactly one node being connected to from via the joined mapping, otherwise we need to return PSSIFOption<Edge> with possibly none or many
    Node actualFromNode = joinedMapping.applyTo(joinedMapping.applyOutgoing(from).getOne());
    return targetMapping.create(model, actualFromNode, to);
  }
}
