package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;

/**
 * Class for horse search parameter DTOs
 * Contains all possible search parameters
 */
public record HorseSearchDto(String name, String description, LocalDate bornAfter, LocalDate bornBefore, HorseBiologicalGender sex, Long ownerId, Integer limit) {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public String toString() {
        LOGGER.trace("toString() called");
        return "HorseSearchDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", bornAfter=" + bornAfter +
                ", bornBefore=" + bornBefore +
                ", sex=" + sex +
                ", ownerId=" + ownerId +
                ", limit=" + limit +
                '}';
    }
}
