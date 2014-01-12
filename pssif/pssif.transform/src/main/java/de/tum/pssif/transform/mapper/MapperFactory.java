package de.tum.pssif.transform.mapper;

import de.tum.pssif.core.exception.PSSIFException;
import de.tum.pssif.core.util.PSSIFUtil;
import de.tum.pssif.transform.mapper.graphml.GraphMlMapper;
import de.tum.pssif.transform.mapper.sysml.SysMlMapper;
import de.tum.pssif.transform.mapper.visio.VisioMapper;


public final class MapperFactory {

  public static final String GRAPHML = "graphml";
  public static final String SYSML   = "sysml";
  public static final String VISIO   = "visio";

  public static Mapper getMapper(String name) {
    if (PSSIFUtil.areSame(GRAPHML, name)) {
      return new GraphMlMapper();
    }
    else if (PSSIFUtil.areSame(SYSML, name)) {
      return new SysMlMapper();
    }
    else if (PSSIFUtil.areSame(VISIO, name)) {
      return new VisioMapper();
    }
    throw new PSSIFException("No mapper found for name: " + name);
  }

  private MapperFactory() {
    //Nothing here
  }

}
