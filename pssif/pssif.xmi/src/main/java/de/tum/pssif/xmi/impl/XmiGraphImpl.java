package de.tum.pssif.xmi.impl;

import java.util.Map;

import org.xml.sax.Attributes;

import com.google.common.collect.Maps;

import de.tum.pssif.xmi.XmiConstants;
import de.tum.pssif.xmi.XmiGraph;
import de.tum.pssif.xmi.entities.XmiNode;

//das Zwischenformat zwischen der XMIDatei und dem Generischen Graphen
//um die Objekte flexibler nutzen zu können

public class XmiGraphImpl implements XmiGraph {

	private String name;

	// Hashmap mit den Key-Value Werten von einem String und einem XmiNode
	private final Map<String, XmiNode> xmiNodes;

	// Konstruktor: Hashmap wir initialisisert
	public XmiGraphImpl() {
		this.xmiNodes = Maps.newHashMap();
	}
	
	// Konstruktor: Hashmap und Attribut "Name" werden initialisisert
	public XmiGraphImpl(Attributes atts) {
		this.name = atts.getValue(XmiConstants.ATTRIBUTE_NAME);
		this.xmiNodes = Maps.newHashMap();
	}

	public void setAttributes(Attributes atts) {
		this.name = atts.getValue(XmiConstants.ATTRIBUTE_NAME);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, XmiNode> getXmiNodes() {
		return xmiNodes;
	}

	// der Hashmap wird ein Key-Value Paar hinzugefügt
	@Override
	public void addXmiNode(XmiNode xmiNode) {
		this.xmiNodes.put(xmiNode.getXmiID(), xmiNode);
	}

	// XmiNode finden
	public XmiNode findNode(String id) {
		return this.xmiNodes.get(id);
	}

	@Override
	public String toString() {
		return "XmiModel [name=" + name + ", xmiNodes=" + xmiNodes + "]";
	}

}
