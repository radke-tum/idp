package org.pssif.consistencyDataStructures;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.model.Edge;

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

public class EdgeAndType {

	/**
	 * @param edge
	 * @param type
	 */
	public EdgeAndType(Edge edge, EdgeType type) {
		super();
		this.edge = edge;
		this.type = type;
	}
	private Edge edge;
	private EdgeType type;
	/**
	 * @return the edge
	 */
	public Edge getEdge() {
		return edge;
	}
	/**
	 * @param edge the edge to set
	 */
	public void setEdge(Edge edge) {
		this.edge = edge;
	}
	/**
	 * @return the type
	 */
	public EdgeType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(EdgeType type) {
		this.type = type;
	}
	
	 
	
}
