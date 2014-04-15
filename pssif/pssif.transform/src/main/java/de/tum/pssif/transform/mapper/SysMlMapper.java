package de.tum.pssif.transform.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
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

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.ModelImpl;
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

  private static final String INTERFACE_ELECTRONIC     = "EEInterfaceBlock";
  private static final String INTERFACE_MECHANIC       = "MechanicalInterfaceBlock";
  private static final String INTERFACE_SOFTWARE       = "SoftwareInterfaceBlock";

  private static final String FUNCTIONALITY            = "Functionality";

  private static final String EE_DATA_TYPE             = "EEDataType";

  private static final String OWNED_BLOCK              = "ownedBlock";
  private static final String OWNED_PORT               = "ownedPort";
  private static final String IS_CONNECTED_TO          = "isConnectedTo";
  private static final String OWNED_FUNCTIONALITY      = "ownedFunctionality";
  private static final String IS_MANDATORY_FOR         = "isMandatoryFor";
  private static final String OWNED_INTERFACE_BLOCK    = "ownedInterfaceBlock";
  private static final String OWMED_DATA_TYPE          = "ownedDataType";
  private static final String OPERATION_NAME           = "OperationName";

  @Override
  public Model read(Metamodel metamodel, InputStream inputStream) {
    EPackage ePackage = getEPackage();
    EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
    Resource resource = new XMIResourceFactoryImpl().createResource(URI.createURI(""));
    try {
      resource.load(inputStream, getOptions());
    } catch (IOException e) {
      throw new PSSIFIoException("Failed to read XMI from stream.", e);
    } finally {
      EPackage.Registry.INSTANCE.remove(ePackage.getNsURI());
    }
    return readSysMLModel(metamodel, ePackage, resource.getContents());
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

  private Model readSysMLModel(Metamodel metamodel, EPackage ePackage, Collection<EObject> eObjects) {
    Model model = new ModelImpl();
    Map<EObject, Node> nodes = readNodes(metamodel, ePackage, model, eObjects);
    readEdges(metamodel, ePackage, model, nodes);
    //TODO fix ports, i.e. flow directions?
    return model;
  }

  private Map<EObject, Node> readNodes(Metamodel metamodel, EPackage ePackage, Model model, Collection<EObject> eObjects) {
    Map<EObject, Node> nodes = Maps.newHashMap();
    //read blocks
    readNodesOfType(metamodel, ePackage, model, eObjects, nodes, BLOCK_MODULE, PSSIFCanonicMetamodelCreator.N_MODULE);
    readNodesOfType(metamodel, ePackage, model, eObjects, nodes, BLOCK_SOFTWARE, PSSIFCanonicMetamodelCreator.N_SOFTWARE);
    readNodesOfType(metamodel, ePackage, model, eObjects, nodes, BLOCK_ELECTRONIC, PSSIFCanonicMetamodelCreator.N_ELECTRONIC);
    readNodesOfType(metamodel, ePackage, model, eObjects, nodes, BLOCK_MECHANIC, PSSIFCanonicMetamodelCreator.N_HARDWARE);

    //read ports
    readNodesOfType(metamodel, ePackage, model, eObjects, nodes, PORT_ELECTRONIC, PSSIFCanonicMetamodelCreator.N_PORT_ELECTRONIC);
    readNodesOfType(metamodel, ePackage, model, eObjects, nodes, PORT_MECHANIC, PSSIFCanonicMetamodelCreator.N_PORT_MECHANIC);
    readNodesOfType(metamodel, ePackage, model, eObjects, nodes, PORT_SOFTWARE, PSSIFCanonicMetamodelCreator.N_PORT_SOFTWARE);

    //read functionalities
    readNodesOfType(metamodel, ePackage, model, eObjects, nodes, FUNCTIONALITY, PSSIFCanonicMetamodelCreator.N_FUNCTIONALITY);

    //read interface blocks and data types
    readInterfaceBlocksAndEeDataTypes(metamodel, ePackage, nodes);

    //e TODO
    return nodes;
  }

  private void readEdges(Metamodel metamodel, EPackage ePackage, Model model, Map<EObject, Node> nodes) {
    //TODO
    //read block2blockRelationships
    //read block2portRelationships
    //read block2functionalityRelationships
    //read port2functionalityRelatuionships

    //read relationship -> all eObjects, fromType, toType, EdgeType, eReferenceName, eClassName
  }

  private void readNodesOfType(Metamodel metamodel, EPackage ePackage, Model model, Collection<EObject> eObjects, Map<EObject, Node> nodes,
                               String eClassName, String nodeTypeName) {
    EClass eClass = getEClass(ePackage, eClassName);
    NodeTypeBase nodeType = getNodeType(metamodel, nodeTypeName);
    if (eClass == null || nodeType == null) {
      return;
    }
    Set<EObject> eInstances = getEInstances(eClass, eObjects);
    for (EObject eInstance : eInstances) {
      Node node = readEObject(nodeType, model, eInstance);
      if (node != null) {
        nodes.put(eInstance, node);
      }
    }
  }

  private Node readEObject(NodeTypeBase nodeType, Model model, EObject eObject) {
    Node node = nodeType.create(model);
    for (Attribute attribute : nodeType.getAttributes()) {
      EAttribute eAttribute = findEAttribute(eObject.eClass(), attribute);
      if (eAttribute != null) {
        Object eAttributeValue = eObject.eGet(eAttribute);
        if (eAttributeValue != null) {
          PSSIFValue pssifValue = internalizeEAttributeValue(eAttributeValue, eAttribute, attribute);
          if (pssifValue != null) {
            attribute.set(node, PSSIFOption.one(pssifValue));
          }
        }
      }
    }
    return node;
  }

  private void readInterfaceBlocksAndEeDataTypes(Metamodel metamodel, EPackage ePackage, Map<EObject, Node> nodes) {
    //TODO this one is special -> these are attributes of already read nodes, i.e. use both model and eObjects to read
    //alright, this one comes with a theory about how to handle it...
  }

  private Set<EObject> getEInstances(EClass eClass, Collection<EObject> eObjects) {
    Set<EObject> eInstances = Sets.newHashSet();
    for (EObject eObject : eObjects) {
      if (eClass.isInstance(eObject)) {
        eInstances.add(eObject);
      }
    }
    return eInstances;
  }

  private Set<EObject> writeSysMlModel(Metamodel metamodel, EPackage ePackage, Model model) {
    Set<EObject> eObjects = Sets.newHashSet();
    Map<Node, EObject> nodesMap = Maps.newHashMap();

    //mechanic blocks
    eObjects.addAll(writeNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_MECHANIC), getEClass(ePackage, BLOCK_MECHANIC), model, nodesMap,
        true));
    //electronics blocks
    eObjects.addAll(writeNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_ELECTRONIC), getEClass(ePackage, BLOCK_ELECTRONIC), model,
        nodesMap, true));
    //software blocks
    eObjects.addAll(writeNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_SOFTWARE), getEClass(ePackage, BLOCK_SOFTWARE), model, nodesMap,
        true));
    //modules
    eObjects.addAll(writeNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_MODULE), getEClass(ePackage, BLOCK_MODULE), model, nodesMap,
        true));

    //ports
    eObjects.addAll(writeNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_PORT_MECHANIC), getEClass(ePackage, PORT_MECHANIC), model,
        nodesMap, true));
    eObjects.addAll(writeNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_PORT_ELECTRONIC), getEClass(ePackage, PORT_ELECTRONIC), model,
        nodesMap, true));
    eObjects.addAll(writeNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_PORT_SOFTWARE), getEClass(ePackage, PORT_SOFTWARE), model,
        nodesMap, true));

    //functionalities
    eObjects.addAll(writeNodes(getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_FUNCTIONALITY), getEClass(ePackage, FUNCTIONALITY), model,
        nodesMap, true));

    //relationships
    writeBlock2BlockRelationships(metamodel, ePackage, model, nodesMap);
    writeBlock2PortRelationships(metamodel, ePackage, model, nodesMap);
    writePort2PortRelationships(metamodel, ePackage, model, nodesMap);
    writeBlock2FunctionalityRelationships(metamodel, ePackage, model, nodesMap);
    writePort2FunctionalityRelationships(metamodel, ePackage, model, nodesMap);

    eObjects.addAll(writeInterfaceBlocks(metamodel, ePackage, model, nodesMap));

    return eObjects;
  }

  private Set<EObject> writeInterfaceBlocks(Metamodel metamodel, EPackage ePackage, Model model, Map<Node, EObject> eObjects) {
    Set<EObject> createdEObjects = Sets.newHashSet();
    NodeTypeBase portType = getNodeType(metamodel, PSSIFCanonicMetamodelCreator.N_PORT);
    Attribute directionAttribute = portType.getAttribute(PSSIFCanonicMetamodelCreator.A_DIRECTION).getOne();
    Attribute dataTypeAttribute = portType.getAttribute(PSSIFCanonicMetamodelCreator.A_DATA_TYPE).getOne();

    for (Node portNode : portType.apply(model, true)) {
      EObject ePort = eObjects.get(portNode);
      if (ePort == null) {
        continue;
      }
      EClass interfaceBlockClass = getInterfaceBlockClass(ePackage, ePort);
      if (interfaceBlockClass == null) {
        continue;
      }
      EObject eInterfaceBlock = EcoreUtil.create(interfaceBlockClass);
      createdEObjects.add(eInterfaceBlock);
      ePort.eSet(ePort.eClass().getEStructuralFeature(OWNED_INTERFACE_BLOCK), eInterfaceBlock);
      EAttribute eDirection = findEAttribute(interfaceBlockClass, directionAttribute);
      PSSIFOption<PSSIFValue> directionValue = directionAttribute.get(portNode);
      if (eDirection != null && directionValue.isOne()) {
        //Note: FIXME XMI serialization contains only the one side in asymmetric cases.
        //Keep in mind when reconstructing from XMI.
        Object eValue = externalizeAttributeValue(directionValue.getOne(), directionAttribute, eDirection);
        eInterfaceBlock.eSet(eDirection, eValue);
      }

      PSSIFOption<PSSIFValue> dataTypeValue = dataTypeAttribute.get(portNode);
      if (dataTypeValue.isOne()) {
        if (INTERFACE_SOFTWARE.equals(interfaceBlockClass.getName())) {
          EAttribute eOperationName = findEAttribute(interfaceBlockClass, OPERATION_NAME);
          eInterfaceBlock.eSet(eOperationName, dataTypeValue.getOne().asString());
        }
        else {
          EClass eEeDataTypeClass = getEClass(ePackage, EE_DATA_TYPE);
          EAttribute eNameAttribute = findEAttribute(eEeDataTypeClass, PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
          EObject eEeDataTypeObject = EcoreUtil.create(eEeDataTypeClass);
          createdEObjects.add(eEeDataTypeObject);
          eEeDataTypeObject.eSet(eNameAttribute, dataTypeValue.getOne().asString());
          EReference eOwmedDataType = (EReference) interfaceBlockClass.getEStructuralFeature(OWMED_DATA_TYPE);
          eInterfaceBlock.eSet(eOwmedDataType, eEeDataTypeObject);
        }
      }
    }
    return createdEObjects;
  }

  private EClass getInterfaceBlockClass(EPackage ePackage, EObject eObject) {
    if (eObject.eClass().getName().equals(PORT_MECHANIC)) {
      return getEClass(ePackage, INTERFACE_MECHANIC);
    }
    else if (eObject.eClass().getName().equals(PORT_ELECTRONIC)) {
      return getEClass(ePackage, INTERFACE_ELECTRONIC);
    }
    else if (eObject.eClass().getName().equals(PORT_SOFTWARE)) {
      return getEClass(ePackage, INTERFACE_SOFTWARE);
    }
    else {
      return null;
    }
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

  private void writeBlock2FunctionalityRelationships(Metamodel metamodel, EPackage ePackage, Model model, Map<Node, EObject> eObjects) {
    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_FULFILLS, OWNED_FUNCTIONALITY, PSSIFCanonicMetamodelCreator.N_ELECTRONIC,
        PSSIFCanonicMetamodelCreator.N_FUNCTIONALITY, model, eObjects);
    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_FULFILLS, OWNED_FUNCTIONALITY, PSSIFCanonicMetamodelCreator.N_MECHANIC,
        PSSIFCanonicMetamodelCreator.N_FUNCTIONALITY, model, eObjects);
    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_FULFILLS, OWNED_FUNCTIONALITY, PSSIFCanonicMetamodelCreator.N_SOFTWARE,
        PSSIFCanonicMetamodelCreator.N_FUNCTIONALITY, model, eObjects);
    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_FULFILLS, OWNED_FUNCTIONALITY, PSSIFCanonicMetamodelCreator.N_MODULE,
        PSSIFCanonicMetamodelCreator.N_FUNCTIONALITY, model, eObjects);
  }

  private void writePort2FunctionalityRelationships(Metamodel metamodel, EPackage ePackage, Model model, Map<Node, EObject> eObjects) {
    writeRelationship(metamodel, ePackage, PSSIFCanonicMetamodelCreator.E_IS_MANDATORY_FOR, IS_MANDATORY_FOR, PSSIFCanonicMetamodelCreator.N_PORT,
        PSSIFCanonicMetamodelCreator.N_FUNCTIONALITY, model, eObjects);
  }

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

  private Set<EObject> writeNodes(NodeTypeBase nodeType, EClass eClass, Model model, Map<Node, EObject> nodesMap, boolean subtypes) {
    Set<EObject> eObjects = Sets.newHashSet();
    for (Node node : nodeType.apply(model, subtypes)) {
      EObject eObject = writeNode(nodeType, eClass, node);
      eObjects.add(eObject);
      nodesMap.put(node, eObject);
    }
    return eObjects;
  }

  private EObject writeNode(NodeTypeBase nodeType, EClass eClass, Node node) {
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
    if (EEnum.class.isInstance(eAttribute.getEType())) {
      return findLiteral((EEnum) eAttribute.getEType(), value.getValue().toString());
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

  private PSSIFValue internalizeEAttributeValue(Object eAttributeValue, EAttribute eAttribute, Attribute attribute) {
    if (eAttribute.getEType() instanceof EEnum) {
      try {
        return attribute.getType().fromObject(((EEnumLiteral) eAttributeValue).getName());
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
    else {
      try {
        return attribute.getType().fromObject(eAttributeValue);
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
  }

  private EEnumLiteral findLiteral(EEnum enumeration, String literalName) {
    for (EEnumLiteral literal : enumeration.getELiterals()) {
      if (literalName.trim().equalsIgnoreCase(literal.getName().trim())) {
        return literal;
      }
    }
    return null;
  }

  private EAttribute findEAttribute(EClass eClass, Attribute attribute) {
    return findEAttribute(eClass, attribute.getName());
  }

  private EAttribute findEAttribute(EClass eClass, String attributeName) {
    for (EStructuralFeature eFeature : eClass.getEAllStructuralFeatures()) {
      if (EAttribute.class.isInstance(eFeature) && attributeName.trim().equalsIgnoreCase(eFeature.getName().trim())) {
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
