package de.tum.pssif.transform.transformation.retyped;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;
import de.tum.pssif.transform.transformation.viewed.ViewedConnectionMapping;


public class RetypedConnectionMapping extends ViewedConnectionMapping {
  private final ConnectionMapping originalMapping;

  public RetypedConnectionMapping(ConnectionMapping baseMapping, EdgeEnd from, EdgeEnd to, ConnectionMapping originalMapping) {
    super(baseMapping, from, to);
    this.originalMapping = originalMapping;
  }

  @Override
  public Edge create(Model model, Node from, Node to) {
    Edge result = getBaseMapping().create(model, from, to);

    EdgeType origType = originalMapping.getFrom().getEdgeType();
    Attribute id = origType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    Edge originalEdge = originalMapping.create(model, from, to);
    id.set(originalEdge, PSSIFOption.one(PSSIFValue.create(origType.getName() + "_" + from.getId() + "_" + to.getId())));

    return result;
  }

  @Override
  public PSSIFOption<Edge> apply(Model model) {
    Collection<Edge> result = Sets.newHashSet();

    PSSIFOption<Edge> baseResult = getBaseMapping().apply(model);
    for (Edge e : baseResult.getMany()) {
      PSSIFOption<Node> froms = getBaseMapping().getFrom().apply(e);
      PSSIFOption<Node> tos = getBaseMapping().getTo().apply(e);
      for (Edge orig : originalMapping.apply(model).getMany()) {
        if (froms.equals(originalMapping.getFrom().apply(orig)) && tos.equals(originalMapping.getTo().apply(orig))) {
          result.add(e);
        }
      }
    }

    return PSSIFOption.many(result);
  }
}
