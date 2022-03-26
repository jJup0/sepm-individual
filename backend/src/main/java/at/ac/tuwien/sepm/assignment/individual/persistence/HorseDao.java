package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDtoIdReferences;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;

import java.util.List;

/**
 * Data Access Object for horses.
 * Implements access functionality to the application's persistent data store regarding horses.
 */
public interface HorseDao {

    /**
     * Get all horses stored in the persistent data store.
     *
     * @return a list of all stored horses
     * @throws PersistenceException if some internal error occurs in the database
     */
    List<Horse> getAll() throws PersistenceException;

    /**
     * Get a specific horse according to the given id.
     *
     * @param id ID of the horse to be returned
     * @return The horse that was requested as an entity
     * @throws PersistenceException if some internal error occurs in the database
     * @throws NotFoundException    if the id could not be found in the database
     */
    Horse getHorse(long id) throws PersistenceException, NotFoundException;

    /**
     * Gets the family tree according to a given horse ID.
     *
     * @param id            ID of the horse for which the family tree should be fetched
     * @param ancestorDepth The amount of generations that should be returned to the family tree.
     * @return A single horse entity with recursive mother and father entities with a maximum depth of ancestorDepth.
     * @throws PersistenceException if some internal error occurs in the database
     * @throws NotFoundException    if the ID could not be found in the database
     */
    Horse getFamilyTreeOfHorse(long id, int ancestorDepth) throws PersistenceException, NotFoundException;

    /**
     * Searches for horses matching the search DTO
     *
     * @param horseSearchDto A horse search DTO containing all the parameters that a horse must have
     * @return A list of all the horses that match all the parameters
     * @throws PersistenceException if some internal error occurs in the database
     */
    List<Horse> searchHorses(HorseSearchDto horseSearchDto) throws PersistenceException;

    /**
     * Gets all the children of the horse with the given id.
     * For internal use only.
     *
     * @param id ID of the horse for which the children should be searched for
     * @return A list of all the children of the horse
     * @throws PersistenceException if some internal error occurs in the database
     * @throws NotFoundException    if the ID could not be found in the database
     */
    List<Horse> getChildren(long id) throws PersistenceException, NotFoundException;

    /**
     * Adds a given horse DTO to persistence.
     *
     * @param horseDto The horse that should be added
     * @return An entity representation of the horse DTO including an ID
     * @throws PersistenceException if some internal error occurs in the database
     */
    Horse addHorse(HorseDtoIdReferences horseDto) throws PersistenceException;

    /**
     * Edits a horse according to the ID given in the horseDto.
     *
     * @param horseDto A horse DTO containing all the information that should be updated for a horse.
     *                 The chosen horse that will be updated is determined by ID.
     * @return An entity version of the given horseDto
     * @throws PersistenceException if some internal error occurs in the database
     * @throws NotFoundException    if the ID could not be found in the database
     */
    Horse editHorse(HorseDtoIdReferences horseDto) throws PersistenceException, NotFoundException;

    /**
     * Deletes a horse from the database. If horse not in the database, nothing happens.
     *
     * @param id ID of the horse that should be deleted
     */
    void deleteHorse(long id);
}
