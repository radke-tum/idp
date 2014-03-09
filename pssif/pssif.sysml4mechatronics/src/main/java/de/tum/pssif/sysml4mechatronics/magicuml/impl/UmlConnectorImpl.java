package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlAttributePort;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlAttributeReferencedType;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlConnector;


public class UmlConnectorImpl extends UmlOwnedFeatureImpl implements MagicumlConnector {

  public UmlConnectorImpl(SysML4MIdentifier identifier, SysML4MName name) {
    super(identifier, name);
  }

  @Override
  public MagicumlAttributeReferencedType getSourceAttribute() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlAttributeReferencedType getTargetAttribute() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlAttributePort getSourcePort() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlAttributePort getTargetPort() {
    // TODO Auto-generated method stub
    return null;
  }

}
