package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.IdentifiableNamedImpl;
import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicUmlOwnedFeature;


public abstract class UmlOwnedFeatureImpl extends IdentifiableNamedImpl implements MagicUmlOwnedFeature, UmlReferenceResolvable {

  private final UmlClassImpl owner;

  public UmlOwnedFeatureImpl(SysML4MIdentifier identifier, SysML4MName name, UmlClassImpl owner) {
    super(identifier, name);
    this.owner = owner;
  }

  @Override
  public UmlClassImpl getOwner() {
    return this.owner;
  }

}
