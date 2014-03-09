package de.tum.pssif.sysml4mechatronics.common;

public final class SysML4MName {

  private final String name;

  private SysML4MName(String name) {
    this.name = name;
  }

  public static SysML4MName create(String string) {
    return new SysML4MName(string);
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
    if (!(obj instanceof SysML4MName)) {
      return false;
    }
    return name.equalsIgnoreCase(((SysML4MName) obj).name);
  }

}
