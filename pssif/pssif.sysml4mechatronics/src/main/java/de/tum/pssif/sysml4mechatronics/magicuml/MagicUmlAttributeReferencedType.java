package de.tum.pssif.sysml4mechatronics.magicuml;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;


public interface MagicUmlAttributeReferencedType extends MagicUmlAttribute {

  SysML4MIdentifier getReferencedTypeIdentifier();

  MagicUmlClass getReferencedType();

}
