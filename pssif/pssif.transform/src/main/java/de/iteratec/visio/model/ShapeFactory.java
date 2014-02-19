package de.iteratec.visio.model;

import org.w3c.dom.Element;


/**
 * Interface that forces callers to use the createNewShape factory methods 
 * instead of the constructors of {@link Shape} implementations.
 */
public interface ShapeFactory {
  /**
   * Creates a new shape element based on the master in the given container.
   * 
   * @param parent The parent container as wrapper object.
   * @param master The master to base the new shape on.
   * @param idHandler the {@link IdHandler} to manage the new shape's (and possible sub-shapes') IDs
   * @return the newly created Shape
   */
  Shape createNewShape(ShapeContainer parent, Master master, IdHandler idHandler);

  /**
   * Creates a wrapper around an existing shape element.
   * @param shapesElement The DOM element of the shape in the outer container's XML representation.
   * @param parent The parent container as wrapper object.
   * @return the newly created Shape
   */
  Shape createNewShapeWrapper(Element shapeElement, ShapeContainer parent);

  /**
   * Creates a new empty shape with some basic initializations and returns the wrapper object for it 
   * @param parent The parent container as wrapper object.
   * @param idHandler the {@link IdHandler} to manage the new shape's (and possible sub-shapes') IDs
   * @return the newly created Shape
   */
  Shape createNewEmptyShape(ShapeContainer parent, IdHandler idHandler);
}
