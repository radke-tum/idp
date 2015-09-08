package jena.database;

import java.io.File;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public interface RDFModel {
	/**
	 * Inserts a triple into the model
	 * 
	 * @param subject
	 *            Subject that should be inserted.
	 * @param predicate
	 *            Predicate that should be inserted.
	 * @param object
	 *            Object that should be inserted.
	 */
	public void insert(Resource subject, Property predicate, Resource object);

	/**
	 * Writes model to a file
	 * 
	 * @param name
	 *            Name of the file
	 * @param loc
	 *            Location of the file
	 */
	public void writeModelToFile(String name, String loc);

	/**
	 * Find all Subjects that are stored in the model
	 * 
	 * @return A list of resources containing all Subjects
	 */
	public List<Resource> findAllSubjects();

	/**
	 * Find all Subjects that are stored in the model with a certain property
	 * 
	 * @param p
	 *            Property the triple should contain
	 * @return A list of resources containing Subjects
	 */
	public List<Resource> findAllSubjects(Property p);

	/**
	 * Create a bag if it don't exist yet and add a subject to it
	 * 
	 * @param uri
	 *            Uri of the bag
	 * @param subject
	 *            The subject that should be added
	 */
	public void addToBag(String uri, Resource subject);

	/**
	 * Get all subjects of a certain bag
	 * 
	 * @param uri
	 *            Uri of the bag
	 * @return List of Resources containing subjects in bag
	 */
	public List<Resource> getSubjectsOfBag(String uri);

	/**
	 * Remove a triple with a given subject, predicate and object
	 * 
	 * @param s
	 *            Subject of triple
	 * @param p
	 *            Predicate of triple
	 * @param o
	 *            Object of triple
	 */
	public void removeTriple(Resource s, Property p, Resource r);

	/**
	 * Remove a triple with given URIs of subject, predicate and object
	 * 
	 * @param s
	 *            URI of of Subject
	 * @param p
	 *            URI of of Predicate
	 * @param o
	 *            URI of of Object
	 */
	public void removeTriple(String s, String p, String o);

	/**
	 * Remove a triple with given URIs of subject and predicate and given value
	 * String of the object
	 * 
	 * @param s
	 *            URI of of Subject
	 * @param p
	 *            URI of of Predicate
	 * @param o
	 *            String of value of Object
	 */
	public void removeTripleLiteral(String s, String p, String o);

	/**
	 * Removes a statement
	 * 
	 * @param st
	 *            Statement to be removed
	 */
	public void removeStatement(Statement st);

	/**
	 * Remove all the statements from this model
	 */
	public void removeAll();

	/**
	 * Create a new resource associated with this model. If the uri string is
	 * null, this creates a bnode, as per createResource(). Otherwise it creates
	 * a URI node. A URI resource is .equals() to any other URI Resource with
	 * the same URI (even in a different model - be warned).
	 * 
	 * This method may return an existing Resource with the correct URI and
	 * model, or it may construct a fresh one, as it sees fit.
	 * 
	 * Operations on the result Resource may change this model.
	 * 
	 * @param uri
	 *            the URI of the resource to be created Returns: a new resource
	 *            linked to this model.
	 * @return a new resource linked to this model.
	 */
	public Resource createResource(String uri);

	/**
	 * Return a Resource instance with the given URI in this model. This method
	 * behaves identically to createResource(String) and exists as legacy:
	 * createResource is now capable of, and allowed to, reuse existing objects.
	 * 
	 * Subsequent operations on the returned object may modify this model.
	 * 
	 * @param: uri the URI of the resource
	 * @return: a resource instance
	 */
	public Resource getResource(String uri);

	/**
	 * Create a property.
	 * 
	 * Subsequent operations on the returned property may modify this model.
	 * 
	 * @param uri
	 *            the URI of the property
	 * @return a property instance
	 */
	public Property createProperty(String uri);

	/**
	 * Commit the current transaction.
	 */
	public void commit();

	/**
	 * Removes an element from a bag.
	 * 
	 * @param uri
	 *            the URI of the bag
	 * @param uri
	 *            the URI of the element to be removed
	 */
	void removeFromBag(String uri, String subject);

	/**
	 * Checks whether a bag contains an element
	 * 
	 * @param uri
	 *            the URI of the element
	 * @param bag
	 *            the URI of the bag
	 * @return result of the check
	 */
	boolean bagContainsResource(String uri, String bag);

	/**
	 * Checks whether a bag contains an JunctionNode
	 * 
	 * @param uri
	 *            the URI of the JunctionNode
	 * @return result of the check
	 */
	boolean containsJunctionNode(String uri);

	/**
	 * Return a Property instance in this model.
	 * 
	 * Subsequent operations on the returned property may modify this model.
	 * 
	 * The property is assumed to already exist in the model. If it does not,
	 * createProperty should be used instead.
	 * 
	 * @param uri
	 *            the URI of the property
	 * 
	 * @return a property object
	 */
	Property getProperty(String uri);
	
	/**
	 * Writes model to a file in Turtle format.
	 * 
	 * @param name
	 *            Name of the file
	 * @param loc
	 *            Location of the file
	 */
	void writeModelToTurtleFile(File file);
}
