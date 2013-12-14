package de.tum.pssif.core.metamodel;

public final class Units {

  private Units() {
    //Nothing
  }

  public static final Unit NONE       = new UnitImpl("none", "", false);
  public static final Unit METRE      = new UnitImpl("Metre", "m", true);
  public static final Unit KILOGRAM   = new UnitImpl("Kilogram", "kg", true);
  public static final Unit SECOND     = new UnitImpl("Second", "s", true);
  public static final Unit AMPERE     = new UnitImpl("Ampere", "A", true);
  public static final Unit KELVIN     = new UnitImpl("Kelvin", "K", true);
  public static final Unit MOLE       = new UnitImpl("Mole", "mol", true);
  public static final Unit CANDELA    = new UnitImpl("Candela", "cd", true);

  public static final Unit INCH       = new UnitImpl("Inch", "in", false);
  public static final Unit CENTIMETER = new UnitImpl("Centimeter", "cm", false);

  private static class UnitImpl implements Unit {
    private final String  name;
    private final String  abbreviation;
    private final boolean si;

    private UnitImpl(String name, String abbreviation, boolean si) {
      this.name = name;
      this.abbreviation = abbreviation;
      this.si = si;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getAbbreviation() {
      return abbreviation;
    }

    @Override
    public boolean isSi() {
      return si;
    }

    @Override
    public Unit getSi() {
      return this;
    }
  }

}
