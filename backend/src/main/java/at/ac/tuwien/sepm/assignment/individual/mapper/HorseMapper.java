package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import org.springframework.stereotype.Component;

@Component
public class HorseMapper {

    public HorseDto entityToDto(Horse horse) {
        return new HorseDto(horse.getId(), horse.getName(), horse.getDescription(), horse.getBirthdate(), horse.getSex(), horse.getOwner(), horse.getMotherId(), horse.getFatherId());
    }

    public Horse dtoToEntity(HorseDto horseDto) {
        Horse newHorse = new Horse();
        newHorse.setId(horseDto.id());
        newHorse.setName(horseDto.name());
        newHorse.setDescription(horseDto.description());
        newHorse.setBirthdate(horseDto.birthdate());
        newHorse.setSex(horseDto.sex());
        newHorse.setOwner(horseDto.owner());
        newHorse.setMotherId(horseDto.motherId());
        newHorse.setFatherId(horseDto.fatherId());
        return newHorse;
    }
}
