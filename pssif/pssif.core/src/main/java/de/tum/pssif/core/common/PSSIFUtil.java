package de.tum.pssif.core.common;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;


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

  public static boolean isValidName(String name) {
    return !normalize(name).isEmpty();
  }

  public static void checkNameValidity(String name) {
    if (!PSSIFUtil.isValidName(name)) {
      throw new PSSIFStructuralIntegrityException("invalid name '" + name + "'");
    }
  }
}
