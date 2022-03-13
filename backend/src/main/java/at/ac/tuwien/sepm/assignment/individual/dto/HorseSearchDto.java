package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;

public record HorseSearchDto(String searchTerm, HorseBiologicalGender sex) {
    @Override
    public String toString() {
        return "HorseSearchDto{" +
                "searchTerm='" + searchTerm + '\'' +
                ", sex=" + sex +
                '}';
    }
}
