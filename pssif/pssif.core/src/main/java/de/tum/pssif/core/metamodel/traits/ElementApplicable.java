package de.tum.pssif.core.metamodel.traits;

import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public interface ElementApplicable {
  PSSIFOption<Edge> apply(Node node);

  PSSIFOption<Node> apply(Edge edge);
}
