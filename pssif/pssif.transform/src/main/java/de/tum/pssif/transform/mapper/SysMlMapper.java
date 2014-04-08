package de.tum.pssif.transform.mapper;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.transform.IoMapper;
import de.tum.pssif.transform.Mapper;
import de.tum.pssif.transform.ModelMapper;
import de.tum.pssif.transform.io.SysML4MechatronicsEmfIoMapper;
import de.tum.pssif.transform.model.SysMl4MechatronicsModelMapper;


public class SysMlMapper extends AbstractMapper implements Mapper {

  @Override
  protected Metamodel getView(Metamodel metamodel) {
    // TODO Auto-generated method stub
    return metamodel;
  }

  @Override
  protected ModelMapper getModelMapper() {
    // TODO Auto-generated method stub
    return new SysMl4MechatronicsModelMapper();
  }

  @Override
  protected IoMapper getIoMapper() {
    return new SysML4MechatronicsEmfIoMapper();
  }

}
