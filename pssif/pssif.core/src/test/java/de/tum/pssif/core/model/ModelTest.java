package de.tum.pssif.core.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.impl.MetamodelImpl;
import de.tum.pssif.core.model.impl.ModelImpl;


public class ModelTest {
  private Metamodel metamodel;
  private Model     model;

  @Before
  public void init() {
    metamodel = new MetamodelImpl();
    NodeType node = metamodel.create("node");
    NodeType development = metamodel.create("development artifact");
    development.inherit(node);
    NodeType solution = metamodel.create("solution artifact");
    solution.inherit(development);
    NodeType hardware = metamodel.create("hardware");
    hardware.inherit(solution);
    NodeType software = metamodel.create("software");
    software.inherit(solution);
    NodeType usecase = metamodel.create("usecase");
    usecase.inherit(development);
    NodeType requirement = metamodel.create("requirement");
    requirement.inherit(development);

    metamodel.create("containment", "contains", hardware, MultiplicityContainer.of(0, UnlimitedNatural.UNLIMITED), "contained in", hardware,
        MultiplicityContainer.of(0, 1));
    metamodel.create("containment", "contains", hardware, MultiplicityContainer.of(0, UnlimitedNatural.UNLIMITED), "contained in", software,
        MultiplicityContainer.of(0, 1));
    metamodel.create("containment", "contains", software, MultiplicityContainer.of(0, UnlimitedNatural.UNLIMITED), "contained in", software,
        MultiplicityContainer.of(0, 1));

    model = new ModelImpl();
  }

  @Test
  public void test() {
    Node ebike = node("hardware").create(model);
    Node smartphone = node("hardware").create(model);
    Node battery = node("hardware").create(model);
    Node rentalApp = node("software").create(model);

    Multimap<EdgeEnd, Node> connections = HashMultimap.create();
    EdgeType hwContainment = node("hardware").findEdgeType("containment");
    connections.put(hwContainment.getIncoming(), ebike);
    connections.put(hwContainment.getOutgoing(), smartphone);
    connections.put(hwContainment.getOutgoing(), battery);
    hwContainment.create(model, connections);

    connections = HashMultimap.create();
    connections.put(hwContainment.getIncoming(), smartphone);
    connections.put(hwContainment.getOutgoing(), rentalApp);
    hwContainment.create(model, connections);

    Assert.assertEquals(2, ebike.get(hwContainment.getIncoming()).getOne().get(hwContainment.getOutgoing()).size());
  }

  private NodeType node(String name) {
    return metamodel.findNodeType(name);
  }
}
