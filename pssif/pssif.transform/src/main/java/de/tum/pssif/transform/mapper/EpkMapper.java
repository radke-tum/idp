package de.tum.pssif.transform.mapper;

import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.transform.ModelMapper;
import de.tum.pssif.transform.model.EpkModelMapper;
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
    Metamodel view = new RenameEdgeTypeTransformation(metamodel.findEdgeType("Control Flow"), "Dynamic connector").apply(metamodel);
    view = new RenameNodeTypeTransformation(view.findNodeType("Event"), "Dev Event").apply(view);
    view = new RenameNodeTypeTransformation(view.findNodeType("Function"), "Abstract Function").apply(view);
    view = new RenameNodeTypeTransformation(view.findNodeType("Activity"), "Function").apply(view);
    view = new RenameNodeTypeTransformation(view.findNodeType("State"), "Event").apply(view);
    view = new AliasNodeTypeTransformation(view.findNodeType("Block"), "Information/ Material").apply(view);
    view = new AliasNodeTypeTransformation(view.findNodeType("Block"), "Organizational unit").apply(view);
    return view;
  }

}
