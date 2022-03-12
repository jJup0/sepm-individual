package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;

import java.time.LocalDate;

/**
 * Class for Horse DTOs
 * Contains all common properties
 */
public record HorseDto(Long id, String name, String description, LocalDate birthdate, HorseBiologicalGender sex, String owner, Long motherId, Long fatherId) {
    @Override
    public String toString() {
        return "HorseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", birthdate=" + birthdate +
                ", sex=" + sex +
                ", owner='" + owner + '\'' +
                ", mother='" + motherId + '\'' +
                ", father='" + fatherId + '\'' +
                '}';
    }
}
