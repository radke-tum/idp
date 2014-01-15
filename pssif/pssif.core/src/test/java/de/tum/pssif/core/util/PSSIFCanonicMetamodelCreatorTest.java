package de.tum.pssif.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;


public class PSSIFCanonicMetamodelCreatorTest {

  @Test
  public void testCanonicMetamodelCreator() {
    Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
    NodeType node = metamodel.findNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    assertNotNull(node);
    testHasDefaultAttributes(node);

    EdgeType edge = metamodel.findEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME);
    assertNotNull(edge);
    testHasDefaultAttributes(edge);
  }

  @Test
  public void testSolutionArtifacts() {
    Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
    NodeType rootNode = metamodel.findNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    assertNotNull(rootNode.findIncomingEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME));
    assertNotNull(rootNode.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(rootNode.findOutgoingEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME));
    assertNotNull(rootNode.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));

    NodeType solArtifact = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_SOL_ARTIFACT);
    assertNotNull(solArtifact);
    assertEquals(rootNode, solArtifact.getGeneral());
    assertTrue(rootNode.getSpecials().contains(solArtifact));
    testHasDefaultAttributes(solArtifact);
    assertNotNull(solArtifact.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW));
    assertNotNull(solArtifact.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(solArtifact.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW));
    assertNotNull(solArtifact.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));

    NodeType block = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_BLOCK);
    assertNotNull(block);
    assertEquals(block.getGeneral(), solArtifact);
    assertTrue(solArtifact.getSpecials().contains(block));
    testHasDefaultAttributes(block);
    assertNotNull(block.findAttribute(PSSIFCanonicMetamodelCreator.A_BLOCK_COST));
    assertNotNull(block.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(block.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(block.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(block.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(block.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(block.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(block.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));
    assertNotNull(block.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));

    NodeType function = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_FUNCTION);
    assertNotNull(function);
    assertEquals(function.getGeneral(), solArtifact);
    assertTrue(solArtifact.getSpecials().contains(function));
    testHasDefaultAttributes(function);
    assertNotNull(function.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(function.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(function.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_CONTROL));
    assertNotNull(function.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_CONTROL));

    NodeType activity = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_ACTIVITY);
    assertNotNull(activity);
    assertEquals(activity.getGeneral(), function);
    assertTrue(function.getSpecials().contains(activity));
    testHasDefaultAttributes(activity);
    assertNotNull(activity.findAttribute(PSSIFCanonicMetamodelCreator.A_DURATION));
    assertNotNull(activity.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(activity.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(activity.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_CONTROL));
    assertNotNull(activity.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_CONTROL));

    NodeType state = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_STATE);
    assertNotNull(state);
    assertEquals(state.getGeneral(), function);
    assertTrue(function.getSpecials().contains(state));
    testHasDefaultAttributes(state);
    assertNotNull(state.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(state.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(state.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_CONTROL));
    assertNotNull(state.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_CONTROL));

    NodeType actor = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_ACTOR);
    assertNotNull(actor);
    assertEquals(actor.getGeneral(), block);
    assertTrue(block.getSpecials().contains(actor));
    testHasDefaultAttributes(actor);
    assertNotNull(actor.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(actor.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(actor.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(actor.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(actor.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(actor.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(actor.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));
    assertNotNull(actor.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));

    NodeType service = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_SERVICE);
    assertNotNull(service);
    assertEquals(service.getGeneral(), block);
    assertTrue(block.getSpecials().contains(service));
    testHasDefaultAttributes(service);
    assertNotNull(service.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(service.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(service.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(service.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(service.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(service.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(service.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));
    assertNotNull(service.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));

    NodeType software = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_SOFTWARE);
    assertNotNull(software);
    assertEquals(software.getGeneral(), block);
    assertTrue(block.getSpecials().contains(software));
    testHasDefaultAttributes(software);
    assertNotNull(software.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(software.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(software.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(software.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(software.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(software.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(software.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));
    assertNotNull(software.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));

    NodeType hardware = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_HARDWARE);
    assertNotNull(hardware);
    assertEquals(hardware.getGeneral(), block);
    assertTrue(block.getSpecials().contains(hardware));
    testHasDefaultAttributes(hardware);
    assertNotNull(hardware.findAttribute(PSSIFCanonicMetamodelCreator.A_HARDWARE_WEIGHT));
    assertNotNull(hardware.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(hardware.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(hardware.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(hardware.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(hardware.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(hardware.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(hardware.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));
    assertNotNull(hardware.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));

    NodeType mechanic = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_MECHANIC);
    assertNotNull(mechanic);
    assertEquals(mechanic.getGeneral(), hardware);
    assertTrue(hardware.getSpecials().contains(mechanic));
    testHasDefaultAttributes(mechanic);
    assertNotNull(mechanic.findAttribute(PSSIFCanonicMetamodelCreator.A_HARDWARE_WEIGHT));
    assertNotNull(mechanic.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(mechanic.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(mechanic.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(mechanic.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(mechanic.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(mechanic.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(mechanic.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));
    assertNotNull(mechanic.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));

    NodeType electronic = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_ELECTRONIC);
    assertNotNull(electronic);
    assertEquals(electronic.getGeneral(), hardware);
    assertTrue(hardware.getSpecials().contains(electronic));
    testHasDefaultAttributes(electronic);
    assertNotNull(electronic.findAttribute(PSSIFCanonicMetamodelCreator.A_HARDWARE_WEIGHT));
    assertNotNull(electronic.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(electronic.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(electronic.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(electronic.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION));
    assertNotNull(electronic.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(electronic.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY));
    assertNotNull(electronic.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));
    assertNotNull(electronic.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL));
  }

  @Test
  public void testDevelopmentArtifacts() {
    Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
    NodeType rootNode = metamodel.findNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);

    NodeType devArtifact = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_DEV_ARTIFACT);
    assertNotNull(devArtifact);
    assertEquals(rootNode, devArtifact.getGeneral());
    assertTrue(rootNode.getSpecials().contains(devArtifact));
    testHasDefaultAttributes(devArtifact);
    assertNotNull(devArtifact.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(devArtifact.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));

    NodeType requirement = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_REQUIREMENT);
    assertNotNull(requirement);
    assertEquals(devArtifact, requirement.getGeneral());
    assertTrue(devArtifact.getSpecials().contains(requirement));
    testHasDefaultAttributes(requirement);
    assertNotNull(requirement.findAttribute(PSSIFCanonicMetamodelCreator.A_REQUIREMENT_PRIORITY));
    assertNotNull(requirement.findAttribute(PSSIFCanonicMetamodelCreator.A_REQUIREMENT_TYPE));
    assertNotNull(requirement.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(requirement.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));

    NodeType useCase = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_USE_CASE);
    assertNotNull(useCase);
    assertEquals(devArtifact, useCase.getGeneral());
    assertTrue(devArtifact.getSpecials().contains(useCase));
    testHasDefaultAttributes(useCase);
    assertNotNull(useCase.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(useCase.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));

    NodeType testCase = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_TEST_CASE);
    assertNotNull(testCase);
    assertEquals(devArtifact, testCase.getGeneral());
    assertTrue(devArtifact.getSpecials().contains(testCase));
    testHasDefaultAttributes(testCase);
    assertNotNull(testCase.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(testCase.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));

    NodeType view = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_VIEW);
    assertNotNull(view);
    assertEquals(devArtifact, view.getGeneral());
    assertTrue(devArtifact.getSpecials().contains(view));
    testHasDefaultAttributes(view);
    assertNotNull(view.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(view.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));

    NodeType event = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_EVENT);
    assertNotNull(event);
    assertEquals(devArtifact, event.getGeneral());
    assertTrue(devArtifact.getSpecials().contains(event));
    testHasDefaultAttributes(event);
    assertNotNull(event.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(event.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));

    NodeType issue = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_ISSUE);
    assertNotNull(issue);
    assertEquals(event, issue.getGeneral());
    assertTrue(event.getSpecials().contains(issue));
    testHasDefaultAttributes(issue);
    assertNotNull(issue.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(issue.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));

    NodeType decision = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_DECISION);
    assertNotNull(decision);
    assertEquals(event, decision.getGeneral());
    assertTrue(event.getSpecials().contains(decision));
    testHasDefaultAttributes(decision);
    assertNotNull(decision.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(decision.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));

    NodeType changeEvent = metamodel.findNodeType(PSSIFCanonicMetamodelCreator.N_CHANGE_EVENT);
    assertNotNull(changeEvent);
    assertEquals(event, changeEvent.getGeneral());
    assertTrue(event.getSpecials().contains(changeEvent));
    testHasDefaultAttributes(changeEvent);
    assertNotNull(changeEvent.findIncomingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
    assertNotNull(changeEvent.findOutgoingEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP));
  }

  @Test
  public void testFlows() {
    Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
    EdgeType flow = metamodel.findEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW);
    assertNotNull(flow);
    testHasDefaultAttributes(flow);

    EdgeType energyFlow = metamodel.findEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY);
    assertNotNull(energyFlow);
    assertEquals(flow, energyFlow.getGeneral());
    testHasDefaultAttributes(energyFlow);

    EdgeType materialFlow = metamodel.findEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL);
    assertNotNull(materialFlow);
    assertEquals(flow, materialFlow.getGeneral());
    testHasDefaultAttributes(materialFlow);

    EdgeType informationFlow = metamodel.findEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION);
    assertNotNull(informationFlow);
    assertEquals(flow, informationFlow.getGeneral());
    testHasDefaultAttributes(informationFlow);

    EdgeType controlFlow = metamodel.findEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_CONTROL);
    assertNotNull(controlFlow);
    assertEquals(flow, controlFlow.getGeneral());
    testHasDefaultAttributes(controlFlow);

    EdgeType valueFlow = metamodel.findEdgeType(PSSIFCanonicMetamodelCreator.E_FLOW_VALUE);
    assertNotNull(valueFlow);
    assertEquals(flow, valueFlow.getGeneral());
    testHasDefaultAttributes(valueFlow);
    //TODO
  }

  @Test
  public void testRelationships() {
    //TODO
  }

  private void testHasDefaultAttributes(NodeType nodeType) {
    assertNotNull(nodeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID));
    assertNotNull(nodeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME));
    assertNotNull(nodeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT));
    assertNotNull(nodeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION));
    assertNotNull(nodeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START));
    assertNotNull(nodeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END));
  }

  private void testHasDefaultAttributes(EdgeType edgeType) {
    assertNotNull(edgeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID));
    assertNotNull(edgeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME));
    assertNotNull(edgeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT));
    assertNotNull(edgeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION));
    assertNotNull(edgeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START));
    assertNotNull(edgeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END));
    assertNotNull(edgeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED));
  }

}
