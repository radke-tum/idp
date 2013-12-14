package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;

import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.Named;


public interface AttributeGroups extends AttributableRead, AttributableWrite, Named {

  AttributeGroup createAttributeGroup(String name);

  Collection<AttributeGroup> getAttributeGroups();

  AttributeGroup getDefaultAttributeGroup();

  AttributeGroup findAttributeGroup(String name);

  void removeAttributeGroup(AttributeGroup attributeGroup);

}