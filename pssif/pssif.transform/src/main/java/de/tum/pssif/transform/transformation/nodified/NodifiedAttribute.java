package de.tum.pssif.transform.transformation.nodified;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.metamodel.Units;
import de.tum.pssif.core.metamodel.impl.base.AbstractAttribute;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;


public class NodifiedAttribute extends AbstractAttribute {
  private final NodeType          targetType;           // the type of the node to create
  private final EdgeType          edgeType;
  private final ConnectionMapping mapping;              // the connection mapping to create an edge of for the connection of the created node and the source node

  private final Attribute         targetTypeIdAttribute;
  private final Attribute         edgeTypeIdAttribute;

  public NodifiedAttribute(String name, NodeType targetType, EdgeType edgeType, ConnectionMapping mapping) {
    super(name, PrimitiveDataType.STRING, Units.NONE, true, AttributeCategory.METADATA);
    this.targetType = targetType;
    this.edgeType = edgeType;
    this.mapping = mapping;

    targetTypeIdAttribute = this.targetType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    edgeTypeIdAttribute = this.edgeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
  }

  @Override
  public void set(Element element, PSSIFOption<PSSIFValue> value) {
    Model model = element.getModel();
    for (PSSIFValue val : value.getMany()) {
      PSSIFOption<Node> node = targetType.apply(model, val.asString());
      if (node.isNone()) {
        node = PSSIFOption.one(targetType.create(model));
      }
      targetTypeIdAttribute.set(node.getOne(), PSSIFOption.one(val));
      Edge edge = mapping.create(model, (Node) element, node.getOne());
      edgeTypeIdAttribute.set(edge, PSSIFOption.one(PSSIFValue.create("nodified_" + val.asString())));
    }
  }

  @Override
  public PSSIFOption<PSSIFValue> get(Element element) {
    Collection<PSSIFValue> result = Sets.newHashSet();

    PSSIFOption<Edge> edges = mapping.getFrom().apply((Node) element);

    for (Edge edge : edges.getMany()) {
      PSSIFOption<Node> connecteds = mapping.getTo().apply(edge);
      for (Node connected : connecteds.getMany()) {
        result.add(targetTypeIdAttribute.get(connected).getOne());
      }
    }

    return PSSIFOption.many(result);
  }
}
