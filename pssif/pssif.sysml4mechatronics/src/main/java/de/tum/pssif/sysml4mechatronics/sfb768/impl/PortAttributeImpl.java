package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.sfb768.Port;
import de.tum.pssif.sysml4mechatronics.sfb768.PortAttribute;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;


public class PortAttributeImpl extends AttributeImpl<Port> implements PortAttribute {

  private final PortImpl port;

  PortAttributeImpl(SFB768Identifier identifier, SFB768Name name, BlockImpl owner, PortImpl port) {
    super(identifier, name, owner);
    this.port = port;
  }

  @Override
  public Port getValue() {
    return this.port;
  }

}
