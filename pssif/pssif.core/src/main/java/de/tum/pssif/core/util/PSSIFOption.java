package de.tum.pssif.core.util;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.exception.PSSIFIllegalAccessException;


public final class PSSIFOption<P> {

  private final Set<P> elements;

  public static <T> PSSIFOption<T> merge(PSSIFOption<T> option1, PSSIFOption<T> option2) {
    Set<T> all = Sets.newHashSet();
    all.addAll(option1.getMany());
    all.addAll(option2.getMany());
    return many(all);
  }

  public static <T> PSSIFOption<T> one(T element) {
    Set<T> elements = Sets.newHashSet();
    elements.add(element);
    return new PSSIFOption<T>(elements);
  }

  public static <T> PSSIFOption<T> none() {
    return new PSSIFOption<T>(null);
  }

  public static <T> PSSIFOption<T> many(Collection<T> elements) {
    return new PSSIFOption<T>(elements);
  }

  private PSSIFOption(Collection<P> elements) {
    this.elements = Sets.newHashSet();
    if (elements != null) {
      for (P elem : elements) {
        if (elem != null) {
          this.elements.add(elem);
        }
      }
    }
  }

  public boolean isOne() {
    return elements.size() == 1;
  }

  public boolean isMany() {
    return elements.size() > 1;
  }

  public boolean isNone() {
    return elements.isEmpty();
  }

  public P getOne() {
    if (!isOne()) {
      throw new PSSIFIllegalAccessException("many or none can not be accessed as one");
    }
    return elements.iterator().next();
  }

  public Set<P> getMany() {
    return Sets.newHashSet(elements);
  }

}
