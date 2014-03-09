package de.tum.pssif.sysml4mechatronics.magicuml;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;


public interface UmlAttributeReferencedType extends UmlAttribute {

  SysML4MIdentifier getReferencedTypeIdentifier();

  UmlClass getReferencedType();

}
