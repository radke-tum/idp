package de.tum.pssif.sysml4mechatronics.mapper;

public class MapperXmlIdentifier {

  private final String nsPrefix;
  private final String localName;

  public static MapperXmlIdentifier create(String localName) {
    return create(null, localName);
  }

  public static MapperXmlIdentifier create(String nxPrefix, String localName) {
    if (localName == null) {
      throw new IllegalArgumentException("Invalid init params. Only the nsPrefix can be null.");
    }
    return new MapperXmlIdentifier(nxPrefix, localName);
  }

  private MapperXmlIdentifier(String nsPrefix, String localName) {
    this.nsPrefix = nsPrefix;
    this.localName = localName;
  }

  public int hashCode() {
    return (getClass().getSimpleName() + nsPrefixNullSafe() + localName).hashCode();
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof MapperXmlIdentifier)) {
      return false;
    }
    MapperXmlIdentifier other = (MapperXmlIdentifier) obj;
    return nsPrefixNullSafe().equals(other.nsPrefixNullSafe()) && localName.equals(other.localName);
  }

  private String nsPrefixNullSafe() {
    return nsPrefix == null ? "<nil>" : nsPrefix;
  }

}
