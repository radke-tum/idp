package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.Enumeration;
import de.tum.pssif.core.metamodel.EnumerationLiteral;
import de.tum.pssif.core.util.PSSIFUtil;
import de.tum.pssif.core.util.PSSIFValue;


public class EnumerationImpl extends NamedImpl implements Enumeration {
  private final Collection<EnumerationLiteralImpl> literals = Sets.newHashSet();

  public EnumerationImpl(String name) {
    super(name);
  }

  @Override
  public EnumerationLiteral createLiteral(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new PSSIFStructuralIntegrityException("name of a literal can not be null or empty");
    }
    if (findLiteral(name) != null) {
      throw new PSSIFStructuralIntegrityException("literal with this name already exists");
    }
    EnumerationLiteralImpl result = new EnumerationLiteralImpl(this, name);
    literals.add(result);
    return result;
  }

  @Override
  public void removeLiteral(EnumerationLiteral literal) {
    literals.remove(literal);
  }

  @Override
  public Collection<EnumerationLiteral> getLiterals() {
    return Sets.<EnumerationLiteral> newHashSet(this.literals);
  }

  @Override
  public EnumerationLiteral findLiteral(String name) {
    for (EnumerationLiteral literal : this.literals) {
      if (PSSIFUtil.areSame(name, literal.getName())) {
        return literal;
      }
    }
    return null;
  }

  @Override
  public Class<?> getMetaType() {
    return Enumeration.class;
  }

  @Override
  public String toString() {
    return "Enumeration:" + this.getName();
  }

  @Override
  public PSSIFValue fromObject(Object object) {
    if (object instanceof EnumerationLiteral) {
      return PSSIFValue.create(object);
    }
    else if (object instanceof String) {
      EnumerationLiteral literal = findLiteral((String) object);
      if (literal != null) {
        return PSSIFValue.create(literal);
      }
      else {
        throw new IllegalArgumentException();
      }
    }
    else {
      throw new IllegalArgumentException();
    }
  }
}
