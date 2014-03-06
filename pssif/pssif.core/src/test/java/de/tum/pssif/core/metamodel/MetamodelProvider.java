package de.tum.pssif.core.metamodel;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.impl.MetamodelImpl;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.core.metamodel.mutable.MutableJunctionNodeType;
import de.tum.pssif.core.metamodel.mutable.MutableMetamodel;
import de.tum.pssif.core.metamodel.mutable.MutableNodeType;


public class MetamodelProvider {
  public static Metamodel create() {
    MutableMetamodel result = new MetamodelImpl();

    MutableNodeType x = result.createNodeType("X");
    MutableNodeType s = result.createNodeType("S");
    s.inherit(x);
    MutableNodeType y = result.createNodeType("Y");
    y.inherit(x);
    MutableNodeType e = result.createNodeType("E");
    e.inherit(s);
    MutableNodeType f = result.createNodeType("F");
    f.inherit(s);
    MutableNodeType a = result.createNodeType("A");
    a.inherit(y);
    MutableNodeType b = result.createNodeType("B");
    b.inherit(y);
    MutableNodeType c = result.createNodeType("C");
    c.inherit(a);
    MutableNodeType d = result.createNodeType("D");
    d.inherit(a);

    MutableJunctionNodeType jun = result.createJunctionNodeType("jun");

    MutableEdgeType cf = result.createEdgeType("CF");
    cf.createMapping(s, a, Sets.<JunctionNodeType> newHashSet(jun));
    cf.createMapping(s, b, Sets.<JunctionNodeType> newHashSet(jun));
    cf.createMapping(a, s, Sets.<JunctionNodeType> newHashSet(jun));
    cf.createMapping(b, s, Sets.<JunctionNodeType> newHashSet(jun));

    return result;
  }
}
