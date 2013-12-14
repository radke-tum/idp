package de.tum.pssif.core.metamodel;



public interface Unit extends Named {

  String getAbbreviation();

  boolean isSi();

  Unit getSi();

}
