package de.tum.pssif.transform.mapper.sysml;

import org.junit.Test;

import de.tum.pssif.transform.graph.Graph;
import de.tum.pssif.transform.io.SysML4MechatronicsEmfMapper;


public class SysML4MechatronicsEmfIoMapperTest {

  //  private static final String ECORE_RESOURCE = "/sysml/sysml4mechatronics.ecore";

  @Test
  public void testWriteXMI2StdOut() {
    SysML4MechatronicsEmfMapper mapper = new SysML4MechatronicsEmfMapper();
    System.out.println("--Begin write XMI to Sysout--");
    mapper.write(new Graph(), System.out);
    System.out.println("--End write XMI to Sysout--");

  }

}
