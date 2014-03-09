package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlAttribute;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlClass;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlConnector;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlOwnedFeature;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlVisibility;


public class UmlClassImpl extends UmlPackagedElementImpl implements MagicumlClass {

  private final MagicumlVisibility                       visibility;

  private final Map<SysML4MIdentifier, UmlAttributeImpl> attributesById   = Maps.newHashMap();
  private final Map<SysML4MName, UmlAttributeImpl>       attributesByName = Maps.newHashMap();
  private final Map<SysML4MIdentifier, UmlConnectorImpl> connectorsById   = Maps.newHashMap();
  private final Map<SysML4MName, UmlConnectorImpl>       connectorsByName = Maps.newHashMap();

  public UmlClassImpl(SysML4MIdentifier identifier, SysML4MName name, MagicumlVisibility visibility) {
    super(identifier, name);
    this.visibility = visibility;
  }

  @Override
  public MagicumlVisibility getVisibility() {
    return this.visibility;
  }

  @Override
  public Set<MagicumlOwnedFeature> getOwnedFeatures() {
    Set<MagicumlOwnedFeature> result = Sets.newHashSet();
    result.addAll(getAttributes());
    result.addAll(getConnectors());
    return result;
  }

  @Override
  public Set<MagicumlAttribute> getAttributes() {
    return Sets.<MagicumlAttribute> newHashSet(this.attributesById.values());
  }

  @Override
  public Set<MagicumlConnector> getConnectors() {
    return Sets.<MagicumlConnector> newHashSet(this.connectorsById.values());
  }

  @Override
  public UmlOwnedFeatureImpl findOwnedFeatureByName(SysML4MName name) {
    UmlOwnedFeatureImpl result = findAttributeByName(name);
    if (result != null) {
      return result;
    }
    return findConnectorByName(name);
  }

  @Override
  public UmlOwnedFeatureImpl findOwnedFeatureByIdentifier(SysML4MIdentifier identifier) {
    UmlOwnedFeatureImpl result = findAttributeByIdentifier(identifier);
    if (result != null) {
      return result;
    }
    return findConnectorByIdentifier(identifier);
  }

  @Override
  public UmlAttributeImpl findAttributeByName(SysML4MName name) {
    return this.attributesByName.get(name);
  }

  @Override
  public UmlAttributeImpl findAttributeByIdentifier(SysML4MIdentifier identifier) {
    return this.attributesById.get(identifier);
  }

  @Override
  public UmlConnectorImpl findConnectorByName(SysML4MName name) {
    return this.connectorsByName.get(name);
  }

  @Override
  public UmlConnectorImpl findConnectorByIdentifier(SysML4MIdentifier identifier) {
    return this.connectorsById.get(identifier);
  }

  @Override
  public UmlAttributeStringImpl createUmlAttributeString(SysML4MIdentifier identifier, SysML4MName name) {
    return register(new UmlAttributeStringImpl(identifier, name, this));
  }

  @Override
  public UmlAttributePortImpl createUmlAttributePort(SysML4MIdentifier identifier, SysML4MName name) {
    return register(new UmlAttributePortImpl(identifier, name, this));
  }

  @Override
  public UmlAttributeReferencedTypeImpl createUmlAttributeReferencedType(SysML4MIdentifier identifier, SysML4MName name,
                                                                         SysML4MIdentifier refTypeIdentifier) {
    return register(new UmlAttributeReferencedTypeImpl(identifier, name, this, refTypeIdentifier));
  }

  @Override
  public UmlConnectorImpl createConnector(SysML4MIdentifier identifier, SysML4MName name, SysML4MIdentifier sourceId, SysML4MIdentifier targetId,
                                          SysML4MIdentifier sourceRoleId, SysML4MIdentifier targetRoleId) {
    UmlConnectorImpl conn = new UmlConnectorImpl(identifier, name, this, sourceId, targetId, targetRoleId, targetRoleId);
    this.connectorsById.put(identifier, conn);
    this.connectorsByName.put(name, conn);
    return conn;
  }

  private <T extends UmlAttributeImpl> T register(T attr) {
    this.attributesById.put(attr.getIdentifier(), attr);
    this.attributesByName.put(attr.getName(), attr);
    return attr;
  }

}
