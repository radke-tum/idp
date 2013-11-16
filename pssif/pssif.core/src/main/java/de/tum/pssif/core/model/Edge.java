package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.util.PSSIFOption;


public interface Edge extends Element {

  PSSIFOption<Node> get(EdgeEnd end);
}
