package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;

import de.tum.pssif.core.metamodel.Annotation;
import de.tum.pssif.core.util.PSSIFOption;


public interface Annotatable<E> {

  Collection<Annotation> getAnnotations(E element);

  PSSIFOption<String> getAnnotationValue(E element, String key);

  void setAnnotation(E element, String key, String value);

}
