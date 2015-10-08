package jena.database;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

import de.tum.pssif.core.common.PSSIFConstants;

public class Properties {
	private static Model m = ModelFactory.createDefaultModel();

	// // Properties of Attributes
	public static final Property PROP_ATTR_ID = m
			.createProperty(URIs.uriAttribute
					+ PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
	// public static final Property PROP_ATTR_NAME = m
	// .createProperty(URIs.uriAttribute
	// + PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
	// public static final Property PROP_ATTR_VALIDITY_START = m
	// .createProperty(URIs.uriAttribute
	// + PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START);
	// public static final Property PROP_ATTR_VALIDITY_END = m
	// .createProperty(URIs.uriAttribute
	// + PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END);
	// public static final Property PROP_ATTR_VERSION = m
	// .createProperty(URIs.uriAttribute
	// + PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION);
	// public static final Property PROP_ATTR_COMMENT = m
	// .createProperty(URIs.uriAttribute
	// + PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT);
	// public static final Property PROP_ATTR_ABS_LEVEL = m
	// .createProperty(URIs.uriAttribute
	// + PSSIFConstants.BUILTIN_ATTRIBUTE_ABS_LEVEL);
	// public static final Property PROP_ATTR_DIRECTED = m
	// .createProperty(URIs.uriAttribute
	// + PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED);
	// public static final Property Prop_ATTR_ALIAS = m
	// .createProperty(URIs.uriAttribute
	// + PSSIFConstants.ALIAS_ANNOTATION_KEY);
	public static final Property Prop_ATTR_GLOBAL_ID = m
			.createProperty(URIs.uriAttribute
					+ PSSIFConstants.BUILTIN_ATTRIBUTE_GLOBAL_ID);
	// public static final Property Prop_ATTR_GROUP_NAME = m
	// .createProperty(URIs.uriAttribute
	// + PSSIFConstants.DEFAULT_ATTRIBUTE_GROUP_NAME);
	// public static final Property Prop_ATTR_ROOT_EDGE = m
	// .createProperty(URIs.uriAttribute
	// + PSSIFConstants.ROOT_EDGE_TYPE_NAME);
	// public static final Property Prop_ATTR_ROOT_NODE = m
	// .createProperty(URIs.uriAttribute
	// + PSSIFConstants.ROOT_NODE_TYPE_NAME);

	// Just needed if Value, Datatype and Unit of an Attribute are stored
	public static final Property PROP_ATTR_VALUE = m
			.createProperty(URIs.uriAttribute.concat("VALUE"));
	public static final Property PROP_ATTR_UNIT = m
			.createProperty(URIs.uriAttribute.concat("UNIT"));
	public static final Property PROP_ATTR_DATATYPE = m
			.createProperty(URIs.uriAttribute.concat("DATATYPE"));
	public static final Property PROP_ATTR_CATEGORY = m
			.createProperty(URIs.uriAttribute.concat("CATEGORY"));

	// Property of Type
	public static final Property PROP_TYPE = m.createProperty(URIs.namespace
			.concat("Type"));

	// Properties of in and out going nodes
	public static final Property PROP_NODE_IN = m.createProperty(URIs.namespace
			.concat("in"));
	public static final Property PROP_NODE_OUT = m
			.createProperty(URIs.namespace.concat("out"));

	// Property of Bags
	public static final Property PROP_BAG = m
			.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#BagElement");
}
