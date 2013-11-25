package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.util.PSSIFUtil;


public abstract class ElementTypeImpl extends NamedImpl implements ElementType {
  private Collection<AttributeImpl> attributes = Sets.newHashSet();

  public ElementTypeImpl(String name) {
    super(name);
  }

  @Override
  public Attribute createAttribute(String name, DataType type, Unit unit) {
    AttributeImpl result = new AttributeImpl(name, type, unit);
    attributes.add(result);
    return result;
  }

  @Override
  public Attribute findAttribute(String name) {
    for (AttributeImpl attribute : attributes) {
      if (PSSIFUtil.areSame(name, attribute.getName())) {
        return attribute;
      }
    }
    return null;
  }

  @Override
  public Collection<Attribute> getAttributes() {
    return Collections.<Attribute> unmodifiableCollection(attributes);
  }

  @Override
  public void removeAttribute(Attribute attribute) {

    attributes.remove(attribute);
  }
}
