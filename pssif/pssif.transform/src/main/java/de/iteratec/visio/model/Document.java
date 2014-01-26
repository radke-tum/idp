/*
 * gxl2visio is a library to manipulate visio documents developed by iteratec, GmbH Copyright (C) 2008 iteratec, GmbH This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU Affero General Public License version 3 as published by the Free Software
 * Foundation with the addition of the following permission added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK IN
 * WHICH THE COPYRIGHT IS OWNED BY ITERATEC, ITERATEC DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU Affero General Public License along with
 * this program; if not, see http://www.gnu.org/licenses or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA. You can contact iteratec GmbH headquarters at Inselkammerstr. 4 82008 Munich - Unterhaching, Germany, or via email
 * info@iteratec.de. The interactive user interfaces in modified source and object code versions of this program must display Appropriate Legal
 * Notices, as required under Section 5 of the GNU Affero General Public License version 3.
 */
package de.iteratec.visio.model;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Collection;

import de.iteratec.visio.model.exceptions.MasterNotFoundException;
import de.iteratec.visio.model.exceptions.NoSuchElementException;


public interface Document {

  /**
   * Sets the title in the document properties.
   * 
   * @param title
   *            The new title for the document.
   */
  void setTitle(String title);

  /**
   * Retrieves a master from the document stencils by its unique name.
   * 
   * @param uniqueName
   *            The unique name of the master in the Visio document
   * @return The Visio model class representing the master.
   * @throws MasterNotFoundException
   *             If the uniqueName provided does not identify a master in the
   *             document.
   */
  Master getMaster(String uniqueName) throws MasterNotFoundException;

  //TODO
  Collection<Master> getMasters();

  /**
   * Retrieves a master from the document stencils by its ID.
   * 
   * @param id
   *            The identifier for the master in the Visio document
   * @return The Visio model class representing the master.
   * @throws MasterNotFoundException
   *             If the ID provided does not identify a master in the
   *             document.
   */
  Master getMaster(BigInteger id) throws MasterNotFoundException;

  /**
   * Saves the document to the given file.
   * 
   * @param outputFile
   *            The file to write to.
   * @throws IOException
   *             If the file can not be written.
   */
  void save(File outputFile) throws IOException;

  /**
   * Retrieves the page at the given index.
   * 
   * @param pageNumber
   *            The number of the page (starts with 0).
   * @return An object representing the page
   * @throws NoSuchElementException
   *             If the document does not have enough pages
   */
  Page getPage(int pageNumber) throws NoSuchElementException;

  /**
   * Returns the handler for Visio shape IDs
   * @return the {@link IdHandler}
   */
  IdHandler getIdHandler();

  /**
   * Writes this document to the given output stream.
   *  
   * @param out The OutputStream to write to.
   * @throws IOException
   *             If the write operation was unsuccessful.
   */
  void write(OutputStream out) throws IOException;

}