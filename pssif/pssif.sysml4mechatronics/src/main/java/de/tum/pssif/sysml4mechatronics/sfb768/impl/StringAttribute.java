package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Attribute;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768StringAttributeValue;


public final class StringAttribute extends AttributeImpl<SFB768StringAttributeValue> implements SFB768Attribute<SFB768StringAttributeValue> {

  private final SFB768StringAttributeValue value;

  StringAttribute(SFB768Identifier identifier, SFB768Name name, BlockImpl owner, SFB768StringAttributeValue value) {
    super(identifier, name, owner);
    this.value = value;
  }

  public StringAttribute(SFB768Identifier identifier, SFB768Name name, BlockImpl owner, String string) {
    this(identifier, name, owner, new SFB768StringAttributeValue(string));
  }

  @Override
  public SFB768StringAttributeValue getValue() {
    return this.value;
  }

}
