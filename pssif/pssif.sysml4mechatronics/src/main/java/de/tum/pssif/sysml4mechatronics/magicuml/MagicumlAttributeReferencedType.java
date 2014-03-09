package de.tum.pssif.sysml4mechatronics.magicuml;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;


public interface MagicumlAttributeReferencedType extends MagicumlAttribute {

  SysML4MIdentifier getReferencedTypeIdentifier();

  MagicumlClass getReferencedType();

}
