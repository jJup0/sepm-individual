package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;

import java.time.LocalDate;

public record HorseSearchDto(String name, String description, LocalDate bornAfter, LocalDate bornBefore, HorseBiologicalGender sex, Long ownerId, Integer limit) {

}
