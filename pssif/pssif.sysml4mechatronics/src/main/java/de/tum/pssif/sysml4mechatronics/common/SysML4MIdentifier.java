package de.tum.pssif.sysml4mechatronics.common;

import java.util.UUID;


public abstract class SysML4MIdentifier {

  public abstract String serialize();

  public abstract String toString();

  public abstract int hashCode();

  public abstract boolean equals(Object obj);

  public static SysML4MIdentifier create(final String string) {
    try {
      return create(UUID.fromString(string));
    } catch (IllegalArgumentException e) {
      return new SysML4MIdentifier() {

        @Override
        public String toString() {
          return serialize();
        }

        @Override
        public String serialize() {
          return string;
        }

        @Override
        public int hashCode() {
          return (getClass().getSimpleName() + string).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
          if (!getClass().isAssignableFrom(obj.getClass())) {
            return false;
          }
          return string.equalsIgnoreCase(((SysML4MIdentifier) obj).serialize());
        }
      };
    }
  }

  public static SysML4MIdentifier create(final UUID uuid) {
    return new SysML4MIdentifier() {

      @Override
      public String toString() {
        return serialize();
      }

      @Override
      public String serialize() {
        return uuid.toString();
      }

      @Override
      public int hashCode() {
        return (getClass().getSimpleName() + uuid.toString()).hashCode();
      }

      @Override
      public boolean equals(Object obj) {
        if (!getClass().isAssignableFrom(obj.getClass())) {
          return false;
        }
        return serialize().equalsIgnoreCase(((SysML4MIdentifier) obj).serialize());
      }
    };
  }

}
