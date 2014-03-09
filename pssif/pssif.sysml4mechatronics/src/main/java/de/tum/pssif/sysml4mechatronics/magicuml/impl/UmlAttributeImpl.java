package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlAttribute;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlClass;


public class UmlAttributeImpl extends UmlOwnedFeatureImpl implements MagicumlAttribute {

  public UmlAttributeImpl(SysML4MIdentifier identifier, SysML4MName name) {
    super(identifier, name);
  }

  @Override
  public MagicumlClass getOwner() {
    // TODO Auto-generated method stub
    return null;
  }

}
