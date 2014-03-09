package de.tum.pssif.transform.mapper;

import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.transform.ModelMapper;
import de.tum.pssif.transform.model.EpkModelMapper;
import de.tum.pssif.transform.transformation.AliasJunctionNodeTypeTransformation;
import de.tum.pssif.transform.transformation.AliasNodeTypeTransformation;
import de.tum.pssif.transform.transformation.RenameEdgeTypeTransformation;
import de.tum.pssif.transform.transformation.RenameNodeTypeTransformation;


public class EpkMapper extends BaseVisioMapper {

  private static final String      EPK_TEMPLATE     = "/epk-data.vsdx";
  private static final Set<String> EPK_NODE_MASTERS = Sets.newHashSet("Event", "Function", "Organizational unit", "Process path", "XOR", "OR", "AND",
                                                        "Information/ Material", "Main process", "Component", "Enterprise area", "Process group");
  private static final Set<String> EPK_EDGE_MASTERS = Sets.newHashSet("Dynamic connector");

  public EpkMapper() {
    super(EPK_TEMPLATE, EPK_NODE_MASTERS, EPK_EDGE_MASTERS);
  }

  @Override
  protected Metamodel getView(Metamodel metamodel) {
    return createEpkView(metamodel);
  }

  @Override
  protected ModelMapper getModelMapper() {
    return new EpkModelMapper();
  }

  private static Metamodel createEpkView(Metamodel metamodel) {
    Metamodel view = new RenameEdgeTypeTransformation(metamodel.getEdgeType("Control Flow").getOne(), "Dynamic connector").apply(metamodel);
    view = new RenameNodeTypeTransformation(view.getNodeType("Event").getOne(), "Dev Event").apply(view);
    view = new RenameNodeTypeTransformation(view.getNodeType("Function").getOne(), "Abstract Function").apply(view);
    view = new RenameNodeTypeTransformation(view.getNodeType("Activity").getOne(), "Function").apply(view);
    view = new RenameNodeTypeTransformation(view.getNodeType("State").getOne(), "Event").apply(view);
    view = new AliasNodeTypeTransformation(view.getNodeType("Block").getOne(), "Information/ Material").apply(view);
    view = new AliasNodeTypeTransformation(view.getNodeType("Block").getOne(), "Organizational unit").apply(view);
    view = new AliasJunctionNodeTypeTransformation(view.getJunctionNodeType(PSSIFCanonicMetamodelCreator.N_CONJUNCTION).getOne(), "XOR").apply(view);
    view = new AliasJunctionNodeTypeTransformation(view.getJunctionNodeType(PSSIFCanonicMetamodelCreator.N_CONJUNCTION).getOne(), "AND").apply(view);
    view = new AliasJunctionNodeTypeTransformation(view.getJunctionNodeType(PSSIFCanonicMetamodelCreator.N_CONJUNCTION).getOne(), "OR").apply(view);
    return view;
  }

}
