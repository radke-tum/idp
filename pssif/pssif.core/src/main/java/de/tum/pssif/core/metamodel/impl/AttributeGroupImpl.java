package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFUtil;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;


public class AttributeGroupImpl extends NamedImpl implements MutableAttributeGroup {
  private Map<String, Attribute> attributes = Maps.newHashMap();

  public AttributeGroupImpl(String name) {
    super(name);
  }

  @Override
  public Collection<Attribute> getAttributes() {
    return ImmutableSet.copyOf(attributes.values());
  }

  @Override
  public PSSIFOption<Attribute> getAttribute(String name) {
    return PSSIFOption.one(attributes.get(PSSIFUtil.normalize(name)));
  }

  @Override
  public Class<?> getMetaType() {
    return AttributeGroup.class;
  }

  @Override
  public void addAttribute(Attribute attribute) {
    attributes.put(PSSIFUtil.normalize(attribute.getName()), attribute);
  }

  @Override
  public void removeAttribute(Attribute attribute) {
    attributes.remove(PSSIFUtil.normalize(attribute.getName()));
  }
}
