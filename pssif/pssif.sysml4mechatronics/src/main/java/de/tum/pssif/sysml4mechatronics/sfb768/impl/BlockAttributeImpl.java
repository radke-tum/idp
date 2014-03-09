package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Block;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768BlockAttribute;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;


public class BlockAttributeImpl extends AttributeImpl<SFB768Block> implements SFB768BlockAttribute {

  private final BlockImpl value;

  BlockAttributeImpl(SFB768Identifier identifier, SFB768Name name, BlockImpl owner, BlockImpl value) {
    super(identifier, name, owner);
    this.value = value;
  }

  @Override
  public SFB768Block getValue() {
    return this.value;
  }

}
