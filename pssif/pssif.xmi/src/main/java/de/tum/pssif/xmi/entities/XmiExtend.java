package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

public class XmiExtend extends XmiChild {
	
	private String extendedCase;
	private String conditionBody;


	public XmiExtend(Attributes atts) {
		super(atts);
		this.extendedCase = atts.getValue(ATTRIBUTE_EXTENDED_CASE);
	}

	public String getExtendedCase() {
		return extendedCase;
	}

	public void setExtendedCase(String extendedCase) {
		this.extendedCase = extendedCase;
	}

	public String getConditionBody() {
		return conditionBody;
	}

	public void setConditionBody(String conditionBody) {
		this.conditionBody = conditionBody;
	}

	@Override
	public String toString() {
		return "XmiExtend [extendedCase=" + extendedCase + ", conditionBody="
				+ conditionBody + "]";
	}

}
