package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

// die Parameter einer Methode im Klassendiagramm
public class XmiOwnedParameter extends XmiChildVisibility {

	// zusätzliche Attribute die XmiOwnedParameter besitzt
	private String direction;

	// Konstruktor: erbt von Oberklasse - Initialisierung der eigenen
	// Attribute
	public XmiOwnedParameter(Attributes atts) {
		super(atts);
		this.direction = atts.getValue(ATTRIBUTE_DIRECTION);
	}

	// bestimmen, ob der parameter ein rückgabeparameter oder ein
	// übergabeparameter ist
	public boolean isReturnParameter() {
		return this.direction != null
				&& this.direction.equalsIgnoreCase("return");
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "XmiOwnedParameter [direction="
				+ direction + ", xmiID=" + xmiID + ", xmiType=" + xmiType + "]";
	}

}
