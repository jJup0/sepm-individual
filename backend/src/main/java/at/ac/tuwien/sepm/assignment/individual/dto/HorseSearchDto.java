package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;

import java.time.LocalDate;

public record HorseSearchDto(String searchTerm, HorseBiologicalGender sex, LocalDate bornBefore) {
    @Override
    public String toString() {
        return "HorseSearchDto{" +
                "searchTerm='" + searchTerm + '\'' +
                ", sex=" + sex +
                ", bornBefore=" + bornAfter +
                '}';
    }
}
