package de.tum.pssif.core.metamodel.mutable;

import java.util.Collection;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.NodeType;


public interface MutableEdgeType extends EdgeType, MutableElementType<EdgeType> {
  /**
   * Create a {@link ConnectionMapping} for this EdgeType mapping the specified from {@link NodeType} to the specified to {@link NodeType} possibly adding additional mappings from and to a set of junctions.
   * 
   * @param from
   * @param to
   * @param junctions
   * 
   * @return the {@link ConnectionMapping} created directly from the from {@link NodeType} to the to {@link NodeType}. To get a {@link ConnectionMapping} from or to one of the specified {@link JunctionNodeType}s use the {@link #getMapping(NodeType, NodeType)}-method.
   */
  ConnectionMapping createMapping(NodeType from, NodeType to, Collection<JunctionNodeType> junctions);
}
