package de.tum.pssif.sysml4mechatronics.sfb768;

public final class StringAttributeValue implements AttributeValue {

  private final String value;

  public StringAttributeValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public String toString() {
    return this.value;
  }

  public int hashCode() {
    return (getClass().getSimpleName() + value).hashCode();
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof StringAttributeValue)) {
      return false;
    }
    return ((StringAttributeValue) obj).value.equals(value);
  }
}
