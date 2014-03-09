package de.tum.pssif.sysml4mechatronics.sfb768;

public final class SFB768StringAttributeValue implements SFB768AttributeValue {

  private final String value;

  public SFB768StringAttributeValue(String value) {
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
    if (!(obj instanceof SFB768StringAttributeValue)) {
      return false;
    }
    return ((SFB768StringAttributeValue) obj).value.equals(value);
  }
}
