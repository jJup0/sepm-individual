package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import org.springframework.stereotype.Component;

@Component
public class HorseMapper {
    private final OwnerMapper ownerMapper;

    public HorseMapper(OwnerMapper ownerMapper){
        this.ownerMapper = ownerMapper;
    }

    public HorseDto entityToDto(Horse horse) {
        // normal if parent depth limit reached
        if (horse == null){
            return null;
        }
        return new HorseDto(horse.getId(), horse.getName(), horse.getDescription(), horse.getBirthdate(), horse.getSex(), ownerMapper.entityToDto(horse.getOwner()), entityToDto(horse.getMother()), entityToDto(horse.getFather()));
    }

    public Horse dtoToEntity(HorseDto horseDto) {
        // normal if parent depth limit reached
        if (horseDto == null){
            return null;
        }
        Horse newHorse = new Horse();
        newHorse.setId(horseDto.id());
        newHorse.setName(horseDto.name());
        newHorse.setDescription(horseDto.description());
        newHorse.setBirthdate(horseDto.birthdate());
        newHorse.setSex(horseDto.sex());
        newHorse.setOwner(ownerMapper.dtoToEntity(horseDto.owner()));
        newHorse.setMother(dtoToEntity(horseDto.mother()));
        newHorse.setFather(dtoToEntity(horseDto.father()));
        return newHorse;
    }
}
