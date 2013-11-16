package de.tum.pssif.core.metamodel;

import java.io.Serializable;

import com.google.common.base.Preconditions;

public interface Multiplicity {
	int getLower();

	UnlimitedNatural getUpper();

	public static class MultiplicityContainer implements Multiplicity {
		private final int lower;
		private final UnlimitedNatural upper;

		public static Multiplicity of(int lower, UnlimitedNatural upper) {
			Preconditions.checkArgument(lower >= 0);
			Preconditions.checkArgument(upper.compareTo(lower) >= 0);
			return new MultiplicityContainer(lower, upper);
		}

		public static Multiplicity of(int lower, int upper) {
			return of(lower, UnlimitedNatural.of(upper));
		}

		private MultiplicityContainer(int lower, UnlimitedNatural upper) {
			this.lower = lower;
			this.upper = upper;
		}

		@Override
		public int getLower() {
			return lower;
		}

		@Override
		public UnlimitedNatural getUpper() {
			return upper;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + lower;
			result = prime * result + ((upper == null) ? 0 : upper.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Multiplicity other = (Multiplicity) obj;
			if (lower != other.getLower()) {
				return false;
			}
			if (upper == null) {
				if (other.getUpper() != null) {
					return false;
				}
			} else if (!upper.equals(other.getUpper())) {
				return false;
			}
			return true;
		}
	}

	public static final class UnlimitedNatural extends Number implements
			Comparable<Number>, Serializable {
		private static final long serialVersionUID = 1L;

		public static final UnlimitedNatural UNLIMITED = new UnlimitedNatural(
				-1);

		private int value;

		private UnlimitedNatural(int value) {
			this.value = value;
		}

		public static UnlimitedNatural of(int value) {
			Preconditions.checkArgument(value >= 0);
			return new UnlimitedNatural(value);
		}

		public static UnlimitedNatural max(UnlimitedNatural a,
				UnlimitedNatural b) {
			if (a.equals(UNLIMITED) || b.equals(UNLIMITED)) {
				return UNLIMITED;
			} else {
				return UnlimitedNatural.of(Math.max(a.value, b.value));
			}
		}

		@Override
		public int intValue() {
			return value;
		}

		@Override
		public long longValue() {
			return value;
		}

		@Override
		public float floatValue() {
			return value;
		}

		@Override
		public double doubleValue() {
			return value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + value;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				if (Number.class.isAssignableFrom(obj.getClass())) {
					return value == ((Number) obj).intValue();
				}
				return false;
			}
			UnlimitedNatural other = (UnlimitedNatural) obj;
			if (value != other.value) {
				return false;
			}
			return true;
		}

		@Override
		public int compareTo(Number o) {
			if (value == -1) {
				if (o.intValue() == -1) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if (o.intValue() == -1) {
					return -1;
				} else {
					return Integer.valueOf(value).compareTo(o.intValue());
				}
			}
		}
	}
}
