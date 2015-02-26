package de.tum.pssif.xmi;

import java.util.Map;

import org.xml.sax.Attributes;

import de.tum.pssif.xmi.entities.XmiNode;

// das Zwischenformat zwischen der XMIDatei und dem Generischen Graphen
// um die Objekte flexibler nutzen zu können
public interface XmiGraph {
	
	public String getName();

	public void setName(String name);

	public Map<String, XmiNode> getXmiNodes();
	
	public void addXmiNode(XmiNode xmiNode);
	
	public void setAttributes(Attributes atts);
	
	public XmiNode findNode(String id);

}
