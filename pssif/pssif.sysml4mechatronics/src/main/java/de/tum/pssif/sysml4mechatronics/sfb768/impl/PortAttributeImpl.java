package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Port;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768PortAttribute;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;


public class PortAttributeImpl extends AttributeImpl<SFB768Port> implements SFB768PortAttribute {

  private final PortImpl port;

  PortAttributeImpl(SFB768Identifier identifier, SFB768Name name, BlockImpl owner, PortImpl port) {
    super(identifier, name, owner);
    this.port = port;
  }

  @Override
  public SFB768Port getValue() {
    return this.port;
  }

}
