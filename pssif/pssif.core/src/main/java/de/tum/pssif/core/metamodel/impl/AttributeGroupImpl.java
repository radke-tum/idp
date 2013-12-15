package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.util.PSSIFUtil;


public class AttributeGroupImpl extends NamedImpl implements AttributeGroup {

  private Map<String, Attribute> attributes = Maps.newHashMap();

  public AttributeGroupImpl(String name) {
    super(name);
  }

  void addAttribute(AttributeImpl attribute) {
    this.attributes.put(PSSIFUtil.normalize(attribute.getName()), attribute);
  }

  @Override
  public Attribute findAttribute(String name) {
    return this.attributes.get(PSSIFUtil.normalize(name));
  }

  @Override
  public Collection<Attribute> getAttributes() {
    return Collections.unmodifiableCollection(this.attributes.values());
  }

  @Override
  public void removeAttribute(Attribute attribute) {
    attributes.remove(PSSIFUtil.normalize(attribute.getName()));
  }

  @Override
  public Class<?> getMetaType() {
    return AttributeGroup.class;
  }

}
