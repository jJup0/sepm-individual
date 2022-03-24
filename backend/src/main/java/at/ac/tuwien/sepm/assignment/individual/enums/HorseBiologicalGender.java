package at.ac.tuwien.sepm.assignment.individual.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public enum HorseBiologicalGender {

    male ("male"),
    female ("female");

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String sexString;

    HorseBiologicalGender(String s) {
        sexString = s;
    }

    public String toString() {
        LOGGER.trace("toString() returning {}", this.sexString);
        return this.sexString;
    }
}
