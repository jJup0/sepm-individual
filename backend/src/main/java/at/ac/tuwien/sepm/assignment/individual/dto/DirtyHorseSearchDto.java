package at.ac.tuwien.sepm.assignment.individual.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Class for horse search parameter DTOs coming from outside
 * Contains all possible search parameters
 */
public record DirtyHorseSearchDto(String name, String description, String bornAfter, String bornBefore, String sex, Long ownerId, Integer limit) {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public String toString() {
        LOGGER.trace("toString() called");
        return "DirtyHorseSearchDto{" +
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
