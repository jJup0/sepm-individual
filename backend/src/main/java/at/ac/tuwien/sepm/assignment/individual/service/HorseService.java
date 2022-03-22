package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exception.IllegalEditException;
import at.ac.tuwien.sepm.assignment.individual.exception.MissingAttributeException;

import java.util.List;

/**
 * Service for working with horses.
 */
public interface HorseService {
    /**
     * Lists all horses stored in the system.
     * @return list of all stored horses
     */
    List<Horse> allHorses();

    Horse addHorse(HorseDto horseDto) throws MissingAttributeException;

    Horse getHorse(long id);

    Horse editHorse(HorseDto horseDto) throws MissingAttributeException, IllegalEditException;

    void deleteHorse(long id);

    List<Horse> searchHorses(HorseSearchDto horseSearchDto);

    Horse getHorseFamilyTree(long id, Integer depth);
}
