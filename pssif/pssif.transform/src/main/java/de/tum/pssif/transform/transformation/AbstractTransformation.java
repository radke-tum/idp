package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.Metamodel;


public abstract class AbstractTransformation implements Transformation {
  @Override
  public Metamodel apply(Metamodel input) {
    return new View(input).transform(this);
  }

  public abstract Metamodel apply(View view);
}
