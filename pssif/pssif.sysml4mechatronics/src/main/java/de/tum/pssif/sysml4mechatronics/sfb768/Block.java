package de.tum.pssif.sysml4mechatronics.sfb768;

import java.util.Set;


public interface Block extends SFB768Identifiable, SFB768Named, AttributeValue {

  SFB768Layer getLayer();

  Set<Port> getPorts();

  Set<Attribute<?>> getAttributes();

  Port createPort(SFB768Name name, SFB768Identifier identifier, PortDirection direction, SFB768Layer layer);

  Attribute<?> setAttributeValue(SFB768Name name, SFB768Identifier identifier, AttributeValue value);

  Port findPort(SFB768Identifier identifier);

  Attribute<?> findAttribute(SFB768Identifier identifier);

}
