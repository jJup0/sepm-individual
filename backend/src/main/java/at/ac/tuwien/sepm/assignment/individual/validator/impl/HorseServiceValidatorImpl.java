package at.ac.tuwien.sepm.assignment.individual.validator.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exception.IllegalEditException;
import at.ac.tuwien.sepm.assignment.individual.exception.MissingAttributeException;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.validator.HorseServiceValidator;
import org.springframework.stereotype.Component;

import java.util.List;

// TODO is this the right annotation?
@Component
public class HorseServiceValidatorImpl implements HorseServiceValidator {
    private final HorseDao dao;

    public HorseServiceValidatorImpl(HorseDao dao) {
        this.dao = dao;
    }

    @Override
    public void validateAddHorse(HorseDto horseDto) throws MissingAttributeException {
        String failString = "failed to add new horse: ";
        if (horseDto.name() == null) {
            throw new MissingAttributeException(failString + "name missing");
        }
        if (horseDto.birthdate() == null) {
            throw new MissingAttributeException(failString + "birthdate missing");
        }
        if (horseDto.sex() == null) {
            throw new MissingAttributeException(failString + "sex missing");
        }
    }

    @Override
    public void validateEditHorse(HorseDto horseDto) throws MissingAttributeException, IllegalEditException {
        // handle null values first
        // Owner, description, mother and father can be null
        String failString = "failed to edit horse: ";
        if (horseDto.id() == null) {
            throw new MissingAttributeException(failString + "can not edit horse without given id");
        }
        failString = "failed to edit horse with id (" + horseDto.id() + "): ";
        if (horseDto.name() == null) {
            throw new MissingAttributeException(failString + "can not set name of horse to null");
        }
        if (horseDto.birthdate() == null) {
            throw new MissingAttributeException(failString + "can not set birthdate of horse to null");
        }
        if (horseDto.sex() == null) {
            throw new MissingAttributeException(failString + "can not set sex of horse to null");
        }

        Horse previousHorse;
        previousHorse = dao.getHorse(horseDto.id());

        // id can not be edited, it is used to identify the horse
        // name can be edited without repercussions, as long as it is not null
        // description can be edited without repercussions
        // owner can be edited without repercussions


        // new birthdate needs to be larger than all its children's, younger than its parents
        // if new birthdate later (horse now younger)
        if (newHorseOlderThan(horseDto, previousHorse)) {
            List<Horse> children = dao.getChildren(horseDto.id());
            for (Horse child : children) {
                if (newHorseYoungerThan(horseDto, child)) {
                    throw new IllegalEditException(failString + "new birthdate can not be later than birthdate of any child");
                }
            }
        } else if (newHorseYoungerThan(horseDto, previousHorse)) {
            if (previousHorse.getMother() != null && newHorseOlderThan(horseDto, previousHorse.getMother())) {
                throw new IllegalEditException(failString + "new birthdate can not be earlier than mother's birthdate");
            }
            if (previousHorse.getFather() != null && newHorseOlderThan(horseDto, previousHorse.getFather())) {
                throw new IllegalEditException(failString + "new birthdate can not be earlier than father's birthdate");
            }
        }

        // sex can only change if horse has no children
        if (horseDto.sex() != previousHorse.getSex()){
            if (dao.getChildren(horseDto.id()).size() != 0){
                throw new IllegalEditException(failString + "sex can not be changed if the horse has children");
            }
        }


    }


    private boolean newHorseOlderThan(HorseDto horseDto, Horse otherHorse) {
        return horseDto.birthdate().compareTo(otherHorse.getBirthdate()) < 0;
    }

    private boolean newHorseYoungerThan(HorseDto horseDto, Horse otherHorse) {
        return horseDto.birthdate().compareTo(otherHorse.getBirthdate()) > 0;
    }
}
