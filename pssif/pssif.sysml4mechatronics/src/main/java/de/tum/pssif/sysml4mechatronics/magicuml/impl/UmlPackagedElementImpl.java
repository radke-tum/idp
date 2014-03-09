package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.IdentifiableNamedImpl;
import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlPackagedElement;


public abstract class UmlPackagedElementImpl extends IdentifiableNamedImpl implements MagicumlPackagedElement {

  public UmlPackagedElementImpl(SysML4MIdentifier identifier, SysML4MName name) {
    super(identifier, name);
  }

}
