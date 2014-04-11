package de.tum.pssif.transform.mapper.sysml;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;

import org.junit.Test;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
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

    Node mechBlock1 = mechBlock.create(model);
    mechVersion.set(mechBlock1, poPv(BigInteger.valueOf(1)));
    mechName.set(mechBlock1, poPv("Mechanical Block 1"));
    Node mechBlock2 = mechBlock.create(model);
    mechVersion.set(mechBlock2, poPv(BigInteger.valueOf(1)));
    mechName.set(mechBlock2, poPv("Mechanical Block 2"));
    Node mechBlock3 = mechBlock.create(model);
    mechVersion.set(mechBlock3, poPv(BigInteger.valueOf(1)));
    mechName.set(mechBlock3, poPv("Mechanical Block 3"));

    Node elBlock1 = elBlock.create(model);
    elVersion.set(elBlock1, poPv(BigInteger.valueOf(1)));
    elName.set(elBlock1, poPv("Electronic Block 1"));
    Node elBlock2 = elBlock.create(model);
    elVersion.set(elBlock2, poPv(BigInteger.valueOf(1)));
    elName.set(elBlock2, poPv("Electronic Block 2"));
    Node elBlock3 = elBlock.create(model);
    elVersion.set(elBlock3, poPv(BigInteger.valueOf(1)));
    elName.set(elBlock3, poPv("Electronic Block 3"));

    Node softBlock1 = softBlock.create(model);
    softVersion.set(softBlock1, poPv(BigInteger.valueOf(1)));
    softName.set(softBlock1, poPv("Software Block 1"));
    Node softBlock2 = softBlock.create(model);
    softVersion.set(softBlock2, poPv(BigInteger.valueOf(1)));
    softName.set(softBlock2, poPv("Software Block 2"));
    Node softBlock3 = softBlock.create(model);
    softVersion.set(softBlock3, poPv(BigInteger.valueOf(1)));
    softName.set(softBlock3, poPv("Software Block 3"));

    return model;
  }

  private static PSSIFOption<PSSIFValue> poPv(Object object) {
    return PSSIFOption.one(PSSIFValue.create(object));
  }

}
