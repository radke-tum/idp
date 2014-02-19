package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.ElementType;


public abstract class HideTypeTransformation<T extends ElementType<T, ?>> extends AbstractTransformation {
  private T type;

  protected T getType() {
    return type;
  }

  public HideTypeTransformation(T type) {
    this.type = type;
  }
}
