package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.sfb768.Port;
import de.tum.pssif.sysml4mechatronics.sfb768.PortAssociation;
import de.tum.pssif.sysml4mechatronics.sfb768.PortDirection;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Layer;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;


public class PortImpl extends SFB768IdentifiableNamedImpl implements Port {

  private final BlockImpl     holder;
  private final PortDirection direction;
  private final SFB768Layer   layer;

  private PortAssociationImpl assciation = null;

  PortImpl(BlockImpl holder, SFB768Identifier identifier, SFB768Name name, PortDirection direction, SFB768Layer layer) {
    super(identifier, name);
    this.holder = holder;
    this.direction = direction;
    this.layer = layer;
  }

  void setAssociation(PortAssociationImpl association) {
    this.assciation = association;
  }

  @Override
  public BlockImpl getBlock() {
    return holder;
  }

  @Override
  public PortAssociation getPortAssociation() {
    return this.assciation;
  }

  @Override
  public PortDirection getDirection() {
    return this.direction;
  }

  @Override
  public SFB768Layer getLayer() {
    return this.layer;
  }

}
