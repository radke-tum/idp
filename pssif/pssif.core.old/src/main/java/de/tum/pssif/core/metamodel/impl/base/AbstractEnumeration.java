package de.tum.pssif.core.metamodel.impl.base;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.Enumeration;
import de.tum.pssif.core.metamodel.EnumerationLiteral;
import de.tum.pssif.core.util.PSSIFUtil;
import de.tum.pssif.core.util.PSSIFValue;


public abstract class AbstractEnumeration extends AbstractNamed implements Enumeration {
  private final Collection<EnumerationLiteral> literals = Sets.newHashSet();

  public AbstractEnumeration(String name) {
    super(name);
  }

  protected final void addLiteral(EnumerationLiteral literal) {
    literals.add(literal);
  }

  protected final void removeLiteralInternal(EnumerationLiteral literal) {
    literals.remove(literal);
  }

  @Override
  public final Collection<EnumerationLiteral> getLiterals() {
    return Sets.<EnumerationLiteral> newHashSet(this.literals);
  }

  @Override
  public final EnumerationLiteral findLiteral(String name) {
    for (EnumerationLiteral literal : this.literals) {
      if (PSSIFUtil.areSame(name, literal.getName())) {
        return literal;
      }
    }
    return null;
  }

  @Override
  public final Class<?> getMetaType() {
    return Enumeration.class;
  }

  @Override
  public final String toString() {
    return "Enumeration:" + this.getName();
  }

  @Override
  public final PSSIFValue fromObject(Object object) {
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
