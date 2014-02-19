package de.iteratec.visio.model;

/**
 * Used to manage unique IDs for shapes in Visio documents
 */
public interface IdHandler {

  /**
   * Returns the next valid ID and marks it as used internally
   * @return a valid ID
   */
  Long getNextId();
}
