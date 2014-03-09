package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.IdentifiableNamedImpl;
import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlOwnedFeature;


public class UmlOwnedFeatureImpl extends IdentifiableNamedImpl implements MagicumlOwnedFeature {

  public UmlOwnedFeatureImpl(SysML4MIdentifier identifier, SysML4MName name) {
    super(identifier, name);
  }

}
