package de.tum.pssif.sysml4mechatronics.sfb768;

public interface Port extends SFB768Identifiable, SFB768Named, AttributeValue {

  Block getBlock();

  PortAssociation getPortAssociation();

  PortDirection getDirection();

  SFB768Layer getLayer();

}
