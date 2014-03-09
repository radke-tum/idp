package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.mutable.MutableElementType;


public abstract class HideAttributeTransformation<T extends MutableElementType> extends AbstractTransformation {
  private final ElementType type;
  private final Attribute   attribute;

  public HideAttributeTransformation(ElementType type, Attribute attribute) {
    this.type = type;
    this.attribute = attribute;
  }

  protected ElementType getType() {
    return type;
  }

  protected Attribute getAttribute() {
    return attribute;
  }

  protected abstract PSSIFOption<T> getActualTarget(View view);

  @Override
  public final void apply(View view) {
    for (T actualTarget : getActualTarget(view).getMany()) {
      actualTarget.removeAttribute(getAttribute());
    }
  }
}
