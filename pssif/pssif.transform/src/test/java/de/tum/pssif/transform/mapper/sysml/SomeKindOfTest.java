package de.tum.pssif.transform.mapper.sysml;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.UUID;

import org.junit.Test;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.transform.mapper.SysMlMapper;


public class SomeKindOfTest {

  @Test
  public void pretendToTest() {
    Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
    Model model = getModel();
    SysMlMapper mapper = new SysMlMapper();
    try {
      mapper.write(metamodel, model, new FileOutputStream(new File("C:" + File.separator + "Dev" + File.separator + "sysml.xmi")));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail("NOPE!");
    }
  }

  private Model getModel() {
    Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
    Model model = new ModelImpl();

    NodeTypeBase mechBlock = metamodel.getBaseNodeType(PSSIFCanonicMetamodelCreator.N_MECHANIC).getOne();
    Attribute mechName = mechBlock.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).getOne();
    Attribute mechVersion = mechBlock.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne();

    NodeTypeBase elBlock = metamodel.getBaseNodeType(PSSIFCanonicMetamodelCreator.N_ELECTRONIC).getOne();
    Attribute elName = mechBlock.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).getOne();
    Attribute elVersion = mechBlock.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne();

    NodeTypeBase softBlock = metamodel.getBaseNodeType(PSSIFCanonicMetamodelCreator.N_SOFTWARE).getOne();
    Attribute softName = mechBlock.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).getOne();
    Attribute softVersion = mechBlock.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne();

    NodeTypeBase moduleBlock = metamodel.getBaseNodeType(PSSIFCanonicMetamodelCreator.N_MODULE).getOne();
    Attribute moduleName = moduleBlock.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).getOne();
    Attribute moduleVersion = moduleBlock.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne();

    Node mechBlock1 = mechBlock.create(model);
    setId(mechBlock1);
    mechVersion.set(mechBlock1, poPv(BigInteger.valueOf(1)));
    mechName.set(mechBlock1, poPv("Mechanical Block 1"));
    Node mechBlock2 = mechBlock.create(model);
    setId(mechBlock2);
    mechVersion.set(mechBlock2, poPv(BigInteger.valueOf(1)));
    mechName.set(mechBlock2, poPv("Mechanical Block 2"));
    Node mechBlock3 = mechBlock.create(model);
    setId(mechBlock3);
    mechVersion.set(mechBlock3, poPv(BigInteger.valueOf(1)));
    mechName.set(mechBlock3, poPv("Mechanical Block 3"));

    Node elBlock1 = elBlock.create(model);
    setId(elBlock1);
    elVersion.set(elBlock1, poPv(BigInteger.valueOf(1)));
    elName.set(elBlock1, poPv("Electronic Block 1"));
    Node elBlock2 = elBlock.create(model);
    setId(elBlock2);
    elVersion.set(elBlock2, poPv(BigInteger.valueOf(1)));
    elName.set(elBlock2, poPv("Electronic Block 2"));
    Node elBlock3 = elBlock.create(model);
    setId(elBlock3);
    elVersion.set(elBlock3, poPv(BigInteger.valueOf(1)));
    elName.set(elBlock3, poPv("Electronic Block 3"));

    Node softBlock1 = softBlock.create(model);
    setId(softBlock1);
    softVersion.set(softBlock1, poPv(BigInteger.valueOf(1)));
    softName.set(softBlock1, poPv("Software Block 1"));
    Node softBlock2 = softBlock.create(model);
    setId(softBlock2);
    softVersion.set(softBlock2, poPv(BigInteger.valueOf(1)));
    softName.set(softBlock2, poPv("Software Block 2"));
    Node softBlock3 = softBlock.create(model);
    setId(softBlock3);
    softVersion.set(softBlock3, poPv(BigInteger.valueOf(1)));
    softName.set(softBlock3, poPv("Software Block 3"));

    Node moduleBlock1 = moduleBlock.create(model);
    setId(moduleBlock1);
    moduleVersion.set(moduleBlock1, poPv(BigInteger.valueOf(1)));
    moduleName.set(moduleBlock1, poPv("Module 1"));
    Node moduleBlock2 = moduleBlock.create(model);
    setId(moduleBlock2);
    moduleVersion.set(moduleBlock2, poPv(BigInteger.valueOf(1)));
    moduleName.set(moduleBlock2, poPv("Module 2"));

    EdgeType containment = metamodel.getEdgeType(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS).getOne();
    ConnectionMapping mod2SoftCM = containment.getMapping(moduleBlock, softBlock).getOne();
    ConnectionMapping mod2MechCM = containment.getMapping(moduleBlock, mechBlock).getOne();
    ConnectionMapping mod2ElCM = containment.getMapping(moduleBlock, elBlock).getOne();

    mod2SoftCM.create(model, moduleBlock1, softBlock1);
    mod2SoftCM.create(model, moduleBlock1, softBlock2);
    mod2MechCM.create(model, moduleBlock1, mechBlock1);
    mod2MechCM.create(model, moduleBlock1, mechBlock2);
    mod2ElCM.create(model, moduleBlock1, elBlock1);

    mod2SoftCM.create(model, moduleBlock2, softBlock3);
    mod2MechCM.create(model, moduleBlock2, mechBlock3);
    mod2ElCM.create(model, moduleBlock2, elBlock2);
    mod2ElCM.create(model, moduleBlock2, elBlock3);

    NodeTypeBase mechPort = metamodel.getBaseNodeType(PSSIFCanonicMetamodelCreator.N_PORT_MECHANIC).getOne();
    NodeTypeBase elPort = metamodel.getBaseNodeType(PSSIFCanonicMetamodelCreator.N_PORT_ELECTRONIC).getOne();
    NodeTypeBase softPort = metamodel.getBaseNodeType(PSSIFCanonicMetamodelCreator.N_PORT_SOFTWARE).getOne();
    Attribute portNameAttribute = metamodel.getBaseNodeType(PSSIFCanonicMetamodelCreator.N_PORT).getOne()
        .getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).getOne();
    Attribute portConjugated = metamodel.getBaseNodeType(PSSIFCanonicMetamodelCreator.N_PORT).getOne()
        .getAttribute(PSSIFCanonicMetamodelCreator.A_CONJUGATED).getOne();

    ConnectionMapping mechBlock2MechPort = containment.getMapping(mechBlock, mechPort).getOne();
    ConnectionMapping elBlock2elPort = containment.getMapping(elBlock, elPort).getOne();
    ConnectionMapping elBlock2softPort = containment.getMapping(elBlock, softPort).getOne();
    ConnectionMapping elBlock2mechPort = containment.getMapping(elBlock, mechPort).getOne();
    ConnectionMapping softBlock2SoftPort = containment.getMapping(softBlock, softPort).getOne();

    Node mechPort1 = mechPort.create(model);
    setId(mechPort1);
    portNameAttribute.set(mechPort1, poPv("Mechanical Port 1"));

    Node mechPort2 = mechPort.create(model);
    setId(mechPort2);
    portNameAttribute.set(mechPort2, poPv("Mechanical Port 2"));

    Node elPort1 = elPort.create(model);
    setId(elPort1);
    portNameAttribute.set(elPort1, poPv("Electrical Port 1"));
    portConjugated.set(elPort1, poPv(Boolean.valueOf(true)));

    Node elPort2 = elPort.create(model);
    setId(elPort2);
    portNameAttribute.set(elPort2, poPv("Electrical Port 2"));
    portConjugated.set(elPort2, poPv(Boolean.valueOf(false)));

    Node softPort1 = softPort.create(model);
    setId(softPort1);
    portNameAttribute.set(softPort1, poPv("Software Port 1"));

    Node softPort2 = softPort.create(model);
    setId(softPort2);
    portNameAttribute.set(softPort2, poPv("Software Port 2"));

    mechBlock2MechPort.create(model, mechBlock1, mechPort1);
    elBlock2mechPort.create(model, elBlock1, mechPort2);
    elBlock2elPort.create(model, elBlock2, elPort1);
    elBlock2elPort.create(model, elBlock1, elPort2);
    elBlock2softPort.create(model, elBlock2, softPort2);
    softBlock2SoftPort.create(model, softBlock1, softPort1);

    EdgeType isConnectedTo = metamodel.getEdgeType(PSSIFCanonicMetamodelCreator.E_IS_CONNECTED_TO).getOne();
    ConnectionMapping elIsConnectedTo = isConnectedTo.getMapping(elPort, elPort).getOne();
    ConnectionMapping mechIsConnectedTo = isConnectedTo.getMapping(mechPort, mechPort).getOne();
    ConnectionMapping softIsConnectedTo = isConnectedTo.getMapping(softPort, softPort).getOne();

    elIsConnectedTo.create(model, elPort1, elPort2);
    mechIsConnectedTo.create(model, mechPort1, mechPort2);
    softIsConnectedTo.create(model, softPort1, softPort2);

    return model;
  }

  private static PSSIFOption<PSSIFValue> poPv(Object object) {
    return PSSIFOption.one(PSSIFValue.create(object));
  }

  private static void setId(Element element) {
    element.setId(UUID.randomUUID().toString());
  }

}
