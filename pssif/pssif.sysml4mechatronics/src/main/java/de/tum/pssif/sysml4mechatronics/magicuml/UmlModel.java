package de.tum.pssif.sysml4mechatronics.magicuml;

import java.util.Set;

import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifiable;
import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.common.SysML4MNamed;


public interface UmlModel extends SysML4MIdentifiable, SysML4MNamed {

  Set<UmlPackagedElement> getPackagedElements();

  Set<UmlClass> getClasses();

  Set<UmlDataType> getDataTypes();

  UmlPackagedElement findPackagedElementByIdentifier(SysML4MIdentifier identitifer);

  UmlClass findClassByIdentifier(SysML4MIdentifier identitifer);

  UmlDataType findDataTypeByIdentifier(SysML4MIdentifier identitifer);

  UmlPackagedElement findPackagedElementByName(SysML4MName name);

  UmlClass findClassByName(SysML4MName name);

  UmlDataType findDataTypeByName(SysML4MName name);

  UmlClass createUmlClass(SysML4MIdentifier identitifer, SysML4MName name, UmlVisibility visibility);

  UmlDataType createDataType(SysML4MIdentifier identitifer, SysML4MName name);

}
