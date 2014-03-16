package de.tum.pssif.sysml4mechatronics.mapper;

import java.util.Map;

import com.google.common.collect.Maps;


public class MappersRegistry {

  private final Map<MapperXmlIdentifier, XmlStreamMapper<?, ?>> mappersXml = Maps.newHashMap();

  //Enable on write
  //  private final Map<MapperClassIdentifier, XmlStreamMapper> mappersClass = Maps.newHashMap();

  public void register(XmlStreamMapper<?, ?> mapper) {
    this.mappersXml.put(mapper.getMapperXmlIdentifier(), mapper);
    //    this.mappersClass.put(mapper.getMapperClassIdentifier(), mapper);
  }

  public XmlStreamMapper<?, ?> resolve(MapperXmlIdentifier identifier) {
    return this.mappersXml.get(identifier);
  }

  public XmlStreamMapper<?, ?> resolve(Class<?> clazz) {
    throw new UnsupportedOperationException();
  }

}
