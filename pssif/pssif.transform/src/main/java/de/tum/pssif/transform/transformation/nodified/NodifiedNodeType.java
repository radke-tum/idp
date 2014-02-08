package de.tum.pssif.transform.transformation.nodified;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class NodifiedNodeType extends ViewedNodeType {
  private final NodeType          baseType;
  private final ConnectionMapping mapping;
  private final NodifiedAttribute attribute;

  public NodifiedNodeType(NodeType baseType, ConnectionMapping mapping, NodifiedAttribute attribute) {
    super(baseType);
    this.baseType = baseType;
    this.mapping = mapping;
    this.attribute = attribute;
  }

  @Override
  public Node create(Model model) {
    return baseType.create(model);
  }

  @Override
  public PSSIFOption<Node> apply(Model model) {
    Collection<Node> result = Sets.newHashSet();

    for (Node candidate : baseType.apply(model).getMany()) {
      for (Edge e : mapping.getTo().apply(candidate).getMany()) {
        for (Node fromNode : mapping.getFrom().apply(e).getMany()) {
          if (attribute.get(fromNode).getOne().getValue().equals(candidate.getId())) {
            System.out.println("NT: Node(" + candidate.getId() + ")");
          }
          else {
            System.out.println("real node, keep it");
            result.add(candidate);
          }
        }
      }
    }

    return PSSIFOption.many(result);
  }

  @Override
  public AttributeGroup createAttributeGroup(String name) {
    return baseType.createAttributeGroup(name);
  }

  @Override
  public void removeAttributeGroup(AttributeGroup attributeGroup) {
    baseType.removeAttributeGroup(attributeGroup);
  }

  @Override
  public Attribute createAttribute(AttributeGroup group, String name, DataType dataType, boolean visible, AttributeCategory category) {
    return baseType.createAttribute(group, name, dataType, visible, category);
  }

  @Override
  public Attribute createAttribute(AttributeGroup group, String name, DataType dataType, Unit unit, boolean visible, AttributeCategory category) {
    return baseType.createAttribute(group, name, dataType, unit, visible, category);
  }
}
