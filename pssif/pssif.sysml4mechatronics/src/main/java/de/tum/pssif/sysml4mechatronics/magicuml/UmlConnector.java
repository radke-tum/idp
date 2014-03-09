package de.tum.pssif.sysml4mechatronics.magicuml;

public interface UmlConnector extends UmlOwnedFeature {

  UmlAttributeReferencedType getSourceAttribute();

  UmlAttributeReferencedType getTargetAttribute();

  UmlAttributePort getSourcePort();

  UmlAttributePort getTargetPort();

}
