package de.tum.pssif.transform.graph;

import java.util.Set;

import com.google.common.collect.Sets;


public final class Node extends AElement {

  private final Set<Edge> incoming = Sets.newHashSet();
  private final Set<Edge> outgoing = Sets.newHashSet();

  Node(String id) {
    super(id);
  }

  void addIncoming(Edge edge) {
    this.incoming.add(edge);
  }

  void addOutgoing(Edge edge) {
    this.outgoing.add(edge);
  }

  void removeIncoming(Edge edge) {
    this.incoming.remove(edge);
  }

  void removeOutgoing(Edge edge) {
    this.outgoing.remove(edge);
  }

  public Set<Edge> getIncoming() {
    return Sets.newHashSet(incoming);
  }

  public Set<Edge> getOutgoing() {
    return Sets.newHashSet(outgoing);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Node)) {
      return false;
    }
    return getId().equals(((Node) obj).getId());
  }

}
