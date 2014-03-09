package de.tum.pssif.sysml4mechatronics.sfb768;

public final class SFB768Name {

  private final String name;

  private SFB768Name(String name) {
    this.name = name;
  }

  public static SFB768Name create(String string) {
    return new SFB768Name(string);
  }

  public String serialize() {
    return name;
  }

  public String toString() {
    return serialize();
  }

  public int hashCode() {
    return (getClass().getSimpleName() + name).hashCode();
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof SFB768Name)) {
      return false;
    }
    return name.equalsIgnoreCase(((SFB768Name) obj).name);
  }

}
