package de.tum.pssif.transform.metamodel.alternatives;

import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.impl.MetamodelImpl;


// TODO overwrite all from metamodel...
public class ViewImpl extends MetamodelImpl implements View {

  private final Metamodel baseMetamodel;

  protected ViewImpl(Metamodel metamodel) {
    this.baseMetamodel = metamodel;
  }

  @Override
  public void add(ElementType<?, ?> elementType) {
    // TODO Auto-generated method stub

  }

  @Override
  public void replace(ElementType<?, ?> toReplace, ElementType<?, ?> replaceWith) {
    //re-create base metamodel with new type TODO
    // TODO Auto-generated method stub

  }

  @Override
  public void remove(ElementType<?, ?> elementType) {
    // TODO Auto-generated method stub

  }

}
