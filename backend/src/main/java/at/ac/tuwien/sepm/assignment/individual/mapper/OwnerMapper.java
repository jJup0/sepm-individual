package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class OwnerMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public OwnerDto entityToDto(Owner owner) {
        LOGGER.trace("entityToDto() called on {}", owner);

        if (owner == null) {
            return null;
        }
        return new OwnerDto(owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail());
    }

    public Owner dtoToEntity(OwnerDto ownerDto) {
        LOGGER.trace("dtoToEntity() called on {}", ownerDto);
        if (ownerDto == null) {
            return null;
        }
        Owner owner = new Owner();
        owner.setId(ownerDto.id());
        owner.setFirstName(ownerDto.firstName());
        owner.setLastName(ownerDto.lastName());
        owner.setEmail(ownerDto.email());
        return owner;
    }
}
