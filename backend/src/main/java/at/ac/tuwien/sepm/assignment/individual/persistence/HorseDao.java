package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
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
     */
    List<Horse> getAll() throws PersistenceException;

    Horse addHorse(HorseDto horseDto) throws PersistenceException;

    Horse getHorse(long id) throws PersistenceException, NotFoundException;

    List<Horse> getChildren(long id) throws PersistenceException, NotFoundException;

    Horse editHorse(HorseDto horseDto) throws PersistenceException;

    void deleteHorse(long id);

    List<Horse> searchHorses(HorseSearchDto horseSearchDto) throws PersistenceException;

    Horse getFamilyTreeOfHorse(long id, int ancestorDepth) throws PersistenceException, NotFoundException;
}
