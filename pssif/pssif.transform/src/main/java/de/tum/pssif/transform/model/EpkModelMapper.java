package de.tum.pssif.transform.model;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.transform.ModelMapper;
import de.tum.pssif.transform.graph.Graph;


public class EpkModelMapper implements ModelMapper {

  @Override
  public Model read(Metamodel metamodel, Graph graph) {
    // TODO Auto-generated method stub
    return new ModelImpl();
  }

  @Override
  public Graph write(Metamodel metamodel, Model model) {
    // TODO Auto-generated method stub
    return new Graph();
  }

}
