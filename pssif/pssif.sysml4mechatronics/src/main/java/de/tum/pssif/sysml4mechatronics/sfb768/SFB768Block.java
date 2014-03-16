package de.tum.pssif.sysml4mechatronics.sfb768;

import java.util.Set;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifiable;
import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.common.SysML4MNamed;


public interface SFB768Block extends SysML4MIdentifiable, SysML4MNamed, SFB768AttributeValue {

  SFB768Layer getLayer();

  Set<SFB768Port> getPorts();

  Set<SFB768Attribute<?>> getAttributes();

  SFB768Port createPort(SysML4MName name, SysML4MIdentifier identifier, SFB768PortDirection direction, SFB768Layer layer);

  SFB768Attribute<?> setAttributeValue(SysML4MName name, SysML4MIdentifier identifier, SFB768AttributeValue value);

  SFB768Port findPort(SysML4MIdentifier identifier);

  SFB768Attribute<?> findAttribute(SysML4MIdentifier identifier);

}
