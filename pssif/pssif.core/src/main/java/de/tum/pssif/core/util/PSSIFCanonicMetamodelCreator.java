package de.tum.pssif.core.util;

import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.metamodel.impl.MetamodelImpl;


public final class PSSIFCanonicMetamodelCreator {

  public static final String N_DEV_ARTIFACT         = "Development Artifact";

  public static final String N_REQUIREMENT          = "Requirement";
  public static final String N_USE_CASE             = "Use Case";
  public static final String N_TEST_CASE            = "Test Case";
  public static final String N_VIEW                 = "View";
  public static final String N_EVENT                = "Event";
  public static final String N_ISSUE                = "Issue";
  public static final String N_DECISION             = "Decision";
  public static final String N_CHANGE_EVENT         = "Change Event";

  public static final String N_SOL_ARTIFACT         = "Solution Artifact";

  public static final String N_BLOCK                = "Block";
  public static final String N_FUNCTION             = "Function";
  public static final String N_ACTIVITY             = "Activity";
  public static final String N_STATE                = "State";
  public static final String N_ACTOR                = "Actor";
  public static final String N_SERVICE              = "Service";
  public static final String N_SOFTWARE             = "Software";
  public static final String N_HARDWARE             = "Hardware";

  public static final String A_REQUIREMENT_PRIORITY = "priority";
  public static final String A_REQUIREMENT_TYPE     = "type";
  public static final String A_BLOCK_COST           = "cost";
  public static final String A_HARDWARE_WEIGHT      = "weight";

  public static final String E_FLOW                 = "Flow";
  public static final String E_ENERGY_FLOW          = "Energy Flow";
  public static final String E_MATERIAL_FLOW        = "Material Flow";
  public static final String E_INFORMATION_FLOW     = "Information Flow";
  public static final String E_CONTROL_FLOW         = "Control Flow";
  public static final String E_VALUE_FLOW           = "Value Flow";

  private PSSIFCanonicMetamodelCreator() {
    //Nop
  }

  public static Metamodel create() {
    MetamodelImpl metamodel = new MetamodelImpl();

    createDevArtifacts(metamodel);
    createSolArtifacts(metamodel);
    createRelationships(metamodel);
    createFlows(metamodel);

    return metamodel;
  }

  private static void createDevArtifacts(MetamodelImpl metamodel) {
    NodeType devArtifact = metamodel.createNodeType(N_DEV_ARTIFACT);

    NodeType requirement = metamodel.createNodeType(N_REQUIREMENT);
    requirement.inherit(devArtifact);
    requirement.createAttribute(requirement.getDefaultAttributeGroup(), A_REQUIREMENT_PRIORITY, PrimitiveDataType.STRING, true,
        AttributeCategory.METADATA);
    requirement.createAttribute(requirement.getDefaultAttributeGroup(), A_REQUIREMENT_TYPE, PrimitiveDataType.STRING, true,
        AttributeCategory.METADATA);

    NodeType useCase = metamodel.createNodeType(N_USE_CASE);
    useCase.inherit(devArtifact);

    NodeType testCase = metamodel.createNodeType(N_TEST_CASE);
    testCase.inherit(devArtifact);

    NodeType view = metamodel.createNodeType(N_VIEW);
    view.inherit(devArtifact);

    NodeType event = metamodel.createNodeType(N_EVENT);
    event.inherit(devArtifact);

    NodeType issue = metamodel.createNodeType(N_ISSUE);
    issue.inherit(event);

    NodeType decision = metamodel.createNodeType(N_DECISION);
    decision.inherit(event);

    NodeType changeEvent = metamodel.createNodeType(N_CHANGE_EVENT);
    changeEvent.inherit(event);
  }

  private static void createSolArtifacts(MetamodelImpl metamodel) {
    NodeType solutionArtifact = metamodel.createNodeType(N_SOL_ARTIFACT);

    NodeType function = metamodel.createNodeType(N_FUNCTION);
    function.inherit(solutionArtifact);

    NodeType activity = metamodel.createNodeType(N_ACTIVITY);
    activity.inherit(function);
    //TODO attribute 'duration': question for next meeting

    NodeType state = metamodel.createNodeType(N_STATE);
    state.inherit(function);

    NodeType block = metamodel.createNodeType(N_BLOCK);
    block.inherit(solutionArtifact);
    block.createAttribute(block.getDefaultAttributeGroup(), A_BLOCK_COST, PrimitiveDataType.DECIMAL, true, AttributeCategory.MONETARY);

    NodeType actor = metamodel.createNodeType(N_ACTOR);
    actor.inherit(block);

    NodeType service = metamodel.createNodeType(N_SERVICE);
    service.inherit(block);

    NodeType software = metamodel.createNodeType(N_SOFTWARE);
    software.inherit(block);

    NodeType hardware = metamodel.createNodeType(N_HARDWARE);
    hardware.inherit(block);
    hardware.createAttribute(hardware.getDefaultAttributeGroup(), A_HARDWARE_WEIGHT, PrimitiveDataType.DECIMAL, true, AttributeCategory.WEIGHT);
  }

  private static void createRelationships(MetamodelImpl metamodel) {
    //TODO clarify
  }

  private static void createFlows(MetamodelImpl metamodel) {
    EdgeType flow = metamodel.createEdgeType(E_FLOW);
    flow.createMapping("from", node(N_SOL_ARTIFACT, metamodel), MultiplicityContainer.of(1, 1, 0, UnlimitedNatural.UNLIMITED), "to",
        node(N_SOL_ARTIFACT, metamodel), MultiplicityContainer.of(1, 1, 0, UnlimitedNatural.UNLIMITED));
    //TODO what about the data object?

    EdgeType energyFlow = metamodel.createEdgeType(E_ENERGY_FLOW);
    energyFlow.inherit(flow);

    EdgeType materialFlow = metamodel.createEdgeType(E_MATERIAL_FLOW);
    materialFlow.inherit(flow);

    EdgeType informationFlow = metamodel.createEdgeType(E_INFORMATION_FLOW);
    informationFlow.inherit(flow);

    EdgeType controlFlow = metamodel.createEdgeType(E_CONTROL_FLOW);
    controlFlow.inherit(flow);

    EdgeType valueFlow = metamodel.createEdgeType(E_VALUE_FLOW);
    valueFlow.inherit(flow);
  }

  private static NodeType node(String name, Metamodel metamodel) {
    return metamodel.findNodeType(name);
  }

}
