package de.tum.pssif.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import de.tum.pssif.core.metamodel.EnumerationLiteral;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.metamodel.Units;


public class PSSIFValue {

  private final Object val;
  private final Unit   unit;

  private PSSIFValue(Object val, Unit unit) {
    this.val = val;
    this.unit = unit;
  }

  public static PSSIFValue create(Object obj, Unit unit) {
    if (obj == null || unit == null) {
      throw new IllegalArgumentException("Can not create a PSSIFValue with null content, or null unit!");
    }
    return new PSSIFValue(obj, unit);
  }

  public static PSSIFValue create(Object obj) {
    return create(obj, Units.NONE);
  }

  public Object getValue() {
    return val;
  }

  public Unit getUnit() {
    return this.unit;
  }

  public boolean isString() {
    return val instanceof String;
  }

  public boolean isBoolean() {
    return val instanceof Boolean;
  }

  public boolean isDate() {
    return val instanceof Date;
  }

  public boolean isInteger() {
    return val instanceof Integer || val instanceof BigInteger || val instanceof Long;
  }

  public boolean isDecimal() {
    return val instanceof Double || val instanceof BigDecimal || val instanceof Float || isInteger();
  }

  public boolean isEnumeration() {
    return val instanceof EnumerationLiteral;
  }

  public String asString() {
    return (String) val;
  }

  public Boolean asBoolean() {
    return (Boolean) val;
  }

  public Date asDate() {
    return (Date) val;
  }

  public BigInteger asInteger() {
    if (val instanceof Integer) {
      return BigInteger.valueOf(((Integer) val).longValue());
    }
    else if (val instanceof Long) {
      return BigInteger.valueOf(((Long) val).longValue());
    }
    return (BigInteger) val;
  }

  public BigDecimal asDecimal() {
    if (isInteger()) {
      return BigDecimal.valueOf(asInteger().longValue());
    }
    else if (val instanceof Double) {
      return BigDecimal.valueOf(((Double) val).longValue());
    }
    else if (val instanceof Float) {
      return BigDecimal.valueOf(((Float) val).longValue());
    }
    return (BigDecimal) val;
  }

  public Object asEnumeration() {
    return (EnumerationLiteral) val;
  }

}
