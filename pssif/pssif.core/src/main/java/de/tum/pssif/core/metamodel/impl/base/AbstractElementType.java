package de.tum.pssif.core.metamodel.impl.base;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.util.PSSIFUtil;


public abstract class AbstractElementType<T extends ElementType<T>> extends AbstractNamed implements ElementType<T> {
  private T                                 general         = null;
  private final Set<T>                      specializations = Sets.newHashSet();
  private final Set<AbstractAttributeGroup> attributeGroups = Sets.newHashSet();

  public AbstractElementType(String name) {
    super(name);
  }

  @Override
  public final T getGeneral() {
    return general;
  }

  @Override
  public final Collection<T> getSpecials() {
    return Collections.unmodifiableCollection(specializations);
  }

  //TODO get rid of the cast
  @SuppressWarnings("unchecked")
  @Override
  public final void inherit(T general) {
    if (this.equals(general)) {
      throw new PSSIFStructuralIntegrityException("can not inherit self");
    }
    if (this.general != null) {
      this.general.unregisterSpecialization((T) this);
    }
    this.general = general;
    this.general.registerSpecialization((T) this);
  }

  @Override
  public final void registerSpecialization(T special) {
    specializations.add(special);
  }

  @Override
  public final void unregisterSpecialization(T special) {
    specializations.remove(special);
  }

  @Override
  public final Attribute findAttribute(String name) {
    Attribute result = null;
    for (AbstractAttributeGroup group : this.attributeGroups) {
      result = group.findAttribute(name);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  @Override
  public final Collection<Attribute> getAttributes() {
    Collection<Attribute> attrs = Sets.newHashSet();
    for (AttributeGroup group : getAttributeGroups()) {
      attrs.addAll(group.getAttributes());
    }
    return Collections.unmodifiableCollection(attrs);
  }

  protected final void addAttributeGroup(AbstractAttributeGroup ag) {
    attributeGroups.add(ag);
  }

  protected final void removeAttributeGroup(AbstractAttributeGroup ag) {
    attributeGroups.remove(ag);
  }

  protected final void addAttribute(AttributeGroup ag, Attribute attribute) {
    AbstractAttributeGroup actual = findAttributeGroup(ag.getName());
    if (actual == null) {
      throw new PSSIFStructuralIntegrityException("Provided attribute group not part of this type.");
    }
    actual.addAttribute(attribute);
  }

  @Override
  public final Collection<AttributeGroup> getAttributeGroups() {
    return Collections.<AttributeGroup> unmodifiableCollection(this.attributeGroups);
  }

  @Override
  public final AttributeGroup getDefaultAttributeGroup() {
    return findAttributeGroup(PSSIFConstants.DEFAULT_ATTRIBUTE_GROUP_NAME);
  }

  @Override
  public final AbstractAttributeGroup findAttributeGroup(String name) {
    for (AbstractAttributeGroup atg : this.attributeGroups) {
      if (PSSIFUtil.areSame(name, atg.getName())) {
        return atg;
      }
    }
    return null;
  }
}
