package de.tum.pssif.core.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.core.metamodel.MutableMetamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.impl.MetamodelImpl;
import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.core.util.PSSIFOption;


public class ModelTest {
  private MutableMetamodel metamodel;
  private Model            model;

  @Before
  public void init() {
    metamodel = new MetamodelImpl();
    NodeType development = metamodel.createNodeType("development artifact");

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

    hw2hw.create(model, ebike, battery);
    hw2hw.create(model, ebike, smartphone);
    hw2sw.create(model, smartphone, rentalApp);

    PSSIFOption<Edge> edges = hwContainment.getIncoming().apply(ebike);
    Assert.assertEquals(2, edges.size());
    for (Edge edge : edges.getMany()) {
      Assert.assertEquals(ebike, hwContainment.getIncoming().apply(edge).getOne());
    }
  }

  @Test(expected = PSSIFStructuralIntegrityException.class)
  public void testConstraintViolation() {
    Node ebike = node("hardware").create(model);
    Node smartphone = node("hardware").create(model);
    Node battery = node("hardware").create(model);
    Node rentalApp = node("software").create(model);
    Node gpsApp = node("software").create(model);

    EdgeType hwContainment = node("hardware").findOutgoingEdgeType("containment");
    ConnectionMapping hw2hw = hwContainment.getMapping(node("hardware"), node("hardware"));
    ConnectionMapping hw2sw = hwContainment.getMapping(node("hardware"), node("software"));

    hw2hw.create(model, ebike, battery);
    hw2hw.create(model, ebike, smartphone);
    Edge smartphoneContainment = hw2sw.create(model, smartphone, rentalApp);
    hw2sw.connectTo(smartphoneContainment, gpsApp);
  }

  //TODO test attributes

  private NodeType node(String name) {
    return metamodel.findNodeType(name);
  }
}
