package de.iteratec.visio.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.iteratec.visio.model.GeometryPart;
import de.iteratec.visio.model.GeometryPartField;


public abstract class AVdxGeomPart implements GeometryPart {

  private final String                   name;
  private Map<String, GeometryPartField> fields;

  protected AVdxGeomPart(String name) {
    this.name = name;
    this.fields = Maps.newHashMap();
  }

  @Override
  public String getName() {
    return name;
  }

  public List<GeometryPartField> getFields() {
    ArrayList<GeometryPartField> fieldList = Lists.newArrayList(fields.values());
    Collections.sort(fieldList);
    return fieldList;
  }

  public GeometryPartField getField(String name) {
    return fields.get(name);
  }

  protected void addField(GeometryPartField field) {
    this.fields.put(field.getName(), field);
  }

  /**
   * Writes a {@link GeometryPart} to a DOM Geom element 
   */
  public static class Writer {

    /**
     * Writes this geometry to the given DOM element.
     * @param geomPart the {@link GeometryPart} to write.
     * @param geom the DOM element representing the geometry this part should be set to
     * @param ix the ID of the geom part element to write to (1 based)
     */
    public static void writeToGeomElement(GeometryPart geomPart, Element geom, int ix) {
      Element geomPartElement = Reader.getGeomPartElement(geom, ix);
      if (geomPartElement != null) {
        VisioDOMUtils.removeElementFromParent(geom, geomPartElement);
      }
      geomPartElement = VisioDOMUtils.createChildWithName(geom, geomPart.getName());
      geomPartElement.setAttribute("IX", String.valueOf(ix));

      for (GeometryPartField field : geomPart.getFields()) {
        Element fieldElement = VisioDOMUtils.createChildWithName(geomPartElement, field.getName());
        if (!field.isNoFormula()) {
          fieldElement.setAttribute("F", field.getFormula());
        }
        if (field.getUnit() != null && !field.getUnit().isEmpty()) {
          fieldElement.setAttribute("Unit", field.getUnit());
        }
        fieldElement.setTextContent(field.getValue());
      }
    }
  }

  /**
   * Reads a {@link GeometryPart} from a DOM geom element
   */
  public static class Reader {

    /**
     * Reads geometry information from the given DOM element into this {@link VdxShapeGeometry}.
     * @param geom the DOM element representing the geometry the part to read is in.
     * @param ix the ID of the geom part element to read (1 based)
     * @return the {@link GeometryPart} read from the shape
     */
    public static GeometryPart readFromGeomElement(Element geom, int ix) {
      AVdxGeomPart geomPart = null;

      Element geomPartElement = getGeomPartElement(geom, ix);
      if (geomPartElement != null) {
        geomPart = new VdxFreeGeomPart(geomPartElement.getTagName());

        NodeList childNodes = geomPartElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
          Node childNode = childNodes.item(i);
          if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            geomPart.addField(createGeometryPartFieldFromNode((Element) childNode));
          }
        }
      }
      return geomPart;
    }

    private static GeometryPartField createGeometryPartFieldFromNode(Element fieldNode) {
      GeometryPartField field = new VdxGeomPartField(((Element) fieldNode).getTagName());
      field.setValue(fieldNode.getTextContent());
      field.setFormula(VisioDOMUtils.readInheritableAttribute(fieldNode, "F"));
      field.setUnit(VisioDOMUtils.readInheritableAttribute(fieldNode, "Unit"));
      if (field.getFormula().isEmpty()) {
        field.setNoFormula(true);
      }
      return field;
    }

    /**
     * Returns whether the given Geom element has a part with the given IX
     * @param geom
     * @param ix
     * @return true if a fitting geom part was found
     */
    public static boolean hasPart(Element geom, int ix) {
      return getGeomPartElement(geom, ix) != null;
    }

    static Element getGeomPartElement(Element parentGeom, int ix) {
      NodeList childNodes = parentGeom.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        Node child = childNodes.item(i);
        if (child.getNodeType() == Node.ELEMENT_NODE && String.valueOf(ix).equals(((Element) child).getAttribute("IX"))) {
          return (Element) child;
        }
      }
      return null;
    }
  }
}
