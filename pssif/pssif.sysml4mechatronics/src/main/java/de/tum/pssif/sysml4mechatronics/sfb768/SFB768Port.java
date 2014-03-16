package de.tum.pssif.sysml4mechatronics.sfb768;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifiable;
import de.tum.pssif.sysml4mechatronics.common.SysML4MNamed;


public interface SFB768Port extends SysML4MIdentifiable, SysML4MNamed, SFB768AttributeValue {

  SFB768Block getBlock();

  SFB768PortAssociation getPortAssociation();

  SFB768PortDirection getDirection();

  SFB768Layer getLayer();

}
