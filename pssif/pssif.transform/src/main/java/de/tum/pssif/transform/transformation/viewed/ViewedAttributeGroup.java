package de.tum.pssif.transform.transformation.viewed;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.impl.base.AbstractAttributeGroup;
import de.tum.pssif.core.metamodel.impl.base.AbstractElementType;


public class ViewedAttributeGroup extends AbstractAttributeGroup {
  public ViewedAttributeGroup(AttributeGroup baseGroup, String name, AbstractElementType<?, ?> owner) {
    super(name, owner);

    for (Attribute a : baseGroup.getDirectAttributes()) {
      addAttribute(new ViewedAttribute(a, a.getName(), a.getType(), a.getUnit(), a.isVisible(), a.getCategory()));
    }
  }
}
