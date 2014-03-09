package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicUmlAttributeReferencedType;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicUmlClass;


public class UmlAttributeReferencedTypeImpl extends UmlAttributeImpl implements MagicUmlAttributeReferencedType {

  private final SysML4MIdentifier refTypeId;

  private UmlClassImpl            refType = null;

  public UmlAttributeReferencedTypeImpl(SysML4MIdentifier identifier, SysML4MName name, UmlClassImpl owner, SysML4MIdentifier refTypeId) {
    super(identifier, name, owner);
    this.refTypeId = refTypeId;
  }

  @Override
  public SysML4MIdentifier getReferencedTypeIdentifier() {
    return this.refTypeId;
  }

  @Override
  public MagicUmlClass getReferencedType() {
    return this.refType;
  }

  //TODO refs resoluion

}
