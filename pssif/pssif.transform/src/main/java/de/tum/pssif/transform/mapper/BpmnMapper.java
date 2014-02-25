package de.tum.pssif.transform.mapper;

import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.transform.ModelMapper;
import de.tum.pssif.transform.model.BpmnModelMapper;


public class BpmnMapper extends BaseVisioMapper {

  //TODO
  private static final String      BPMN_TEMPLATE     = "";
  private static final Set<String> BPMN_NODE_MASTERS = Sets.newHashSet();
  private static final Set<String> BPMN_EDGE_MASTERS = Sets.newHashSet();

  public BpmnMapper() {
    super(BPMN_TEMPLATE, BPMN_NODE_MASTERS, BPMN_EDGE_MASTERS);
  }

  @Override
  protected Metamodel getView(Metamodel metamodel) {
    // TODO Auto-generated method stub
    return metamodel;
  }

  @Override
  protected ModelMapper getModelMapper() {
    // TODO Auto-generated method stub
    return new BpmnModelMapper();
  }

}
