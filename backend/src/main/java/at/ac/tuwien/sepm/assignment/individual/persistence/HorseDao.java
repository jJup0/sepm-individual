package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;

import java.util.List;

/**
 * Data Access Object for horses.
 * Implements access functionality to the application's persistent data store regarding horses.
 */
public interface HorseDao {
    /**
     * Get all horses stored in the persistent data store.
     * @return a list of all stored horses
     */
    List<Horse> getAll();

    Horse addHorse(HorseDto horseDto);

    Horse getHorse(long id);

    Horse editHorse(HorseDto horseDto);

    void deleteHorse(long id);

    List<Horse> searchHorses(HorseSearchDto horseSearchDto);
}
