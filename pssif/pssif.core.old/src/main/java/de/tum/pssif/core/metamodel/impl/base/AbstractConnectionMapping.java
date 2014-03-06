package de.tum.pssif.core.metamodel.impl.base;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;


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
}
