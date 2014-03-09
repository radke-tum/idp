package de.tum.pssif.sysml4mechatronics.mapper;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;


public interface XmlStreamMapper<T, C extends XmlMappingContext> {

  MapperXmlIdentifier getMapperXmlIdentifier();

  MapperClassIdentifier getMapperClassIdentifier();

  T read(XMLStreamReader reader, C context);

  void write(XMLStreamWriter writer, T element, C context);

}
