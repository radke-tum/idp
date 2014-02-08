package de.tum.pssif.core.model.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.impl.EdgeEndImpl;
import de.tum.pssif.core.metamodel.impl.GetValueOperation;
import de.tum.pssif.core.metamodel.impl.SetValueOperation;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFUtil;
import de.tum.pssif.core.util.PSSIFValue;


abstract class ElementImpl implements Element {
  private final Model                          model;
  private Map<String, PSSIFOption<PSSIFValue>> values = Maps.newHashMap();

  public ElementImpl(Model model) {
    this.model = model;
  }

  @Override
  public String getId() {
    return values.get(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).isOne() ? values.get(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne().asString()
        : "undefined";
  }

  @Override
  public PSSIFOption<PSSIFValue> apply(GetValueOperation op) {
    return values.get(op.getAttributeType().getName()) != null ? values.get(op.getAttributeType().getName()) : PSSIFOption.<PSSIFValue> none();
  }

  @Override
  public void apply(SetValueOperation op) {
    values.put(op.getAttributeType().getName(), op.getValue());
  }

  protected Set<EdgeEndImpl> locateEdgeEnds(EdgeEnd edgeEnd, Collection<EdgeEndImpl> candidates) {
    Set<EdgeEndImpl> result = Sets.newHashSet();

    for (EdgeEndImpl impl : candidates) {
      if (PSSIFUtil.areSame(edgeEnd.getName(), impl.getName())) {
        result.add(impl);
      }
    }

    return result;
  }

  @Override
  public Model getModel() {
    return model;
  }
}
