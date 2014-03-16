package de.tum.pssif.sysml4mechatronics.magicuml;

import java.util.Set;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;


public interface MagicUmlClass extends MagicUmlPackagedElement {

  MagicUmlVisibility getVisibility();

  Set<MagicUmlOwnedFeature> getOwnedFeatures();

  Set<MagicUmlAttribute> getAttributes();

  Set<MagicUmlConnector> getConnectors();

  MagicUmlOwnedFeature findOwnedFeatureByName(SysML4MName name);

  MagicUmlOwnedFeature findOwnedFeatureByIdentifier(SysML4MIdentifier identifier);

  MagicUmlAttribute findAttributeByName(SysML4MName name);

  MagicUmlAttribute findAttributeByIdentifier(SysML4MIdentifier identifier);

  MagicUmlConnector findConnectorByName(SysML4MName name);

  MagicUmlConnector findConnectorByIdentifier(SysML4MIdentifier identifier);

  MagicUmlAttributeString createUmlAttributeString(SysML4MIdentifier identifier, SysML4MName name);

  MagicUmlAttributePort createUmlAttributePort(SysML4MIdentifier identifier, SysML4MName name);

  MagicUmlAttributeReferencedType createUmlAttributeReferencedType(SysML4MIdentifier identifier, SysML4MName name, SysML4MIdentifier refTypeIdentifier);

  MagicUmlConnector createConnector(SysML4MIdentifier identifier, SysML4MName name, SysML4MIdentifier sourceId, SysML4MIdentifier targetId,
                               SysML4MIdentifier sourceRoleId, SysML4MIdentifier targetRoleId);

}
