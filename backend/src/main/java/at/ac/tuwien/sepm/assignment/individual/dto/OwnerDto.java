package at.ac.tuwien.sepm.assignment.individual.dto;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public record OwnerDto(Long id, String firstName, String lastName, String email) {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public String toString() {
        LOGGER.trace("toString() called");
        return "OwnerDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
