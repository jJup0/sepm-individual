package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;

import java.util.List;

public interface OwnerService {
    /**
     * Get all owners stored in the persistent data store.
     *
     * @return a list of all stored owners
     * @throws ServiceException if some internal error occurs in the database
     */
    List<Owner> allOwners() throws ServiceException;

    /**
     * Get a specific owner according to the given id.
     *
     * @param id ID of the owner to be returned
     * @return The owner that was requested as an entity
     * @throws NotFoundException if the id could not be found in the database
     * @throws ServiceException if some internal error occurs in the database
     */
    Owner getWithId(long id) throws NotFoundException, ServiceException;
}
