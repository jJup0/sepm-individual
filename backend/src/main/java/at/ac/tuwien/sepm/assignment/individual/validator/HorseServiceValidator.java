package at.ac.tuwien.sepm.assignment.individual.validator;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.exception.*;

public interface HorseServiceValidator {
    /**
     * Validates a DTO that is to be added, does not return, only throws exceptions when the DTO is not valid to add.
     *
     * @param horseDto DTO to validate
     * @throws MissingAttributeException if some required properties (like name) are null
     */
    void validateAddHorse(HorseDto horseDto) throws MissingAttributeException;

    /**
     * Validates a DTO containing changes for an edit of a horse, does not return, only throws exceptions when the DTO is a valid edit.
     *
     * @param horseDto DTO to validate
     * @throws MissingAttributeException if some required properties (like name) are null
     * @throws IllegalEditException      if some edits violate constraints like parents being younger than children
     * @throws PersistenceException      if an internal error occurs, for example while fetching the current data for the horse that is to be edited
     * @throws NotFoundException         if there is no entry in the database for a horse with the given id
     */
    void validateEditHorse(HorseDto horseDto) throws MissingAttributeException, IllegalEditException, PersistenceException, NotFoundException;
}
