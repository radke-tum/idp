package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.impl.CreateEdgeOperation;
import de.tum.pssif.core.metamodel.impl.CreateNodeOperation;
import de.tum.pssif.core.metamodel.impl.ReadNodesOperation;
import de.tum.pssif.core.util.PSSIFOption;


public interface Model {

  Node apply(CreateNodeOperation op);

  Edge apply(CreateEdgeOperation op);

  PSSIFOption<Node> apply(ReadNodesOperation op);
}
