package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.mapper.OwnerMapper;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/owners")
public class OwnerEndpoint {

    private final OwnerService service;
    private final OwnerMapper mapper;

    public OwnerEndpoint(OwnerService service, OwnerMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public Stream<OwnerDto> allOwners() {
        return service.allOwners().stream().map(mapper::entityToDto);
    }
}
