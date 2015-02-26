package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

// Attribute einer Klasse oder das "Assoziationsattribut" einer Klasse 

public class XmiOwnedAttribute extends XmiChildVisibility {

	// zusätzliche Attribute die XmiOwnedAttribute besitzt
	private String type;
	private String aggregation;
	private String association;
	private String lowerValue;
	private String upperValue;

	// Konstruktor: erbt von Oberklasse - Initialisierung der eigenen
	// Attribute
	public XmiOwnedAttribute(Attributes atts) {
		super(atts);
		this.type = atts.getValue(ATTRIBUTE_TYPE);
		this.aggregation = atts.getValue(ATTRIBUTE_AGGREGATION);
		this.association = atts.getValue(ATTRIBUTE_ASSOCIATION);
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAggregation() {
		return aggregation;
	}

	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}

	public String getAssociation() {
		return association;
	}

	public void setAssociation(String association) {
		this.association = association;
	}

	public String getLowerValue() {
		return lowerValue;
	}

	public void setLowerValue(Attributes atts) {
		this.lowerValue = atts.getValue(ATTRIBUTE_VALUE);
	}

	public String getUpperValue() {
		return upperValue;
	}
	
	public void setUpperValue(Attributes atts) {
		this.upperValue = atts.getValue(ATTRIBUTE_VALUE);
	}

}
