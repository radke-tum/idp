package de.tum.pssif.transform.mapper.sysml;

import org.junit.Test;

import de.tum.pssif.transform.graph.Graph;
import de.tum.pssif.transform.io.SysML4MechatronicsEmfIoMapper;


public class SysML4MechatronicsEmfIoMapperTest {

  //  private static final String ECORE_RESOURCE = "/sysml/sysml4mechatronics.ecore";

  @Test
  public void testWriteXMI2StdOut() {
    SysML4MechatronicsEmfIoMapper mapper = new SysML4MechatronicsEmfIoMapper();
    System.out.println("--Begin write XMI to Sysout--");
    mapper.write(new Graph(), System.out);
    System.out.println("--End write XMI to Sysout--");

  }

}
