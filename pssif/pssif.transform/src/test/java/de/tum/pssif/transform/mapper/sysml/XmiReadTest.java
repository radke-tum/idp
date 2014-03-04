package de.tum.pssif.transform.mapper.sysml;

import java.io.InputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Test;

import com.google.common.collect.Maps;


public class XmiReadTest {

  @Test
  public void testRead() {
    try {
      Resource ecoreResource = new EcoreResourceFactoryImpl().createResource(URI.createURI(""));
      InputStream ecoreInputStream = getClass().getResourceAsStream("/sysml/uml2-4.0.0.ecore");
      ecoreResource.load(ecoreInputStream, getOptions());
      EPackage ePackage = (EPackage) ecoreResource.getContents().get(0);
      //      ePackage.setNsURI("http://www.eclipse.org/uml2/4.0.0/UML");
      EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);

      InputStream xmiInputStream = getClass().getResourceAsStream("/sysml/test3.xml");
      Resource resource = new XMIResourceFactoryImpl().createResource(URI.createURI(""));
      resource.load(xmiInputStream, getOptions());

      EObject eContainerObject = resource.getContents().get(0);
      EPackage.Registry.INSTANCE.remove(ePackage.getNsURI());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates the save options for saving ecore and xmi models.
   * 
   * @return the map containing save options
   */
  public static Map<String, Object> getOptions() {
    Map<String, Object> options = Maps.newHashMap();
    options.put(XMLResource.OPTION_ENCODING, "UTF-8");
    options.put(XMIResource.OPTION_EXTENDED_META_DATA, ExtendedMetaData.INSTANCE);
    options.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
    options.put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
    options.put(XMIResource.OPTION_LAX_FEATURE_PROCESSING, Boolean.TRUE);
    options.put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);
    options.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);

    return options;
  }

}
