package de.tum.pssif.sysml4mechatronics.magicuml;

public interface MagicumlConnector extends MagicumlOwnedFeature {

  MagicumlAttributeReferencedType getSourceAttribute();

  MagicumlAttributeReferencedType getTargetAttribute();

  MagicumlAttributePort getSourcePort();

  MagicumlAttributePort getTargetPort();

}
