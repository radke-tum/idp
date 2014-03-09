package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifiable;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Named;


public abstract class IdentifiableNamedImpl implements SFB768Identifiable, SFB768Named {

  private final SFB768Name       name;
  private final SFB768Identifier identifier;

  IdentifiableNamedImpl(SFB768Identifier identifier, SFB768Name name) {
    this.identifier = identifier;
    this.name = name;
  }

  @Override
  public SFB768Name getName() {
    return this.name;
  }

  @Override
  public SFB768Identifier getIdentifier() {
    return this.identifier;
  }

}
