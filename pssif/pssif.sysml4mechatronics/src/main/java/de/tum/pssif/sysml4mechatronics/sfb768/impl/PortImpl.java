package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Port;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768PortAssociation;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768PortDirection;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Layer;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;


public class PortImpl extends IdentifiableNamedImpl implements SFB768Port {

  private final BlockImpl     holder;
  private final SFB768PortDirection direction;
  private final SFB768Layer   layer;

  private PortAssociationImpl assciation = null;

  PortImpl(BlockImpl holder, SFB768Identifier identifier, SFB768Name name, SFB768PortDirection direction, SFB768Layer layer) {
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
  public SFB768PortAssociation getPortAssociation() {
    return this.assciation;
  }

  @Override
  public SFB768PortDirection getDirection() {
    return this.direction;
  }

  @Override
  public SFB768Layer getLayer() {
    return this.layer;
  }

}
