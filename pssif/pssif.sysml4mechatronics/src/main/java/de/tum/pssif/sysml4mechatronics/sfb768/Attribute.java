package de.tum.pssif.sysml4mechatronics.sfb768;

public interface Attribute<V extends AttributeValue> extends SFB768Identifiable, SFB768Named {

  Block getBlock();

  V getValue();

}
