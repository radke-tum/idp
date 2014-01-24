package de.iteratec.visio.model.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.iteratec.visio.model.IdHandler;
import de.iteratec.visio.model.Master;
import de.iteratec.visio.model.Shape;
import de.iteratec.visio.model.ShapeContainer;
import de.iteratec.visio.model.ShapeFactory;


public class VdxShapeFactory implements ShapeFactory {

  private static final VdxShapeFactory INSTANCE = new VdxShapeFactory();

  private VdxShapeFactory() {
    // private constructor to prevent outside instantiation
  }

  public static VdxShapeFactory getInstance() {
    return INSTANCE;
  }

  @Override
  public VdxShape createNewShape(ShapeContainer parent, Master master, IdHandler idHandler) {
    Element shapesElement = VisioDOMUtils.getOrCreateFirstChildWithName(parent.getDOMElement(), "Shapes");
    Element shapeElement = instantiateBasedOnMaster(shapesElement, master, idHandler);
    return new VdxShape(shapeElement, parent);
  }

  @Override
  public VdxShape createNewShapeWrapper(Element shape, ShapeContainer parent) {
    return new VdxShape(shape, parent);
  }

  @Override
  public VdxShape createNewEmptyShape(ShapeContainer parent, IdHandler idHandler) {
    Element shapesElement = VisioDOMUtils.getOrCreateFirstChildWithName(parent.getDOMElement(), "Shapes");
    Element shape = VisioDOMUtils.createChildWithName(shapesElement, "Shape");

    shape.setAttribute("ID", idHandler.getNextId().toString());
    // TODO initializations?
    return createNewShapeWrapper(shape, parent);
  }

  /**
   * Copies all content from a master shape onto a new shape.
   * 
   * This is in some way similar to the deep-copy version of {@link Node#cloneNode(boolean)},
   * but it also adjust the references in a way Visio would do it on instantiation of a new
   * shape. Additionally the references to the master shapes get written to the new shapes
   * created.
   * 
   * What Visio does is to use only absolute references of the form "Sheet.[N]!prop" if a
   * shape uses a references to another shape in any formula. The [N] is a number referencing
   * the ID field of the target shape. This mechanism is used even in master shapes, which
   * means all shapes in a grouped master shape refer to each other by absolute reference,
   * which would break if the shapes are just copied onto the page. To avoid this, Visio fixes
   * the references when instantiating the new shape by changing any reference to a shape
   * in the master to a reference to the matching sub-shape of the new shape.
   * 
   * The mechanism for the references to the master and master shapes used is the following:
   * the outermost new shape gets a reference to the master (the "Master" attribute) but not
   * the "MasterShape" Attribute. The inner shapes get references to their "MasterShape", but
   * not to the master. This seems to be what Visio does, including the implication that a
   * master will always contain only one outermost shape (any master with more than one shape
   * has a group shape as outermost shape).

   * @param shapesElement The &lt;Shapes&gt; element to put the new shape element into.
   * @param master The Master to use as base of the new shape
   * @param idHandler the {@link IdHandler} to manage the new shape's (and possible sub-shapes') IDs
   * @return The new shape element.
   */
  private Element instantiateBasedOnMaster(Element shapesElement, Master master, IdHandler idHandler) {
    List<? extends Shape> masterShapes = master.getShapes();
    Map<Long, Long> masterIdToShapeId = new HashMap<Long, Long>();
    Set<Attr> formulaNodes = new HashSet<Attr>();
    Element result = copyMasterShape(masterShapes.get(0).getDOMElement(), masterIdToShapeId, formulaNodes, idHandler);
    // the top one gets the @Master attribute
    result.setAttribute("Master", master.getID().toString());
    // but not the @MasterShape -- removing it here saves us a case in the copy method
    result.removeAttribute("MasterShape");
    shapesElement.appendChild(result);
    adjustFormulae(masterIdToShapeId, formulaNodes);
    return result;
  }

  /**
   * This method copies the content of the masterElement into a new element and adjusts it.
   * 
   * While doing that it keeps track of new shapes created and their master shape, sets the
   * MasterShape attributes and records all attribute nodes that contain formulae.
   * 
   * @param masterElement The XML element with the master shape.
   * @param masterIdToShapeId [OUT] A mapping from the IDs of the master shapes to the instantiated ones. 
   * @param formulaNodes [OUT] A list of all attribute nodes containing formulae (the "F" attributes).
   * @param idHandler the {@link IdHandler} to manage the new shape's (and possible sub-shapes') IDs
   * 
   * @return The new shape element with the adjusted data.
   */
  private Element copyMasterShape(Element masterElement, Map<Long, Long> masterIdToShapeId, Set<Attr> formulaNodes, IdHandler idHandler) {
    Element result = (Element) masterElement.cloneNode(false);
    NodeList children = masterElement.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node curChild = children.item(i);
      if (curChild.getNodeType() == Node.ELEMENT_NODE) {
        Element curElement = (Element) curChild;
        if (curElement.getTagName().equals("Shapes")) {
          // if shapes element, make a shallow copy and recurse
          Node newShapesElement = curElement.cloneNode(false);
          result.appendChild(newShapesElement);
          // copy all shapes in the element across (it shouldn't have any other
          // children)
          NodeList shapesChildren = curElement.getChildNodes();
          for (int j = 0; j < shapesChildren.getLength(); j++) {
            Node curSubShape = shapesChildren.item(j);
            // TODO check if sufficient...
            if (curSubShape instanceof Element) {
              newShapesElement.appendChild(copyMasterShape((Element) curSubShape, masterIdToShapeId, formulaNodes, idHandler));
            }
          }
        }
        else {
          // all the rest gets deeply copied while collecting the formulae
          deepCopy(result, curElement, formulaNodes);
        }
      }
    }
    Long masterShapeId = Long.valueOf(masterElement.getAttribute("ID"));
    // new shape gets its own ID
    Long newShapeId = idHandler.getNextId();
    result.setAttribute("ID", newShapeId.toString());
    // and the master shape's ID as reference
    result.setAttribute("MasterShape", masterShapeId.toString());
    masterIdToShapeId.put(masterShapeId, newShapeId);
    return result;
  }

  /**
   * Creates a deep copy of the source element into the target element, collecting formulae.
   * 
   * @param targetElement The target element for the new copy (will be a parent of the copy
   *                      of the source).
   * @param sourceElement The source which is to be copied.
   * @param formulaNodes [OUT] Collects all attribute nodes that contain formulae.
   * 
   * TODO it might be easier to read (although slightly less performant) to copy first, then
   *      collect formulae. This method could then be replaced by {@link Node#cloneNode(boolean)}.
   */
  private void deepCopy(Element targetElement, Element sourceElement, Set<Attr> formulaNodes) {
    Element newElement = (Element) sourceElement.cloneNode(false);
    targetElement.appendChild(newElement);
    NodeList children = sourceElement.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node curChild = children.item(i);
      if (curChild.getNodeType() == Node.ELEMENT_NODE) {
        Element curElement = (Element) curChild;
        deepCopy(newElement, curElement, formulaNodes);
      }
      else {
        newElement.appendChild(curChild.cloneNode(true)); // e.g. text nodes
      }
    }
    Attr formulaAttr = newElement.getAttributeNode("F");
    if (formulaAttr != null) {
      formulaNodes.add(formulaAttr);
    }
  }

  /**
   * Changes all references to master shape IDs into references to the IDs of the created 
   * shapes. 
   * 
   * @param masterIdToShapeId A map from the IDs of the master shapes to the IDs of the new shapes.
   * @param formulaNodes All attribute nodes that contain formulae.
   */
  private void adjustFormulae(Map<Long, Long> masterIdToShapeId, Set<Attr> formulaNodes) {
    for (Iterator<Attr> iter = formulaNodes.iterator(); iter.hasNext();) {
      Attr attribute = (Attr) iter.next();
      // important in the next pattern: the first group is not greedy (the extra quesion mark),
      // otherwise in case of multiple occurances of the "Sheet." part all but the last will
      // be matched into the first group
      Pattern pattern = Pattern.compile("^(.*?)Sheet\\.(\\d+)!(.*)$");
      Matcher matcher = pattern.matcher(attribute.getValue());
      StringBuffer result = new StringBuffer();
      String rest = attribute.getValue();
      while (matcher.matches()) {
        result.append(matcher.group(1)); // anything before the ref
        result.append("Sheet."); // the fixed part
        result.append(map(matcher.group(2), masterIdToShapeId)); // the mapped id
        result.append("!"); // the other fixed part
        rest = matcher.group(3); // remainders
        matcher = pattern.matcher(rest); // leftovers are checked again
      }
      result.append(rest); // final remainder gets appended again
      attribute.setValue(result.toString());
    }
  }

  /**
   * Tries to retrieve the mapped ID for a master ID.
   * 
   * @param masterShapeId The ID of the master shape as String.
   * @param masterShapeIdToShapeId The map between the IDs.
   * @return The mapped ID as String.
   * 
   * @throws IllegalArgumentException iff the mapping is not successful.
   */
  private String map(String masterShapeId, Map<Long, Long> masterShapeIdToShapeId) {
    Long oldIdVal = Long.valueOf(masterShapeId);
    Long newIdVal = masterShapeIdToShapeId.get(oldIdVal);
    if (newIdVal == null) {
      throw new IllegalArgumentException("Visio document contains invalid reference on master shape");
    }
    return newIdVal.toString();
  }
}
