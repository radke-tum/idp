package de.tum.pssif.core.util;

import java.util.Collection;

import de.tum.pssif.core.metamodel.Named;


public class PSSIFUtil {

  public static boolean areSame(String name1, String name2) {
    String n1 = name1 == null ? "" : name1;
    String n2 = name2 == null ? "" : name2;
    return normalize(n1).equals(normalize(n2));
  }

  public static String normalize(String in) {
    return in.trim().toLowerCase();
  }

  public static <T extends Named> T find(String name, Collection<T> collection) {
    T result = null;

    for (T candidate : collection) {
      if (candidate.getName().equals(name)) {
        result = candidate;
        break;
      }
    }

    return result;
  }
}
