package de.tum.pssif.core.metamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public class MetamodelTest {

  private Metamodel metamodel;

  private void resetMetamodel() {
    //TODO
  }

  @Test
  public void myTest() {
    NodeType a = metamodel.createNode("A");
    NodeType b = metamodel.createNode("B");
    NodeType c = metamodel.createNode("C");

    assertNotNull(a);
    assertEquals(a, metamodel.findNodeType("A"));
    assertEquals("A", a.getName());

    assertNotNull(b);
    assertEquals("B", b.getName());
    assertEquals(b, metamodel.findNodeType("B"));

    assertNotNull(c);
    assertEquals("C", c.getName());
    assertEquals(c, metamodel.findNodeType("C"));

    b.inherit(a);
    assertEquals(a, b.getParent());
    assertEquals(b, a.getChildren().iterator().next());

    assertEquals(0, a.getIncomming());
    assertEquals(0, a.getOutgoing());
    //TODO this should fail when id and other properties are added by definition!
    assertEquals(0, a.getAttributes());
  }

  @Test
  public void testLinearConnect() {
    resetMetamodel();
    NodeType a = metamodel.createNode("a");
    NodeType b = metamodel.createNode("b");
    EdgeType edge = metamodel.createEdge("edge");
    edge.allow(a, b);
    assertEquals(edge, a.getOutgoing().iterator().next());
    assertEquals(edge, b.getIncomming().iterator().next());
    assertEquals(0, a.getIncomming());
    assertEquals(0, b.getOutgoing());
  }

  @Test
  public void testLinearConnectWithAuxiliaries() {
    //TODO
  }

  @Test
  public void testManyToOneEdge() {
    //TODO
  }

  @Test
  public void testOneToManyEdge() {
    //TODO
  }

  public void testOneToManyEdgeWithAuxiliaries() {
    //TODO
  }

  public void testManyToOneEdgeWithAuxiliaries() {
    //TODO
  }

  public void testEdgeConstraintsInheritanceAllToOne() {
    //TODO what exactly?...
  }

}
