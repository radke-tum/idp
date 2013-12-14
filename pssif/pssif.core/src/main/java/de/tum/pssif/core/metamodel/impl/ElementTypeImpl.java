package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Enumeration;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.util.PSSIFUtil;


public abstract class ElementTypeImpl<T extends ElementType<T>> extends NamedImpl implements ElementType<T> {
  private T                      general         = null;
  private Set<T>                 specializations = Sets.newHashSet();
  private Map<String, Attribute> attributes      = Maps.newHashMap();

  public ElementTypeImpl(String name) {
    super(name);
  }

  @Override
  public T getGeneral() {
    return general;
  }

  @Override
  public Collection<T> getSpecials() {
    return Collections.unmodifiableCollection(specializations);
  }

  //TODO get rid of the cast
  @SuppressWarnings("unchecked")
  @Override
  public void inherit(T general) {
    this.general = general;
    general.registerSpecialization((T) this);
  }

  @Override
  public void registerSpecialization(T special) {
    specializations.add(special);
  }

  @Override
  public Attribute createAttribute(String name, DataType type, Unit unit) {
    if (name == null || name.trim().isEmpty()) {
      throw new PSSIFStructuralIntegrityException("name can not be null or empty");
    }
    String normalized = PSSIFUtil.normalize(name);
    if (attributes.containsKey(normalized)) {
      throw new PSSIFStructuralIntegrityException("duplicate attribute with name " + name);
    }
    //TODO this can be done better...
    if (!(type instanceof Enumeration) && !unit.isAllowedForDataType(type)) {
      throw new PSSIFStructuralIntegrityException("unit " + unit.getName() + " not allowed for data type " + type.getName());
    }
    else if (type instanceof Enumeration && unit != null) {
      throw new PSSIFStructuralIntegrityException("Enumerations can not have units");
    }
    Attribute result = new AttributeImpl(name, type, unit);
    attributes.put(normalized, result);
    return result;
  }

  @Override
  public Attribute findAttribute(String name) {
    return attributes.get(PSSIFUtil.normalize(name));
  }

  @Override
  public Collection<Attribute> getAttributes() {
    return Collections.unmodifiableCollection(attributes.values());
  }

  @Override
  public void removeAttribute(Attribute attribute) {
    attributes.remove(PSSIFUtil.normalize(attribute.getName()));
  }
}
