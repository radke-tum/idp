package de.tum.pssif.sysml4mechatronics.sfb768;

public interface SFB768Port extends SFB768Identifiable, SFB768Named, SFB768AttributeValue {

  SFB768Block getBlock();

  SFB768PortAssociation getPortAssociation();

  SFB768PortDirection getDirection();

  SFB768Layer getLayer();

}
