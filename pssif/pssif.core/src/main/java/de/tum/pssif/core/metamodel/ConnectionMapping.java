package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;


public interface ConnectionMapping {
  EdgeEnd getFrom();

  EdgeEnd getTo();

  Edge create(Model model);
}
