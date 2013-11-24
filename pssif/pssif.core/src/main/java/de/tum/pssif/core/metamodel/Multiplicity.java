package de.tum.pssif.core.metamodel;

import java.io.Serializable;

import com.google.common.base.Preconditions;


public interface Multiplicity {
  int getEdgeEndLower();

  UnlimitedNatural getEdgeEndUpper();

  int getEdgeTypeLower();

  UnlimitedNatural getEdgeTypeUpper();

  boolean includesEdgeType(int count);

  boolean includesEdgeEnd(int count);

  public static final class MultiplicityContainer implements Multiplicity {
    private final int              endLower;
    private final UnlimitedNatural endUpper;
    private final int              typeLower;
    private final UnlimitedNatural typeUpper;

    public static Multiplicity of(int endLower, UnlimitedNatural endUpper, int typeLower, UnlimitedNatural typeUpper) {
      Preconditions.checkArgument(endLower >= 0);
      Preconditions.checkArgument(endUpper.compareTo(endLower) >= 0);
      Preconditions.checkArgument(typeLower >= 0);
      Preconditions.checkArgument(typeUpper.compareTo(typeUpper) >= 0);
      return new MultiplicityContainer(endLower, endUpper, typeLower, typeUpper);
    }

    public static Multiplicity of(int endLower, int endUpper, int typeLower, UnlimitedNatural typeUpper) {
      return of(endLower, UnlimitedNatural.of(endUpper), typeLower, typeUpper);
    }

    public static Multiplicity of(int endLower, UnlimitedNatural endUpper, int typeLower, int typeUpper) {
      return of(endLower, endUpper, typeLower, UnlimitedNatural.of(typeUpper));
    }

    public static Multiplicity of(int endLower, int endUpper, int typeLower, int typeUpper) {
      return of(endLower, UnlimitedNatural.of(endUpper), typeLower, UnlimitedNatural.of(typeUpper));
    }

    private MultiplicityContainer(int endLower, UnlimitedNatural endUpper, int typeLower, UnlimitedNatural typeUpper) {
      this.endLower = endLower;
      this.endUpper = endUpper;
      this.typeLower = typeLower;
      this.typeUpper = typeUpper;
    }

    @Override
    public int getEdgeEndLower() {
      return endLower;
    }

    @Override
    public UnlimitedNatural getEdgeEndUpper() {
      return endUpper;
    }

    @Override
    public int getEdgeTypeLower() {
      return typeLower;
    }

    @Override
    public UnlimitedNatural getEdgeTypeUpper() {
      return typeUpper;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + endLower;
      result = prime * result + ((endUpper == null) ? 0 : endUpper.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      Multiplicity other = (Multiplicity) obj;
      if (endLower != other.getEdgeEndLower()) {
        return false;
      }
      if (endUpper == null) {
        if (other.getEdgeEndUpper() != null) {
          return false;
        }
      }
      else if (!endUpper.equals(other.getEdgeEndUpper())) {
        return false;
      }
      return true;
    }

    @Override
    public boolean includesEdgeType(int count) {
      return typeLower >= count && typeUpper.compareTo(Integer.valueOf(count)) >= 0;
    }

    @Override
    public boolean includesEdgeEnd(int count) {
      return endLower >= count && endUpper.compareTo(Integer.valueOf(count)) >= 0;
    }
  }

  public static final class UnlimitedNatural extends Number implements Comparable<Number>, Serializable {
    private static final long            serialVersionUID = 1L;

    public static final UnlimitedNatural UNLIMITED        = new UnlimitedNatural(-1);

    private int                          value;

    private UnlimitedNatural(int value) {
      this.value = value;
    }

    public static UnlimitedNatural of(int value) {
      Preconditions.checkArgument(value >= 0);
      return new UnlimitedNatural(value);
    }

    public static UnlimitedNatural max(UnlimitedNatural a, UnlimitedNatural b) {
      if (a.equals(UNLIMITED) || b.equals(UNLIMITED)) {
        return UNLIMITED;
      }
      else {
        return UnlimitedNatural.of(Math.max(a.value, b.value));
      }
    }

    @Override
    public int intValue() {
      return value;
    }

    @Override
    public long longValue() {
      return value;
    }

    @Override
    public float floatValue() {
      return value;
    }

    @Override
    public double doubleValue() {
      return value;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + value;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        if (Number.class.isAssignableFrom(obj.getClass())) {
          return value == ((Number) obj).intValue();
        }
        return false;
      }
      UnlimitedNatural other = (UnlimitedNatural) obj;
      if (value != other.value) {
        return false;
      }
      return true;
    }

    @Override
    public int compareTo(Number o) {
      if (value == -1) {
        if (o.intValue() == -1) {
          return 0;
        }
        else {
          return 1;
        }
      }
      else {
        if (o.intValue() == -1) {
          return -1;
        }
        else {
          return Integer.valueOf(value).compareTo(o.intValue());
        }
      }
    }
  }
}
