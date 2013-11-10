package de.tum.pssif.core.metamodel;

import java.math.BigDecimal;
import java.math.BigInteger;


public class PrimitiveTypes {
  public static final PrimitiveType BOOLEAN = createType(Boolean.class.getSimpleName(), Boolean.class);
  public static final PrimitiveType INTEGER = createType(BigInteger.class.getSimpleName(), BigInteger.class);
  public static final PrimitiveType DECIMAL = createType(BigDecimal.class.getSimpleName(), BigDecimal.class);
  public static final PrimitiveType STRING  = createType(String.class.getSimpleName(), String.class);

  private PrimitiveTypes() {
    // do not instantiate
  }

  private static PrimitiveType createType(final String name, final Class<?> clazz) {
    return new PrimitiveType() {
      @Override
      public String getName() {
        return name;
      }

      @Override
      public Class<?> getType() {
        return clazz;
      }
    };
  }
}
