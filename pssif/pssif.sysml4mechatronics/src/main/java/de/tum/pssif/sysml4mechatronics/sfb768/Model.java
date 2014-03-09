package de.tum.pssif.sysml4mechatronics.sfb768;

import java.util.Set;


public interface Model extends SFB768Identifiable, SFB768Named {

  Set<Block> getBlocks();

  Set<PortAssociation> getPortAssociations();

}
