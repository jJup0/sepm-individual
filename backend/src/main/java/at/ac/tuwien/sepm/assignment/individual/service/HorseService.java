package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.dto.DirtyHorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDtoIdReferences;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exception.*;

import java.util.List;

/**
 * Service for working with horses.
 */
public interface HorseService {
    /**
     * Lists all horses stored in the system.
     *
     * @return list of all stored horses
     * @throws ServiceException if some internal error occurs in the database
     */
    List<Horse> allHorses() throws ServiceException;

    /**
     * Get a specific horse according to the given id.
     *
     * @param id ID of the horse to be returned
     * @return The horse that was requested as an entity
     * @throws ServiceException  if some internal error occurs in the database
     * @throws NotFoundException if the id could not be found in the database
     */
    Horse getHorse(long id) throws NotFoundException, ServiceException;

    /**
     * Gets the family tree according to a given horse ID.
     *
     * @param id    ID of the horse for which the family tree should be fetched
     * @param depth The amount of generations that should be returned to the family tree.
     * @return A single horse entity with recursive mother and father entities with a maximum depth of ancestorDepth.
     * @throws ServiceException  if some internal error occurs in the database
     * @throws NotFoundException if the ID could not be found in the database
     */
    Horse getHorseFamilyTree(long id, Integer depth) throws NotFoundException, ServiceException;

    /**
     * Searches for horses matching the search DTO
     *
     * @param dirtyHorseSearchDto A horse search DTO containing all the parameters that a horse must have
     * @return A list of all the horses that match all the parameters
     * @throws ServiceException if some internal error occurs in the database
     */
    List<Horse> searchHorses(DirtyHorseSearchDto dirtyHorseSearchDto) throws ServiceException, NotParsableValueException;

    /**
     * Adds a given horse DTO to persistence.
     *
     * @param horseDto The horse that should be added
     * @return An entity representation of the horse DTO including an ID
     * @throws MissingAttributeException if some properties required for a horse are missing
     * @throws ServiceException          if some internal error occurs in the database
     */
    Horse addHorse(HorseDtoIdReferences horseDto) throws MissingAttributeException, ServiceException, ConstraintViolation, NotFoundException;

    /**
     * Edits a horse according to the ID given in the horseDto.
     *
     * @param horseDto A horse DTO containing all the information that should be updated for a horse.
     *                 The chosen horse that will be updated is determined by ID.
     * @return An entity version of the given horseDto
     * @throws MissingAttributeException if some properties required for a horse are missing
     * @throws ConstraintViolation      if some properties that want to be edited violate constraints
     * @throws ServiceException          if some internal error occurs in the database
     * @throws NotFoundException         if the ID could not be found in the database
     */
    Horse editHorse(HorseDtoIdReferences horseDto) throws MissingAttributeException, ConstraintViolation, NotFoundException, ServiceException;

    /**
     * Deletes a horse from the database. If horse not in the database, nothing happens.
     *
     * @param id ID of the horse that should be deleted
     */
    void deleteHorse(long id);
}
