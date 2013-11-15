package de.tum.pssif.core.metamodel;

import org.junit.Assert;
import org.junit.Test;

import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;

public class UnlimitedNaturalTest {
	@Test(expected = IllegalArgumentException.class)
	public void testNegativeArgument() {
		UnlimitedNatural.of(-1);
	}

	@Test
	public void testEquals() {
		UnlimitedNatural u0 = UnlimitedNatural.of(3);
		UnlimitedNatural u1 = UnlimitedNatural.of(3);
		UnlimitedNatural u2 = UnlimitedNatural.of(3);
		UnlimitedNatural u3 = UnlimitedNatural.of(4);
		UnlimitedNatural u4 = UnlimitedNatural.UNLIMITED;

		// reflexive
		Assert.assertTrue(u0.equals(u0));
		Assert.assertTrue(u4.equals(u4));
		Assert.assertTrue(u4.equals(UnlimitedNatural.UNLIMITED));

		// symmetric
		Assert.assertTrue(u0.equals(u1));
		Assert.assertTrue(u1.equals(u0));
		Assert.assertFalse(u0.equals(u3));
		Assert.assertFalse(u3.equals(u0));

		// transitive
		Assert.assertTrue(u0.equals(u1));
		Assert.assertTrue(u1.equals(u2));
		Assert.assertTrue(u0.equals(u2));

		Assert.assertFalse(u0.equals(null));
	}

	@Test
	public void testCompare() {
		UnlimitedNatural u0 = UnlimitedNatural.of(3);
		UnlimitedNatural u1 = UnlimitedNatural.of(4);

		Assert.assertTrue(u0.compareTo(u1) < 0);
		Assert.assertTrue(u0.compareTo(u0) == 0);
		Assert.assertTrue(u1.compareTo(u0) > 0);
		Assert.assertTrue(u0.compareTo(UnlimitedNatural.UNLIMITED) < 0);
		Assert.assertTrue(UnlimitedNatural.UNLIMITED
				.compareTo(UnlimitedNatural.UNLIMITED) == 0);
		Assert.assertTrue(UnlimitedNatural.UNLIMITED.compareTo(u0) > 0);

		Assert.assertTrue(u0.compareTo(4) < 0);
		Assert.assertTrue(u0.compareTo(2) > 0);
		Assert.assertTrue(UnlimitedNatural.UNLIMITED.compareTo(1) > 0);
	}
}
