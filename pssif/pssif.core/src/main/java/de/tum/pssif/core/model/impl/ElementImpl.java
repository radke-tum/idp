package de.tum.pssif.core.model.impl;

import de.tum.pssif.core.model.Element;


public abstract class ElementImpl implements Element {
  private String id;

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public final String getId() {
    return id;
  }
}
