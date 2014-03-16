package de.tum.pssif.sysml4mechatronics;

import de.tum.pssif.sysml4mechatronics.mapper.SFB768MapperImpl;


public final class SFB769MapperFactory {

  public static SFB769MapperFactory INSTANCE = new SFB769MapperFactory();

  private SFB769MapperFactory() {
    //NOOP
  }

  public SFB768Mapper create() {
    return new SFB768MapperImpl();
  }

}
