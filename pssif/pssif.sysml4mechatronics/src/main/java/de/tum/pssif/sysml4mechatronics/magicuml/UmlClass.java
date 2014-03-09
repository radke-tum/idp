package de.tum.pssif.sysml4mechatronics.magicuml;

import java.util.Set;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;


public interface UmlClass extends UmlPackagedElement {

  UmlVisibility getVisibility();

  Set<UmlOwnedFeature> getOwnedFeatures();

  Set<UmlAttribute> getAttributes();

  Set<UmlConnector> getConnectors();

  UmlOwnedFeature findOwnedFeatureByName(SysML4MName name);

  UmlOwnedFeature findOwnedFeatureByIdentifier(SysML4MIdentifier identifier);

  UmlAttribute findAttributeByName(SysML4MName name);

  UmlAttribute findAttributeByIdentifier(SysML4MIdentifier identifier);

  UmlConnector findConnectorByName(SysML4MName name);

  UmlConnector findConnectorByIdentifier(SysML4MIdentifier identifier);

  UmlAttributeString createUmlAttributeString(SysML4MIdentifier identifier, SysML4MName name);

  UmlAttributePort createUmlAttributePort(SysML4MIdentifier identifier, SysML4MName name);

  UmlAttributeReferencedType createUmlAttributeReferencedType(SysML4MIdentifier identifier, SysML4MName name, SysML4MIdentifier refTypeIdentifier);

  UmlConnector createConnector(SysML4MIdentifier identifier, SysML4MName name, SysML4MIdentifier sourceId, SysML4MIdentifier targetId,
                               SysML4MIdentifier sourceRoleId, SysML4MIdentifier targetRoleId);

}
