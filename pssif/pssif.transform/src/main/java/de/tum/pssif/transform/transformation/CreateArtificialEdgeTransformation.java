package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.transform.transformation.artificial.ArtificializedConnectionMapping;
import de.tum.pssif.transform.transformation.artificial.ArtificializingConnectionMapping;


public class CreateArtificialEdgeTransformation extends AbstractTransformation {
  private final NodeType from;  //S
  private final NodeType to;    //F
  private final EdgeType base;  //IF
  private final EdgeType target; //CF

  public CreateArtificialEdgeTransformation(NodeType from, NodeType to, EdgeType base, EdgeType target) {
    this.from = from;
    this.to = to;
    this.base = base;
    this.target = target;
  }

  @Override
  public void apply(View view) {
    NodeType actualFrom = view.getNodeType(from.getName()).getOne();
    NodeType actualTo = view.getNodeType(to.getName()).getOne();
    MutableEdgeType actualBase = view.getMutableEdgeType(base.getName()).getOne();
    MutableEdgeType actualTarget = view.getMutableEdgeType(target.getName()).getOne();

    ConnectionMapping baseMapping = actualBase.getMapping(actualFrom, actualTo).getOne();
    ConnectionMapping targetMapping = actualTarget.getMapping(actualFrom, actualTo).getOne();

    if (baseMapping.getFrom().equals(actualFrom) && baseMapping.getTo().equals(actualTo)) {
      //replace the mapping
      actualBase.removeMapping(baseMapping);
    }
    actualBase.addMapping(new ArtificializingConnectionMapping(baseMapping, baseMapping.getType(), actualFrom, actualTo, actualTarget));

    if (targetMapping.getFrom().equals(actualFrom) && targetMapping.getTo().equals(actualTo)) {
      //replace the mapping
      actualTarget.removeMapping(targetMapping);
    }
    ArtificializedConnectionMapping artificializing = new ArtificializedConnectionMapping(targetMapping, targetMapping.getType(), actualFrom,
        actualTo, actualBase);
    actualTarget.addMapping(artificializing);
  }
}
