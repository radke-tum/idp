package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import java.util.Set;

import de.tum.pssif.sysml4mechatronics.sfb768.Block;
import de.tum.pssif.sysml4mechatronics.sfb768.Model;
import de.tum.pssif.sysml4mechatronics.sfb768.Port;
import de.tum.pssif.sysml4mechatronics.sfb768.PortAssociation;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Layer;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;


public class ModelImpl extends SFB768IdentifiableNamedImpl implements Model {

  ModelImpl(SFB768Identifier identifier, SFB768Name name) {
    super(identifier, name);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Set<Block> getBlocks() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<PortAssociation> getPortAssociations() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Block createBlock(SFB768Name name, SFB768Identifier identifier, SFB768Layer layer) {
    //TODO register, create depending on layer
    return new SoftwareBlock(identifier, name);
  }

  @Override
  public PortAssociation associatePorts(Port from, Port to) {
    //TODO locate locally, to avoid the cast
    return new PortAssociationImpl((PortImpl) from, (PortImpl) to);
  }

}
