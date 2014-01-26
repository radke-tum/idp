package de.tum.pssif.transform.metamodel.alternatives;

import java.util.Collection;

import de.tum.pssif.core.metamodel.Annotation;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.util.PSSIFOption;


public abstract class WrappingElementType<T extends ElementType<T, E>, E extends Element> implements ElementType<T, E> {

  private final ElementType<T, E> wrapped;
  private String                  name = null;

  protected WrappingElementType(ElementType<T, E> wrapped) {
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

  @Override
  public Collection<Annotation> getAnnotations(E element) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PSSIFOption<String> getAnnotationValue(E element, String key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setAnnotation(E element, String key, String value) {
    // TODO Auto-generated method stub

  }

}
