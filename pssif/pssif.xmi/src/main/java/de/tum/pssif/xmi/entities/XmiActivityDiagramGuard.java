package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

public class XmiActivityDiagramGuard extends XmiChild {
	
	private String value;
	private String body;
	private String operand;
	private String expr;

	public XmiActivityDiagramGuard(Attributes atts) {
		super(atts);
		this.value = atts.getValue(ATTRIBUTE_VALUE);
		this.body = atts.getValue(TAG_CONDITION_BODY);
		this.expr = atts.getValue(TAG_GUARDEXPR);
		this.operand = atts.getValue(TAG_GUARDOPERAND);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOperand() {
		return operand;
	}

	public void setOperand(String operand) {
		this.operand = operand;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}
	
	

}
