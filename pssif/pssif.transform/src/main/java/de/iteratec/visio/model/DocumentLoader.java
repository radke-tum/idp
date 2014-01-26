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
package de.iteratec.visio.model;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.iteratec.visio.model.impl.VdxDocumentLoader;


/**
 * Loader for Visio documents. Use DocumentFactory.getInstance().loadDocument(InputStream) 
 * to load a Visio 2003 document from its XML representation. 
 */
public abstract class DocumentLoader {

  public static DocumentLoader getVdxLoader() {
    return VdxDocumentLoader.getInstance();
  }

  @Deprecated
  public static DocumentLoader getInstance() {
    return getVdxLoader();
  }

  public abstract Document loadDocument(InputStream stream) throws IOException, ParserConfigurationException, SAXException;
}
