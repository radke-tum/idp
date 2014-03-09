package de.tum.pssif.sysml4mechatronics.sfb768;

public interface SFB768Attribute<V extends SFB768AttributeValue> extends SFB768Identifiable, SFB768Named {

  SFB768Block getBlock();

  V getValue();

}
