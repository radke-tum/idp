package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.sfb768.Attribute;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;
import de.tum.pssif.sysml4mechatronics.sfb768.StringAttributeValue;


public final class StringAttribute extends AttributeImpl<StringAttributeValue> implements Attribute<StringAttributeValue> {

  private final StringAttributeValue value;

  StringAttribute(SFB768Identifier identifier, SFB768Name name, BlockImpl owner, StringAttributeValue value) {
    super(identifier, name, owner);
    this.value = value;
  }

  public StringAttribute(SFB768Identifier identifier, SFB768Name name, BlockImpl owner, String string) {
    this(identifier, name, owner, new StringAttributeValue(string));
  }

  @Override
  public StringAttributeValue getValue() {
    return this.value;
  }

}
