package de.iteratec.visio.model.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.iteratec.visio.model.IdHandler;


/**
 * {@link IdHandler} implementation 
 * 
 * This approach initializes its ID based on a given {@link Document}.
 * The maximum ID value of all <Shape> tags is determined, then incremented by 1.
 * From there the IdHandler counts up for each new shape ID to apply. 
 */
public class VdxIdHandler implements IdHandler {

  private long nextShapeId = 1;

  @Override
  public Long getNextId() {
    return Long.valueOf(nextShapeId++);
  }

  void initialize(Document document) {
    NodeList allShapeElements = document.getElementsByTagName("Shape");
    for (int i = 0; i < allShapeElements.getLength(); i++) {
      Node node = allShapeElements.item(i);
      Node idAttribute = node.getAttributes().getNamedItem("ID");
      String idValue = idAttribute.getNodeValue();
      long id = Long.parseLong(idValue);
      nextShapeId = Math.max(id + 1, nextShapeId);
    }
  }
}
