package de.tum.pssif.transform;

import de.tum.pssif.core.common.PSSIFUtil;
import de.tum.pssif.core.exception.PSSIFException;
import de.tum.pssif.transform.mapper.BpmnMapper;
import de.tum.pssif.transform.mapper.EpkMapper;
import de.tum.pssif.transform.mapper.SysMlMapper;
import de.tum.pssif.transform.mapper.graphml.PssifMapper;
import de.tum.pssif.transform.mapper.graphml.UfpMapper;


public final class MapperFactory {

  /**
   * Umsatzorientierte Funktionsplanung.
   */
  public static final String UOFP  = "uofp";

  /**
   * SysML.
   */
  public static final String SYSML = "sysml";

  /**
   * EPK.
   */
  public static final String EPK   = "epk";

  /**
   * BPMN.
   */
  public static final String BPMN  = "bpmn";

  /**
   * PSSIF.
   */
  public static final String PSSIF = "PSSIF";

  public static Mapper getMapper(String name) {
    if (PSSIFUtil.areSame(UOFP, name)) {
      return new UfpMapper();
    }
    else if (PSSIFUtil.areSame(SYSML, name)) {
      return new SysMlMapper();
    }
    else if (PSSIFUtil.areSame(EPK, name)) {
      return new EpkMapper();
    }
    else if (PSSIFUtil.areSame(BPMN, name)) {
      return new BpmnMapper();
    }
    else if (PSSIFUtil.areSame(PSSIF, name)) {
      return new PssifMapper();
    }
    throw new PSSIFException("No mapper found for name: " + name);
  }

  private MapperFactory() {
    //Nothing here
  }

}
