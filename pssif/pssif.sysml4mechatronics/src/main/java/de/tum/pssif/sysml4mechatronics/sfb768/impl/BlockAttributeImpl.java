package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Block;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768BlockAttribute;


public class BlockAttributeImpl extends AttributeImpl<SFB768Block> implements SFB768BlockAttribute {

  private final BlockImpl value;

  BlockAttributeImpl(SysML4MIdentifier identifier, SysML4MName name, BlockImpl owner, BlockImpl value) {
    super(identifier, name, owner);
    this.value = value;
  }

  @Override
  public SFB768Block getValue() {
    return this.value;
  }

}
