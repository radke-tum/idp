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
    metamodel.create("infoflow", "infoflows", is, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED), "infoflows",
        is, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED));
    EdgeType is2comp = metamodel.create("infoflow", "infoflows", is,
        MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED), "infoflows", comp,
        MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED));
    metamodel
        .createAuxiliaryEnd(is2comp, "dataobjects", MultiplicityContainer.of(0, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED), data);

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

    // TODO check on incomings, outgoings and auxiliaries of the NodeTypes.
    // TODO define behavior of incomings, outgoings and auxiliaries:
    // are all "infoflow" outgoings bundled into an EdgeTypeBundle?
  }

  @Test
  public void generalizationTest() {
    NodeType node = metamodel.create("node");
    NodeType development = metamodel.create("development");
    NodeType solution = metamodel.create("solution");
    NodeType sw = metamodel.create("software");

    development.inherit(node);
    solution.inherit(node);
    sw.inherit(solution);

    Assert.assertNull(node.getGeneral());
    Collection<NodeType> specials = node.getSpecials();
    Assert.assertEquals(2, specials.size());
    Assert.assertTrue(specials.contains(development));
    Assert.assertTrue(specials.contains(solution));

    Assert.assertEquals(node, development.getGeneral());
    Assert.assertEquals(node, solution.getGeneral());

    EdgeType flow = metamodel.create("flow", "flows", solution, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED),
        "flows", solution, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED));
    EdgeType infoflow = metamodel.create("infoflow", "infoflows", sw,
        MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED), "infoflows", sw,
        MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED));

    infoflow.inherit(flow);

    Assert.assertNull(flow.getGeneral());
    Collection<EdgeType> flowSpecials = flow.getSpecials();
    Assert.assertEquals(1, flowSpecials.size());
    Assert.assertTrue(flowSpecials.contains(infoflow));
    Assert.assertEquals(flow, infoflow.getGeneral());
  }
}
