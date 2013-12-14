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
    NodeType is = metamodel.createNodeType("is");
    NodeType comp = metamodel.createNodeType("component");
    NodeType data = metamodel.createNodeType("dataobject");
    EdgeType infoflow = metamodel.createEdgeType("infoflow");
    ConnectionMapping is2is = infoflow.createMapping("infoflows", is,
        MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED), "infoflows", is,
        MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED));
    ConnectionMapping is2comp = infoflow.createMapping("infoflows", is,
        MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED), "infoflows", comp,
        MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED));
    EdgeEnd aux = infoflow.createAuxiliary("dataobjects", MultiplicityContainer.of(0, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED),
        data);

    infoflow = metamodel.findEdgeType("infoflow");
    Assert.assertEquals(is2is, infoflow.getMapping(is, is));
    Assert.assertEquals(is2comp, infoflow.getMapping(is, comp));
    Assert.assertEquals(1, infoflow.getAuxiliaries().size());
    Assert.assertEquals(aux, infoflow.getAuxiliaries().iterator().next());

    // TODO check on incomings, outgoings and auxiliaries of the NodeTypes.
    // TODO define behavior of incomings, outgoings and auxiliaries:
    // are all "infoflow" outgoings bundled into an EdgeTypeBundle?
  }

  @Test
  public void generalizationTest() {
    NodeType node = metamodel.createNodeType("node");
    NodeType development = metamodel.createNodeType("development");
    NodeType solution = metamodel.createNodeType("solution");
    NodeType sw = metamodel.createNodeType("software");

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

    EdgeType flow = metamodel.createEdgeType("flow");
    flow.createMapping("flows", solution, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED), "flows", solution,
        MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED));
    EdgeType infoflow = metamodel.createEdgeType("infoflow");
    infoflow.createMapping("infoflows", sw, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED), "infoflows", sw,
        MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED));

    infoflow.inherit(flow);

    Assert.assertNull(flow.getGeneral());
    Collection<EdgeType> flowSpecials = flow.getSpecials();
    Assert.assertEquals(1, flowSpecials.size());
    Assert.assertTrue(flowSpecials.contains(infoflow));
    Assert.assertEquals(flow, infoflow.getGeneral());
  }
}
