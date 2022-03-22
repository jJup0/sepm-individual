package at.ac.tuwien.sepm.assignment.individual.validator;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.exception.IllegalEditException;
import at.ac.tuwien.sepm.assignment.individual.exception.MissingAttributeException;

public interface HorseServiceValidator {
    void validateAddHorse(HorseDto horseDto) throws MissingAttributeException;

    void validateEditHorse(HorseDto horseDto) throws MissingAttributeException, IllegalEditException;
}
