package de.tum.pssif.core.metamodel.impl.base;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.util.PSSIFOption;


public abstract class AbstractConnectionMapping implements ConnectionMapping {
  private final EdgeEnd from;
  private final EdgeEnd to;

  public AbstractConnectionMapping(EdgeEnd from, EdgeEnd to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public final EdgeEnd getFrom() {
    return from;
  }

  @Override
  public final EdgeEnd getTo() {
    return to;
  }

  @Override
  public PSSIFOption<Edge> apply(Model model) {
    return model.apply(new ReadEdgesOperation(this));
  }
}
