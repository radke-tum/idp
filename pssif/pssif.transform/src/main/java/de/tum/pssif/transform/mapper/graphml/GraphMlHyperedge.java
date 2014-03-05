package de.tum.pssif.transform.mapper.graphml;

import java.util.Collection;


public interface GraphMlHyperedge extends GraphMLElement {
  Collection<String> getSourceIds();

  Collection<String> getTargetIds();

  Boolean isDirected();
}
