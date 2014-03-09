package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import java.util.Set;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlAttribute;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlAttributePort;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlAttributeReferencedType;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlAttributeString;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlClass;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlConnector;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlOwnedFeature;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlVisibility;


public class UmlClassImpl extends UmlPackagedElementImpl implements MagicumlClass {

  public UmlClassImpl(SysML4MIdentifier identifier, SysML4MName name) {
    super(identifier, name);
  }

  @Override
  public MagicumlVisibility getVisibility() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<MagicumlOwnedFeature> getOwnedFeatures() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<MagicumlAttribute> getAttributes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<MagicumlConnector> getConnectors() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlOwnedFeature findOwnedFeatureByName(SysML4MName name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlOwnedFeature findOwnedFeatureByIdentifier(SysML4MIdentifier identifier) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlAttribute findAttributeByName(SysML4MName name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlAttribute findAttributeByIdentifier(SysML4MIdentifier identifier) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlConnector findConnectorByName(SysML4MName name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlConnector findConnectorByIdentifier(SysML4MIdentifier identifier) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlAttributeString createUmlAttributeString(SysML4MIdentifier identifier, SysML4MName name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlAttributePort createUmlAttributePort(SysML4MIdentifier identifier, SysML4MName name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlAttributeReferencedType createUmlAttributeReferencedType(SysML4MIdentifier identifier, SysML4MName name,
                                                                          SysML4MIdentifier refTypeIdentifier) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlConnector createConnector(SysML4MIdentifier identifier, SysML4MName name, SysML4MIdentifier sourceId, SysML4MIdentifier targetId,
                                           SysML4MIdentifier sourceRoleId, SysML4MIdentifier targetRoleId) {
    // TODO Auto-generated method stub
    return null;
  }

}
