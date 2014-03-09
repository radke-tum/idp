package de.tum.pssif.sysml4mechatronics.magicuml;

import java.util.Set;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifiable;
import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.common.SysML4MNamed;


/**
 * Notes:
 * <li>Separate namespaces for data types and classes are neccessary.</li>
 */
public interface MagicumlModel extends SysML4MIdentifiable, SysML4MNamed {

  Set<MagicumlPackagedElement> getPackagedElements();

  Set<MagicumlClass> getClasses();

  Set<MagicumlDataType> getDataTypes();

  MagicumlPackagedElement findPackagedElementByIdentifier(SysML4MIdentifier identitifer);

  MagicumlClass findClassByIdentifier(SysML4MIdentifier identitifer);

  MagicumlDataType findDataTypeByIdentifier(SysML4MIdentifier identitifer);

  MagicumlPackagedElement findPackagedElementByName(SysML4MName name);

  MagicumlClass findClassByName(SysML4MName name);

  MagicumlDataType findDataTypeByName(SysML4MName name);

  MagicumlClass createUmlClass(SysML4MIdentifier identitifer, SysML4MName name, MagicumlVisibility visibility);

  MagicumlDataType createDataType(SysML4MIdentifier identitifer, SysML4MName name);

  void resolveReferences();

}
