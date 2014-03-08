package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;

/*package*/interface MutableAttributeGroup extends AttributeGroup {
  void addAttribute(Attribute attribute);

  void removeAttribute(Attribute attribute);
}