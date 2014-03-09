package de.tum.pssif.sysml4mechatronics.sfb768.impl;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.sysml4mechatronics.sfb768.Attribute;
import de.tum.pssif.sysml4mechatronics.sfb768.AttributeValue;
import de.tum.pssif.sysml4mechatronics.sfb768.Block;
import de.tum.pssif.sysml4mechatronics.sfb768.Port;
import de.tum.pssif.sysml4mechatronics.sfb768.PortDirection;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Identifier;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Layer;
import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Name;


public abstract class BlockImpl extends SFB768IdentifiableNamedImpl implements Block {

  private final Map<SFB768Identifier, PortImpl>     ports      = Maps.newHashMap();
  private final Map<SFB768Identifier, Attribute<?>> attributes = Maps.newHashMap();

  BlockImpl(SFB768Identifier identifier, SFB768Name name) {
    super(identifier, name);
  }

  @Override
  public Set<Port> getPorts() {
    return Sets.<Port> newHashSet(this.ports.values());
  }

  @Override
  public Set<Attribute<?>> getAttributes() {
    return Sets.<Attribute<?>> newHashSet(this.attributes.values());
  }

  @Override
  public PortImpl createPort(SFB768Name name, SFB768Identifier identifier, PortDirection direction, SFB768Layer layer) {
    PortImpl port = new PortImpl(this, identifier, name, direction, layer);
    this.ports.put(identifier, port);
    return port;
  }

  @Override
  public Attribute<?> setAttributeValue(SFB768Name name, SFB768Identifier identifier, AttributeValue value) {
    // TODO Auto-generated method stub
    return null;
  }

}
