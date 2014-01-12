package de.tum.pssif.transform.mapper.graphml;

import java.io.InputStream;
import java.util.Map;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.core.util.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.util.PSSIFOption;


public class GraphMLImporter {
  public Model read(InputStream in) {
    Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
    Model result = new ModelImpl();

    GraphMLGraph graph = GraphMLGraph.read(in);

    for (GraphMLNode inNode : graph.getNodes()) {
      NodeType type = metamodel.findNodeType(inNode.getType());
      if (type != null) {
        Attribute idAttribute = type.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
        Node resultNode = type.create(result);
        idAttribute.set(resultNode, idAttribute.getType().fromObject(inNode.getId()));

        readAttributes(type, resultNode, inNode);
      }
      else {
        System.out.println("NodeType " + inNode.getType() + " not found!");
      }
    }

    for (GraphMLEdge inEdge : graph.getEdges()) {
      EdgeType type = metamodel.findEdgeType(inEdge.getType());
      if (type != null) {

        GraphMLNode inSourceNode = graph.getNode(inEdge.getSourceId());
        GraphMLNode inTargetNode = graph.getNode(inEdge.getTargetId());

        NodeType sourceType = metamodel.findNodeType(inSourceNode.getType());
        PSSIFOption<Node> sourceNode = sourceType.apply(result, inSourceNode.getId());

        NodeType targetType = metamodel.findNodeType(inTargetNode.getType());
        PSSIFOption<Node> targetNode = targetType.apply(result, inTargetNode.getId());

        if (sourceType != null && targetType != null) {
          ConnectionMapping mapping = type.getMapping(sourceType, targetType);
          if (sourceNode.isOne() && targetNode.isOne()) {
            Edge edge = mapping.create(result, sourceNode.getOne(), targetNode.getOne());
            readAttributes(type, edge, inEdge);
          }
          else {
            System.out.println("either source or target node not found!");
          }
        }
        else {
          System.out.println("either source or target type of edge " + inEdge.getId() + " not found!");
        }
      }
      else {
        System.out.println("EdgeType " + inEdge.getType() + " not found!");
      }
    }

    return result;
  }

  private static void readAttributes(ElementType<?> type, Element element, GraphMLElement inElement) {
    Map<String, String> values = inElement.getValues();
    for (String key : values.keySet()) {
      Attribute attribute = type.findAttribute(key);
      if (attribute != null) {
        attribute.set(element, attribute.getType().fromObject(values.get(key)));
      }
      else {
        System.out.println("AttributeType " + key + " not found!");
      }
    }
  }
}
