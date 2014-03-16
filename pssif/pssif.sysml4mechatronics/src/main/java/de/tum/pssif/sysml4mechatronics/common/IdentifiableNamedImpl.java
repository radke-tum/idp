package de.tum.pssif.sysml4mechatronics.common;



public abstract class IdentifiableNamedImpl implements SysML4MIdentifiable, SysML4MNamed {

  private final SysML4MName       name;
  private final SysML4MIdentifier identifier;

  public IdentifiableNamedImpl(SysML4MIdentifier identifier, SysML4MName name) {
    this.identifier = identifier;
    this.name = name;
  }

  @Override
  public SysML4MName getName() {
    return this.name;
  }

  @Override
  public SysML4MIdentifier getIdentifier() {
    return this.identifier;
  }

}
