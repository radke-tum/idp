package de.tum.pssif.sysml4mechatronics.magicuml;

import java.util.Set;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;


public interface MagicumlClass extends MagicumlPackagedElement {

  MagicumlVisibility getVisibility();

  Set<MagicumlOwnedFeature> getOwnedFeatures();

  Set<MagicumlAttribute> getAttributes();

  Set<MagicumlConnector> getConnectors();

  MagicumlOwnedFeature findOwnedFeatureByName(SysML4MName name);

  MagicumlOwnedFeature findOwnedFeatureByIdentifier(SysML4MIdentifier identifier);

  MagicumlAttribute findAttributeByName(SysML4MName name);

  MagicumlAttribute findAttributeByIdentifier(SysML4MIdentifier identifier);

  MagicumlConnector findConnectorByName(SysML4MName name);

  MagicumlConnector findConnectorByIdentifier(SysML4MIdentifier identifier);

  MagicumlAttributeString createUmlAttributeString(SysML4MIdentifier identifier, SysML4MName name);

  MagicumlAttributePort createUmlAttributePort(SysML4MIdentifier identifier, SysML4MName name);

  MagicumlAttributeReferencedType createUmlAttributeReferencedType(SysML4MIdentifier identifier, SysML4MName name, SysML4MIdentifier refTypeIdentifier);

  MagicumlConnector createConnector(SysML4MIdentifier identifier, SysML4MName name, SysML4MIdentifier sourceId, SysML4MIdentifier targetId,
                               SysML4MIdentifier sourceRoleId, SysML4MIdentifier targetRoleId);

}
