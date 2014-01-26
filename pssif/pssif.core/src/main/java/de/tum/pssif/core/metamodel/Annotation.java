package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.util.PSSIFOption;


public interface Annotation {

  String getKey();

  PSSIFOption<String> getValue();

}
