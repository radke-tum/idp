package de.tum.pssif.sysml4mechatronics.magicuml;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifiable;
import de.tum.pssif.sysml4mechatronics.common.SysML4MNamed;


public interface MagicumlOwnedFeature extends SysML4MIdentifiable, SysML4MNamed {

  MagicumlClass getOwner();

}
