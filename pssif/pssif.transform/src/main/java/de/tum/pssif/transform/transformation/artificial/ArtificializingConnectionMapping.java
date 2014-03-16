package de.tum.pssif.transform.transformation.artificial;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.transform.transformation.viewed.ViewedConnectionMapping;


public class ArtificializingConnectionMapping extends ViewedConnectionMapping {
  private final ConnectionMapping targetMapping;

  public ArtificializingConnectionMapping(ConnectionMapping baseMapping, EdgeType type, NodeTypeBase from, NodeTypeBase to, EdgeType targetType) {
    super(baseMapping, type, from, to);
    this.targetMapping = targetType.getMapping(from, to).getOne();
  }

  @Override
  public Edge create(Model model, Node from, Node to) {
    Edge artificial = targetMapping.create(model, from, to);
    targetMapping.getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne()
        .set(artificial, PSSIFOption.one(PSSIFValue.create(model.generateId())));

    Edge result = getBaseMapping().create(model, from, to);

    return result;
  }
}
