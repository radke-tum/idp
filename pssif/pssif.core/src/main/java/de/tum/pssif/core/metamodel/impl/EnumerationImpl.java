package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.Enumeration;
import de.tum.pssif.core.metamodel.EnumerationLiteral;


public class EnumerationImpl extends NamedImpl implements Enumeration {
  private final Collection<EnumerationLiteral> literals = Sets.newHashSet();

  public EnumerationImpl(String name) {
    super(name);
  }

  @Override
  public EnumerationLiteral createLiteral(String name) {
    EnumerationLiteral result = new EnumerationLiteralImpl(name);
    literals.add(result);
    return result;
  }

  @Override
  public void removeLiteral(EnumerationLiteral literal) {
    literals.remove(literal);
  }
}
