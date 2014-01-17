package de.tum.pssif.transform.metamodel.alternatives;

import java.util.Collection;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Unit;


public abstract class WrappingElementType<T extends ElementType<T>> implements ElementType<T> {

  private final ElementType<T> wrapped;
  private String               name = null;

  protected WrappingElementType(ElementType<T> wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public String getName() {
    return name == null ? wrapped.getName() : name;
  }

  protected void setName(String name) {
    this.name = name;
  }

  @Override
  public AttributeGroup createAttributeGroup(String name) {
    //TODO
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<AttributeGroup> getAttributeGroups() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AttributeGroup getDefaultAttributeGroup() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AttributeGroup findAttributeGroup(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void removeAttributeGroup(AttributeGroup attributeGroup) {
    // TODO Auto-generated method stub

  }

  @Override
  public Attribute findAttribute(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Attribute> getAttributes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Attribute createAttribute(AttributeGroup group, String name, DataType dataType, boolean visible, AttributeCategory category) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Attribute createAttribute(AttributeGroup group, String name, DataType dataType, Unit unit, boolean visible, AttributeCategory category) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public T getGeneral() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<T> getSpecials() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void inherit(T general) {
    // TODO Auto-generated method stub

  }

  @Override
  public void registerSpecialization(T special) {
    // TODO Auto-generated method stub

  }

  @Override
  public void unregisterSpecialization(T special) {
    // TODO Auto-generated method stub

  }

}
