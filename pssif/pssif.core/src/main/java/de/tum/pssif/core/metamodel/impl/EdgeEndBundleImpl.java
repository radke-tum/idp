package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.traits.ElementApplicable;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class EdgeEndBundleImpl implements ElementApplicable {
  private final Collection<EdgeEnd> bundled;

  public EdgeEndBundleImpl(Collection<EdgeEnd> bundled) {
    this.bundled = bundled;
  }

  @Override
  public PSSIFOption<Edge> apply(Node node) {
    PSSIFOption<Edge> result = PSSIFOption.none();
    for (EdgeEnd end : bundled) {
      result = PSSIFOption.merge(result, end.apply(node));
    }
    return result;
  }

  @Override
  public PSSIFOption<Node> apply(Edge edge) {
    PSSIFOption<Node> result = PSSIFOption.none();
    for (EdgeEnd end : bundled) {
      result = PSSIFOption.merge(result, end.apply(edge));
    }
    return result;
  }
}
