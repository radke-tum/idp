package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.IdentifiableNamedImpl;
import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicUmlPackagedElement;


public abstract class UmlPackagedElementImpl extends IdentifiableNamedImpl implements MagicUmlPackagedElement, UmlReferenceResolvable {

  public UmlPackagedElementImpl(SysML4MIdentifier identifier, SysML4MName name) {
    super(identifier, name);
  }

}
