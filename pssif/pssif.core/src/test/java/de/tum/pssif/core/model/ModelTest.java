package de.tum.pssif.core.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.impl.MetamodelImpl;
import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.core.util.PSSIFOption;


public class ModelTest {
  private Metamodel metamodel;
  private Model     model;

  @Before
  public void init() {
    metamodel = new MetamodelImpl();
    NodeType node = metamodel.createNodeType("node");
    NodeType development = metamodel.createNodeType("development artifact");
    development.inherit(node);
    NodeType solution = metamodel.createNodeType("solution artifact");
    solution.inherit(development);
    NodeType hardware = metamodel.createNodeType("hardware");
    hardware.inherit(solution);
    NodeType software = metamodel.createNodeType("software");
    software.inherit(solution);
    NodeType usecase = metamodel.createNodeType("usecase");
    usecase.inherit(development);
    NodeType requirement = metamodel.createNodeType("requirement");
    requirement.inherit(development);

    EdgeType containment = metamodel.createEdgeType("containment");
    containment.createMapping("contains", hardware, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED),
        "contained in", hardware, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 1, UnlimitedNatural.of(1)));
    containment.createMapping("contains", hardware, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED),
        "contained in", software, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 1, UnlimitedNatural.of(1)));
    containment.createMapping("contains", software, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED),
        "contained in", software, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 1, UnlimitedNatural.of(1)));

    model = new ModelImpl();
  }

  @Test
  public void test() {
    Node ebike = node("hardware").create(model);
    Node smartphone = node("hardware").create(model);
    Node battery = node("hardware").create(model);
    Node rentalApp = node("software").create(model);

    EdgeType hwContainment = node("hardware").findOutgoingEdgeType("containment");
    ConnectionMapping hw2hw = hwContainment.getMapping(node("hardware"), node("hardware"));
    ConnectionMapping hw2sw = hwContainment.getMapping(node("hardware"), node("software"));

    EdgeEnd from = hw2hw.getFrom();
    EdgeEnd to = hw2hw.getTo();

    Edge containment = hw2hw.create(model);
    containment.connect(from, ebike);
    containment.connect(to, battery);

    containment = hw2hw.create(model);
    containment.connect(from, ebike);
    containment.connect(to, smartphone);

    containment = hw2sw.create(model);
    containment.connect(from, smartphone);
    containment.connect(to, rentalApp);

    PSSIFOption<Edge> edges = hwContainment.getIncoming().apply(ebike);
    Assert.assertEquals(2, edges.size());
    for (Edge edge : edges.getMany()) {
      Assert.assertEquals(ebike, hwContainment.getIncoming().apply(edge).getOne());
    }

    //    Assert.assertEquals(2, ebike.get(hwContainment.getIncoming()).getOne().get(hwContainment.getOutgoing()).size());
  }

  private NodeType node(String name) {
    return metamodel.findNodeType(name);
  }
}
