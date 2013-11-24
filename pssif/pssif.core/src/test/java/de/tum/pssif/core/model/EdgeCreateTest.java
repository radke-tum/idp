package de.tum.pssif.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.tum.pssif.core.exception.PSSIFException;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.impl.EdgeTypeBundle;
import de.tum.pssif.core.metamodel.impl.EdgeTypeImpl;
import de.tum.pssif.core.metamodel.impl.MetamodelImpl;
import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.core.util.PSSIFOption;


public class EdgeCreateTest {

  private Metamodel           metamodel;

  private static final String FROM_TYPE      = "FromType";
  private static final String TO_TYPE_1      = "ToType1";
  private static final String TO_TYPE_2      = "ToType2";
  private static final String AUX_TYPE_1     = "AuxType1";
  private static final String AUX_TYPE_2     = "AuxType2";
  private static final String EDGE_TYPE      = "EdgeType";
  private static final String EDGE_END_IN    = "EdgeEndIn";
  private static final String EDGE_END_OUT   = "EdgeEndOut";
  private static final String EDGE_END_AUX_1 = "EdgeEndAux1";
  private static final String EDGE_END_AUX_2 = "EdgeEndAux2";

  @Before
  public void onSetUp() {
    this.metamodel = createMetamodel();
  }

  @Test(expected = PSSIFException.class)
  public void testMismatchMissingFromType() {
    Model model = new ModelImpl();
    Node toType1Node = toType1().create(model);

    EdgeEnd toEdgeEnd = edgeType().getOutgoing();

    Multimap<EdgeEnd, Node> connections = HashMultimap.create();

    connections.put(toEdgeEnd, toType1Node);

    edgeType().create(model, connections);
  }

  @Test(expected = PSSIFException.class)
  public void testMismatchMissingToType() {
    Model model = new ModelImpl();
    Node fromNode1 = fromType().create(model);

    EdgeEnd fromEdgeEnd = edgeType().getIncoming();

    Multimap<EdgeEnd, Node> connections = HashMultimap.create();

    connections.put(fromEdgeEnd, fromNode1);

    edgeType().create(model, connections);
  }

  @Test(expected = PSSIFException.class)
  public void testMismatchAmbiguousToType() {
    Model model = new ModelImpl();
    Node fromNode1 = fromType().create(model);
    Node toType1Node = toType1().create(model);
    Node toType2Node = toType2().create(model);

    EdgeEnd fromEdgeEnd = edgeType().getIncoming();
    EdgeEnd toEdgeEnd = edgeType().getOutgoing();

    Multimap<EdgeEnd, Node> connections = HashMultimap.create();

    connections.put(fromEdgeEnd, fromNode1);
    connections.put(toEdgeEnd, toType1Node);
    connections.put(toEdgeEnd, toType2Node);

    edgeType().create(model, connections);
  }

  @Test
  public void testMatchEdgeType1Config1() {
    //Config: edge type 1, without aux elements
    Model model = new ModelImpl();
    Node fromNode1 = fromType().create(model);
    Node fromNode2 = fromType().create(model);
    Node toType1Node1 = toType1().create(model);
    Node toType1Node2 = toType1().create(model);

    EdgeEnd fromEdgeEnd = edgeType().getIncoming();
    EdgeEnd toEdgeEnd = edgeType().getOutgoing();
    EdgeEnd auxEdgeEnd = edgeType().findEdgeEnd(EDGE_END_AUX_1);

    Multimap<EdgeEnd, Node> connections = HashMultimap.create();

    connections.put(fromEdgeEnd, fromNode1);
    connections.put(fromEdgeEnd, fromNode2);
    connections.put(toEdgeEnd, toType1Node1);
    connections.put(toEdgeEnd, toType1Node2);

    Edge edge = edgeType().create(model, connections);

    assertNotNull(edge);
    PSSIFOption<Node> fromNodes = edge.get(fromEdgeEnd);
    assertEquals(2, fromNodes.size());
    assertTrue(fromNodes.getMany().contains(fromNode1));
    assertTrue(fromNodes.getMany().contains(fromNode2));

    PSSIFOption<Node> toNodes = edge.get(toEdgeEnd);
    assertEquals(2, toNodes.size());
    assertTrue(toNodes.getMany().contains(toType1Node1));
    assertTrue(toNodes.getMany().contains(toType1Node2));

    assertTrue(edge.get(auxEdgeEnd).isNone());
  }

  @Test
  public void testMatchEdgeType1Config2() {
    //Config: edge type 1, with aux elements
    Model model = new ModelImpl();
    Node fromNode1 = fromType().create(model);
    Node fromNode2 = fromType().create(model);
    Node toType1Node1 = toType1().create(model);
    Node toType1Node2 = toType1().create(model);
    Node auxNode1 = auxType1().create(model);
    Node auxNode2 = auxType1().create(model);

    EdgeEnd fromEdgeEnd = edgeType().getIncoming();
    EdgeEnd toEdgeEnd = edgeType().getOutgoing();
    EdgeEnd auxEdgeEnd = edgeType().findEdgeEnd(EDGE_END_AUX_1);

    Multimap<EdgeEnd, Node> connections = HashMultimap.create();

    connections.put(fromEdgeEnd, fromNode1);
    connections.put(fromEdgeEnd, fromNode2);
    connections.put(toEdgeEnd, toType1Node1);
    connections.put(toEdgeEnd, toType1Node2);
    connections.put(auxEdgeEnd, auxNode1);
    connections.put(auxEdgeEnd, auxNode2);

    Edge edge = edgeType().create(model, connections);

    assertNotNull(edge);
    PSSIFOption<Node> fromNodes = edge.get(fromEdgeEnd);
    assertEquals(2, fromNodes.size());
    assertTrue(fromNodes.getMany().contains(fromNode1));
    assertTrue(fromNodes.getMany().contains(fromNode2));

    PSSIFOption<Node> toNodes = edge.get(toEdgeEnd);
    assertEquals(2, toNodes.size());
    assertTrue(toNodes.getMany().contains(toType1Node1));
    assertTrue(toNodes.getMany().contains(toType1Node2));

    PSSIFOption<Node> auxNodes = edge.get(auxEdgeEnd);
    assertEquals(2, auxNodes.size());
    assertTrue(auxNodes.getMany().contains(auxNode1));
    assertTrue(auxNodes.getMany().contains(auxNode2));
  }

  @Test(expected = PSSIFException.class)
  public void testMismatchEdgeType1ToType() {
    //mismatch by having 4 toNodes
    Model model = new ModelImpl();
    Node fromNode1 = fromType().create(model);
    Node toType1Node1 = toType1().create(model);
    Node toType1Node2 = toType1().create(model);
    Node toType1Node3 = toType1().create(model);
    Node toType1Node4 = toType1().create(model);
    Node auxNode1 = auxType1().create(model);
    Node auxNode2 = auxType1().create(model);

    EdgeEnd fromEdgeEnd = edgeType().getIncoming();
    EdgeEnd toEdgeEnd = edgeType().getOutgoing();
    EdgeEnd auxEdgeEnd = edgeType().findEdgeEnd(EDGE_END_AUX_1);

    Multimap<EdgeEnd, Node> connections = HashMultimap.create();

    connections.put(fromEdgeEnd, fromNode1);
    connections.put(toEdgeEnd, toType1Node1);
    connections.put(toEdgeEnd, toType1Node2);
    connections.put(toEdgeEnd, toType1Node3);
    connections.put(toEdgeEnd, toType1Node4);
    connections.put(auxEdgeEnd, auxNode1);
    connections.put(auxEdgeEnd, auxNode2);

    edgeType().create(model, connections);
  }

  @Test(expected = PSSIFException.class)
  public void testMismatchEdgeType1AuxType1() {
    //mismatch by having 3 node instances connected over auxType1
    Model model = new ModelImpl();
    Node fromNode1 = fromType().create(model);
    Node toType1Node1 = toType1().create(model);
    Node auxNode1 = auxType1().create(model);
    Node auxNode2 = auxType1().create(model);
    Node auxNode3 = auxType1().create(model);

    EdgeEnd fromEdgeEnd = edgeType().getIncoming();
    EdgeEnd toEdgeEnd = edgeType().getOutgoing();
    EdgeEnd auxEdgeEnd = edgeType().findEdgeEnd(EDGE_END_AUX_1);

    Multimap<EdgeEnd, Node> connections = HashMultimap.create();

    connections.put(fromEdgeEnd, fromNode1);
    connections.put(toEdgeEnd, toType1Node1);
    connections.put(auxEdgeEnd, auxNode1);
    connections.put(auxEdgeEnd, auxNode2);
    connections.put(auxEdgeEnd, auxNode3);

    edgeType().create(model, connections);
  }

  @Test
  public void testMatchEdgeType2Config1() {
    //in: 1 nodes
    //out: 2 nodes
    //aux1: 1 node
    //aux2: 1 node
    Model model = new ModelImpl();
    Node fromNode1 = fromType().create(model);
    Node toType2Node1 = toType2().create(model);
    Node toType2Node2 = toType2().create(model);
    Node auxType1Node1 = auxType1().create(model);
    Node auxType2Node1 = auxType2().create(model);

    EdgeEnd fromEdgeEnd = edgeType().getIncoming();
    EdgeEnd toEdgeEnd = edgeType().getOutgoing();
    EdgeEnd aux1EdgeEnd = edgeType().findEdgeEnd(EDGE_END_AUX_1);
    EdgeEnd aux2EdgeEnd = edgeType().findEdgeEnd(EDGE_END_AUX_2);

    //Multimap<Pair<EdgeEnd, NodeType>, Node> 
    Multimap<EdgeEnd, Node> connections = HashMultimap.create();

    connections.put(fromEdgeEnd, fromNode1);
    connections.put(toEdgeEnd, toType2Node1);
    connections.put(toEdgeEnd, toType2Node2);
    connections.put(aux1EdgeEnd, auxType1Node1);
    connections.put(aux2EdgeEnd, auxType2Node1);

    Edge edge = edgeType().create(model, connections);

    PSSIFOption<Node> froms = edge.get(fromEdgeEnd);
    assertTrue(froms.isOne());
    assertTrue(froms.getOne().equals(fromNode1));

    PSSIFOption<Node> tos = edge.get(toEdgeEnd);
    assertEquals(2, tos.size());
    assertTrue(tos.getMany().contains(toType2Node1));
    assertTrue(tos.getMany().contains(toType2Node2));

    PSSIFOption<Node> aux1s = edge.get(aux1EdgeEnd);
    assertTrue(aux1s.isOne());
    assertTrue(aux1s.getOne().equals(auxType1Node1));

    PSSIFOption<Node> aux2s = edge.get(aux2EdgeEnd);
    assertTrue(aux2s.isOne());
    assertTrue(aux2s.getOne().equals(auxType2Node1));
  }

  @Test
  public void testMatchEdgeType2Config2() {
    //in: 4 nodes
    //out: 6 nodes
    //aux1: 3 node
    //aux2: 2 node

  }

  @Test
  public void testMismatchEdgeType2FromType() {
    //TODO
  }

  @Test
  public void testMismatchEdgeType2ToType() {
    //TODO present but wrong multiplicity?
  }

  @Test
  public void testMismatchEdgeType2AuxType1() {
    //TODO
  }

  @Test
  public void testMismatchEdgeType2AuxType2() {
    //TODO
  }

  @Test
  public void testMetamodel() {
    assertEquals(5, metamodel.getNodeTypes().size());
    assertEquals(2, metamodel.getEdgeTypes().size());
    Collection<EdgeType> edges = metamodel.getEdgeTypes();
    for (EdgeType edgeType : edges) {
      assertEquals(EDGE_TYPE, edgeType.getName());
      assertTrue(edgeType instanceof EdgeTypeImpl);
    }

    assertNotNull(metamodel.findNodeType(FROM_TYPE));
    assertNotNull(metamodel.findNodeType(TO_TYPE_1));
    assertNotNull(metamodel.findNodeType(TO_TYPE_2));
    assertNotNull(metamodel.findNodeType(AUX_TYPE_1));
    assertNotNull(metamodel.findNodeType(AUX_TYPE_2));

    assertNotNull(metamodel.findEdgeType(EDGE_TYPE));

    assertTrue(metamodel.findEdgeType(EDGE_TYPE) instanceof EdgeTypeBundle);
    EdgeType bundledEdgeType = metamodel.findEdgeType(EDGE_TYPE);
    NodeType fromType = metamodel.findNodeType(FROM_TYPE);
    NodeType toType1 = metamodel.findNodeType(TO_TYPE_1);
    NodeType toType2 = metamodel.findNodeType(TO_TYPE_2);
    NodeType auxType1 = metamodel.findNodeType(AUX_TYPE_1);
    NodeType auxType2 = metamodel.findNodeType(AUX_TYPE_2);

    assertNotNull(bundledEdgeType);
    assertNotNull(fromType);
    assertNotNull(toType1);
    assertNotNull(toType2);
    assertNotNull(auxType1);
    assertNotNull(auxType2);

    assertTrue(bundledEdgeType.getIncoming().getTypes().contains(fromType));
    assertTrue(bundledEdgeType.getOutgoing().getTypes().contains(toType1));
    assertTrue(bundledEdgeType.getOutgoing().getTypes().contains(toType2));

    boolean auxTypeFound = false;
    for (EdgeEnd edgeEnd : bundledEdgeType.getAuxiliaries()) {
      if (edgeEnd.getTypes().contains(auxType1)) {
        auxTypeFound = true;
      }
    }
    assertTrue(auxTypeFound);

    auxTypeFound = false;
    for (EdgeEnd edgeEnd : bundledEdgeType.getAuxiliaries()) {
      if (edgeEnd.getTypes().contains(auxType2)) {
        auxTypeFound = true;
      }
    }
    assertTrue(auxTypeFound);
  }

  private NodeType fromType() {
    return metamodel.findNodeType(FROM_TYPE);
  }

  private NodeType toType1() {
    return metamodel.findNodeType(TO_TYPE_1);
  }

  private NodeType toType2() {
    return metamodel.findNodeType(TO_TYPE_2);
  }

  private NodeType auxType1() {
    return metamodel.findNodeType(AUX_TYPE_1);
  }

  private NodeType auxType2() {
    return metamodel.findNodeType(AUX_TYPE_2);
  }

  private EdgeType edgeType() {
    return metamodel.findEdgeType(EDGE_TYPE);
  }

  private Metamodel createMetamodel() {
    Metamodel metamodel = new MetamodelImpl();
    NodeType fromType = metamodel.create(FROM_TYPE);
    NodeType toType1 = metamodel.create(TO_TYPE_1);
    NodeType toType2 = metamodel.create(TO_TYPE_2);
    NodeType auxType1 = metamodel.create(AUX_TYPE_1);
    NodeType auxType2 = metamodel.create(AUX_TYPE_2);

    Multiplicity inMult = MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED);
    Multiplicity outMult = MultiplicityContainer.of(1, 3, 0, UnlimitedNatural.UNLIMITED);
    EdgeType edgeType1 = metamodel.create(EDGE_TYPE, EDGE_END_IN, fromType, inMult, EDGE_END_OUT, toType1, outMult);
    Multiplicity auxMult1 = MultiplicityContainer.of(0, 2, 0, UnlimitedNatural.UNLIMITED);
    metamodel.createAuxiliaryEnd(edgeType1, EDGE_END_AUX_1, auxMult1, auxType1);

    inMult = MultiplicityContainer.of(1, 4, 0, UnlimitedNatural.UNLIMITED);
    outMult = MultiplicityContainer.of(2, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED);
    EdgeType edgeType2 = metamodel.create(EDGE_TYPE, EDGE_END_IN, fromType, inMult, EDGE_END_OUT, toType2, outMult);
    auxMult1 = MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED);
    metamodel.createAuxiliaryEnd(edgeType2, EDGE_END_AUX_1, auxMult1, auxType1);
    Multiplicity auxMult2 = MultiplicityContainer.of(1, 2, 0, UnlimitedNatural.UNLIMITED);
    metamodel.createAuxiliaryEnd(edgeType2, EDGE_END_AUX_2, auxMult2, auxType2);

    return metamodel;
  }
}
