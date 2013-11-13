package de.tum.pssif.core.metamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.tum.pssif.core.metamodel.impl.MetamodelImpl;

public class MetamodelTest {

	private Metamodel metamodel;

	@Before
	public void init() {
		metamodel = new MetamodelImpl();
	}

	@Test
	public void myTest() {
		NodeType a = metamodel.createNode("A");
		NodeType b = metamodel.createNode("B");
		NodeType c = metamodel.createNode("C");

		assertTrue(metamodel.getElementTypes().contains(a));
		assertTrue(metamodel.getNodeTypes().contains(a));
		assertFalse(metamodel.getEdgeTypes().contains(a));

		assertTrue(metamodel.getElementTypes().contains(b));
		assertTrue(metamodel.getNodeTypes().contains(b));
		assertFalse(metamodel.getEdgeTypes().contains(b));

		assertTrue(metamodel.getElementTypes().contains(c));
		assertTrue(metamodel.getNodeTypes().contains(c));
		assertFalse(metamodel.getEdgeTypes().contains(c));

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
		assertEquals(a, b.getGeneralization());
		assertEquals(b, a.getSpecializations().iterator().next());

		assertEquals(0, a.getIncomming());
		assertEquals(0, a.getOutgoing());
		// TODO this should fail when id and other properties are added by
		// definition!
		assertEquals(0, a.getAttributes());
	}

	@Test
	public void testLinearConnect() {
		NodeType a = metamodel.createNode("a");
		NodeType b = metamodel.createNode("b");
		EdgeType edge = metamodel.createEdge("edge");
		edge.allow(a, b);

		assertEquals(edge, a.getOutgoing().iterator().next());
		assertEquals(0, b.getOutgoing().size());

		assertEquals(edge, b.getIncomming().iterator().next());
		assertEquals(0, a.getIncomming().size());

		assertEquals(a, edge.getIncoming(b));
		assertEquals(b, edge.getOutgoing(b));
		assertEquals(0, edge.getAuxiliaries(a, b).size());
	}

	@Test
	public void testLinearConnectWithAuxiliaries() {
		NodeType a = metamodel.createNode("a");
		NodeType b = metamodel.createNode("b");
		NodeType c = metamodel.createNode("c");

		EdgeType edge = metamodel.createEdge("edge");
		edge.allow(a, b);
		edge.allowAuxiliaryFor(a, b, c);

		assertEquals(c, edge.getAuxiliaries(a, b).iterator().next());
	}

	@Test(expected = IllegalStateException.class)
	public void testLinearConnectWithAuxRequiresLinearConnect() {
		NodeType a = metamodel.createNode("a");
		NodeType b = metamodel.createNode("b");
		NodeType c = metamodel.createNode("c");

		EdgeType edge = metamodel.createEdge("edge");
		edge.allowAuxiliaryFor(a, b, c);
	}

	@Test
	public void testManyToOneEdge() {
		NodeType a = metamodel.createNode("a");
		NodeType b = metamodel.createNode("b");
		NodeType c = metamodel.createNode("c");

		EdgeType edge = metamodel.createEdge("edge");
		edge.allow(a, b);
		edge.allow(c, b);

		assertTrue(edge.getIncoming(b).contains(a));
		assertTrue(edge.getIncoming(b).contains(c));

		assertEquals(b, edge.getOutgoing(a).iterator().next());
		assertEquals(b, edge.getOutgoing(c).iterator().next());
	}

	@Test
	public void testOneToManyEdge() {
		NodeType a = metamodel.createNode("a");
		NodeType b = metamodel.createNode("b");
		NodeType c = metamodel.createNode("c");

		EdgeType edge = metamodel.createEdge("edge");
		edge.allow(a, b);
		edge.allow(a, c);

		assertEquals(a, edge.getIncoming(b).iterator().next());
		assertEquals(a, edge.getIncoming(c).iterator().next());

		assertTrue(edge.getOutgoing(a).contains(b));
		assertTrue(edge.getOutgoing(a).contains(c));
	}

	public void testOneToManyEdgeWithAuxiliaries() {
		NodeType a = metamodel.createNode("a");
		NodeType b = metamodel.createNode("b");
		NodeType c = metamodel.createNode("c");
		NodeType d = metamodel.createNode("d");

		EdgeType edge = metamodel.createEdge("edge");
		edge.allow(a, b);
		edge.allow(a, c);
		edge.allowAuxiliaryFor(a, b, d);

		assertEquals(a, edge.getIncoming(b).iterator().next());
		assertEquals(a, edge.getIncoming(c).iterator().next());

		assertTrue(edge.getOutgoing(a).contains(b));
		assertTrue(edge.getOutgoing(a).contains(c));

		assertTrue(edge.getAuxiliaries(a, b).contains(d));
		assertFalse(edge.getAuxiliaries(a, c).contains(d));
	}

	public void testManyToOneEdgeWithAuxiliaries() {
		NodeType a = metamodel.createNode("a");
		NodeType b = metamodel.createNode("b");
		NodeType c = metamodel.createNode("c");
		NodeType d = metamodel.createNode("d");

		EdgeType edge = metamodel.createEdge("edge");

		edge.allow(a, b);
		edge.allow(c, b);
		edge.allowAuxiliaryFor(a, b, d);

		assertTrue(edge.getIncoming(b).contains(a));
		assertTrue(edge.getIncoming(b).contains(c));

		assertEquals(b, edge.getOutgoing(a).iterator().next());
		assertEquals(b, edge.getOutgoing(c).iterator().next());

		assertTrue(edge.getAuxiliaries(a, b).contains(d));
		assertFalse(edge.getAuxiliaries(c, b).contains(d));
	}

	// TODO inehritance, effects on constraints and auxiliaries

}
