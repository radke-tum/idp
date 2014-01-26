package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.Named;


public abstract class RenameTransformation<T extends Named> extends AbstractTransformation {
  private final T      target;
  private final String name;

  public RenameTransformation(T target, String name) {
    this.target = target;
    this.name = name;
  }

  protected final T getTarget() {
    return target;
  }

  protected final String getName() {
    return name;
  }
}
