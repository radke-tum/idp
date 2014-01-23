package de.tum.pssif.transform.transformation.viewed;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.impl.base.AbstractAttributeGroup;
import de.tum.pssif.core.metamodel.impl.base.AbstractElementType;


public class ViewedAttributeGroup extends AbstractAttributeGroup {
  private final AttributeGroup baseGroup;

  public ViewedAttributeGroup(AttributeGroup baseGroup, String name, AbstractElementType<?> owner) {
    super(name, owner);
    this.baseGroup = baseGroup;

    for (Attribute a : baseGroup.getAttributes()) {
      addAttribute(new ViewedAttribute(a, a.getName(), a.getType(), a.getUnit(), a.isVisible(), a.getCategory()));
    }
  }
}
