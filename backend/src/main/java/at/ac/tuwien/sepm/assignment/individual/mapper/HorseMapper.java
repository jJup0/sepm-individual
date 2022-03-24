package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class HorseMapper {
    private final OwnerMapper ownerMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public HorseMapper(OwnerMapper ownerMapper){
        this.ownerMapper = ownerMapper;
    }

    public HorseDto entityToDto(Horse horse) {
        LOGGER.trace("entityToDto() called on {}", horse);
        // normal if parent depth limit reached
        if (horse == null){
            return null;
        }
        return new HorseDto(horse.getId(), horse.getName(), horse.getDescription(), horse.getBirthdate(), horse.getSex(), ownerMapper.entityToDto(horse.getOwner()), entityToDto(horse.getMother()), entityToDto(horse.getFather()));
    }

    public Horse dtoToEntity(HorseDto horseDto) {
        LOGGER.trace("dtoToEntity() called on {}", horseDto);
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
