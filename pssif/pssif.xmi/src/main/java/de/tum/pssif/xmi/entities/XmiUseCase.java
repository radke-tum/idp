//package de.tum.pssif.xmi.entities;
//
//import org.xml.sax.Attributes;
//
//import de.tum.pssif.xmi.XmiConstants;
//
//// Tag <usecase> die nur in einem XmiPackagedElementComponent vorkommen kann
//// es beschriebt die Use-Cases die ein System beinhaltet
//public class XmiUseCase implements XmiConstants {
//
//	private String idref;
//
//	public XmiUseCase(Attributes atts) {
//		this.idref = atts.getValue(ATTRIBUTE_XMI_IDREF);
//	}
//
//	public String getIdref() {
//		return idref;
//	}
//
//	public void setIdref(String idref) {
//		this.idref = idref;
//	}
//
//	@Override
//	public String toString() {
//		return "\n\tXmiUseCase [idref=" + idref + "]";
//	}
//
//}
