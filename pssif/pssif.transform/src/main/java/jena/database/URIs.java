package jena.database;

public class URIs {
	// URIs
	// public static final String uri = "http://localhost:3030/PSSIF";
	public static final String uri = "http://vmkrcmar2.informatik.tu-muenchen.de:3030/PSSIF";
	public static final String namespace = uri.concat("#");

	public static final String uriBagNodes = namespace.concat("BagNodes");
	public static final String uriBagEdges = namespace.concat("BagEdges");
	public static final String uriBagJunctionNodes = namespace
			.concat("/BagJunctionNodes");

	public static final String uriNode = namespace.concat("Node/");
	public static final String uriEdge = namespace.concat("Edge/");
	public static final String uriJunctionNode = namespace
			.concat("JunctionNode/");

	public static final String uriAttribute = namespace.concat("Attr/");
	public static final String uriNodeAttribute = namespace
			.concat("Node/Attr/");
	public static final String uriEdgeAttribute = namespace
			.concat("Edge/Attr/");
	public static final String uriJunctionNodeAttribute = namespace
			.concat("JunctionNode/Attr/");
	public static final String uriNodeType = namespace.concat("Node/Type/");
	public static final String uriEdgeType = namespace.concat("Edge/Type/");
	public static final String uriJunctionNodeType = namespace
			.concat("JunctionNode/Type/");
	public static final String uriAnnotation = namespace.concat("Annot/");

	// Database Location
	public static final String location = "C:\\Users\\Andrea\\Documents\\Studium\\Master\\IDP\\Datenbankprojekt\\testPSSIFDB";
	// public static final String location = "C:\\Windows\\Temp\\JenaPSSIFDB";

	// Modelname
	public static final String modelname = "PSSIFModel";
}
