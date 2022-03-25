package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
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

    /**
     * Get owners matching search term.
     *
     * @param term String search term to search for
     * @return a list owners (maximum of 5) matching that search term
     * @throws ResponseStatusException if some internal error occurs in the database
     */
    List<Owner> searchOwners(String term) throws ServiceException;
}
