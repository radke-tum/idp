package de.tum.pssif.core.metamodel;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.core.metamodel.impl.MetamodelImpl;

public class MetamodelTest {
	private Metamodel metamodel = new MetamodelImpl();

	@Test
	public void test() {
		NodeType is = metamodel.create("is");
		NodeType comp = metamodel.create("component");
		NodeType data = metamodel.create("dataobject");
		metamodel.create("infoflow", "infoflows", is,
				MultiplicityContainer.of(1, 1), "infoflows", is,
				MultiplicityContainer.of(1, 1));
		EdgeType is2comp = metamodel.create("infoflow", "infoflows", is,
				MultiplicityContainer.of(1, 1), "infoflows", comp,
				MultiplicityContainer.of(1, 1));
		is2comp.createAuxiliaryEnd("dataobjects",
				MultiplicityContainer.of(0, UnlimitedNatural.UNLIMITED), data);

		EdgeType infoflow = metamodel.findEdgeType("infoflow");
		EdgeEnd incoming = infoflow.getIncoming();
		EdgeEnd outgoing = infoflow.getOutgoing();
		Collection<EdgeEnd> aux = infoflow.getAuxiliaries();

		Assert.assertEquals(1, incoming.getTypes().size());
		Assert.assertTrue(incoming.getTypes().contains(is));
		Assert.assertEquals(2, outgoing.getTypes().size());
		Assert.assertTrue(outgoing.getTypes().contains(is));
		Assert.assertTrue(outgoing.getTypes().contains(comp));
		Assert.assertEquals(1, aux.iterator().next().getTypes().size());
		Assert.assertTrue(aux.iterator().next().getTypes().contains(data));
	}

	@Test
	public void generalizationTest() {
		NodeType node = metamodel.create("node");
		NodeType development = metamodel.create("development");
		NodeType solution = metamodel.create("solution");

		development.inherit(node);
		solution.inherit(node);

		Assert.assertEquals(null, node.getGeneral());
		Collection<NodeType> specials = node.getSpecials();
		Assert.assertEquals(2, specials.size());
		Assert.assertTrue(node.getSpecials().contains(development));
		Assert.assertTrue(node.getSpecials().contains(solution));

		Assert.assertEquals(node, development.getGeneral());
		Assert.assertEquals(node, solution.getGeneral());
	}
}
