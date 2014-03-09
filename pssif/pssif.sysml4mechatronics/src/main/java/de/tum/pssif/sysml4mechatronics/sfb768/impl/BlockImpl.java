package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Attribute;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768AttributeValue;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Block;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Layer;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Port;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768PortDirection;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768StringAttributeValue;


public abstract class BlockImpl extends IdentifiableNamedImpl implements SFB768Block {

  private final Map<SysML4MIdentifier, PortImpl>         ports      = Maps.newHashMap();
  private final Map<SysML4MIdentifier, AttributeImpl<?>> attributes = Maps.newHashMap();

  BlockImpl(SysML4MIdentifier identifier, SysML4MName name) {
    super(identifier, name);
  }

  @Override
  public Set<SFB768Port> getPorts() {
    return Sets.<SFB768Port> newHashSet(this.ports.values());
  }

  @Override
  public Set<SFB768Attribute<?>> getAttributes() {
    return Sets.<SFB768Attribute<?>> newHashSet(this.attributes.values());
  }

  @Override
  public PortImpl createPort(SysML4MName name, SysML4MIdentifier identifier, SFB768PortDirection direction, SFB768Layer layer) {
    PortImpl port = new PortImpl(this, identifier, name, direction, layer);
    this.ports.put(identifier, port);
    return port;
  }

  @Override
  public SFB768Attribute<?> setAttributeValue(SysML4MName name, SysML4MIdentifier identifier, SFB768AttributeValue value) {
    AttributeImpl<?> result = null;
    if (value instanceof SFB768StringAttributeValue) {
      result = new StringAttribute(identifier, name, this, (SFB768StringAttributeValue) value);
    }
    else if (value instanceof SFB768Block) {
      result = new BlockAttributeImpl(identifier, name, this, (BlockImpl) value);
    }
    else {
      result = new PortAttributeImpl(identifier, name, this, (PortImpl) value);
    }
    this.attributes.put(identifier, result);
    return result;
  }

  @Override
  public PortImpl findPort(SysML4MIdentifier identifier) {
    return ports.get(identifier);
  }

  @Override
  public AttributeImpl<?> findAttribute(SysML4MIdentifier identifier) {
    return attributes.get(identifier);
  }
}
