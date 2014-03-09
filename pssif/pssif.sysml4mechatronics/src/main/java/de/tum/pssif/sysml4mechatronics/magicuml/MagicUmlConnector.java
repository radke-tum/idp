package de.tum.pssif.sysml4mechatronics.magicuml;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;


public interface MagicUmlConnector extends MagicUmlOwnedFeature {

  SysML4MIdentifier getSourceAttributeIdentifier();

  SysML4MIdentifier getTargetAttributeIdentifier();

  SysML4MIdentifier getSourcePortIdenitifier();

  SysML4MIdentifier getTargetPortIdentifier();

  MagicUmlAttributeReferencedType getSourceAttribute();

  MagicUmlAttributeReferencedType getTargetAttribute();

  MagicUmlAttributePort getSourcePort();

  MagicUmlAttributePort getTargetPort();

}
