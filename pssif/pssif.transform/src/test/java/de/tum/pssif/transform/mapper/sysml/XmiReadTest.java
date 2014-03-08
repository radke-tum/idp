package de.tum.pssif.transform.mapper.sysml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class XmiReadTest {

  @Test
  public void testRead() {
    try {
      Resource ecoreResource = new EcoreResourceFactoryImpl().createResource(URI.createURI(""));
      //      InputStream ecoreInputStream = getClass().getResourceAsStream("/sysml/uml2-4.0.0.ecore");
      //      InputStream ecoreInputStream = getClass().getResourceAsStream("/sysml/Ecore.ecore");
      //      InputStream ecoreInputStream = getClass().getResourceAsStream("/sysml/uml2-2.0.0.ecore");
      InputStream ecoreInputStream = getClass().getResourceAsStream("/sysml/uml2-4.0.0-tweaked.ecore");
      //      InputStream ecoreInputStream = getClass().getResourceAsStream("/sysml/iteraplan.ecore");
      ecoreResource.load(ecoreInputStream, getOptions());
      EPackage ePackage = (EPackage) ecoreResource.getContents().get(0);
      //      ePackage.setNsURI("http://www.eclipse.org/uml2/4.0.0/UML");
      EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);

      List<EClass> eClasses = Lists.newArrayList();
      List<EDataType> eDataTypes = Lists.newArrayList();

      for (EClassifier classifier : ePackage.getEClassifiers()) {
        if (classifier instanceof EClass) {
          EClass eClass = (EClass) classifier;
          eClasses.add(eClass);
          //          System.out.println("eClass: " + eClass.getName());
        }
        else {
          EDataType eDataType = (EDataType) classifier;
          eDataTypes.add(eDataType);
          //          System.out.println("eDataType: " + eDataType.getName());
        }
      }
      //      for (EClass eClass : eClasses) {
      //        System.out.println("eClass: " + eClass.getName());
      //        //        if (eClass.getName().equals("Model")) {
      //        //          printSupers(eClass, "");
      //        //        }
      //      }
      //      for (EDataType eDataType : eDataTypes) {
      //        System.out.println("eDataType: " + eDataType.getName());
      //      }

      InputStream xmiInputStream = getClass().getResourceAsStream("/sysml/test3.xml");
      //      InputStream xmiInputStream = getClass().getResourceAsStream("/sysml/iteraplan.xmi");
      Resource resource = new XMIResourceFactoryImpl().createResource(URI.createURI(""));
      resource.load(xmiInputStream, getOptions());

      EObject eContainerObject = resource.getContents().get(0);
      EPackage.Registry.INSTANCE.remove(ePackage.getNsURI());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void printSupers(EClass eClass, String spacing) {
    System.out.println(spacing + (eClass.getName() != null ? eClass.getName() : eClass.toString()));
    for (EClass superClass : eClass.getESuperTypes()) {
      printSupers(superClass, spacing + "-");
    }
  }

  /**
   * Creates the save options for saving ecore and xmi models.
   * 
   * @return the map containing save options
   */
  public static Map<String, Object> getOptions() {
    Map<String, Object> options = Maps.newHashMap();
    //    options.put(XMLResource.OPTION_ENCODING, "UTF-8");
    //    options.put(XMIResource.OPTION_EXTENDED_META_DATA, ExtendedMetaData.INSTANCE);
    //    options.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
    //    options.put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
    //    options.put(XMIResource.OPTION_LAX_FEATURE_PROCESSING, Boolean.TRUE);
    //    options.put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);
    //    options.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);

    return options;
  }

}
