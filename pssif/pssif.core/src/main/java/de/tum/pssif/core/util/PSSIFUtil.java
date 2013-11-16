package de.tum.pssif.core.util;



public class PSSIFUtil {

  public static boolean areSame(String name1, String name2) {
    String n1 = name1 == null ? "" : name1;
    String n2 = name2 == null ? "" : name2;
    return n1.trim().toLowerCase().equals(n2.trim().toLowerCase());
  }
}
