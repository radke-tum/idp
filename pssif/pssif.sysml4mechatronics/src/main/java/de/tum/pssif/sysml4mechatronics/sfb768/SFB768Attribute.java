package de.tum.pssif.sysml4mechatronics.sfb768;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifiable;
import de.tum.pssif.sysml4mechatronics.common.SysML4MNamed;


public interface SFB768Attribute<V extends SFB768AttributeValue> extends SysML4MIdentifiable, SysML4MNamed {

  SFB768Block getBlock();

  V getValue();

}
