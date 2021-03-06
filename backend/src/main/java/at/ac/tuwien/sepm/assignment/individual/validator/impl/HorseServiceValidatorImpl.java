package at.ac.tuwien.sepm.assignment.individual.validator.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDtoIdReferences;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.exception.*;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.validator.HorseServiceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.List;

@Component
public class HorseServiceValidatorImpl implements HorseServiceValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HorseDao dao;
    private final HorseMapper mapper;

    public HorseServiceValidatorImpl(HorseDao dao, HorseMapper mapper) {
        LOGGER.trace("HorseServiceValidatorImpl constructed");
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public void validateAddHorse(HorseDtoIdReferences horseDto) throws MissingAttributeException, ConstraintViolation, PersistenceException, NotFoundException {
        LOGGER.debug("validating new horse {}", horseDto);

        validateHorseCU(mapper.idReferenceDtoToDto(horseDto), "add");
    }

    @Override
    public void validateEditHorse(HorseDtoIdReferences horseDtoIdReference) throws MissingAttributeException, ConstraintViolation, PersistenceException, NotFoundException {
        LOGGER.debug("validating horse edit on {}", horseDtoIdReference);

        HorseDto horseDto = mapper.idReferenceDtoToDto(horseDtoIdReference);

        if (horseDto.id() == null) {
            throw new MissingAttributeException("failed to edit horse: can not edit horse without given id");
        }
        validateHorseCU(horseDto, "edit");

        String failString = "failed to edit horse with id(" + horseDto.id() + "): ";
        Horse previousHorse;
        previousHorse = dao.getHorse(horseDto.id());

        // id can not be edited, it is used to identify the horse
        // name can be edited without repercussions, as long as it is not null
        // description can be edited without repercussions
        // owner can be edited without repercussions

        // new birthdate needs to be larger than all its children's, younger than its parents
        // if new birthdate later (horse now younger)
        if (newHorseYoungerThan(horseDto, previousHorse)) {
            List<Horse> children = dao.getChildren(horseDto.id());
            for (Horse child : children) {
                if (newHorseYoungerThan(horseDto, child)) {
                    throw new ConstraintViolation(failString + "new birthdate can not be later than birthdate of any child");
                }
            }
        } else if (newHorseOlderThan(horseDto, previousHorse)) {
            if (previousHorse.getMother() != null && newHorseOlderThan(horseDto, previousHorse.getMother())) {
                throw new ConstraintViolation(failString + "new birthdate can not be earlier than mother's birthdate");
            }
            if (previousHorse.getFather() != null && newHorseOlderThan(horseDto, previousHorse.getFather())) {
                throw new ConstraintViolation(failString + "new birthdate can not be earlier than father's birthdate");
            }
        }

        // sex can only change if horse has no children
        if (horseDto.sex() != previousHorse.getSex()) {
            if (dao.getChildren(horseDto.id()).size() != 0) {
                throw new ConstraintViolation(failString + "sex can not be changed if the horse has children");
            }
        }

    }

    @Override
    public LocalDate validateDate(String date) throws NotParsableValueException {
        if (date == null){
            return null;
        }
        try {
            return LocalDate.parse(date);
        } catch (java.time.DateTimeException e){
            throw new NotParsableValueException("Date not parseable", e);
        }
    }

    @Override
    public HorseBiologicalGender validateSex(String sex) throws NotParsableValueException {
        if (sex == null){
            return null;
        }
        try {
            return HorseBiologicalGender.valueOf(sex);
        } catch (IllegalArgumentException e){
            throw new NotParsableValueException("Sex not parseable", e);
        }
    }


    /**
     * Used to avoid code duplication, validates both create and update operations
     *
     * @param horseDto      DTO to validate
     * @param operationType A string representation of the operation type that is called, used for error messages, should be "add" or "edit"
     * @throws MissingAttributeException if some required properties (like name) are null
     * @throws ConstraintViolation       if mother and father are not suitable in terms of age or sex
     * @throws PersistenceException      if an internal error occurs, for example while fetching the current data for the horse that is to be edited
     * @throws NotFoundException         if there is no entry in the database for the mother or father
     */
    private void validateHorseCU(HorseDto horseDto, String operationType) throws MissingAttributeException, ConstraintViolation, PersistenceException, NotFoundException {
        LOGGER.trace("validateHorse(_,[]) helper function called", horseDto, operationType);

        // handle null values first
        // Owner, description, mother and father can be null
        String failString = "failed to " + operationType + " horse: ";

        failString = "failed to " + operationType + " horse with id (" + horseDto.id() + "): ";
        if (horseDto.name() == null) {
            throw new MissingAttributeException(failString + "name can not be null");
        }
        if (horseDto.birthdate() == null) {
            throw new MissingAttributeException(failString + "birthdate can not be null");
        }
        if (horseDto.sex() == null) {
            throw new MissingAttributeException(failString + "sex can not be null");
        }

        // mother has to be female and older
        if (horseDto.mother() != null) {
            Horse mother = dao.getHorse(horseDto.mother().id());
            if (mother.getSex() != HorseBiologicalGender.female) {
                throw new ConstraintViolation(failString + "mother has to be female");
            }
            if (newHorseOlderThan(horseDto, mother)) {
                throw new ConstraintViolation(failString + "mother has to be older");
            }
        }
        // father has to be male and older
        if (horseDto.father() != null) {
            Horse father = dao.getHorse(horseDto.father().id());
            if (father.getSex() != HorseBiologicalGender.male) {
                throw new ConstraintViolation(failString + "father has to be male");
            }
            if (newHorseOlderThan(horseDto, father)) {
                throw new ConstraintViolation(failString + "father has to be older");
            }
        }

    }


    /**
     * Compares birthdays of an edit request horse DTO and corresponding existing horse entity
     *
     * @param horseDto   The edit request horse DTO
     * @param otherHorse The existing horse entity
     * @return True if the birthdate of the edit request is earlier than it is in the entity
     */
    private boolean newHorseOlderThan(HorseDto horseDto, Horse otherHorse) {
        LOGGER.trace("newHorseOlderThan() called");

        return horseDto.birthdate().compareTo(otherHorse.getBirthdate()) < 0;
    }


    /**
     * Compares birthdays of an edit request horse DTO and corresponding existing horse entity
     *
     * @param horseDto   The edit request horse DTO
     * @param otherHorse The existing horse entity
     * @return True if the birthdate of the edit request is later than it is in the entity
     */
    private boolean newHorseYoungerThan(HorseDto horseDto, Horse otherHorse) {
        LOGGER.trace("newHorseYoungerThan() called");

        return horseDto.birthdate().compareTo(otherHorse.getBirthdate()) > 0;
    }


}
