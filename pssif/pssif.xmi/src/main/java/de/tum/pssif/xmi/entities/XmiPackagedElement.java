package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

// beschreibt eine Klasse im Klassendiagramm
public class XmiPackagedElement extends XmiNode {

	// Konstruktor: erbt von der Oberklasse
	public XmiPackagedElement(Attributes atts) {
		super(atts);
	}

	// Unterteilungen der <packagedElement> Tags
	public static XmiPackagedElement create(Attributes atts) {
		if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(TYPE_CLASS)) {
			return new XmiPackagedElementClass(atts);
		} else if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
				TYPE_ASSOCIATION)) {
			return new XmiPackagedElementAssociation(atts);
		} else if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
				TYPE_USE_CASE)) {
			return new XmiPackagedElementUseCase(atts);
		} else if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
				TYPE_ACTOR)) {
			return new XmiPackagedElementActor(atts);
		} else if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
				TYPE_COMPONENT)) {
			return new XmiPackagedElementComponent(atts);
		} else if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
				TYPE_INTERFACE)) {
			return new XmiPackagedElementInterface(atts);
		}
		return new XmiPackagedElement(atts);
	}

}
