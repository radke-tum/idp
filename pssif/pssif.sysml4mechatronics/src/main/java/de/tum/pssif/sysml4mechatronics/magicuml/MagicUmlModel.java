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
public interface MagicUmlModel extends SysML4MIdentifiable, SysML4MNamed {

  Set<MagicUmlPackagedElement> getPackagedElements();

  Set<MagicUmlClass> getClasses();

  Set<MagicUmlDataType> getDataTypes();

  MagicUmlPackagedElement findPackagedElementByIdentifier(SysML4MIdentifier identitifer);

  MagicUmlClass findClassByIdentifier(SysML4MIdentifier identitifer);

  MagicUmlDataType findDataTypeByIdentifier(SysML4MIdentifier identitifer);

  MagicUmlPackagedElement findPackagedElementByName(SysML4MName name);

  MagicUmlClass findClassByName(SysML4MName name);

  MagicUmlDataType findDataTypeByName(SysML4MName name);

  MagicUmlClass createUmlClass(SysML4MIdentifier identitifer, SysML4MName name, MagicUmlVisibility visibility);

  MagicUmlDataType createDataType(SysML4MIdentifier identitifer, SysML4MName name);

  void resolveReferences();

}
