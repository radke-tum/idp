package de.tum.pssif.core.metamodel;

import java.util.Set;

import com.google.common.collect.Sets;


public interface Unit extends Named {

  Unit INCH = new UnitImpl("Inch", Sets.newHashSet(PrimitiveDataType.DECIMAL, PrimitiveDataType.INTEGER));
  Unit CM   = new UnitImpl("Inch", Sets.newHashSet(PrimitiveDataType.DECIMAL, PrimitiveDataType.INTEGER));
  Unit KG   = new UnitImpl("Inch", Sets.newHashSet(PrimitiveDataType.DECIMAL, PrimitiveDataType.INTEGER));

  //TODO what are the units?
  //TODO what about enumerations? -> they have no units... inverse direction, possibly?

  boolean isAllowedForDataType(DataType type);

  static class UnitImpl implements Unit {

    private final String                 name;
    private final Set<PrimitiveDataType> allowedForDataTypes;

    private UnitImpl(String name, Set<PrimitiveDataType> allowedForTypes) {
      this.name = name;
      this.allowedForDataTypes = allowedForTypes;
    }

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public boolean isAllowedForDataType(DataType type) {
      return this.allowedForDataTypes.contains(type);
    }

  }
}
