package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Port;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768PortAttribute;


public class PortAttributeImpl extends AttributeImpl<SFB768Port> implements SFB768PortAttribute {

  private final PortImpl port;

  PortAttributeImpl(SysML4MIdentifier identifier, SysML4MName name, BlockImpl owner, PortImpl port) {
    super(identifier, name, owner);
    this.port = port;
  }

  @Override
  public SFB768Port getValue() {
    return this.port;
  }

}
