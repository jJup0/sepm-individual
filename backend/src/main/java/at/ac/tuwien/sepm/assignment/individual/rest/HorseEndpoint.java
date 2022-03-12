package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/horses")
public class HorseEndpoint {
    private final HorseService service;
    private final HorseMapper mapper;

    public HorseEndpoint(HorseService service, HorseMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public Stream<HorseDto> allHorses() {
        return service.allHorses().stream()
                .map(mapper::entityToDto);
    }

    @GetMapping("/{id}")
    public HorseDto getHorse(@PathVariable long id) {
        return mapper.entityToDto(service.getHorse(id));
    }

    @PostMapping
    public HorseDto addHorse(@RequestBody HorseDto horseDto) {
        System.out.println("horseDto: " + horseDto);
        return mapper.entityToDto(service.addHorse(horseDto));
    }

    @PutMapping()
    public HorseDto editHorse(@RequestBody HorseDto horseDto) {
        return mapper.entityToDto(service.editHorse(horseDto));
    }
}
