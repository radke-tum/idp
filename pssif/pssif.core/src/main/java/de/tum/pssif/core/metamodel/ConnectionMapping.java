package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;


public interface ConnectionMapping {
  EdgeEnd getFrom();

  EdgeEnd getTo();

  Edge create(Model model, Node from, Node to);

  void connectFrom(Edge edge, Node node);

  void connectTo(Edge edge, Node node);

  void disconnectFrom(Edge edge, Node node);

  void disconnectTo(Edge edge, Node node);
}
