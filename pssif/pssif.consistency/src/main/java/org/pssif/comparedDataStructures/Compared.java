package org.pssif.comparedDataStructures;

/**
This project is part of the Product-Service System integration framework. It is responsible for keeping consistency between different requirements models or versions of models.
Copyright (C) 2014 Andreas Genz

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Feel free to contact me via eMail: genz@in.tum.de
*/

/**
 * Classes extendig this class are able to say whether their elements have
 * been equals or not
 * 
 * @author Andreas
 * 
 */
public abstract class Compared {
	/**
	 * bool saying if the proposed equals link was accepted by the user
	 */
	private boolean equals;

	/**
	 * @return the equals
	 */
	public boolean isEquals() {
		return equals;
	}

	/**
	 * @param equals
	 *            the equals to set
	 */
	public void setEquals(boolean equals) {
		this.equals = equals;
	}
}
