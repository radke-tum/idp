package de.iteratec.visio.model;

import org.w3c.dom.Element;


public interface DOMElementWrapper {

  /**
   * Returns the DOM element this Shape is based on
   * @return DOM element
   */
  Element getDOMElement();

}
