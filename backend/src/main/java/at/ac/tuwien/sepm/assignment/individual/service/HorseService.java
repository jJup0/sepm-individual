package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exception.*;

import java.util.List;

/**
 * Service for working with horses.
 */
public interface HorseService {
    /**
     * Lists all horses stored in the system.
     * @return list of all stored horses
     */
    List<Horse> allHorses() throws MyInternalServerError;

    Horse addHorse(HorseDto horseDto) throws MissingAttributeException, MyInternalServerError;

    Horse getHorse(long id) throws NotFoundException, MyInternalServerError;

    Horse editHorse(HorseDto horseDto) throws MissingAttributeException, IllegalEditException, NotFoundException, MyInternalServerError;

    void deleteHorse(long id);

    List<Horse> searchHorses(HorseSearchDto horseSearchDto) throws MyInternalServerError;

    Horse getHorseFamilyTree(long id, Integer depth) throws NotFoundException, MyInternalServerError;
}
