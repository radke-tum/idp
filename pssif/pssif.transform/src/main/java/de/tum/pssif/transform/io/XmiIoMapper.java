package de.tum.pssif.transform.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import de.tum.pssif.transform.IoMapper;
import de.tum.pssif.transform.graph.Edge;
import de.tum.pssif.transform.graph.Graph;
import de.tum.pssif.transform.graph.Node;
import de.tum.pssif.xmi.XmiConstants;
import de.tum.pssif.xmi.XmiGraph;
import de.tum.pssif.xmi.XmiDocumentLoader;
import de.tum.pssif.xmi.XmiIoMapperTypes;
import de.tum.pssif.xmi.entities.XmiActivityDiagramEdge;
import de.tum.pssif.xmi.entities.XmiActivityDiagramEdgeControlFlow;
import de.tum.pssif.xmi.entities.XmiActivityDiagramEdgeObjectFlow;
import de.tum.pssif.xmi.entities.XmiActivityDiagramGuard;
import de.tum.pssif.xmi.entities.XmiActivityDiagramNode;
import de.tum.pssif.xmi.entities.XmiChild;
import de.tum.pssif.xmi.entities.XmiChildVisibility;
import de.tum.pssif.xmi.entities.XmiExtend;
import de.tum.pssif.xmi.entities.XmiExtensionPoint;
import de.tum.pssif.xmi.entities.XmiGeneralization;
import de.tum.pssif.xmi.entities.XmiInclude;
import de.tum.pssif.xmi.entities.XmiInterfaceRealization;
import de.tum.pssif.xmi.entities.XmiNode;
import de.tum.pssif.xmi.entities.XmiOwnedAttribute;
import de.tum.pssif.xmi.entities.XmiOwnedEnd;
import de.tum.pssif.xmi.entities.XmiOwnedOperation;
import de.tum.pssif.xmi.entities.XmiOwnedParameter;
import de.tum.pssif.xmi.entities.XmiPackagedElement;
import de.tum.pssif.xmi.entities.XmiPackagedElementActor;
import de.tum.pssif.xmi.entities.XmiPackagedElementAssociation;
import de.tum.pssif.xmi.entities.XmiPackagedElementClass;
import de.tum.pssif.xmi.entities.XmiPackagedElementComponent;
import de.tum.pssif.xmi.entities.XmiPackagedElementInterface;
import de.tum.pssif.xmi.entities.XmiPackagedElementUseCase;
import de.tum.pssif.xmi.exception.XmiException;
import de.tum.pssif.xmi.impl.XmiDocumentLoaderFactory;

public class XmiIoMapper implements IoMapper, XmiConstants {

	// das xmiDocument wird zu einem Genersichen Graphen transformiert
	@Override
	public Graph read(InputStream in) {
		XmiGraph xmiGraph = null;
		try {
			XmiDocumentLoader loader = XmiDocumentLoaderFactory.INSTANCE
					.create();
			xmiGraph = loader.loadDocument(in);
		} catch (XmiException e) {
			throw new PSSIFIoException("Failed to load XMI document.", e);
		}

		Graph graph = new Graph();
		read(xmiGraph, graph);

		return graph;
	}

	@Override
	public void write(Graph graph, OutputStream out) {

	}

	// für alle Nodes werden alle drei Funktionen aufgerufen um den Graphen zu
	// vervollständigen
	private void read(XmiGraph xmiGraph, Graph graph) {

		for (XmiNode node : xmiGraph.getXmiNodes().values()) {
			readNode(node, graph);
		}

		for (XmiNode node : xmiGraph.getXmiNodes().values()) {
			readEdge(node, graph, xmiGraph);
		}

		for (XmiNode node : xmiGraph.getXmiNodes().values()) {
			applyAttributes(node, graph);
		}
	}

	private void readNode(XmiNode xmiNode, Graph graph) {

		// nur Elemente die in PSS-IF als Knoten dargestellt werden sollen
		// werden hier als Knoten gespeichert
		boolean isClass = xmiNode instanceof XmiPackagedElementClass;
		boolean isInterface = xmiNode instanceof XmiPackagedElementInterface;
		boolean isComponent = xmiNode instanceof XmiPackagedElementComponent;
		boolean isActor = xmiNode instanceof XmiPackagedElementActor;
		boolean isUseCase = xmiNode instanceof XmiPackagedElementUseCase;
		boolean isOwnedOperation = xmiNode instanceof XmiOwnedOperation;
		boolean isOwnedParameter = xmiNode instanceof XmiOwnedParameter;
		boolean isAttribute = (xmiNode instanceof XmiOwnedAttribute)
				&& (((XmiOwnedAttribute) xmiNode).getAssociation() == null);
		boolean isActivityDiagramNode = xmiNode instanceof XmiActivityDiagramNode;

		if (isClass || isInterface || isComponent || isActor || isUseCase
				|| isOwnedOperation || isOwnedParameter || isAttribute
				|| isActivityDiagramNode) {

			// im Generischen Graphen wird eine Node erstellt
			Node node = graph.createNode(xmiNode.getXmiID());
			node.setAttribute(ATTRIBUTE_NAME, xmiNode.getName());

			if (xmiNode instanceof XmiChildVisibility) {
				node.setAttribute(ATTRIBUTE_VISIBILITY,
						((XmiChildVisibility) xmiNode).getVisibility());
			}

			// je nach Nodetyp wird der Typ des Nodes gespeichert
			if (xmiNode instanceof XmiPackagedElement) {
				if (xmiNode instanceof XmiPackagedElementClass) {
					node.setType(XmiIoMapperTypes.CLASS.name());
					node.setAttribute("comment",
							((XmiPackagedElementClass) xmiNode).getIsAbstract());
				} else if (xmiNode instanceof XmiPackagedElementActor) {
					node.setType(XmiIoMapperTypes.ACTOR.name());
				} else if (xmiNode instanceof XmiPackagedElementUseCase) {
					node.setType(XmiIoMapperTypes.USECASE.name());
				} else if (xmiNode instanceof XmiPackagedElementComponent) {
					node.setType(XmiIoMapperTypes.COMPONENT.name());
				} else if (xmiNode instanceof XmiPackagedElementInterface) {
					node.setType(XmiIoMapperTypes.INTERFACE.name());
				}
			}

			if (xmiNode instanceof XmiOwnedAttribute) {
				node.setType(XmiIoMapperTypes.ATTRIBUTE.name());
			}

			if (xmiNode instanceof XmiOwnedOperation) {
				node.setType(XmiIoMapperTypes.OPERATION.name());
			}

			if (xmiNode instanceof XmiOwnedParameter) {
				XmiOwnedParameter parameter = (XmiOwnedParameter) xmiNode;

				node.setType(XmiIoMapperTypes.PARAMATER.name());
				node.setAttribute(ATTRIBUTE_DIRECTION, parameter.getDirection());

			}
			if (xmiNode instanceof XmiActivityDiagramNode) {

				if (xmiNode.getXmiType().equals(TYPE_DECISION_NODE)) {
					node.setType(XmiIoMapperTypes.DECISION_OPERATOR.name());

				} else if (xmiNode.getXmiType().equals(TYPE_MERGE_NODE)) {
					node.setType(XmiIoMapperTypes.MERGE_OPERATOR.name());

				} else if (xmiNode.getXmiType()
						.equals(TYPE_CENTRAL_BUFFER_NODE)
						|| xmiNode.getXmiType().equals(TYPE_FINAL_NODE)
						|| xmiNode.getXmiType().equals(TYPE_INITIAL_NODE)) {
					node.setType(XmiIoMapperTypes.BUFFER.name());

				} else if (xmiNode.getXmiType().equals(
						TYPE_CALL_BEHAVIOUR_ACTION)) {
					node.setType(XmiIoMapperTypes.ACTION.name());

				} else if (xmiNode.getXmiType().equals(TYPE_FORK_NODE)) {
					node.setType(XmiIoMapperTypes.FORK_OPERATOR.name());

				} else if (xmiNode.getXmiType().equals(TYPE_JOIN_NODE))
					node.setType(XmiIoMapperTypes.JOIN_OPERATOR.name());
			}

		}
	}

	// Kanten bzw. Assoziationen werden gebildet
	private void readEdge(XmiNode xmiNode, Graph graph, XmiGraph xmiGraph) {
		Node source = null;
		Node target = null;

		if (xmiNode instanceof XmiPackagedElement) {
			XmiPackagedElement element = (XmiPackagedElement) xmiNode;

			if (element instanceof XmiPackagedElementAssociation) {
				XmiPackagedElementAssociation elementAssociation = (XmiPackagedElementAssociation) xmiNode;

				String idMemberEndSource = elementAssociation
						.getMemberEndSource().getIdref();
				String idMemberEndTarget = elementAssociation
						.getMemberEndTarget().getIdref();

				XmiChild nodeSource = (XmiChild) xmiGraph
						.findNode(idMemberEndSource);
				XmiChild nodeTarget = (XmiChild) xmiGraph
						.findNode(idMemberEndTarget);

				Edge edge = graph.createEdge(elementAssociation.getXmiID());
				edge.setAttribute(ATTRIBUTE_NAME, elementAssociation.getName());

				if (nodeSource instanceof XmiOwnedEnd
						&& nodeTarget instanceof XmiOwnedEnd) {
					XmiOwnedEnd nodeSourceEnd = (XmiOwnedEnd) nodeSource;
					XmiOwnedEnd nodeTargetEnd = (XmiOwnedEnd) nodeTarget;

					XmiNode nodeSourceType = (XmiNode) xmiGraph
							.findNode(nodeSourceEnd.getType());
					XmiNode nodeTargetType = (XmiNode) xmiGraph
							.findNode(nodeTargetEnd.getType());

					if (nodeSourceType.getXmiType().equalsIgnoreCase(
							TYPE_USE_CASE)
							|| nodeSourceType.getXmiType().equalsIgnoreCase(
									TYPE_ACTOR)
							|| nodeTargetType.getXmiType().equalsIgnoreCase(
									TYPE_USE_CASE)
							|| nodeTargetType.getXmiType().equalsIgnoreCase(
									TYPE_ACTOR)) {

						edge.setType(XmiIoMapperTypes.INVOCES.name());

						edge.setAttribute(
								ELEMENT_ASSOCIATION_LOWERCARDINALITYSOURCE,
								nodeSourceEnd.getLowerValue() != null ? nodeSourceEnd
										.getLowerValue() : "0");
						edge.setAttribute(
								ELEMENT_ASSOCIATION_LOWERCARDINALITYDESTINATION,
								nodeTargetEnd.getLowerValue() != null ? nodeTargetEnd
										.getLowerValue() : "0");

						edge.setAttribute(
								ELEMENT_ASSOCIATION_UPPERCARDINALITYSOURCE,
								nodeSourceEnd.getUpperValue());
						edge.setAttribute(
								ELEMENT_ASSOCIATION_UPPERCARDINALITYDESTINATION,
								nodeTargetEnd.getUpperValue());

						source = graph.findNode(nodeSourceType.getXmiID());
						target = graph.findNode(nodeTargetType.getXmiID());

						graph.connect(source, edge, target);
					}

				} else {

					edge.setType(XmiIoMapperTypes.ASSOCIATION.name());

					if (nodeSource instanceof XmiOwnedAttribute
							&& nodeTarget instanceof XmiOwnedAttribute) {
						XmiOwnedAttribute nodeSourceAttribute = (XmiOwnedAttribute) nodeSource;
						XmiOwnedAttribute nodeTargetAttribute = (XmiOwnedAttribute) nodeTarget;

						if (nodeSourceAttribute.getAggregation() != null) {
						edge.setAttribute(ATTRIBUTE_AGGREGATION,
								nodeSourceAttribute.getAggregation());
						} else if (nodeTargetAttribute.getAggregation() != null) {
							edge.setAttribute(ATTRIBUTE_AGGREGATION,
									nodeTargetAttribute.getAggregation());
						}

						edge.setAttribute(
								ELEMENT_ASSOCIATION_LOWERCARDINALITYSOURCE,
								nodeTargetAttribute.getLowerValue() != null ? nodeTargetAttribute
										.getLowerValue() : "0");
						edge.setAttribute(
								ELEMENT_ASSOCIATION_LOWERCARDINALITYDESTINATION,
								nodeSourceAttribute.getLowerValue() != null ? nodeSourceAttribute
										.getLowerValue() : "0");

						edge.setAttribute(
								ELEMENT_ASSOCIATION_UPPERCARDINALITYSOURCE,
								nodeTargetAttribute.getUpperValue());
						edge.setAttribute(
								ELEMENT_ASSOCIATION_UPPERCARDINALITYDESTINATION,
								nodeSourceAttribute.getUpperValue());

						source = graph.findNode(nodeSourceAttribute
								.getParentId());
						target = graph.findNode(nodeTargetAttribute
								.getParentId());

						// bezogen auf die Assoziationen im Use-Case Diagramm
					}

					graph.connect(source, edge, target);
				}

			} else if (xmiNode instanceof XmiPackagedElementUseCase) {
				XmiPackagedElementUseCase useCase = (XmiPackagedElementUseCase) xmiNode;

				if (useCase.getXmiSubject() != null) {

					source = graph.findNode(useCase.getXmiSubject().getIdref()); // id
					target = graph.findNode(useCase.getXmiID()); // id

					Edge edge = graph.createEdge(UUID.randomUUID().toString());
					edge.setType(XmiIoMapperTypes.SUPPORTS.name());

					edge.setDirected(true);
					graph.connect(source, edge, target);
				}
			}

		} else if (xmiNode instanceof XmiGeneralization) {
			XmiGeneralization element = (XmiGeneralization) xmiNode;

			source = graph.findNode(element.getParentId()); // id
			target = graph.findNode(element.getGeneral()); // id

			Edge edge = graph.createEdge(element.getXmiID());
			edge.setType(XmiIoMapperTypes.GENERALIZES.name());

			edge.setDirected(true);
			graph.connect(source, edge, target);

		} else if (xmiNode instanceof XmiInterfaceRealization) {
			XmiInterfaceRealization element = (XmiInterfaceRealization) xmiNode;

			source = graph.findNode(element.getParentId()); // id
			target = graph.findNode(element.getContract()); // id

			Edge edge = graph.createEdge(element.getXmiID());
			edge.setType(XmiIoMapperTypes.INTERFACE_REALIZATION.name());

			edge.setDirected(true);
			graph.connect(source, edge, target);

		} else if (xmiNode instanceof XmiInclude) {
			XmiInclude element = (XmiInclude) xmiNode;

			source = graph.findNode(element.getParentId()); // id
			target = graph.findNode(element.getAddition()); // id

			Edge edge = graph.createEdge(element.getXmiID());
			edge.setType(XmiIoMapperTypes.INCLUDES.name());

			edge.setDirected(true);
			graph.connect(source, edge, target);

		} else if (xmiNode instanceof XmiExtend) {
			XmiExtend element = (XmiExtend) xmiNode;

			source = graph.findNode(element.getParentId()); // id
			target = graph.findNode(element.getExtendedCase()); // id

			Edge edge = graph.createEdge(element.getXmiID());
			edge.setType(XmiIoMapperTypes.EXTENDS.name());
			edge.setAttribute("comment", element.getConditionBody());

			edge.setDirected(true);
			graph.connect(source, edge, target);

		} else if (xmiNode instanceof XmiOwnedOperation) {
			XmiOwnedOperation element = (XmiOwnedOperation) xmiNode;

			target = graph.findNode(element.getXmiID());
			source = graph.findNode(element.getParentId());

			Edge edge = graph.createEdge(element.getXmiID());
			edge.setType(XmiIoMapperTypes.INVOCES.name());

			edge.setDirected(false);
			graph.connect(source, edge, target);

		} else if (xmiNode instanceof XmiOwnedAttribute) {
			XmiOwnedAttribute element = (XmiOwnedAttribute) xmiNode;

			target = graph.findNode(element.getXmiID());
			source = graph.findNode(element.getParentId());

			Edge edge = graph.createEdge(element.getXmiID());
			edge.setType("");

			if (source != null && target != null) {
				edge.setDirected(false);
				graph.connect(source, edge, target);
			}

		} else if (xmiNode instanceof XmiOwnedParameter) {
			XmiOwnedParameter element = (XmiOwnedParameter) xmiNode;

			target = graph.findNode(element.getXmiID());
			source = graph.findNode(element.getParentId());

			Edge edge = graph.createEdge(element.getXmiID());
			edge.setType("");

			if (source != null && target != null) {
				edge.setDirected(false);
				graph.connect(source, edge, target);
			}

		} else if (xmiNode instanceof XmiActivityDiagramEdge) {

			XmiActivityDiagramEdge element = (XmiActivityDiagramEdge) xmiNode;
			Edge edge = graph.createEdge(element.getXmiID());
			;

			if (element instanceof XmiActivityDiagramEdgeControlFlow) {

				source = graph.findNode(element.getSourceNode());
				target = graph.findNode(element.getTargetNode());

				edge.setType(XmiIoMapperTypes.CONTROL_FLOW.name());

			} else if (element instanceof XmiActivityDiagramEdgeObjectFlow) {

				source = graph.findNode(element.getSourceNode());
				target = graph.findNode(element.getTargetNode());

				edge.setType(XmiIoMapperTypes.OBJECT_FLOW.name());

			}

			edge.setDirected(false);
			graph.connect(source, edge, target);

		}
	}

	private void applyAttributes(XmiNode xmiNode, Graph graph) {

		if (xmiNode instanceof XmiExtensionPoint) {
			XmiExtensionPoint point = (XmiExtensionPoint) xmiNode;
			Node parentNode = graph.findNode(point.getParentId());
			parentNode.setAttribute(ATTRIBUTE_EXTENSION_POINT_NAME,
					point.getName());
		}

		if (xmiNode instanceof XmiActivityDiagramGuard) {
			XmiActivityDiagramGuard element = (XmiActivityDiagramGuard) xmiNode;

			Edge parent = graph.findEdge(element.getParentId());

			if (parent != null) {

				if (element.getValue() != null) {
					parent.setAttribute("comment", element.getValue());

				} else if (element.getBody() != null) {
					parent.setAttribute("comment", element.getBody());

				} else if (element.getExpr() != null) {
					parent.setAttribute("comment", element.getExpr());

				} else if (element.getOperand() != null) {
					System.out.println(element.getOperand());

					parent.setAttribute("comment", element.getOperand());

				}

			}
		}

	}

}
