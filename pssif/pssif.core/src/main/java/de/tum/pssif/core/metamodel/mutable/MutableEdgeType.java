package de.tum.pssif.core.metamodel.mutable;

import java.util.Collection;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;


public interface MutableEdgeType extends EdgeType, MutableElementType {
  /**
   * Create a {@link ConnectionMapping} for this EdgeType mapping the specified from {@link NodeTypeBase} to the specified to {@link NodeTypeBase} possibly adding additional mappings from and to a set of junctions.
   * 
   * @param from
   * @param to
   * @param junctions
   * 
   * @return the {@link ConnectionMapping} created directly from the from {@link NodeTypeBase} to the to {@link NodeTypeBase}. To get a {@link ConnectionMapping} from or to one of the specified {@link JunctionNodeType}s use the {@link #getMapping(NodeTypeBase, NodeTypeBase)}-method.
   */
  ConnectionMapping createMapping(NodeTypeBase from, NodeTypeBase to, Collection<JunctionNodeType> junctions);
}