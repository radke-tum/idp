package de.tum.pssif.sysml4mechatronics.sfb768;

import java.util.Set;


public interface Model extends SFB768Identifiable, SFB768Named {

  Set<Block> getBlocks();

  Set<PortAssociation> getPortAssociations();

  Block createBlock(SFB768Name name, SFB768Identifier identifier, SFB768Layer layer);

  PortAssociation associatePorts(Port from, Port to);

}
