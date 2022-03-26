package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;

/**
 * Class for Horse DTOs
 * Contains all common properties
 */
public record HorseDtoIdReferences(Long id, String name, String description, LocalDate birthdate, HorseBiologicalGender sex, Long ownerId, Long motherId, Long fatherId) {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public String toString() {
        LOGGER.trace("toString() called");
        return "HorseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", birthdate=" + birthdate +
                ", sex=" + sex +
                ", owner=" + ownerId +
                ", mother=" + motherId +
                ", father=" + fatherId +
                '}';
    }
}