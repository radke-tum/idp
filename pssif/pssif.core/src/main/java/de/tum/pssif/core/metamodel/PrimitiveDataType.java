package de.tum.pssif.core.metamodel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


public interface PrimitiveDataType extends DataType {

  PrimitiveDataType STRING  = new PrimitiveTypeImpl(String.class.getSimpleName(), String.class);
  PrimitiveDataType BOOLEAN = new PrimitiveTypeImpl(Boolean.class.getSimpleName(), Boolean.class);
  PrimitiveDataType INTEGER = new PrimitiveTypeImpl(BigInteger.class.getSimpleName(), BigInteger.class);
  PrimitiveDataType DECIMAL = new PrimitiveTypeImpl(BigDecimal.class.getSimpleName(), BigDecimal.class);
  PrimitiveDataType DATE    = new PrimitiveTypeImpl(Date.class.getSimpleName(), Date.class);

  Class<?> getType();

  static class PrimitiveTypeImpl implements PrimitiveDataType {

    private final String   name;
    private final Class<?> clazz;

    private PrimitiveTypeImpl(String name, Class<?> clazz) {
      this.name = name;
      this.clazz = clazz;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public Class<?> getType() {
      return clazz;
    }

  }

}
