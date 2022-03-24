package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

/**
 * Component that maps horse DTOs to entities and vice versa
 */
@Component
public class HorseMapper {
    private final OwnerMapper ownerMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Simple constructor for mapper.
     *
     * @param ownerMapper Injected owner mapper, to map horse owners to respected datastructe
     */
    public HorseMapper(OwnerMapper ownerMapper) {
        this.ownerMapper = ownerMapper;
    }

    /**
     * Maps a horse entity to a DTO.
     *
     * @param horse The horse entity to be converted
     * @return A horse DTO matching the given entity
     */
    public HorseDto entityToDto(Horse horse) {
        LOGGER.trace("entityToDto() called on {}", horse);
        // normal if parent depth limit reached
        if (horse == null) {
            return null;
        }
        return new HorseDto(horse.getId(), horse.getName(), horse.getDescription(), horse.getBirthdate(), horse.getSex(), ownerMapper.entityToDto(horse.getOwner()), entityToDto(horse.getMother()), entityToDto(horse.getFather()));
    }


    /**
     * Maps a horse DTO to an entity
     *
     * @param horseDto The horse DTO to be converted
     * @return A horse entity matching the given DTO
     */
    public Horse dtoToEntity(HorseDto horseDto) {
        LOGGER.trace("dtoToEntity() called on {}", horseDto);
        // normal if parent depth limit reached
        if (horseDto == null) {
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
