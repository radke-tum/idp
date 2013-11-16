package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

public class EdgeTypeImpl extends NamedImpl implements EdgeType {
	private EdgeTypeImpl general;
	private Collection<EdgeTypeImpl> specials = Sets.newHashSet();

	private final EdgeEndImpl incoming;
	private final EdgeEndImpl outgoing;
	private final Collection<EdgeEndImpl> auxiliaries = Sets.newHashSet();

	public EdgeTypeImpl(String name, String inName, NodeType inType,
			Multiplicity inMult, String outName, NodeType outType,
			Multiplicity outMult) {
		super(name);
		incoming = new EdgeEndImpl(inName, this, inMult, inType);
		outgoing = new EdgeEndImpl(outName, this, outMult, outType);
		inType.registerOutgoing(outgoing);
		outType.registerIncoming(incoming);
	}

	@Override
	public EdgeEnd createAuxiliaryEnd(String name, Multiplicity mult,
			NodeType to) {
		EdgeEndImpl result = new EdgeEndImpl(name, this, mult, to);
		auxiliaries.add(result);
		to.registerAuxiliary(result);
		return result;
	}

	@Override
	public Collection<EdgeEnd> getEnds() {
		Collection<EdgeEnd> result = Sets.<EdgeEnd> newHashSet(getIncoming(),
				getOutgoing());
		result.addAll(getAuxiliaries());
		return Collections.<EdgeEnd> unmodifiableCollection(result);
	}

	@Override
	public EdgeEnd getIncoming() {
		return incoming;
	}

	@Override
	public EdgeEnd getOutgoing() {
		return outgoing;
	}

	@Override
	public Collection<EdgeEnd> getAuxiliaries() {
		return Collections.<EdgeEnd> unmodifiableCollection(auxiliaries);
	}

	@Override
	public Edge create(Model model, Multimap<EdgeEnd, Node> connections) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EdgeType getGeneral() {
		return general;
	}

	@Override
	public Collection<EdgeType> getSpecials() {
		return Collections.<EdgeType> unmodifiableCollection(specials);
	}

	@Override
	public void inherit(EdgeType general) {
		general.registerSpecialization(this);
	}

	@Override
	public void registerSpecialization(EdgeTypeImpl special) {
		specials.add(special);
		special.registerGeneralization(this);
	}

	@Override
	public void registerGeneralization(EdgeTypeImpl general) {
		this.general = general;
	}
}
