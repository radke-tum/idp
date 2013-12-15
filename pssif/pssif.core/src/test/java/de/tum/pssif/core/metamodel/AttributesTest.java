package de.tum.pssif.core.metamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.core.metamodel.impl.MetamodelImpl;


public class AttributesTest {

  private Metamodel metamodel;

  @Before
  public void createMetamodel() {
    this.metamodel = new MetamodelImpl();
    NodeType a = metamodel.createNodeType("A");
    NodeType b = metamodel.createNodeType("B");
    Multiplicity inMult = MultiplicityContainer.of(1, 1, 0, UnlimitedNatural.UNLIMITED);
    Multiplicity outMult = MultiplicityContainer.of(1, 1, 0, UnlimitedNatural.UNLIMITED);
    EdgeType edgeType = metamodel.createEdgeType("edge");
    edgeType.createMapping("in", a, inMult, "out", b, outMult);
  }

  @Test
  public void testCreatePrimitiveNodeAttribute() {
    nodeA().createAttribute(nodeA().getDefaultAttributeGroup(), "attr", PrimitiveDataType.INTEGER, Units.INCH, true);
    Attribute attr = nodeA().findAttribute("attr");
    assertNotNull(attr);
    assertEquals(PrimitiveDataType.INTEGER, attr.getType());
    assertEquals(Units.INCH, attr.getUnit());
  }

  @Test
  public void testCreatePrimitiveEdgeAttribute() {
    edge().createAttribute(nodeA().getDefaultAttributeGroup(), "attr", PrimitiveDataType.INTEGER, Units.INCH, true);
    Attribute attr = edge().findAttribute("attr");
    assertNotNull(attr);
    assertEquals(PrimitiveDataType.INTEGER, attr.getType());
    assertEquals(Units.INCH, attr.getUnit());
  }

  @Test
  public void testRemoveNodeAttribute() {
    nodeA().createAttribute(nodeA().getDefaultAttributeGroup(), "attr", PrimitiveDataType.INTEGER, Units.INCH, true);
    Attribute attr = nodeA().findAttribute("attr");
    assertNotNull(attr);
    assertEquals(PrimitiveDataType.INTEGER, attr.getType());
    assertEquals(Units.INCH, attr.getUnit());

    nodeA().getDefaultAttributeGroup().removeAttribute(attr);
    assertNull(nodeA().findAttribute("attr"));
  }

  @Test
  public void testRemoveEdgeAttribute() {
    edge().createAttribute(nodeA().getDefaultAttributeGroup(), "attr", PrimitiveDataType.INTEGER, Units.INCH, true);
    Attribute attr = edge().findAttribute("attr");
    assertNotNull(attr);
    assertEquals(PrimitiveDataType.INTEGER, attr.getType());
    assertEquals(Units.INCH, attr.getUnit());

    edge().getDefaultAttributeGroup().removeAttribute(attr);
    assertNull(edge().findAttribute("attr"));
  }

  @Test
  public void testCreateEnumeration() {
    metamodel.createEnumeration("enum");
    Enumeration enumeration = metamodel.findEnumeration("enum");
    assertNotNull(enumeration);
    assertEquals(0, enumeration.getLiterals().size());
    assertEquals("enum", enumeration.getName());
  }

  @Test
  public void testRemoveEnumeration() {
    metamodel.createEnumeration("enum");
    Enumeration enumeration = metamodel.findEnumeration("enum");
    assertNotNull(enumeration);
    assertEquals(0, enumeration.getLiterals().size());
    assertEquals("enum", enumeration.getName());

    //should accept null unit, since enums have no units.
    nodeA().createAttribute(nodeA().getDefaultAttributeGroup(), "enumAttr", enumeration, true);

    Attribute enumAttr = nodeA().findAttribute("enumAttr");
    assertNotNull(enumAttr);
    assertEquals(enumeration, enumAttr.getType());
    assertEquals("enumAttr", enumAttr.getName());
    assertEquals(Units.NONE, enumAttr.getUnit());

    metamodel.removeEnumeration(enumeration);
    assertNull(metamodel.findEnumeration("enum"));
    assertNull(metamodel.findDataType("enum"));

    assertNull(nodeA().findAttribute("enum"));
  }

  @Test
  public void testCreateLiteral() {
    metamodel.createEnumeration("enum");
    Enumeration enumeration = metamodel.findEnumeration("enum");

    assertNotNull(enumeration);

    EnumerationLiteral literal1 = enumeration.createLiteral("literal1");
    EnumerationLiteral literal2 = enumeration.createLiteral("literal2");

    assertEquals(2, enumeration.getLiterals().size());
    assertNotNull(enumeration.findLiteral("literal1"));
    assertNotNull(enumeration.findLiteral("literal2"));

    assertTrue(enumeration.getLiterals().contains(literal1));
    assertTrue(enumeration.getLiterals().contains(literal2));
  }

  @Test
  public void testRemoveLiteral() {
    metamodel.createEnumeration("enum");
    Enumeration enumeration = metamodel.findEnumeration("enum");

    assertNotNull(enumeration);

    EnumerationLiteral literal1 = enumeration.createLiteral("literal1");
    EnumerationLiteral literal2 = enumeration.createLiteral("literal2");

    assertEquals(2, enumeration.getLiterals().size());
    assertNotNull(enumeration.findLiteral("literal1"));
    assertNotNull(enumeration.findLiteral("literal2"));

    assertTrue(enumeration.getLiterals().contains(literal1));
    assertTrue(enumeration.getLiterals().contains(literal2));

    enumeration.removeLiteral(literal1);

    assertEquals(1, enumeration.getLiterals().size());
    assertNull(enumeration.findLiteral("literal1"));
    assertNotNull(enumeration.findLiteral("literal2"));

    assertFalse(enumeration.getLiterals().contains(literal1));
    assertTrue(enumeration.getLiterals().contains(literal2));
  }

  @Test(expected = PSSIFStructuralIntegrityException.class)
  public void testCreateDuplicateLiteral() {
    metamodel.createEnumeration("enum");
    Enumeration enumeration = metamodel.findEnumeration("enum");

    assertNotNull(enumeration);

    enumeration.createLiteral("literal1");
    enumeration.createLiteral("literal1");
  }

  @Test(expected = PSSIFStructuralIntegrityException.class)
  public void testDuplicateEnumerations() {
    metamodel.createEnumeration("enum");
    Enumeration enumeration = metamodel.findEnumeration("enum");

    assertNotNull(enumeration);

    metamodel.createEnumeration("enum");
  }

  @Test(expected = PSSIFStructuralIntegrityException.class)
  public void testDuplicateAttributes() {
    nodeA().createAttribute(nodeA().getDefaultAttributeGroup(), "attr", PrimitiveDataType.INTEGER, Units.KILOGRAM, true);
    nodeA().createAttribute(nodeA().getDefaultAttributeGroup(), "attr", PrimitiveDataType.DECIMAL, Units.INCH, true);
  }

  @Test(expected = PSSIFStructuralIntegrityException.class)
  public void testCreateAttributeWithIncompatibleType() {
    nodeA().createAttribute(nodeA().getDefaultAttributeGroup(), "attr", PrimitiveDataType.DATE, Units.KILOGRAM, true);
  }

  @Test(expected = PSSIFStructuralIntegrityException.class)
  public void testCreateEnumerationAttributeWithUnit() {
    Enumeration enumeration = metamodel.createEnumeration("enum");
    enumeration.createLiteral("lit");
    nodeA().createAttribute(nodeA().getDefaultAttributeGroup(), "enumAttr", enumeration, Units.CENTIMETER, true);
  }

  public void testStaticDataTypes() {
    assertNotNull(metamodel.findDataType(PrimitiveDataType.BOOLEAN.getName()));
    assertNotNull(metamodel.findPrimitiveType(PrimitiveDataType.BOOLEAN.getName()));

    assertNotNull(metamodel.findDataType(PrimitiveDataType.DATE.getName()));
    assertNotNull(metamodel.findPrimitiveType(PrimitiveDataType.DATE.getName()));

    assertNotNull(metamodel.findDataType(PrimitiveDataType.DECIMAL.getName()));
    assertNotNull(metamodel.findPrimitiveType(PrimitiveDataType.DECIMAL.getName()));

    assertNotNull(metamodel.findDataType(PrimitiveDataType.INTEGER.getName()));
    assertNotNull(metamodel.findPrimitiveType(PrimitiveDataType.INTEGER.getName()));

    assertNotNull(metamodel.findDataType(PrimitiveDataType.STRING.getName()));
    assertNotNull(metamodel.findPrimitiveType(PrimitiveDataType.STRING.getName()));
  }

  public void testUnits() {
    assertNotNull(metamodel.findUnit(Units.CENTIMETER.getName()));
    assertNotNull(metamodel.findUnit(Units.INCH.getName()));
    assertNotNull(metamodel.findUnit(Units.KILOGRAM.getName()));
    //TODO all other units!
  }

  private NodeType nodeA() {
    return metamodel.findNodeType("A");
  }

  private EdgeType edge() {
    return metamodel.findEdgeType("edge");
  }
}
