package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.util.PSSIFOption;


public interface Edge extends Element {
  void connect(EdgeEnd end, Node node);

  void disconnect(EdgeEnd end, Node node);

  PSSIFOption<Node> get(EdgeEnd end);
}
