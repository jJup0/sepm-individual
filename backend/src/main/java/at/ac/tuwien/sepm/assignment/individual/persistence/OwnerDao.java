package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;

import java.util.List;

/**
 * Data Access Object for owners.
 * Implements access functionality to the application's persistent data store regarding owners.
 */
public interface OwnerDao {

    /**
     * Get all owners stored in the persistent data store.
     *
     * @return a list of all stored owners
     * @throws PersistenceException if some internal error occurs in the database
     */
    List<Owner> getAll() throws PersistenceException;

    /**
     * Get a specific owner according to the given id.
     *
     * @param id ID of the owner to be returned
     * @return The owner that was requested as an entity
     * @throws NotFoundException if the id could not be found in the database
     * @throws PersistenceException if some internal error occurs in the database
     */
    Owner getOwner(long id) throws PersistenceException, NotFoundException;
}
