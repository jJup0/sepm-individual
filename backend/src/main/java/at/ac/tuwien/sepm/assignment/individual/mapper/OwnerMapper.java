package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {
    public OwnerDto entityToDto(Owner owner) {
        if (owner == null) {
            return null;
        }
        return new OwnerDto(owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail());
    }

    public Owner dtoToEntity(OwnerDto ownerDto) {
        // TODO log this and in horse mapper check for null?
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
