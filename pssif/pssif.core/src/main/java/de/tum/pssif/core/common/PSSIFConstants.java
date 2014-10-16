package de.tum.pssif.core.common;

public final class PSSIFConstants {

  public static final String ROOT_NODE_TYPE_NAME              = "Node";
  public static final String ROOT_EDGE_TYPE_NAME              = "Edge";

  public static final String DEFAULT_ATTRIBUTE_GROUP_NAME     = "defaultGroup";

  public static final String BUILTIN_ATTRIBUTE_ID             = "id";
  
  
  public static final String BUILTIN_ATTRIBUTE_GLOBAL_ID      = "globalId"; //Andreas
  

  public static final String BUILTIN_ATTRIBUTE_NAME           = "name";
  public static final String BUILTIN_ATTRIBUTE_VALIDITY_START = "validityStart";
  public static final String BUILTIN_ATTRIBUTE_VALIDITY_END   = "validityEnd";
  public static final String BUILTIN_ATTRIBUTE_VERSION        = "version";
  public static final String BUILTIN_ATTRIBUTE_COMMENT        = "comment";
  public static final String BUILTIN_ATTRIBUTE_ABS_LEVEL        = "Abstraction Level";

  public static final String BUILTIN_ATTRIBUTE_DIRECTED       = "directed";
  
  public static final String A_TEST_CASE_CONDITION_ATTRIBUTE 		   = "verified attribute";
  public static final String A_TEST_CASE_CONDITION_OP 				   = "operator";
  public static final String A_TEST_CASE_CONDITION_VALUE 			   = "value";

  public static final String ALIAS_ANNOTATION_KEY             = "PSSIF_aliased_key";

  private PSSIFConstants() {
    //Nop
  }

}
