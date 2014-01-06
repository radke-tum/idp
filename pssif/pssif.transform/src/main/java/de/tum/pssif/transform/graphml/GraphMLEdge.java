package de.tum.pssif.transform.graphml;

public interface GraphMLEdge extends GraphMLElement {
  String getSourceId();

  String getTargetId();
}
