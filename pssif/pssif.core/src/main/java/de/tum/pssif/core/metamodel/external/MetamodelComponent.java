package de.tum.pssif.core.metamodel.external;

/**
 * Represents the common information of each metamodel component
 * @author Alex
 *
 */
public abstract class MetamodelComponent {
	
	private String tag;
	private String name;
	private String type;
	
	/**
	 * Constructor for components
	 * @param tag Tag of the component
	 * @param name Name of the component
	 * @param type Type of the component
	 */
	public MetamodelComponent(String tag, String name, String type) {
		this.tag = tag;
		this.name = name;
		this.type = type;
	}
	
	/**
	 * Get the name of the component
	 * @return The name of the component
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the component
	 * @param name The new name of the component
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the tag of the component
	 * @return The tag of the component
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Set the tag of the component
	 * @param tag The new tag of the component
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Get the type of the component
	 * @return The type of the component
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the type of the component
	 * @param type The new type of the component
	 */
	public void setType(String type) {
		this.type = type;
	}
}
