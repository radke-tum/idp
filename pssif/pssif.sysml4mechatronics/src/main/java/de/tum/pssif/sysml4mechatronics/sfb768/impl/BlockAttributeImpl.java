package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.sfb768.Block;
import de.tum.pssif.sysml4mechatronics.sfb768.BlockAttribute;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;


public class BlockAttributeImpl extends AttributeImpl<Block> implements BlockAttribute {

  private final BlockImpl value;

  BlockAttributeImpl(SFB768Identifier identifier, SFB768Name name, BlockImpl owner, BlockImpl value) {
    super(identifier, name, owner);
    this.value = value;
  }

  @Override
  public Block getValue() {
    return this.value;
  }

}
