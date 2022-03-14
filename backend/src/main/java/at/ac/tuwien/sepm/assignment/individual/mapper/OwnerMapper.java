package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {
    public OwnerDto entityToDto(Owner owner){
        return new OwnerDto(owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail());
    }

    public Owner dtoToEntity(OwnerDto ownerDto){
        Owner owner = new Owner();
        owner.setId(ownerDto.id());
        owner.setFirstName(ownerDto.firstName());
        owner.setLastName(ownerDto.lastName());
        owner.setEmail(ownerDto.email());
        return owner;
    }
}
