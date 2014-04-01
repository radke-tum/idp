package reqtool;

import java.util.HashMap;
import java.util.LinkedList;

import graph.model.IMyNode;
import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;
import gui.graph.GraphVisualization;
import model.ModelBuilder;
import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Node;

public class RequirementVersionManager {

	public static boolean createNewVersion(GraphVisualization gViz, Node node, String newVersion) {
		MyNode mNode = RequirementTracer.getMyNode(node);
		MyNodeType requirementNodeType = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_REQUIREMENT);
		MyEdgeType evolvesTo = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_EVOLVES_TO);

		NodeType nodeType = ModelBuilder.getMetamodel().getNodeType(requirementNodeType.getName()).getOne();

		PSSIFOption<PSSIFValue> oldVersion = nodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().get(node);
		String oVersion = oldVersion.getOne().asString();
		String[] idNV = mNode.getNode().getId().toString().split("_");
		
		if (!hasNextVersions(mNode) && !hasPreviousVersions(mNode)){
			/*TODO
			 * When Requirement has no other versions.
			 */
			System.out.println("This Requirement has no previous or next versions");
			
			ModelBuilder.addNewNodeFromGUI(mNode.getNode().getId() + "_", requirementNodeType);
			
			
			
			
			for (MyNode newNode : ModelBuilder.getAllNodes()) {
				//System.out.println("Node: " + newNode.getName() + " | ID: "	+ newNode.getNode().getId());
				String[] idNVNew = newNode.getNode().getId().toString().split("_");
				if (idNVNew.length > 0 && idNVNew[0].equals(idNV[0])) {
					PSSIFOption<PSSIFValue> version2 = nodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().get(newNode.getNode());
					if (version2.isNone()) {
						ModelBuilder.addNewEdgeGUI(newNode, mNode, evolvesTo, true);
						newNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().set(newNode.getNode(), PSSIFOption.one(PSSIFValue.create(String.valueOf(oVersion))));
						newNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne().set(newNode.getNode(), PSSIFOption.one(PSSIFValue.create(mNode.getNode().getId()
								+ "_" + oVersion)));
						newNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).getOne().set(newNode.getNode(), PSSIFOption.one(PSSIFValue.create(mNode.getName())));
						mNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().set(mNode.getNode(), PSSIFOption.one(PSSIFValue.create(String.valueOf(newVersion))));
						return true;
						
					}
				}

				
			}
			
		}
		
		
		if (!hasNextVersions(mNode)) {

			System.out.println("Creating new version for " + mNode.getName());

			

			if (oldVersion.isNone()) {
				nodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().set(node, PSSIFOption.one(PSSIFValue.create("0.0")));
				oldVersion = nodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().get(node);
			}

			//System.out.println("Version: " + oldVersion.getOne().asString());

			

			if (!newVersion.equals(oVersion)) {
				PSSIFOption<PSSIFValue> newVersionVal = PSSIFOption.one(PSSIFValue.create(newVersion));
				// nodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().set(node,
				// newVersionVal);

				ModelBuilder.addNewNodeFromGUI(mNode.getNode().getId() + "_", requirementNodeType);

				MyNode newVersionNode = null;
				MyNode minVersionNode = null;
				MyNode maxVersionNode = null;
				double minVersion = Integer.MAX_VALUE;
				double maxVersion = Integer.MIN_VALUE;
				
				for (MyNode newNode : ModelBuilder.getAllNodes()) {
					//System.out.println("Node: " + newNode.getName() + " | ID: "	+ newNode.getNode().getId());
					String[] idNVNew = newNode.getNode().getId().toString().split("_");
					if (idNVNew.length > 0 && idNVNew[0].equals(idNV[0])) {
						PSSIFOption<PSSIFValue> version2 = nodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().get(newNode.getNode());
						if (version2.isNone()) {
							newVersionNode = newNode;
						} else {
							double version2Dbl = Double.parseDouble(version2.getOne().asString());
							if (version2Dbl < minVersion) {
								minVersion = version2Dbl;
								minVersionNode = newNode;
							}
							if (version2Dbl > maxVersion) {
								maxVersion = version2Dbl;
								maxVersionNode = newNode;
							}
						}
					}

					
				}

				MyEdge edge = null;
				MyNode sourceNode = null;
				for (MyEdge myEdge : ModelBuilder.getAllEdges()) {
					if (myEdge.getDestinationNode().equals(mNode)
							&& myEdge.getEdgeType().equals(evolvesTo)) {
						edge = myEdge;
						sourceNode = (MyNode) myEdge.getSourceNode();
					}
				}
				ModelBuilder.getAllEdges().remove(edge);
				ModelBuilder.addNewEdgeGUI(sourceNode, newVersionNode, evolvesTo, true);
				ModelBuilder.addNewEdgeGUI(newVersionNode, mNode, evolvesTo, true);

				newVersionNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().set(newVersionNode.getNode(), PSSIFOption.one(PSSIFValue.create(String.valueOf(maxVersion))));
				newVersionNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne().set(newVersionNode.getNode(), PSSIFOption.one(PSSIFValue.create(mNode.getNode().getId()
						+ "_" + maxVersion)));
				newVersionNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).getOne().set(newVersionNode.getNode(), PSSIFOption.one(PSSIFValue.create(mNode.getName())));
				mNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().set(mNode.getNode(), PSSIFOption.one(PSSIFValue.create(String.valueOf(newVersion))));

				// HashMap<String, Attribute> attrMaxNode =
				// maxVersionNode.getAttributesHashMap();

				// nodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne().set(maxVersionNode.getNode(),
				// PSSIFOption.one(PSSIFValue.create(mNode.getNode().getId()+"_"+newVersion)));

				// ModelBuilder.addNewEdgeGUI(newVersionNode, minVersionNode,
				// evolvesTo, true);

				// nodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().set(node,
				// PSSIFOption.one(PSSIFValue.create("1.0")));
				return true;
			}

			else
				System.out.println("Cannot create new requirement with same version!");

			return false;

			/*
			 * TODO
			 * 
			 * Create new node with same attributes, except version. Take new
			 * edges from old node for the new one, and connect the old one
			 * through directed "Evolves-To" to new node.
			 */
		} else {
			createNewVersion(gViz, getMaxVersion(mNode).getNode(), newVersion);
			return true;
		}

	}

	public static boolean hasPreviousVersions(MyNode myNode) {
		for (MyEdge e : ModelBuilder.getAllEdges()) {
			if (e.getEdgeType().getName().equals(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_EVOLVES_TO)
					&& e.getDestinationNode().getNode().equals(myNode.getNode())) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasNextVersions(MyNode myNode) {
		for (MyEdge e : ModelBuilder.getAllEdges()) {
			if (e.getEdgeType().getName().equals(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_EVOLVES_TO)
					&& e.getSourceNode().getNode().equals(myNode.getNode())) {
				return true;
			}
		}
		return false;
	}

	public static MyNode getMaxVersion(MyNode myNode) {

		MyNodeType requirementNodeType = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_REQUIREMENT);
		NodeType nodeType = ModelBuilder.getMetamodel().getNodeType(requirementNodeType.getName()).getOne();
		MyNode maxVersionNode = null;
		double maxVersion = Integer.MIN_VALUE;
		String[] idNV = myNode.getNode().getId().toString().split("_");
		for (MyNode newNode : ModelBuilder.getAllNodes()) {
			String[] idNVNew = newNode.getNode().getId().toString().split("_");
			if (idNVNew.length > 0 && idNVNew[0].equals(idNV[0])) {
				PSSIFOption<PSSIFValue> version2 = nodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().get(newNode.getNode());
				if (!version2.isNone()) {

					double version2Dbl = Double.parseDouble(version2.getOne().asString());
					if (version2Dbl > maxVersion) {
						maxVersion = version2Dbl;
						maxVersionNode = newNode;
					}
				}
			}

		}
		return maxVersionNode;

	}

	public static void hideVersions(GraphVisualization gViz, MyNode myNode) {
		MyNode parentNode = getMaxVersion(myNode);
		String[] idNV = myNode.getNode().getId().toString().split("_");
		for (MyNode mN : ModelBuilder.getAllNodes()) {
			String[] idNVNew = mN.getNode().getId().toString().split("_");
			if (idNVNew.length > 0 && idNVNew[0].equals(idNV[0])
					&& !mN.equals(parentNode)) {
				mN.setVisible(false);
			}

		}

	}

	public static void showVersions(GraphVisualization gViz, MyNode myNode) {
		MyNode parentNode = getMaxVersion(myNode);
		String[] idNV = myNode.getNode().getId().toString().split("_");
		for (MyNode mN : ModelBuilder.getAllNodes()) {
			String[] idNVNew = mN.getNode().getId().toString().split("_");
			if (idNVNew.length > 0 && idNVNew[0].equals(idNV[0])
					&& !mN.equals(parentNode)) {
				mN.setVisible(true);
			}

		}

	}

	public static MyNode getMinVersion(MyNode myNode) {
		MyNodeType requirementNodeType = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_REQUIREMENT);
		NodeType nodeType = ModelBuilder.getMetamodel().getNodeType(requirementNodeType.getName()).getOne();
		
		MyNode minVersionNode = null;
		double minVersion = Integer.MAX_VALUE;
		String[] idNV = myNode.getNode().getId().toString().split("_");
		for (MyNode newNode : ModelBuilder.getAllNodes()) {
			//System.out.println("Node: " + newNode.getName() + " | ID: "	+ newNode.getNode().getId());
			String[] idNVNew = newNode.getNode().getId().toString().split("_");
			if (idNVNew.length > 0 && idNVNew[0].equals(idNV[0])) {
				PSSIFOption<PSSIFValue> version2 = nodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().get(newNode.getNode());
					double version2Dbl = Double.parseDouble(version2.getOne().asString());
					if (version2Dbl < minVersion) {
						minVersion = version2Dbl;
						minVersionNode = newNode;
					}
				}
			}

			
		return minVersionNode;
	}

}
