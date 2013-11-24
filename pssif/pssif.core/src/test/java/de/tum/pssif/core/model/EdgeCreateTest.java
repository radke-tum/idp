package de.tum.pssif.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

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

  @Test
  public void testMismatchMissingFromType() {
    //TODO
  }

  @Test
  public void testMismatchMissingToType() {
    //TODO
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
    //TODO
  }

  @Test
  public void testMismatchEdgeType1FromType() {
    //TODO
  }

  @Test
  public void testMismatchEdgeType1ToType() {
    //TODO
  }

  @Test
  public void testMismatchEdgeType1AuxType1() {
    //TODO
  }

  @Test
  public void testMatchEdgeType2Config1() {
    //TODO
  }

  @Test
  public void testMatchEdgeType2Config2() {
    //TODO
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
