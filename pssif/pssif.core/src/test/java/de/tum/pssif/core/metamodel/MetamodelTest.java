package de.tum.pssif.core.metamodel;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;

public class MetamodelTest {
	private Metamodel metamodel;

	@Test
	public void test() {
		NodeType is = metamodel.create("is");
		NodeType comp = metamodel.create("component");
		NodeType data = metamodel.create("dataobject");
		metamodel.create("infoflow", is, MultiplicityContainer.of(1, 1), is,
				MultiplicityContainer.of(1, 1));
		EdgeType is2comp = metamodel.create("infoflow", is,
				MultiplicityContainer.of(1, 1), comp, MultiplicityContainer.of(1, 1));
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
}
