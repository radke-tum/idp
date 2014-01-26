package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Element;


public abstract class HideAttributeTransformation<T extends ElementType<T, E>, E extends Element> extends AbstractTransformation {
  private final T         type;
  private final Attribute attribute;

  public HideAttributeTransformation(T type, Attribute attribute) {
    this.type = type;
    this.attribute = attribute;
  }

  protected T getType() {
    return type;
  }

  protected Attribute getAttribute() {
    return attribute;
  }

  protected abstract T getActualTarget(View view);

  @Override
  public final Metamodel apply(View view) {
    getActualTarget(view).removeAttribute(getAttribute());
    return view;
  }
}
