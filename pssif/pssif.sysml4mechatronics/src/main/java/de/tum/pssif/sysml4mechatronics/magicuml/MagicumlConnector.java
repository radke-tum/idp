package de.tum.pssif.sysml4mechatronics.magicuml;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;


public interface MagicumlConnector extends MagicumlOwnedFeature {

  SysML4MIdentifier getSourceAttributeIdentifier();

  SysML4MIdentifier getTargetAttributeIdentifier();

  SysML4MIdentifier getSourcePortIdenitifier();

  SysML4MIdentifier getTargetPortIdentifier();

  MagicumlAttributeReferencedType getSourceAttribute();

  MagicumlAttributeReferencedType getTargetAttribute();

  MagicumlAttributePort getSourcePort();

  MagicumlAttributePort getTargetPort();

}
