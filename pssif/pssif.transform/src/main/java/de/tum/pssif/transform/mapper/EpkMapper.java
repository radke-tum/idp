package de.tum.pssif.transform.mapper;

import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.transform.ModelMapper;
import de.tum.pssif.transform.model.EpkModelMapper;


public class EpkMapper extends BaseVisioMapper {

  private static final String      EPK_TEMPLATE     = "/epk-data.vsdx";
  private static final Set<String> EPK_NODE_MASTERS = Sets.newHashSet("Event", "Function", "Organizational Unit", "Process path", "XOR", "OR", "AND",
                                                        "Information/ Material", "Main process", "Component", "Enterprise area", "Process group");
  private static final Set<String> EPK_EDGE_MASTERS = Sets.newHashSet("Dynamic connector");

  public EpkMapper() {
    super(EPK_TEMPLATE, EPK_NODE_MASTERS, EPK_EDGE_MASTERS);
  }

  @Override
  protected Metamodel getView(Metamodel metamodel) {
    return createEpkView(metamodel);
  }

  @Override
  protected ModelMapper getModelMapper() {
    return new EpkModelMapper();
  }

  public static Metamodel createEpkView(Metamodel metamodel) {
    //TODO
    return metamodel;
  }

}
