package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.sfb768.PortAssociation;


public class PortAssociationImpl implements PortAssociation {

  private final PortImpl fromPort;
  private final PortImpl toPort;

  PortAssociationImpl(PortImpl from, PortImpl to) {
    this.fromPort = from;
    this.toPort = to;
    from.setAssociation(this);
    to.setAssociation(this);
  }

  @Override
  public PortImpl getFromPort() {
    return fromPort;
  }

  @Override
  public PortImpl getToPort() {
    return toPort;
  }

}
