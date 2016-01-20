package jena.database;

public class URIs {
	// URIs
	private static final String uri = "http://localhost:3030/ds/PSSIF";
	public static final String namespace = uri.concat("#");
	public static final String uriBagNodes = uri.concat("/BagNodes");
	public static final String uriNode = uri.concat("/Node#");
	public static final String uriBagEdges = uri.concat("/BagEdges");
	public static final String uriEdge = uri.concat("/Edge#");
	public static final String uriBagJunctionNodes = uri
			.concat("/BagJunctionNodes");
	public static final String uriJunctionNode = uri.concat("/JunctionNode#");
	public static final String uriAttribute = uri.concat("/Attr#");
	public static final String uriNodeAttribute = uri.concat("/Node/Attr#");
	public static final String uriEdgeAttribute = uri.concat("/Edge/Attr#");
	public static final String uriJunctionNodeAttribute = uri
			.concat("/JunctionNode/Attr#");
	public static final String uriAnnotation = uri.concat("/Annot#");

	// Database Location
	public static final String location = "C:\\Users\\Andrea\\Documents\\Studium\\Master\\IDP\\Datenbankprojekt\\testPSSIFDB";

	// Modelname
	public static final String modelname = "PSSIFModel";
}
