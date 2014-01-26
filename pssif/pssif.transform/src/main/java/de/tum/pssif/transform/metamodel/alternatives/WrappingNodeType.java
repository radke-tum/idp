package de.tum.pssif.transform.metamodel.alternatives;

import java.util.Collection;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class WrappingNodeType extends WrappingElementType<NodeType, Node> implements NodeType {

  protected WrappingNodeType(ElementType<NodeType, Node> wrapped) {
    super(wrapped);
  }

  @Override
  public Class<?> getMetaType() {
    return NodeType.class;
  }

  @Override
  public void registerIncoming(EdgeType type) {
    // TODO Auto-generated method stub

  }

  @Override
  public void registerOutgoing(EdgeType type) {
    // TODO Auto-generated method stub

  }

  @Override
  public void registerAuxiliary(EdgeType type) {
    // TODO Auto-generated method stub

  }

  @Override
  public Collection<EdgeType> getIncomings() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<EdgeType> getOutgoings() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<EdgeType> getAuxiliaries() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdgeType findIncomingEdgeType(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdgeType findOutgoingEdgeType(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdgeType findAuxiliaryEdgeType(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Node create(Model model) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PSSIFOption<Node> apply(Model model) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PSSIFOption<Node> apply(Model model, String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isAssignableFrom(NodeType type) {
    // TODO Auto-generated method stub
    return false;
  }

}
