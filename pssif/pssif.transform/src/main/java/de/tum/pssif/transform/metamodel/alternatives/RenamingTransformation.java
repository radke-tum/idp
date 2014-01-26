package de.tum.pssif.transform.metamodel.alternatives;

import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Element;


public abstract class RenamingTransformation<T extends ElementType<T, E>, E extends Element> implements Transformation {

  private final T      toRename;
  private final String newName;

  public RenamingTransformation(T toRename, String newName) {
    this.toRename = toRename;
    this.newName = newName;
  }

  @Override
  public View apply(Metamodel input) {
    View mmView = getOrCreateView(input);
    mmView.replace(toRename, getRenamedType());
    return mmView;
  }

  T getTypeToRename() {
    return toRename;
  }

  String getNewName() {
    return newName;
  }

  protected abstract T getRenamedType();

  private View getOrCreateView(Metamodel metamodel) {
    return metamodel instanceof View ? (View) metamodel : new ViewImpl(metamodel);
  }

}
