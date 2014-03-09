package de.tum.pssif.sysml4mechatronics.sfb768;

import java.util.Set;


public interface Block extends SFB768Identifiable, SFB768Named, AttributeValue {

  SFB768Layer getLayer();

  Set<Port> getPorts();

  Set<Attribute<?>> getAttributes();

}
