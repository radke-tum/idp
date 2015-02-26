package de.tum.pssif.transform.mapper.xmi;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.external.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.transform.Mapper;
import de.tum.pssif.transform.graph.AElement;
import de.tum.pssif.transform.graph.Edge;
import de.tum.pssif.transform.graph.Graph;
import de.tum.pssif.transform.graph.Node;
import de.tum.pssif.transform.io.XmiIoMapper;
import de.tum.pssif.transform.transformation.AliasJunctionNodeTypeTransformation;
import de.tum.pssif.transform.transformation.AliasNodeTypeTransformation;
import de.tum.pssif.transform.transformation.RenameEdgeTypeTransformation;
import de.tum.pssif.transform.transformation.RenameJunctionNodeTypeTransformation;
import de.tum.pssif.xmi.XmiIoMapperTypes;

public class XmiMapper implements Mapper {

	@Override
	public Model read(Metamodel metamodel, InputStream inputStream) {
		return readInternal(metamodel, inputStream);
	}

	@Override
	public void write(Metamodel metamodel, Model model,
			OutputStream outputStream) {
		// TODO
	}

	private Model readInternal(Metamodel metamodel, InputStream inputStream) {
		// nodes
		// der XmiMapper gibt dem Metamodell die Übersetzungsregeln
		Metamodel view = new AliasNodeTypeTransformation(metamodel.getNodeType(
				"Block").getOne(), XmiIoMapperTypes.CLASS.name(),
				XmiIoMapperTypes.CLASS.name()).apply(metamodel);
		view = new AliasNodeTypeTransformation(metamodel.getNodeType("Actor")
				.getOne(), XmiIoMapperTypes.ACTOR.name(),
				XmiIoMapperTypes.ACTOR.name()).apply(view);
		view = new AliasNodeTypeTransformation(metamodel
				.getNodeType("Activity").getOne(),
				XmiIoMapperTypes.OPERATION.name(),
				XmiIoMapperTypes.OPERATION.name()).apply(view);
		view = new AliasNodeTypeTransformation(metamodel.getNodeType("Block")
				.getOne(), XmiIoMapperTypes.COMPONENT.name(),
				XmiIoMapperTypes.COMPONENT.name()).apply(view);
		view = new AliasNodeTypeTransformation(metamodel
				.getNodeType("Use Case").getOne(),
				XmiIoMapperTypes.USECASE.name(),
				XmiIoMapperTypes.USECASE.name()).apply(view);
		view = new AliasNodeTypeTransformation(metamodel.getNodeType("Block")
				.getOne(), XmiIoMapperTypes.INTERFACE.name(),
				XmiIoMapperTypes.INTERFACE.name()).apply(view);
		view = new AliasNodeTypeTransformation(metamodel
				.getNodeType("Activity").getOne(),
				XmiIoMapperTypes.ACTION.name(), XmiIoMapperTypes.ACTION.name())
				.apply(view);
		view = new AliasNodeTypeTransformation(metamodel.getNodeType("State")
				.getOne(), XmiIoMapperTypes.BUFFER.name(),
				XmiIoMapperTypes.BUFFER.name()).apply(view);

		// // conjunctions
		 view = new AliasJunctionNodeTypeTransformation(view
		 .getJunctionNodeType(
		 PSSIFCanonicMetamodelCreator.TAGS.get("N_CONJUNCTION"))
		 .getOne(), XmiIoMapperTypes.DECISION_OPERATOR.name(),
		 XmiIoMapperTypes.DECISION_OPERATOR.name()).apply(view);
		view = new AliasJunctionNodeTypeTransformation(view
				.getJunctionNodeType(
						PSSIFCanonicMetamodelCreator.TAGS.get("N_CONJUNCTION"))
				.getOne(), XmiIoMapperTypes.MERGE_OPERATOR.name(),
				XmiIoMapperTypes.MERGE_OPERATOR.name()).apply(view);
		 view = new AliasJunctionNodeTypeTransformation(view
		 .getJunctionNodeType(
		 PSSIFCanonicMetamodelCreator.TAGS.get("N_CONJUNCTION"))
		 .getOne(), XmiIoMapperTypes.FORK_OPERATOR.name(),
		 XmiIoMapperTypes.FORK_OPERATOR.name()).apply(view);
		view = new AliasJunctionNodeTypeTransformation(view
				.getJunctionNodeType(
						PSSIFCanonicMetamodelCreator.TAGS.get("N_CONJUNCTION"))
				.getOne(), XmiIoMapperTypes.JOIN_OPERATOR.name(),
				XmiIoMapperTypes.JOIN_OPERATOR.name()).apply(view);

		
		
		// edges
		view = new RenameEdgeTypeTransformation(view.getEdgeType("Contains")
				.getOne(), XmiIoMapperTypes.ASSOCIATION.name()).apply(view);
		view = new RenameEdgeTypeTransformation(view.getEdgeType("Implements")
				.getOne(), XmiIoMapperTypes.INTERFACE_REALIZATION.name())
				.apply(view);
		view = new RenameEdgeTypeTransformation(view.getEdgeType("Generalizes")
				.getOne(), XmiIoMapperTypes.GENERALIZES.name()).apply(view);
		view = new RenameEdgeTypeTransformation(view.getEdgeType("Invoces")
				.getOne(), XmiIoMapperTypes.INVOCES.name()).apply(view);
		view = new RenameEdgeTypeTransformation(view.getEdgeType("Supports")
				.getOne(), XmiIoMapperTypes.SUPPORTS.name()).apply(view);
		view = new RenameEdgeTypeTransformation(view.getEdgeType("Extends")
				.getOne(), XmiIoMapperTypes.EXTENDS.name()).apply(view);
		view = new RenameEdgeTypeTransformation(view.getEdgeType("Includes")
				.getOne(), XmiIoMapperTypes.INCLUDES.name()).apply(view);
		view = new RenameEdgeTypeTransformation(view
				.getEdgeType("Control Flow").getOne(),
				XmiIoMapperTypes.CONTROL_FLOW.name()).apply(view);
		view = new RenameEdgeTypeTransformation(view.getEdgeType("Flow")
				.getOne(), XmiIoMapperTypes.OBJECT_FLOW.name()).apply(view);
		// nach der Anwendung der Übersetzungsregeln wrid das spezielle Modell
		// zurückgegeben

		Model result = new ModelImpl();
		Graph graph = new XmiIoMapper().read(inputStream);

		for (Node node : graph.getNodes()) {
			PSSIFOption<NodeTypeBase> type = view.getBaseNodeType(node
					.getType());
			if (type.isOne()) {
				readNode(result, node, type.getOne());
			} else {
				System.out.println("NodeType " + node.getType()
						+ " not found! Defaulting to Node");
				readNode(result, node,
						view.getNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME)
								.getOne());
			}
		}

		for (Edge inEdge : graph.getEdges()) {
			PSSIFOption<EdgeType> type = view.getEdgeType(inEdge.getType());
			if (type.isOne()) {
				readEdge(view, result, graph, inEdge, type.getOne());
			} else {
				System.out.println("EdgeType " + inEdge.getType()
						+ " not found! Defaulting to Edge");
				readEdge(view, result, graph, inEdge,
						view.getEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME)
								.getOne());
			}
		}

		return result;
	}

	private void readEdge(Metamodel metamodel, Model result, Graph graph,
			Edge inEdge, EdgeType type) {
		if (inEdge.getSource() == null || inEdge.getTarget() == null) {
			return;
		}

		Node inSourceNode = graph.findNode(inEdge.getSource().getId());
		Node inTargetNode = graph.findNode(inEdge.getTarget().getId());

		System.out.println("inSourceNode: " + inSourceNode);
		System.out.println("inTargetNode: " + inTargetNode);

		PSSIFOption<NodeTypeBase> sourceType = metamodel
				.getBaseNodeType(inSourceNode.getType());
		if (sourceType.isNone()) {
			System.out.println("Source Type not found");
			sourceType = metamodel
					.getBaseNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
		}
		PSSIFOption<de.tum.pssif.core.model.Node> sourceNode = sourceType
				.getOne().apply(result, inSourceNode.getId(), true);

		PSSIFOption<NodeTypeBase> targetType = metamodel
				.getBaseNodeType(inTargetNode.getType());
		if (targetType.isNone()) {
			System.out.println("Target Type not found");
			targetType = metamodel
					.getBaseNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
		}
		PSSIFOption<de.tum.pssif.core.model.Node> targetNode = targetType
				.getOne().apply(result, inTargetNode.getId(), true);

		PSSIFOption<ConnectionMapping> mapping = type.getMapping(
				sourceType.getOne(), targetType.getOne());
		if (mapping.isNone()) {
			System.out.println(type.getName() + ": mapping "
					+ sourceType.getOne().getName() + "-"
					+ targetType.getOne().getName()
					+ " not found! Defaulting to Edge");
			type = metamodel.getEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME)
					.getOne();
			mapping = type.getMapping(sourceType.getOne(), targetType.getOne());
		}
		if (!sourceNode.isOne()) {
			System.out.println("source node " + inSourceNode.getId()
					+ " not found!");
		}
		if (!targetNode.isOne()) {
			System.out.println("target node " + inTargetNode.getId()
					+ " not found!");
		}
		if (sourceNode.isOne() && targetNode.isOne() && mapping != null) {
			de.tum.pssif.core.model.Edge edge = mapping.getOne().create(result,
					sourceNode.getOne(), targetNode.getOne());
			type.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID)
					.getOne()
					.set(edge,
							PSSIFOption.one(PSSIFValue.create(inEdge.getId())));
			type.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED)
					.getOne()
					.set(edge,
							PSSIFOption.one(PSSIFValue.create(inEdge
									.isDirected())));
			readAttributes(type, edge, inEdge);
		}
	}

	private void readNode(Model result, Node inNode, NodeTypeBase type) {
		PSSIFOption<Attribute> idAttribute = type
				.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
		de.tum.pssif.core.model.Node resultNode = type.create(result);
		if (idAttribute.isOne()) {
			idAttribute.getOne().set(
					resultNode,
					PSSIFOption.one(idAttribute.getOne().getType()
							.fromObject(inNode.getId())));
		} else {
			System.out.println("Attribute "
					+ PSSIFConstants.BUILTIN_ATTRIBUTE_ID + " not found!");
		}

		readAttributes(type, resultNode, inNode);
	}

	//
	private static void readAttributes(ElementType type,
			de.tum.pssif.core.model.Element element, AElement aelement) {
		Set<String> values = aelement.getAttributeNames();
		for (String key : values) {
			PSSIFOption<Attribute> attribute = type.getAttribute(key);
			if (attribute.isOne()) {
				if (aelement.getAttributeValue(key) != null) {
					attribute
							.getOne()
							.set(element,
									PSSIFOption.one(attribute
											.getOne()
											.getType()
											.fromObject(
													aelement.getAttributeValue(key))));
				}
			} else {
				System.out.println("Attribute " + key + " not found!");
			}
		}
	}

}
