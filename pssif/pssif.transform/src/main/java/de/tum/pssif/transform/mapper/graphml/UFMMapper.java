package de.tum.pssif.transform.mapper.graphml;

import java.io.InputStream;
import java.io.OutputStream;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.transform.transformation.CreateArtificialEdgeTransformation;
import de.tum.pssif.transform.transformation.CreateArtificialNodeTransformation;
import de.tum.pssif.transform.transformation.HideEdgeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.HideEdgeTypeTransformation;
import de.tum.pssif.transform.transformation.HideNodeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.HideNodeTypeTransformation;
import de.tum.pssif.transform.transformation.JoinLeftOutgoingTransformation;
import de.tum.pssif.transform.transformation.JoinRightOutgoingTransformation;
import de.tum.pssif.transform.transformation.MoveAttributeTransformation;
import de.tum.pssif.transform.transformation.RenameEdgeTypeTransformation;
import de.tum.pssif.transform.transformation.RenameNodeTypeTransformation;


public class UFMMapper extends GraphMLMapper {
  private static final String E_MATERIAL_FLOW    = "MaterialFlow";
  private static final String E_ENERGY_FLOW      = "EnergyFlow";
  private static final String E_CONTROL_FLOW     = PSSIFCanonicMetamodelCreator.E_FLOW_CONTROL;
  private static final String E_INFORMATION_FLOW = "InformationFlow";
  private static final String A_COST             = PSSIFCanonicMetamodelCreator.A_BLOCK_COST;
  private static final String A_DURATION         = PSSIFCanonicMetamodelCreator.A_DURATION;
  private static final String A_PRIORITY         = PSSIFCanonicMetamodelCreator.A_REQUIREMENT_PRIORITY;
  private static final String A_TYPE             = PSSIFCanonicMetamodelCreator.A_REQUIREMENT_TYPE;
  private static final String A_WEIGHT           = PSSIFCanonicMetamodelCreator.A_HARDWARE_WEIGHT;
  private static final String A_FUNCTIONARY      = "functionary";
  private static final String N_STATE            = PSSIFCanonicMetamodelCreator.N_STATE;
  private static final String E_RELATIONSHIP     = PSSIFCanonicMetamodelCreator.E_RELATIONSHIP;
  private static final String N_BLOCK            = PSSIFCanonicMetamodelCreator.N_BLOCK;
  private static final String N_FUNCTION         = PSSIFCanonicMetamodelCreator.N_FUNCTION;
  private static final String N_REQUIREMENT      = PSSIFCanonicMetamodelCreator.N_REQUIREMENT;
  private static final String N_HARDWARE         = PSSIFCanonicMetamodelCreator.N_HARDWARE;

  @Override
  public Model read(Metamodel metamodel, InputStream inputStream) {
    return readInternal(createUfpView(metamodel), inputStream);
  }

  @Override
  public void write(Metamodel metamodel, Model model, OutputStream outputStream) {
    writeInternal(createUfpView(metamodel), model, outputStream, false);
  }

  public static Metamodel createUfpView(Metamodel metamodel) {
    Metamodel view = new RenameEdgeTypeTransformation(et(PSSIFCanonicMetamodelCreator.E_FLOW_INFORMATION, metamodel), E_INFORMATION_FLOW)
        .apply(metamodel);
    view = new RenameEdgeTypeTransformation(et(PSSIFCanonicMetamodelCreator.E_FLOW_ENERGY, view), E_ENERGY_FLOW).apply(view);
    view = new RenameEdgeTypeTransformation(et(PSSIFCanonicMetamodelCreator.E_FLOW_MATERIAL, view), E_MATERIAL_FLOW).apply(view);
    view = new RenameNodeTypeTransformation(nt(N_FUNCTION, view), "AbstractFunction").apply(view);
    view = new RenameNodeTypeTransformation(nt("Activity", view), N_FUNCTION).apply(view);

    //hide attributes
    view = new HideNodeTypeAttributeTransformation(node(view), node(view).getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT).getOne())
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(node(view), node(view).getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END).getOne())
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(node(view), node(view).getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START).getOne())
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(node(view), node(view).getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne())
        .apply(view);
    view = new HideEdgeTypeAttributeTransformation(edge(view), edge(view).getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT).getOne())
        .apply(view);
    view = new HideEdgeTypeAttributeTransformation(edge(view), edge(view).getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END).getOne())
        .apply(view);
    view = new HideEdgeTypeAttributeTransformation(edge(view), edge(view).getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START).getOne())
        .apply(view);
    view = new HideEdgeTypeAttributeTransformation(edge(view), edge(view).getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne())
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(nt(N_HARDWARE, view), nt(N_HARDWARE, view).getAttribute(A_WEIGHT).getOne()).apply(view);
    view = new HideNodeTypeAttributeTransformation(nt(N_REQUIREMENT, view), nt(N_REQUIREMENT, view).getAttribute(A_TYPE).getOne()).apply(view);
    view = new HideNodeTypeAttributeTransformation(nt(N_REQUIREMENT, view), nt(N_REQUIREMENT, view).getAttribute(A_PRIORITY).getOne()).apply(view);
    view = new HideNodeTypeAttributeTransformation(nt(N_FUNCTION, view), nt(N_FUNCTION, view).getAttribute(A_DURATION).getOne()).apply(view);
    view = new HideNodeTypeAttributeTransformation(nt(N_BLOCK, view), nt(N_BLOCK, view).getAttribute(A_COST).getOne()).apply(view);

    //get relationship mappings for future use
    EdgeType relationship = view.getEdgeType(E_RELATIONSHIP).getOne();
    ConnectionMapping s2b = relationship.getMapping(nt(N_STATE, view), nt(N_BLOCK, view)).getOne();
    ConnectionMapping f2b = relationship.getMapping(nt(N_FUNCTION, view), nt(N_BLOCK, view)).getOne();

    //create artificial blocks for function and state
    view = new CreateArtificialNodeTransformation(nt(N_FUNCTION, view), et(E_RELATIONSHIP, view), nt(N_BLOCK, view)).apply(view);
    view = new CreateArtificialNodeTransformation(nt(N_STATE, view), et(E_RELATIONSHIP, view), nt(N_BLOCK, view)).apply(view);

    //move functionary attribute as id and name attribute via relationship to connected blocks
    NodeType block = nt(N_BLOCK, view);
    Attribute id = block.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne();
    view = new MoveAttributeTransformation(nt(N_FUNCTION, view), A_FUNCTIONARY, block, id, et(E_RELATIONSHIP, view)).apply(view);
    Attribute name = block.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).getOne();
    view = new MoveAttributeTransformation(nt(N_FUNCTION, view), A_FUNCTIONARY, block, name, et(E_RELATIONSHIP, view)).apply(view);
    view = new MoveAttributeTransformation(nt(N_STATE, view), A_FUNCTIONARY, block, id, et(E_RELATIONSHIP, view)).apply(view);
    view = new MoveAttributeTransformation(nt(N_STATE, view), A_FUNCTIONARY, block, name, et(E_RELATIONSHIP, view)).apply(view);

    //join the informationflow to connect the artificial blocks
    view = joinToArtificialBlocks(E_INFORMATION_FLOW, s2b, f2b, view);
    view = joinToArtificialBlocks(E_ENERGY_FLOW, s2b, f2b, view);
    view = joinToArtificialBlocks(E_MATERIAL_FLOW, s2b, f2b, view);

    //create the artificial control flows
    view = new CreateArtificialEdgeTransformation(nt(N_STATE, view), nt(N_FUNCTION, view), et(E_INFORMATION_FLOW, view), et(E_CONTROL_FLOW, view))
        .apply(view);
    view = new CreateArtificialEdgeTransformation(nt(N_FUNCTION, view), nt(N_STATE, view), et(E_INFORMATION_FLOW, view), et(E_CONTROL_FLOW, view))
        .apply(view);

    //avoid instances of control flow within the viewed model
    //TODO deinstantify
    view = new HideEdgeTypeTransformation(et(E_CONTROL_FLOW, view)).apply(view);

    //avoid instances of block within the viewed model
    //TODO deinstantify
    view = new HideNodeTypeTransformation(block).apply(view);

    return view;
  }

  protected static Metamodel joinToArtificialBlocks(String etName, ConnectionMapping s2b, ConnectionMapping f2b, Metamodel view) {
    view = new JoinLeftOutgoingTransformation(et(etName, view), s2b, nt(N_STATE, view), nt(N_FUNCTION, view)).apply(view);
    view = new JoinLeftOutgoingTransformation(et(etName, view), f2b, nt(N_FUNCTION, view), nt(N_STATE, view)).apply(view);
    view = new JoinRightOutgoingTransformation(et(etName, view), s2b, nt(N_FUNCTION, view), nt(N_STATE, view)).apply(view);
    view = new JoinRightOutgoingTransformation(et(etName, view), f2b, nt(N_STATE, view), nt(N_FUNCTION, view)).apply(view);
    return view;
  }

  private static NodeType nt(String name, Metamodel metamodel) {
    return metamodel.getNodeType(name).getOne();
  }

  private static EdgeType et(String name, Metamodel metamodel) {
    return metamodel.getEdgeType(name).getOne();
  }

  private static NodeType node(Metamodel metamodel) {
    return nt(PSSIFConstants.ROOT_NODE_TYPE_NAME, metamodel);
  }

  private static EdgeType edge(Metamodel metamodel) {
    return et(PSSIFConstants.ROOT_EDGE_TYPE_NAME, metamodel);
  }
}
