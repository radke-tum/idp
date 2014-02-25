package de.tum.pssif.transform.transformation.moved;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.impl.base.AbstractAttribute;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;


public class MovedAttribute extends AbstractAttribute {
  private final Attribute         baseAttribute;
  private final ConnectionMapping mapping;

  public MovedAttribute(String name, Attribute baseAttribute, ConnectionMapping mapping) {
    super(name, baseAttribute.getType(), baseAttribute.getUnit(), baseAttribute.isVisible(), baseAttribute.getCategory());
    this.baseAttribute = baseAttribute;
    this.mapping = mapping;
  }

  @Override
  public void set(Element element, PSSIFOption<PSSIFValue> value) {
    for (Edge e : mapping.getFrom().apply((Node) element).getMany()) {
      for (Node n : mapping.getTo().apply(e).getMany()) {
        baseAttribute.set(n, value);
      }
    }
  }

  @Override
  public PSSIFOption<PSSIFValue> get(Element element) {
    for (Edge e : mapping.getFrom().apply((Node) element).getMany()) {
      for (Node n : mapping.getTo().apply(e).getMany()) {
        return baseAttribute.get(n);
      }
    }
    return PSSIFOption.none();
  }

}
