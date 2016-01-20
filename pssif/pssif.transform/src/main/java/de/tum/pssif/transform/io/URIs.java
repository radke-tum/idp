package de.tum.pssif.transform.io;


public class URIs {
	// URIs
	public static final String baseUri = "http://www.sfb768.tum.de/voc/graph/ns";
	public static final String modelUri = "http://www.sfb768.tum.de/pssifmodel";

	public static final String namespace = baseUri.concat("#");
	public static final String uriBagNodes = modelUri.concat("/BagNodes");
	public static final String uriNode = modelUri.concat("/Node#");
	public static final String uriBagEdges = modelUri.concat("/BagEdges");
	public static final String uriEdge = modelUri.concat("/Edge#");
	public static final String uriBagJunctionNodes = modelUri
			.concat("/BagJunctionNodes");
	public static final String uriJunctionNode = modelUri.concat("/JunctionNode#");
	public static final String uriAttribute = modelUri.concat("/Attr#");
	public static final String uriNodeAttribute = modelUri.concat("/Node/Attr#");
	public static final String uriEdgeAttribute = modelUri.concat("/Edge/Attr#");
	public static final String uriJunctionNodeAttribute = modelUri
			.concat("/JunctionNode/Attr#");
	public static final String uriAnnotation = modelUri.concat("/Annot#");


	// Modelname
	public static final String modelname = "PSSIFModel";
}
