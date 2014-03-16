package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Layer;


public class SoftwareBlock extends BlockImpl {

  SoftwareBlock(SysML4MIdentifier identifier, SysML4MName name) {
    super(identifier, name);
  }

  @Override
  public SFB768Layer getLayer() {
    return SFB768Layer.SOFTWARE;
  }

}
