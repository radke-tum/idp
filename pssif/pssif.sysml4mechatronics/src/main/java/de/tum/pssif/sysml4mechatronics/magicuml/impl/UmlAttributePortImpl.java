package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicUmlAttributePort;


public class UmlAttributePortImpl extends UmlAttributeImpl implements MagicUmlAttributePort {

  public UmlAttributePortImpl(SysML4MIdentifier identifier, SysML4MName name, UmlClassImpl owner) {
    super(identifier, name, owner);
  }

  @Override
  public void resolveReferences(UmlModelImpl contextModel) {
    //NOOP
  }

}
