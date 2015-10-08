package de.tum.pssif.xmi;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.tum.pssif.xmi.entities.XmiActivityDiagramArgument;
import de.tum.pssif.xmi.entities.XmiActivityDiagramEdge;
import de.tum.pssif.xmi.entities.XmiActivityDiagramGuard;
import de.tum.pssif.xmi.entities.XmiActivityDiagramNode;
import de.tum.pssif.xmi.entities.XmiActivityDiagramWeight;
import de.tum.pssif.xmi.entities.XmiExtend;
import de.tum.pssif.xmi.entities.XmiExtensionPoint;
import de.tum.pssif.xmi.entities.XmiGeneralization;
import de.tum.pssif.xmi.entities.XmiInclude;
import de.tum.pssif.xmi.entities.XmiInterfaceRealization;
import de.tum.pssif.xmi.entities.XmiMemberEnd;
import de.tum.pssif.xmi.entities.XmiOwnedAttribute;
import de.tum.pssif.xmi.entities.XmiOwnedEnd;
import de.tum.pssif.xmi.entities.XmiOwnedOperation;
import de.tum.pssif.xmi.entities.XmiOwnedParameter;
import de.tum.pssif.xmi.entities.XmiPackagedElement;
import de.tum.pssif.xmi.entities.XmiPackagedElementAssociation;
import de.tum.pssif.xmi.entities.XmiPackagedElementUseCase;
import de.tum.pssif.xmi.entities.XmiSubject;
import de.tum.pssif.xmi.impl.XmiGraphImpl;

// zum parsen wird der SAX-Parser hergenommen
public class XmiParser implements ContentHandler, XmiConstants {

	// Temporary saved objects
	private XmiGraph xmiGraph;
	private XmiPackagedElement xmiPackagedElement;
	private XmiGeneralization xmiGeneralization;
	private XmiOwnedAttribute xmiOwnedAttribute;
	private XmiOwnedOperation xmiOwnedOperation;
	private XmiOwnedParameter xmiOwnedParameter;
	private XmiExtensionPoint xmiExtensionPoint;
	private XmiOwnedEnd xmiOwnedEnd;
	private XmiInterfaceRealization xmiInterfaceRealization;
	private XmiMemberEnd xmiMemberEnd;
	private XmiInclude xmiInclude;
	private XmiExtend xmiExtend;
	private XmiSubject xmiSubject;
	private XmiActivityDiagramNode xmiActivityDiagramNode;
	private XmiActivityDiagramEdge xmiActivityDiagramEdge;
	private XmiActivityDiagramWeight xmiActivityDiagramWeight;
	private XmiActivityDiagramArgument xmiActivityDiagramArgument;
	private XmiActivityDiagramGuard xmiActivityDiagramGuard;

	private boolean parsingFinished = false;
	private boolean inBody;

	private String bodyContent;

	// falls zum parsen ein Pfad angegeben wird, wird die parse(File) Methode
	// aufgerufen
	public static XmiGraph parse(String path) throws SAXException, IOException {
		File file = new File(path);
		return XmiParser.parse(file);
	}

	// falls zum parsen ein File angegeben wird, wird die parse(InputSoruce)
	// Methode aufgerufen
	public static XmiGraph parse(File file) throws SAXException, IOException {
		FileReader reader = new FileReader(file);
		InputSource inputSource = new InputSource(reader);
		return XmiParser.parse(inputSource);
	}

	// falls zum parsen ein InputStream angegeben wird, wird die
	// parse(InputSoruce) Methode aufgerufen
	public static XmiGraph parse(InputStream input) throws SAXException,
			IOException {
		InputSource inputSource = new InputSource(input);
		return XmiParser.parse(inputSource);
	}

	// parset die InputSurce und gibt einen xmiGraphen zurück
	private static XmiGraph parse(InputSource source) throws SAXException,
			IOException {
		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		XmiParser xmiParser = new XmiParser();
		xmlReader.setContentHandler(xmiParser);
		xmlReader.parse(source);

		return xmiParser.xmiGraph;
	}

	@Override
	public void startDocument() throws SAXException {
		// XMI Graph wird initalisiert
		this.xmiGraph = new XmiGraphImpl();
	}

	// wenn die tags der angegebenen konstanten im file geöffnet werden, werden
	// in den meisten Fällen xmiNodes mit den Dateninhalten erstellt und dem
	// xmiDocument hinzugefügt
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

		if (parsingFinished)
			return;

		if (qName.equalsIgnoreCase(TAG_UML_MODEL)) {
			xmiGraph.setAttributes(atts);

		} else if (qName.equalsIgnoreCase(TAG_PACKAGED_ELEMENT)) {
			xmiPackagedElement = XmiPackagedElement.create(atts);
			xmiGraph.addXmiNode(xmiPackagedElement);

		} else if (qName.equalsIgnoreCase(TAG_OWNED_ATTRIBUTE)) {
			xmiOwnedAttribute = new XmiOwnedAttribute(atts);
			xmiGraph.addXmiNode(xmiOwnedAttribute);

		} else if (qName.equalsIgnoreCase(TAG_OWNED_OPERATION)) {
			xmiOwnedOperation = new XmiOwnedOperation(atts);
			xmiGraph.addXmiNode(xmiOwnedOperation);

		} else if (qName.equalsIgnoreCase(TAG_EXTENSION_POINT)) {
			xmiExtensionPoint = new XmiExtensionPoint(atts);
			xmiGraph.addXmiNode(xmiExtensionPoint);

		} else if (qName.equalsIgnoreCase(TAG_GENERALIZATION)) {
			xmiGeneralization = new XmiGeneralization(atts);
			xmiGraph.addXmiNode(xmiGeneralization);

		} else if (qName.equalsIgnoreCase(TAG_OWNED_PARAMETER)) {
			xmiOwnedParameter = new XmiOwnedParameter(atts);
			xmiGraph.addXmiNode(xmiOwnedParameter);

		} else if (qName.equalsIgnoreCase(TAG_LOWER_VALUE)) {
			if (xmiOwnedAttribute != null) {
				xmiOwnedAttribute.setLowerValue(atts);
			} else if (xmiOwnedEnd != null) {
				xmiOwnedEnd.setLowerValue(atts);
			}

		} else if (qName.equalsIgnoreCase(TAG_UPPER_VALUE)) {
			if (xmiOwnedAttribute != null) {
				xmiOwnedAttribute.setUpperValue(atts);
			} else if (xmiOwnedEnd != null) {
				xmiOwnedEnd.setUpperValue(atts);
			}

		} else if (qName.equalsIgnoreCase(TAG_OWNED_END)) {
			xmiOwnedEnd = new XmiOwnedEnd(atts);
			xmiGraph.addXmiNode(xmiOwnedEnd);

		} else if (qName.equalsIgnoreCase(TAG_INTERFACE_REALIZATION)) {
			xmiInterfaceRealization = new XmiInterfaceRealization(atts);
			xmiGraph.addXmiNode(xmiInterfaceRealization);

		} else if (qName.equalsIgnoreCase(TAG_MEMBER_END)) {
			xmiMemberEnd = new XmiMemberEnd(atts);

		} else if (qName.equalsIgnoreCase(TAG_INCLUDE)) {
			xmiInclude = new XmiInclude(atts);
			xmiGraph.addXmiNode(xmiInclude);

		} else if (qName.equalsIgnoreCase(TAG_EXTEND)) {
			xmiExtend = new XmiExtend(atts);
			xmiGraph.addXmiNode(xmiExtend);

		} else if (qName.equalsIgnoreCase(TAG_CONDITION_BODY)) {
			inBody = true;

		} else if (qName.equalsIgnoreCase(TAG_SUBJECT)) {
			xmiSubject = new XmiSubject(atts);

		} else if (qName.equalsIgnoreCase(TAG_ACTIVITY_NODE)) {
			xmiActivityDiagramNode = XmiActivityDiagramNode.create(atts);
			xmiGraph.addXmiNode(xmiActivityDiagramNode);

		} else if (qName.equalsIgnoreCase(TAG_ACTIVITY_EDGE)) {
			xmiActivityDiagramEdge = XmiActivityDiagramEdge.create(atts);
			xmiGraph.addXmiNode(xmiActivityDiagramEdge);

		} else if (qName.equalsIgnoreCase(TAG_ACTIVITY_WEIGHT)) {
			xmiActivityDiagramWeight = new XmiActivityDiagramWeight(atts);
			xmiActivityDiagramEdge.setWeigth(xmiActivityDiagramWeight);

		} else if (qName.equalsIgnoreCase(TAG_ARGUMENT)) {
			xmiActivityDiagramArgument = new XmiActivityDiagramArgument(atts);
			xmiGraph.addXmiNode(xmiActivityDiagramArgument);

		} else if (qName.equalsIgnoreCase(TAG_GUARD)) {
			xmiActivityDiagramGuard = new XmiActivityDiagramGuard(atts);
			xmiGraph.addXmiNode(xmiActivityDiagramGuard);

		} else if (qName.equalsIgnoreCase(TAG_GUARDEXPR)) {
			xmiActivityDiagramGuard.setExpr(atts.getValue(ATTRIBUTE_VALUE));

		} else if (qName.equalsIgnoreCase(TAG_GUARDOPERAND)) {
			xmiActivityDiagramGuard.setOperand(atts.getValue(ATTRIBUTE_VALUE));

		}
	}

	// wenn die oben geöffneten tags wieder geschlossen werden, werden die
	// parent-IDs gesetzt
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (parsingFinished)
			return;

		if (qName.equalsIgnoreCase(TAG_UML_MODEL)) {
			parsingFinished = true;

		} else if (qName.equalsIgnoreCase(TAG_OWNED_ATTRIBUTE)) {
			xmiOwnedAttribute.setParentId(xmiPackagedElement.getXmiID());
			xmiOwnedAttribute = null;

		} else if (qName.equalsIgnoreCase(TAG_OWNED_OPERATION)) {
			xmiOwnedOperation.setParentId(xmiPackagedElement.getXmiID());
			xmiOwnedOperation = null;

		} else if (qName.equalsIgnoreCase(TAG_GENERALIZATION)) {
			xmiGeneralization.setParentId(xmiPackagedElement.getXmiID());
			xmiGeneralization = null;

		} else if (qName.equalsIgnoreCase(TAG_OWNED_PARAMETER)) {
			xmiOwnedParameter.setParentId(xmiOwnedOperation.getXmiID());
			xmiOwnedParameter = null;

		} else if (qName.equalsIgnoreCase(TAG_MEMBER_END)) {
			if (xmiPackagedElement instanceof XmiPackagedElementAssociation) {
				XmiPackagedElementAssociation association = (XmiPackagedElementAssociation) xmiPackagedElement;
				if (association.getMemberEndSource() == null) {
					association.setMemberEndSource(xmiMemberEnd);
				} else {
					association.setMemberEndTarget(xmiMemberEnd);
				}
			}

		} else if (qName.equalsIgnoreCase(TAG_OWNED_END)) {
			xmiOwnedEnd.setParentId(xmiPackagedElement.getXmiID());
			xmiOwnedEnd = null;

		} else if (qName.equalsIgnoreCase(TAG_INTERFACE_REALIZATION)) {
			xmiInterfaceRealization.setParentId(xmiPackagedElement.getXmiID());
			xmiInterfaceRealization = null;

		} else if (qName.equalsIgnoreCase(TAG_INCLUDE)) {
			xmiInclude.setParentId(xmiPackagedElement.getXmiID());
			xmiInclude = null;

		} else if (qName.equalsIgnoreCase(TAG_EXTEND)) {
			xmiExtend.setParentId(xmiPackagedElement.getXmiID());
			xmiExtend = null;

		} else if (qName.equalsIgnoreCase(TAG_EXTENSION_POINT)) {
			xmiExtensionPoint.setParentId(xmiPackagedElement.getXmiID());
			xmiExtensionPoint = null;

		} else if (qName.equalsIgnoreCase(TAG_CONDITION_BODY)) {
			inBody = false;

			if (xmiExtend instanceof XmiExtend) {
				xmiExtend.setConditionBody(bodyContent.toString());
			} else if (xmiActivityDiagramGuard instanceof XmiActivityDiagramGuard) {
				xmiActivityDiagramGuard.setBody(bodyContent.toString());
			}

		} else if (qName.equalsIgnoreCase(TAG_SUBJECT)) {
			if (xmiPackagedElement instanceof XmiPackagedElementUseCase) {
				XmiPackagedElementUseCase useCase = (XmiPackagedElementUseCase) xmiPackagedElement;
				useCase.setXmiSubject(xmiSubject);
			}

		} else if (qName.equalsIgnoreCase(TAG_ACTIVITY_WEIGHT)) {
			xmiActivityDiagramWeight = null;

		} else if (qName.equalsIgnoreCase(TAG_ARGUMENT)) {
			xmiActivityDiagramArgument.setParentId(xmiActivityDiagramNode
					.getXmiID());
			xmiExtensionPoint = null;

		} else if (qName.equalsIgnoreCase(TAG_GUARD)) {
			xmiActivityDiagramGuard.setParentId(xmiActivityDiagramEdge.getXmiID());

		}
	}

	// unused
	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (inBody) {
			bodyContent = new String(ch, start, length);
		}
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub

	}

}
