package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicUmlConnector;


public class UmlConnectorImpl extends UmlOwnedFeatureImpl implements MagicUmlConnector {

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

  @Override
  public void resolveReferences(UmlModelImpl contextModel) {
    int foundCount = 0;
    for (UmlClassImpl clazz : contextModel.classesImpl()) {
      if (clazz.findAttributeByIdentifier(sourceAttrId) != null) {
        this.sourceAttribute = (UmlAttributeReferencedTypeImpl) clazz.findAttributeByIdentifier(sourceAttrId);
        foundCount++;
      }
      if (clazz.findAttributeByIdentifier(targetAttrId) != null) {
        this.targetAttribute = (UmlAttributeReferencedTypeImpl) clazz.findAttributeByIdentifier(targetAttrId);
        foundCount++;
      }
      if (clazz.findAttributeByIdentifier(sourcePortId) != null) {
        this.sourcePort = (UmlAttributePortImpl) clazz.findAttributeByIdentifier(sourcePortId);
        foundCount++;
      }
      if (clazz.findAttributeByIdentifier(targetPortId) != null) {
        this.targetPort = (UmlAttributePortImpl) clazz.findAttributeByIdentifier(targetPortId);
        foundCount++;
      }
      if (foundCount == 4) {
        break;
      }
    }
  }

}
