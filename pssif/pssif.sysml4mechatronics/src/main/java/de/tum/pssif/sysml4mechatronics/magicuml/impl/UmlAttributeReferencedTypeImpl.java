package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlAttributeReferencedType;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlClass;


public class UmlAttributeReferencedTypeImpl extends UmlAttributeImpl implements MagicumlAttributeReferencedType {

  public UmlAttributeReferencedTypeImpl(SysML4MIdentifier identifier, SysML4MName name) {
    super(identifier, name);
  }

  @Override
  public SysML4MIdentifier getReferencedTypeIdentifier() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlClass getReferencedType() {
    // TODO Auto-generated method stub
    return null;
  }

}
