package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlAttributePort;


public class UmlAttributePortImpl extends UmlAttributeImpl implements MagicumlAttributePort {

  public UmlAttributePortImpl(SysML4MIdentifier identifier, SysML4MName name, UmlClassImpl owner) {
    super(identifier, name, owner);
  }

}
