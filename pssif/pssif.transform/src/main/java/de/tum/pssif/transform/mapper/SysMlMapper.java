package de.tum.pssif.transform.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Enumeration;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.model.Edge;
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

  private static final String FUNCTIONALITY            = "Functionality";

  private static final String OWNED_BLOCK              = "ownedBlock";
  private static final String OWNED_PORT               = "ownedPort";
  private static final String IS_CONNECTED_TO          = "isConnectedTo";

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

    //mechanic blocks
    eObjects.addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_MECHANIC), getEClass(ePackage, BLOCK_MECHANIC), model,
        nodesMap, true));
    //electronics blocks
    eObjects.addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_ELECTRONIC), getEClass(ePackage, BLOCK_ELECTRONIC), model,
        nodesMap, true));
    //software blocks
    eObjects.addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_SOFTWARE), getEClass(ePackage, BLOCK_SOFTWARE), model,
        nodesMap, true));
    //modules
    eObjects.addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_MODULE), getEClass(ePackage, BLOCK_MODULE), model, nodesMap,
        true));

    //ports
    eObjects.addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_PORT_MECHANIC), getEClass(ePackage, PORT_MECHANIC), model,
        nodesMap, true));
    eObjects.addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_PORT_ELECTRONIC), getEClass(ePackage, PORT_ELECTRONIC), model,
        nodesMap, true));
    eObjects.addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_PORT_SOFTWARE), getEClass(ePackage, PORT_SOFTWARE), model,
        nodesMap, true));

    //functionalities
    eObjects.addAll(exportNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_FUNCTIONALITY), getEClass(ePackage, FUNCTIONALITY), model,
        nodesMap, true));

    //what rels are to be writt?
    //write block2blockRelationships
    writeBlock2BlockRelationships(metamodel, ePackage, model, nodesMap);
    writeBlock2PortRelationships(metamodel, ePackage, model, nodesMap);
    writePort2PortRelationships(metamodel, ePackage, model, nodesMap);
    //write block2portRelationships
    //write block2functionality
    //write port2functionality

    //TODO interface blocks

    return eObjects;
  }

  private void writeBlock2BlockRelationships(Metamodel metamodel, EPackage ePackage, Model model, Map<Node, EObject> eObjects) {
    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS, OWNED_BLOCK,
        PSSIFCanonicMetamodelCreator.N_BLOCK, PSSIFCanonicMetamodelCreator.N_BLOCK, model, eObjects);
  }

  private void writeBlock2PortRelationships(Metamodel metamodel, EPackage ePackage, Model model, Map<Node, EObject> eObjects) {
    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS, OWNED_PORT,
        PSSIFCanonicMetamodelCreator.N_SOFTWARE, PSSIFCanonicMetamodelCreator.N_PORT_SOFTWARE, model, eObjects);

    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS, OWNED_PORT,
        PSSIFCanonicMetamodelCreator.N_MECHANIC, PSSIFCanonicMetamodelCreator.N_PORT_MECHANIC, model, eObjects);

    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS, OWNED_PORT,
        PSSIFCanonicMetamodelCreator.N_ELECTRONIC, PSSIFCanonicMetamodelCreator.N_PORT_SOFTWARE, model, eObjects);
    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS, OWNED_PORT,
        PSSIFCanonicMetamodelCreator.N_ELECTRONIC, PSSIFCanonicMetamodelCreator.N_PORT_MECHANIC, model, eObjects);
    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS, OWNED_PORT,
        PSSIFCanonicMetamodelCreator.N_ELECTRONIC, PSSIFCanonicMetamodelCreator.N_PORT_ELECTRONIC, model, eObjects);

    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS, OWNED_PORT,
        PSSIFCanonicMetamodelCreator.N_MODULE, PSSIFCanonicMetamodelCreator.N_PORT, model, eObjects);
  }

  private void writePort2PortRelationships(Metamodel metamodel, EPackage ePackage, Model model, Map<Node, EObject> eObjects) {
    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_IS_CONNECTED_TO, IS_CONNECTED_TO,
        PSSIFCanonicMetamodelCreator.N_PORT_ELECTRONIC, PSSIFCanonicMetamodelCreator.N_PORT_ELECTRONIC, model, eObjects);
    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_IS_CONNECTED_TO, IS_CONNECTED_TO,
        PSSIFCanonicMetamodelCreator.N_PORT_MECHANIC, PSSIFCanonicMetamodelCreator.N_PORT_MECHANIC, model, eObjects);
    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_IS_CONNECTED_TO, IS_CONNECTED_TO,
        PSSIFCanonicMetamodelCreator.N_PORT_SOFTWARE, PSSIFCanonicMetamodelCreator.N_PORT_SOFTWARE, model, eObjects);
  }

  //  private void writeBlock2FunctionalityRelationships(Metamodel metamodel, EPackage ePackage, Model model, Map<Node, EObject> eObjects) {
  //    writeRelationship(metamodel, ePackage, edgeTypeName, eReferenceName, fromTypeName, toTypeName, model, eObjects);
  //  }
  //
  //  private void writePort2FunctionalityRelationships(Metamodel metamodel, EPackage ePackage, Model model, Map<Node, EObject> eObjects) {
  //    writeRelationship(metamodel, ePackage, edgeTypeName, eReferenceName, fromTypeName, toTypeName, model, eObjects);
  //  }

  private void writeRelationship(Metamodel metamodel, EPackage ePackage, String edgeTypeName, String eReferenceName, String fromTypeName,
                                 String toTypeName, Model model, Map<Node, EObject> eObjects) {
    EdgeType edgeType = metamodel.getEdgeType(edgeTypeName).getOne();
    if (edgeType == null) {
      return;
    }
    NodeTypeBase fromType = metamodel.getNodeType(fromTypeName).getOne();
    NodeTypeBase toType = metamodel.getNodeType(toTypeName).getOne();
    ConnectionMapping mapping = edgeType.getMapping(fromType, toType).getOne();

    Map<EObject, List<EObject>> toConnect = Maps.newHashMap();

    for (Edge edge : mapping.apply(model)) {
      EObject fromObject = eObjects.get(edgeType.applyFrom(edge));
      EObject toObject = eObjects.get(edgeType.applyTo(edge));
      if (fromObject != null && toObject != null) {
        if (!toConnect.containsKey(fromObject)) {
          toConnect.put(fromObject, Lists.<EObject> newArrayList());
        }
        toConnect.get(fromObject).add(toObject);
      }
    }
    for (Entry<EObject, List<EObject>> entry : toConnect.entrySet()) {
      if (entry.getKey().eClass().getEStructuralFeature(eReferenceName) != null) {
        entry.getKey().eSet(entry.getKey().eClass().getEStructuralFeature(eReferenceName), entry.getValue());
      }
    }
  }

  private Set<EObject> exportNodes(NodeTypeBase nodeType, EClass eClass, Model model, Map<Node, EObject> nodesMap, boolean subtypes) {
    Set<EObject> eObjects = Sets.newHashSet();
    for (Node node : nodeType.apply(model, subtypes)) {
      EObject eObject = exportNode(nodeType, eClass, node);
      eObjects.add(eObject);
      nodesMap.put(node, eObject);
    }
    return eObjects;
  }

  private EObject exportNode(NodeTypeBase nodeType, EClass eClass, Node node) {
    EObject eObject = EcoreUtil.create(eClass);
    //    EcoreUtil.setID(eObject, node.getId());
    for (Attribute attribute : nodeType.getAttributes()) {
      EAttribute eAttribute = findEAttribute(eClass, attribute);
      if (eAttribute != null) {
        PSSIFOption<PSSIFValue> attributeValue = attribute.get(node);
        if (!attributeValue.isNone()) {
          Object externalAV = externalizeAttributeValue(attributeValue.getOne(), attribute, eAttribute);
          if (externalAV != null) {
            eObject.eSet(eAttribute, externalAV);
          }
        }
      }
    }
    return eObject;
  }

  private Object externalizeAttributeValue(PSSIFValue value, Attribute attribute, EAttribute eAttribute) {
    //TODO this one will be trobule... internalization also needed
    if (attribute.getType() instanceof Enumeration) {
      return ((EEnum) eAttribute.getEType()).getEEnumLiteral(value.asEnumeration().getName());
    }
    else if (eAttribute.getEType().getName().equals("EInt")) {
      try {
        return Integer.valueOf(value.getValue().toString());
      } catch (NumberFormatException e) {
        return null;
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
