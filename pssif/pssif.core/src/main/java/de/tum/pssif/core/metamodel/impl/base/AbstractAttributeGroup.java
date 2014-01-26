package de.tum.pssif.core.metamodel.impl.base;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.util.PSSIFUtil;


public abstract class AbstractAttributeGroup extends AbstractNamed implements AttributeGroup {
  private final Map<String, Attribute>    attributes = Maps.newHashMap();
  private final AbstractElementType<?, ?> owner;

  public AbstractAttributeGroup(String name, AbstractElementType<?, ?> owner) {
    super(name);
    this.owner = owner;
  }

  protected final void addAttribute(Attribute attribute) {
    this.attributes.put(PSSIFUtil.normalize(attribute.getName()), attribute);
  }

  @Override
  public final Attribute findAttribute(String name) {
    Attribute result = PSSIFUtil.find(name, this.attributes.values());
    if (result != null) {
      return result;
    }
    AbstractAttributeGroup generalGroup = owner.getGeneral() != null ? (AbstractAttributeGroup) owner.getGeneral().findAttributeGroup(getName())
        : null;
    if (generalGroup != null) {
      return generalGroup.findAttribute(name);
    }
    return null;
  }

  @Override
  public final Collection<Attribute> getAttributes() {
    Set<Attribute> result = Sets.<Attribute> newHashSet(this.attributes.values());
    if (owner.getGeneral() != null && owner.getGeneral().findAttributeGroup(getName()) != null) {
      result.addAll(owner.getGeneral().findAttributeGroup(getName()).getAttributes());
    }
    return Collections.unmodifiableCollection(result);

  }

  @Override
  public final Class<?> getMetaType() {
    return AttributeGroup.class;
  }

  @Override
  public final String toString() {
    return "AttributeGroup:" + this.getName();
  }

}
