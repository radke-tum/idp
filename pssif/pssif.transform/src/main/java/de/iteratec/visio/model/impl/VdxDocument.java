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
package de.iteratec.visio.model.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.google.common.collect.Sets;

import de.iteratec.visio.model.Document;
import de.iteratec.visio.model.DocumentLoader;
import de.iteratec.visio.model.IdHandler;
import de.iteratec.visio.model.Master;
import de.iteratec.visio.model.exceptions.MasterNotFoundException;
import de.iteratec.visio.model.exceptions.NoSuchElementException;


/**
 * Represents an abstraction of a Visio document as a whole. 
 * 
 * This is the entry point for the Visio model. 
 * It offers access to the master stencils, the pages and the metadata.
 */
public class VdxDocument implements Document {

  private static final Logger      LOGGER        = Logger.getLogger(VdxDocument.class.getName());

  private org.w3c.dom.Document     document;
  private Element                  rootNode;

  /**
   * Manages the unique shape IDs
   */
  private VdxIdHandler             idHandler;

  /**
   * Maps the unique names of masters to the master instances. This is
   * initialized lazily in prepareMasterCaches().
   */
  private Map<String, Element>     mastersByName = null;

  /**
   * Maps the IDs of masters to the master instances. This is initialized
   * lazily in prepareMasterCaches().
   */
  private Map<BigInteger, Element> mastersById   = null;

  /**
   * Creates a new Document model by parsing Visio XML from the given input
   * stream. 
   * 
   * This constructor is package-private. Use {@link DocumentLoader} from outside 
   * the package to create a new document. 
   * 
   * @param inputStream
   *            The input stream with the document XML. Must not be null.
   * @throws IOException
   *            Iff there is a problem reading the input stream.
   * @throws ParserConfigurationException 
   *            Iff there is a problem during the instantiation of the factory.
   * @throws SAXException
   *            Iff there is a parsing problem.
   */
  VdxDocument(InputStream inputStream) throws IOException, SAXException, ParserConfigurationException {
    // make sure that there is no xml-apis.jar in the endorsed folder of tomcat
    // otherwise a configuration error may be thrown...
    // see http://forum.java.sun.com/thread.jspa?tstart=30&forumID=34&threadID=542044&trange=15
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    this.document = builder.parse(inputStream);
    this.rootNode = document.getDocumentElement();
    idHandler = new VdxIdHandler();
    idHandler.initialize(this.document);
  }

  public void setTitle(String title) {
    Element documentPropertiesElement = VisioDOMUtils.getFirstChildWithName(rootNode, "DocumentProperties");
    Element titleElement = VisioDOMUtils.getOrCreateFirstChildWithName(documentPropertiesElement, "Title");
    titleElement.setTextContent(title);
  }

  public VdxMaster getMaster(String name) throws MasterNotFoundException {
    prepareMasterCaches();
    Element masterElement = this.mastersByName.get(name);
    if (masterElement == null) {
      throw new MasterNotFoundException("No master shape with unique name '" + name + "' found.");
    }
    return new VdxMaster(masterElement, this);
  }

  public Collection<Master> getMasters() {
    Set<Master> masters = Sets.newHashSet();
    //
    for (String masterName : mastersByName.keySet()) {
      try {
        masters.add(getMaster(masterName));
      } catch (MasterNotFoundException e) {
        throw new IllegalStateException("This should NEVER EVER happen...AGAIN!");
      }
    }
    //
    return masters;
  }

  public VdxMaster getMaster(BigInteger id) throws MasterNotFoundException {
    prepareMasterCaches();
    Element masterElement = this.mastersById.get(id);
    if (masterElement == null) {
      throw new MasterNotFoundException("No master shape with id '" + id + "' found.");
    }
    return new VdxMaster(masterElement, this);
  }

  /**
   * Initializes the caches for the masters (name to master, id to master).
   */
  private void prepareMasterCaches() {
    if (this.mastersById != null) {
      return; // already initialized
    }
    this.mastersByName = new Hashtable<String, Element>();
    this.mastersById = new Hashtable<BigInteger, Element>();
    Element mastersNode = (Element) rootNode.getElementsByTagName("Masters").item(0);
    List<Element> masterList = VisioDOMUtils.getChildrenWithName(mastersNode, "Master");
    for (int i = 0; i < masterList.size(); i++) {
      Element master = masterList.get(i);
      String name = master.getAttribute("NameU");
      BigInteger id = new BigInteger(master.getAttribute("ID"));
      this.mastersByName.put(name, master);
      this.mastersById.put(id, master);
    }
  }

  @Override
  public IdHandler getIdHandler() {
    return idHandler;
  }

  public void save(File outputFile) throws IOException {
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(outputFile);
      write(fileOutputStream);
    } finally {
      if (fileOutputStream != null) {
        try {
          fileOutputStream.close();
        } catch (IOException e) {
          LOGGER.log(Level.WARNING, "Unable to close output stream.", e);
        }
      }
    }
  }

  /**
   * Transforms *this* Document to the given StreamResult representation.
   * 
   * @param result 
   *    The StreamResult to transform to.
   *    
   * @throws IOException 
   *    If a TransformerException occurs.   
   */
  private void transformTo(StreamResult result) throws TransformerException {

    Transformer transformer;
    DOMSource source = new DOMSource(document);
    transformer = TransformerFactory.newInstance().newTransformer();
    transformer.transform(source, result);
  }

  public VdxPage getPage(int pageNumber) throws NoSuchElementException {
    Element pages = (Element) rootNode.getElementsByTagName("Pages").item(0);
    List<Element> pageList = VisioDOMUtils.getChildrenWithName(pages, "Page");
    if (pageList.size() <= pageNumber) {
      throw new NoSuchElementException("No page in position " + pageNumber);
    }
    Element pageElement = pageList.get(pageNumber);
    return new VdxPage(pageElement, this);
  }

  /**
   * Writes the document to the given Writer. 
   * 
   * As Visio does not like the 
   * standalone attribute of the XML declaration, the XML declaration
   * from writing the DOM (with the standalone attribute) is replaced by a XML declaration without
   * the standalone attribute. The writer class within the method just does this.
   * 
   * @throws IOException 
   */
  public void write(OutputStream out) throws IOException {

    try {
      Writer outWriter = new OutputStreamWriter(out, "UTF-8");
      Writer filteredOut = new FilterWriter(outWriter) {
        private boolean haveDecl = false;

        @Override
        public void write(int c) throws IOException {
          if (haveDecl) {
            super.write(c);
          }
        }

        @Override
        public void write(String str, int off, int len) throws IOException {
          this.write(str.toCharArray(), off, len);
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
          if (haveDecl) {
            out.write(cbuf, off, len);
            return;
          }
          String data = String.valueOf(cbuf, off, len);
          int indexOfGT = data.indexOf('>');
          if (indexOfGT > 0) {
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.write(cbuf, off + indexOfGT + 1, len - indexOfGT - 1);
            haveDecl = true;
          } // else: XML declaration not finished yet, ignore this part
        }
      };

      try {
        transformTo(new StreamResult(filteredOut));
      } catch (TransformerException e) {
        IOException ioe = new IOException();
        ioe.initCause(e);
        throw ioe;
      }
    } catch (UnsupportedEncodingException e) {
      LOGGER.log(Level.SEVERE, "Character Encoding used for the Visio document is not supported.");
    }
  }
}
