package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.sfb768.Attribute;
import de.tum.pssif.sysml4mechatronics.sfb768.AttributeValue;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;


public abstract class AttributeImpl<V extends AttributeValue> extends SFB768IdentifiableNamedImpl implements Attribute<V> {

  private final BlockImpl block;

  AttributeImpl(SFB768Identifier identifier, SFB768Name name, BlockImpl owner) {
    super(identifier, name);
    this.block = owner;
  }

  @Override
  public BlockImpl getBlock() {
    return block;
  }

}
