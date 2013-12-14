package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.util.PSSIFOption;


public interface Node extends Element {

  PSSIFOption<Edge> get(EdgeEnd end);

  void connect(EdgeEnd end, Edge edge);

  void disconnect(EdgeEnd end, Edge edge);
}
