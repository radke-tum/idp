/*
 * gxl2visio is a library to manipulate visio documents developed by iteratec, GmbH
 * Copyright (C) 2008 iteratec, GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED
 * WORK IN WHICH THE COPYRIGHT IS OWNED BY ITERATEC, ITERATEC DISCLAIMS THE
 * WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact iteratec GmbH headquarters at Inselkammerstr. 4
 * 82008 Munich - Unterhaching, Germany, or via email info@iteratec.de.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 */
package de.iteratec.visio.model.impl;

import java.math.BigInteger;

import org.w3c.dom.Element;

import de.iteratec.visio.model.Master;


/**
 * Wraps a Visio master DOM element.
 * 
 * This is the represenation of a stencil as it can be drawn onto a page. Note that
 * this differs from a shape in that a master is not a shape but contains shapes. It
 * is more similar to a page in some regards.
 */
public class VdxMaster extends VdxShapeContainer implements Master {

  /**
   * The internal representation as a DOM element
   */
  private Element           master;

  /**
   * The document this master belongs to.
   */
  private final VdxDocument document;

  /**
   * Creates a new wrapper instance for the given master DOM element.
   * This is intended to be called by the Document factory methods.
   * 
   * @param master The master element to wrap. 
   * @param document The reference to the enclosing document.
   */
  VdxMaster(Element master, VdxDocument document) {
    this.master = master;
    this.document = document;
  }

  public VdxDocument getDocument() {
    return this.document;
  }

  /**
   * The Visio identifier for the master.
   * 
   * @return A unique number for this master.
   */
  public BigInteger getID() {
    return new BigInteger(this.master.getAttribute("ID"));
  }

  @Override
  public Element getDOMElement() {
    return master;
  }
}
