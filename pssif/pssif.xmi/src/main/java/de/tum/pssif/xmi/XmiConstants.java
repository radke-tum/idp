package de.tum.pssif.xmi;

public interface XmiConstants {

	public String TAG_UML_MODEL = "uml:Model";
	public String TAG_PACKAGED_ELEMENT = "packagedElement";
	public String TAG_GENERALIZATION = "generalization";
	public String TAG_OWNED_ATTRIBUTE = "ownedAttribute";
	public String TAG_OWNED_OPERATION = "ownedOperation";
	public String TAG_OWNED_PARAMETER = "ownedParameter";
	public String TAG_OWNED_END = "ownedEnd";
	public String TAG_INTERFACE_REALIZATION = "interfaceRealization";
	public String TAG_MEMBER_END = "memberEnd";
	public String TAG_LOWER_VALUE = "lowerValue";
	public String TAG_UPPER_VALUE = "upperValue";
	public String TAG_TYPE = "type";
	public String TAG_INCLUDE = "include";
	public String TAG_EXTEND = "extend";
	public String TAG_EXTENSION_POINT = "extensionPoint";
	public String TAG_CONDITION_BODY = "body";
	public String TAG_SUBJECT = "subject";
	public String TAG_ACTIVITY_NODE= "node";
	public String TAG_ACTIVITY_EDGE = "edge";
	public String TAG_ACTIVITY_WEIGHT = "weight";
	public String TAG_INCOMING = "incoming";
	public String TAG_OUTGOING = "outgoing";
	public String TAG_ARGUMENT = "argument";
	public String TAG_GUARD = "guard";
	public String TAG_GUARDOPERAND = "operand";
	public String TAG_GUARDEXPR = "expr";
	
	public String ATTRIBUTE_XMI_ID = "xmi:id";
	public String ATTRIBUTE_XMI_IDREF = "xmi:idref";
	public String ATTRIBUTE_XMI_TYPE = "xmi:type";
	public String ATTRIBUTE_NAME = "name";
	public String ATTRIBUTE_VISIBILITY = "visibility";
	public String ATTRIBUTE_DIRECTION = "direction";
	public String ATTRIBUTE_ASSOCIATION = "association";
	public String ATTRIBUTE_TYPE = "type";
	public String ATTRIBUTE_VALUE = "value";
	public String ATTRIBUTE_HREF = "href";
	public String ATTRIBUTE_GENERAL = "general";
	public String ATTRIBUTE_AGGREGATION = "aggregation";
	public String ATTRIBUTE_CONTRACT = "contract";
	public String ATTRIBUTE_ADDITION = "addition";
	public String ATTRIBUTE_IS_ABSTRACT = "isAbstract";
	public String ATTRIBUTE_EXTENDED_CASE = "extendedCase";
	public String ATTRIBUTE_EXTENSION_POINT_NAME = "extensionPointName";
	public String ATTRIBUTE_SOURCE = "source";
	public String ATTRIBUTE_TARGET = "target";
	
	//Activity Diagram elements
	public String TYPE_CENTRAL_BUFFER_NODE = "uml:CentralBufferNode";
	public String TYPE_CALL_BEHAVIOUR_ACTION = "uml:CallBehaviorAction";
	public String TYPE_DECISION_NODE = "uml:DecisionNode";
	public String TYPE_MERGE_NODE = "uml:MergeNode";
	public String TYPE_FORK_NODE = "uml:ForkNode";
	public String TYPE_JOIN_NODE = "uml:JoinNode";
	public String TYPE_INITIAL_NODE = "uml:InitialNode";
	public String TYPE_FINAL_NODE = "uml:ActivityFinalNode";
	public String TYPE_OBJECT_FLOW_EDGE = "uml:ObjectFlow";
	public String TYPE_CONTROL_FLOW_EDGE = "uml:ControlFlow";
	
	public String TYPE_CLASS = "uml:Class";
	public String TYPE_ASSOCIATION = "uml:Association";
	public String TYPE_USE_CASE = "uml:UseCase";
	public String TYPE_ACTOR = "uml:Actor";
	public String TYPE_COMPONENT = "uml:Component";
	public String TYPE_INTERFACE = "uml:Interface";
	
	public static final String ELEMENT_ASSOCIATION_LOWERCARDINALITYSOURCE = "lowerCardinalitySource";
	public static final String ELEMENT_ASSOCIATION_UPPERCARDINALITYSOURCE = "upperCardinalitySource";
	public static final String ELEMENT_ASSOCIATION_LOWERCARDINALITYDESTINATION = "lowerCardinalityDestination";
	public static final String ELEMENT_ASSOCIATION_UPPERCARDINALITYDESTINATION = "upperCardinalityDestination";

}
