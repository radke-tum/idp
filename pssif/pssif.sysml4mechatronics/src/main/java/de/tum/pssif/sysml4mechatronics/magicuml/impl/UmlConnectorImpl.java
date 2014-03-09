package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlConnector;


public class UmlConnectorImpl extends UmlOwnedFeatureImpl implements MagicumlConnector {

  private final SysML4MIdentifier        sourceAttrId;
  private final SysML4MIdentifier        targetAttrId;
  private final SysML4MIdentifier        sourcePortId;
  private final SysML4MIdentifier        targetPortId;

  private UmlAttributeReferencedTypeImpl sourceAttribute = null;
  private UmlAttributeReferencedTypeImpl targetAttribute = null;
  private UmlAttributePortImpl           sourcePort      = null;
  private UmlAttributePortImpl           targetPort      = null;

  public UmlConnectorImpl(SysML4MIdentifier identifier, SysML4MName name, UmlClassImpl owner, SysML4MIdentifier sourceAttrId,
      SysML4MIdentifier targetAttrId, SysML4MIdentifier sourcePortId, SysML4MIdentifier targetPortId) {
    super(identifier, name, owner);
    this.sourceAttrId = sourceAttrId;
    this.targetAttrId = targetAttrId;
    this.sourcePortId = sourcePortId;
    this.targetPortId = targetAttrId;
  }

  @Override
  public UmlAttributeReferencedTypeImpl getSourceAttribute() {
    return this.sourceAttribute;
  }

  @Override
  public UmlAttributeReferencedTypeImpl getTargetAttribute() {
    return this.targetAttribute;
  }

  @Override
  public UmlAttributePortImpl getSourcePort() {
    return this.sourcePort;
  }

  @Override
  public UmlAttributePortImpl getTargetPort() {
    return this.targetPort;
  }

  @Override
  public SysML4MIdentifier getSourceAttributeIdentifier() {
    return this.sourceAttrId;
  }

  @Override
  public SysML4MIdentifier getTargetAttributeIdentifier() {
    return this.targetAttrId;
  }

  @Override
  public SysML4MIdentifier getSourcePortIdenitifier() {
    return this.sourcePortId;
  }

  @Override
  public SysML4MIdentifier getTargetPortIdentifier() {
    return this.targetPortId;
  }

  //TODO resolution of attrs!

}
