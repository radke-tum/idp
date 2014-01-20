package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.EnumerationLiteral;
import de.tum.pssif.core.metamodel.impl.base.AbstractEnumeration;


public class EnumerationImpl extends AbstractEnumeration {
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
    addLiteral(result);
    return result;
  }

  @Override
  public void removeLiteral(EnumerationLiteral literal) {
    removeLiteralInternal(literal);
  }
}
