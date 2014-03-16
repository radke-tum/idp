package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.common.IdentifiableNamedImpl;
import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Attribute;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768AttributeValue;


public abstract class AttributeImpl<V extends SFB768AttributeValue> extends IdentifiableNamedImpl implements SFB768Attribute<V> {

  private final BlockImpl block;

  AttributeImpl(SysML4MIdentifier identifier, SysML4MName name, BlockImpl owner) {
    super(identifier, name);
    this.block = owner;
  }

  @Override
  public BlockImpl getBlock() {
    return block;
  }

}
