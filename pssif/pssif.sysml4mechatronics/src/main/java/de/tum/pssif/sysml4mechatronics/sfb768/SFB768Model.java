package de.tum.pssif.sysml4mechatronics.sfb768;

import java.util.Set;


public interface SFB768Model extends SFB768Identifiable, SFB768Named {

  Set<SFB768Block> getBlocks();

  Set<SFB768PortAssociation> getPortAssociations();

  SFB768Block createBlock(SFB768Name name, SFB768Identifier identifier, SFB768Layer layer);

  SFB768PortAssociation associatePorts(SFB768Port from, SFB768Port to);

  SFB768Block findBlock(SFB768Identifier identifier);

}
