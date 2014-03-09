package de.tum.pssif.sysml4mechatronics.sfb768;

import java.util.UUID;


public abstract class SFB768Identifier {

  public abstract String serialize();

  public abstract String toString();

  public abstract int hashCode();

  public abstract boolean equals(Object obj);

  public static SFB768Identifier create(final String string) {
    try {
      return create(UUID.fromString(string));
    } catch (IllegalArgumentException e) {
      return new SFB768Identifier() {

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
          return string.equalsIgnoreCase(((SFB768Identifier) obj).serialize());
        }
      };
    }
  }

  public static SFB768Identifier create(final UUID uuid) {
    return new SFB768Identifier() {

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
        return serialize().equalsIgnoreCase(((SFB768Identifier) obj).serialize());
      }
    };
  }

}
