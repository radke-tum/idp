package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.sysml4mechatronics.sfb768.Block;
import de.tum.pssif.sysml4mechatronics.sfb768.Model;
import de.tum.pssif.sysml4mechatronics.sfb768.Port;
import de.tum.pssif.sysml4mechatronics.sfb768.PortAssociation;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Layer;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;


public class ModelImpl extends SFB768IdentifiableNamedImpl implements Model {

  private final Map<SFB768Identifier, BlockImpl> blocks           = Maps.newHashMap();
  private final Set<PortAssociationImpl>         portAssociations = Sets.newHashSet();

  ModelImpl(SFB768Identifier identifier, SFB768Name name) {
    super(identifier, name);
  }

  @Override
  public Set<Block> getBlocks() {
    return Sets.<Block> newHashSet(this.blocks.values());
  }

  @Override
  public Set<PortAssociation> getPortAssociations() {
    return Sets.<PortAssociation> newHashSet(this.portAssociations);
  }

  @Override
  public Block createBlock(SFB768Name name, SFB768Identifier identifier, SFB768Layer layer) {
    BlockImpl result = null;
    if (SFB768Layer.ELECTRICAL.equals(layer)) {
      result = new ElectricalBlock(identifier, name);
    }
    else if (SFB768Layer.INTERFACE.equals(layer)) {
      result = new InterfaceBlock(identifier, name);
    }
    else if (SFB768Layer.PHYSICAL.equals(layer)) {
      result = new PhysicalBlock(identifier, name);
    }
    else {
      result = new SoftwareBlock(identifier, name);
    }
    this.blocks.put(identifier, result);
    return result;
  }

  @Override
  public PortAssociation associatePorts(Port from, Port to) {
    PortImpl fromPort = findBlock(from.getBlock().getIdentifier()).findPort(from.getIdentifier());
    PortImpl toPort = findBlock(to.getBlock().getIdentifier()).findPort(to.getIdentifier());
    PortAssociationImpl result = new PortAssociationImpl(fromPort, toPort);
    this.portAssociations.add(result);
    return result;
  }

  @Override
  public BlockImpl findBlock(SFB768Identifier identifier) {
    return this.blocks.get(identifier);
  }

}
