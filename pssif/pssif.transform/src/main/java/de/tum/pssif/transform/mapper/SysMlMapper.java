package de.tum.pssif.transform.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.Enumeration;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.transform.Mapper;
import de.tum.pssif.transform.io.PSSIFIoException;


public class SysMlMapper implements Mapper {

  private static final String SYSML4MECHATRONICS_ECORE = "/sysml/sysml4mechatronics.ecore";

  private static final String BLOCK_MECHANIC           = "MechanicalBlock";
  private static final String BLOCK_ELECTRONIC         = "EEBlock";
  private static final String BLOCK_SOFTWARE           = "SoftwareBlock";
  private static final String BLOCK_MODULE             = "Module";
  private static final String PORT_MECHANIC            = "MechanicalPort";
  private static final String PORT_ELECTRONIC          = "EEPort";
  private static final String PORT_SOFTWARE            = "SoftwarePort";

  @Override
  public Model read(Metamodel metamodel, InputStream inputStream) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void write(Metamodel metamodel, Model model, OutputStream outputStream) {
    EPackage ePackage = getEPackage();
    Set<EObject> sysMlModelContents = writeSysMlModel(metamodel, ePackage, model);
    Resource resource = new XMIResourceFactoryImpl().createResource(URI.createFileURI(""));
    resource.getContents().addAll(sysMlModelContents);
    try {
      resource.save(outputStream, getOptions());
    } catch (IOException e) {
      throw new PSSIFIoException("Failed to serialize SysML4Mechatronics model.", e);
    }
  }

  private EPackage getEPackage() {
    Resource ecoreResource = new EcoreResourceFactoryImpl().createResource(URI.createURI(""));
    InputStream ecoreInputStream = getClass().getResourceAsStream(SYSML4MECHATRONICS_ECORE);
    try {
      ecoreResource.load(ecoreInputStream, getOptions());
    } catch (IOException e) {
      throw new PSSIFIoException("Load of SysML4Mechatronics eCore failed. Check if ecore file is available.", e);
    }
    EPackage ePackage = (EPackage) ecoreResource.getContents().get(0);
    return ePackage;
  }

  private Set<EObject> writeSysMlModel(Metamodel metamodel, EPackage ePackage, Model model) {
    Set<EObject> eObjects = Sets.newHashSet();
    Map<Node, EObject> nodesMap = Maps.newHashMap();
    //blocks
    eObjects
        .addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_MECHANIC), getEClass(ePackage, BLOCK_MECHANIC), model, nodesMap));
    eObjects.addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_ELECTRONIC), getEClass(ePackage, BLOCK_ELECTRONIC), model,
        nodesMap));
    eObjects
        .addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_SOFTWARE), getEClass(ePackage, BLOCK_SOFTWARE), model, nodesMap));
    //modules?

    //ports
    eObjects.addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_PORT_MECHANIC), getEClass(ePackage, PORT_MECHANIC), model,
        nodesMap));
    eObjects.addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_PORT_ELECTRONIC), getEClass(ePackage, PORT_ELECTRONIC), model,
        nodesMap));
    eObjects.addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_PORT_SOFTWARE), getEClass(ePackage, PORT_SOFTWARE), model,
        nodesMap));

    //TODO interface blocks + all relations 

    return eObjects;
  }

  private Set<EObject> exportNodes(NodeTypeBase nodeType, EClass eClass, Model model, Map<Node, EObject> nodesMap) {
    Set<EObject> eObjects = Sets.newHashSet();
    for (Node node : nodeType.apply(model, true)) {
      EObject eObject = exportNode(nodeType, eClass, node);
      eObjects.add(eObject);
      nodesMap.put(node, eObject);
    }
    return eObjects;
  }

  private EObject exportNode(NodeTypeBase nodeType, EClass eClass, Node node) {
    EObject eObject = EcoreUtil.create(eClass);
    for (Attribute attribute : nodeType.getAttributes()) {
      EAttribute eAttribute = findEAttribute(eClass, attribute);
      if (eAttribute != null) {
        PSSIFOption<PSSIFValue> attributeValue = attribute.get(node);
        if (!attributeValue.isNone()) {
          eObject.eSet(eAttribute, externalizeAttributeValue(attributeValue.getOne(), attribute, eAttribute));
        }
      }
    }
    return eObject;
  }

  private Object externalizeAttributeValue(PSSIFValue value, Attribute attribute, EAttribute eAttribute) {
    //TODO this one might be trouble... EDate and stuff
    if (attribute.getType() instanceof Enumeration) {
      return ((EEnum) eAttribute.getEType()).getEEnumLiteral(value.asEnumeration().getName());
    }
    else if (eAttribute.getEType().getName().equals("EInt")) {
      try {
        return Integer.valueOf(value.getValue().toString());
      } catch (NumberFormatException e) {
        //TODO this is bullshit. incompatible values should be ignored
        return Integer.valueOf(-1);
      }
    }
    else if (PrimitiveDataType.DECIMAL.equals(attribute.getType())) {
      return Double.valueOf(value.asDecimal().doubleValue());
    }
    return value.getValue();
  }

  private EAttribute findEAttribute(EClass eClass, Attribute attribute) {
    for (EStructuralFeature eFeature : eClass.getEAllStructuralFeatures()) {
      if (EAttribute.class.isInstance(eFeature) && attribute.getName().trim().equalsIgnoreCase(eFeature.getName().trim())) {
        return (EAttribute) eFeature;
      }
    }
    return null;
  }

  private NodeTypeBase getNodeType(Metamodel metamodel, String name) {
    return metamodel.getBaseNodeType(name).getOne();
  }

  private EClass getEClass(EPackage ePackage, String name) {
    return (EClass) ePackage.getEClassifier(name);
  }

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
