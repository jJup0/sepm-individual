package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDtoIdReferences;
import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.HorseIdReferences;
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

    /**
     * Maps a horse entity with id references to an entity, sets parents null
     *
     * @param horseIdReferences The horse with ids as parent references
     * @return A horse entity matching the given id reference entity
     */
    public Horse referenceEntityToRecurisve(HorseIdReferences horseIdReferences) {
        LOGGER.trace("dtoToEntity() called on {}", horseIdReferences);
        if (horseIdReferences == null) {
            return null;
        }
        Horse newHorse = new Horse();
        newHorse.setId(horseIdReferences.getId());
        newHorse.setName(horseIdReferences.getName());
        newHorse.setDescription(horseIdReferences.getDescription());
        newHorse.setBirthdate(horseIdReferences.getBirthdate());
        newHorse.setSex(horseIdReferences.getSex());
        newHorse.setOwner(horseIdReferences.getOwner());
        return newHorse;
    }

    /**
     * Maps a horse DTO with id references to a pseudo recursive DTO
     *
     * @param horseDtoIdReferences The DTO with id references to convert
     * @return A recursive horse DTO representing the given ID reference DTO
     */
    public HorseDto idReferenceDtoToDto(HorseDtoIdReferences horseDtoIdReferences) {
        return new HorseDto(
                horseDtoIdReferences.id(),
                horseDtoIdReferences.name(),
                horseDtoIdReferences.description(),
                horseDtoIdReferences.birthdate(),
                horseDtoIdReferences.sex(),
                horseDtoIdReferences.ownerId() == null ? null : new OwnerDto(horseDtoIdReferences.ownerId(), null, null, null),
                horseDtoIdReferences.motherId() == null ? null :new HorseDto(horseDtoIdReferences.motherId(), null, null, null, null, null, null, null),
                horseDtoIdReferences.fatherId() == null ? null :new HorseDto(horseDtoIdReferences.fatherId(), null, null, null, null, null, null, null)
        );
    }

    /**
     * Maps a recursive horse DTO to a DTO with id references
     *
     * @param horseDto The recursive DTO to convert
     * @return A DTO with id references representing the given recursive DTO
     */
    public HorseDtoIdReferences recursiveDtoToReferenceIdDto(HorseDto horseDto) {
        return new HorseDtoIdReferences(
                horseDto.id(),
                horseDto.name(),
                horseDto.description(),
                horseDto.birthdate(),
                horseDto.sex(),
                horseDto.owner() == null ? null : horseDto.owner().id(),
                horseDto.mother() == null ? null : horseDto.mother().id(),
                horseDto.father() == null ? null : horseDto.father().id()
        );
    }
}
