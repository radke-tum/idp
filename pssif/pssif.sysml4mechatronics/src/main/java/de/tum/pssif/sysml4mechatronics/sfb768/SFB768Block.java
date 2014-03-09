package de.tum.pssif.sysml4mechatronics.sfb768;

import java.util.Set;


public interface SFB768Block extends SFB768Identifiable, SFB768Named, SFB768AttributeValue {

  SFB768Layer getLayer();

  Set<SFB768Port> getPorts();

  Set<SFB768Attribute<?>> getAttributes();

  SFB768Port createPort(SFB768Name name, SFB768Identifier identifier, SFB768PortDirection direction, SFB768Layer layer);

  SFB768Attribute<?> setAttributeValue(SFB768Name name, SFB768Identifier identifier, SFB768AttributeValue value);

  SFB768Port findPort(SFB768Identifier identifier);

  SFB768Attribute<?> findAttribute(SFB768Identifier identifier);

}
