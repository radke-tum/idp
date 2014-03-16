package de.tum.pssif.sysml4mechatronics.sfb768;

import java.util.Set;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifiable;
import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.common.SysML4MNamed;


public interface SFB768Model extends SysML4MIdentifiable, SysML4MNamed {

  Set<SFB768Block> getBlocks();

  Set<SFB768PortAssociation> getPortAssociations();

  SFB768Block createBlock(SysML4MName name, SysML4MIdentifier identifier, SFB768Layer layer);

  SFB768PortAssociation associatePorts(SFB768Port from, SFB768Port to);

  SFB768Block findBlock(SysML4MIdentifier identifier);

}
