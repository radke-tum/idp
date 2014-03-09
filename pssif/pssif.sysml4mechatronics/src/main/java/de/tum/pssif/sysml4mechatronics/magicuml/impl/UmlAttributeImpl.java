package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicUmlAttribute;


public class UmlAttributeImpl extends UmlOwnedFeatureImpl implements MagicUmlAttribute {

  public UmlAttributeImpl(SysML4MIdentifier identifier, SysML4MName name, UmlClassImpl owner) {
    super(identifier, name, owner);
  }

}
