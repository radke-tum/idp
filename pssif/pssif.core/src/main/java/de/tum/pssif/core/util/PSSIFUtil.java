package de.tum.pssif.core.util;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.Named;
import de.tum.pssif.core.metamodel.traits.Specializable;


/**
 * Utility class for common procedured used throughout the PSS-IF core.
 */
public class PSSIFUtil {

  /**
   * A null-safe case-insensitive trimming comparison of two Strings.
   * @param name1
   *    The first String.
   * @param name2
   *    The second String.
   * @return
   *    true if the two strings are the same after normalization (see {@link de.tum.pssif.core.util.PSSIFUtil.normalize()}).
   */
  public static boolean areSame(String s1, String s2) {
    return normalize(s1).equals(normalize(s2));
  }

  /**
   * Normalizes a String. If the string is null, an empty string
   * is returned, otherwise the string is trimmed and converted to
   * lower case.
   * @param in
   *    The string to normalize.
   * @return
   *    The normalized string.
   */
  public static String normalize(String in) {
    return in == null ? "" : in.trim().toLowerCase();
  }

  /**
   * Locates a named element by name in a collection of named elements.
   * TODO use throughout the metamodel impl.
   * @param name
   *    The name of the element to locate.
   * @param collection
   *    The collection to look into.
   * @return
   *    The named element, or null, it not found in the collection.
   */
  public static <T extends Named> T find(String name, Collection<T> collection) {
    for (T candidate : collection) {
      if (areSame(name, candidate.getName())) {
        return candidate;
      }
    }
    return null;
  }

  public static <T extends Specializable<T>> List<T> specializationsClosure(T element) {
    List<T> result = Lists.newArrayList();
    for (T spec : element.getSpecials()) {
      result.addAll(specializationsClosure(spec));
    }
    return result;
  }

  public static <T extends Specializable<T>> List<T> generalizationsClosure(T element) {
    List<T> result = Lists.newArrayList();
    T next = element;
    while (next.getGeneral() != null) {
      next = next.getGeneral();
      result.add(next);
    }
    return result;
  }

  public static <T extends Specializable<T>> boolean isReadCompatibleWith(T element, T readableAs) {
    return generalizationsClosure(element).contains(readableAs);
  }

  public static <T extends Specializable<T>> boolean isWriteCompatibleWith(T element, T writableAs) {
    return specializationsClosure(element).contains(writableAs);
  }

  public static <T extends Specializable<T>> boolean hasSpecializationIn(T element, Collection<T> collection) {
    for (T candidate : collection) {
      if (isReadCompatibleWith(candidate, element)) {
        return true;
      }
    }
    return false;
  }

  public static boolean hasSpecializationIn(EdgeEnd element, Collection<EdgeEnd> collection) {
    for (EdgeEnd candidate : collection) {
      if (areSame(element.getName(), candidate.getName()) && isReadCompatibleWith(candidate.getNodeType(), element.getNodeType())) {
        return true;
      }
    }
    return false;
  }
}
