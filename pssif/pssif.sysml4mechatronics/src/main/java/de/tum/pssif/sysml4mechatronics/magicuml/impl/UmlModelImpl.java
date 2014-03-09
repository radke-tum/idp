package de.tum.pssif.sysml4mechatronics.magicuml.impl;

import java.util.Set;

import de.tum.pssif.sysml4mechatronics.common.IdentifiableNamedImpl;
import de.tum.pssif.sysml4mechatronics.common.SysML4MIdentifier;
import de.tum.pssif.sysml4mechatronics.common.SysML4MName;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlClass;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlDataType;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlModel;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlPackagedElement;
import de.tum.pssif.sysml4mechatronics.magicuml.MagicumlVisibility;


public class UmlModelImpl extends IdentifiableNamedImpl implements MagicumlModel {

  public UmlModelImpl(SysML4MIdentifier identifier, SysML4MName name) {
    super(identifier, name);
  }

  @Override
  public Set<MagicumlPackagedElement> getPackagedElements() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<MagicumlClass> getClasses() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<MagicumlDataType> getDataTypes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlPackagedElement findPackagedElementByIdentifier(SysML4MIdentifier identitifer) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlClass findClassByIdentifier(SysML4MIdentifier identitifer) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlDataType findDataTypeByIdentifier(SysML4MIdentifier identitifer) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlPackagedElement findPackagedElementByName(SysML4MName name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlClass findClassByName(SysML4MName name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlDataType findDataTypeByName(SysML4MName name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlClass createUmlClass(SysML4MIdentifier identitifer, SysML4MName name, MagicumlVisibility visibility) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MagicumlDataType createDataType(SysML4MIdentifier identitifer, SysML4MName name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void resolveReferences() {
    // TODO Auto-generated method stub

  }

}
