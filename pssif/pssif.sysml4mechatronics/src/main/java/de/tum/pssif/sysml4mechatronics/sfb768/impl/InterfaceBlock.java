package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Layer;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;


public class InterfaceBlock extends BlockImpl {

  InterfaceBlock(SFB768Identifier identifier, SFB768Name name) {
    super(identifier, name);
  }

  @Override
  public SFB768Layer getLayer() {
    return SFB768Layer.INTERFACE;
  }

}
