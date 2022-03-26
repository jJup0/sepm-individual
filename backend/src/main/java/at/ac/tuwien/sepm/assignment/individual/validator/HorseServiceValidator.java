package at.ac.tuwien.sepm.assignment.individual.validator;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.exception.*;

import java.time.LocalDate;

public interface HorseServiceValidator {

    /**
     * Validates a DTO containing a new horse to be added.
     * Does not return, only throws exceptions when the DTO is a valid edit.
     *
     * @param horseDto DTO to validate
     * @throws MissingAttributeException if some required properties (like name) are null
     * @throws ConstraintViolation      if mother and father are not suitable in terms of age or sex
     * @throws PersistenceException      if an internal error occurs, for example while fetching the current data for the horse that is to be edited
     * @throws NotFoundException         if there is no entry in the database for the mother or father
     */
    void validateAddHorse(HorseDto horseDto) throws MissingAttributeException, ConstraintViolation, PersistenceException, NotFoundException;

    /**
     * Validates a DTO containing changes for an edit of a horse, or a new horse to be added.
     * Does not return, only throws exceptions when the DTO is a valid edit.
     *
     * @param horseDto DTO to validate
     * @throws MissingAttributeException if some required properties (like name) are null
     * @throws ConstraintViolation      if some edits violate constraints like parents being younger than children
     * @throws PersistenceException      if an internal error occurs, for example while fetching the current data for the horse that is to be edited
     * @throws NotFoundException         if there is no entry in the database for a horse with the given id
     */
    void validateEditHorse(HorseDto horseDto) throws MissingAttributeException, ConstraintViolation, PersistenceException, NotFoundException;

    /**
     * Validates a date, used for search parameter date, as this can apparently not be processed by spring directly as a local date
     *
     * @param date Date as string to validate
     * @return The parsed birthdate
     * @throws NotParsableValueException if the date is not valid
     */
    LocalDate validateDate(String date) throws NotParsableValueException;

    /**
     * Validates a sex string used for search parameter, as this can apparently not be processed by spring directly as an enum if null
     *
     * @param sex Sex as string to validate
     * @return The parsed horse sex
     * @throws NotParsableValueException if the sex string is not valid
     */
    HorseBiologicalGender validateSex(String sex)throws NotParsableValueException;
}
