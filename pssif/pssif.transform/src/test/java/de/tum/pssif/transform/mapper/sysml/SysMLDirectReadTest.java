package de.tum.pssif.transform.mapper.sysml;

import org.junit.Test;

import de.tum.pssif.transform.io.SysMlIoMapper;


public class SysMLDirectReadTest {

  @Test
  public void testReadSysML() {
    SysMlIoMapper mapper = new SysMlIoMapper();
    mapper.read(getClass().getResourceAsStream("/sysml/AlleElemente.xml"));
  }

}
