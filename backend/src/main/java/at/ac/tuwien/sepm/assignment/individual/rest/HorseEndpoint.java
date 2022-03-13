package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/search")
    public Stream<HorseDto> searchHorses(@RequestParam(name = "st") String searchTerm, @RequestParam(name = "s") HorseBiologicalGender sex, @RequestParam(name = "bb") String bornBefore) {
        HorseSearchDto horseSearchDto = new HorseSearchDto(searchTerm, sex, LocalDate.parse(bornBefore));
        return service.searchHorses(horseSearchDto).stream()
                .map(mapper::entityToDto);
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

    @DeleteMapping("/{id}")
    public void deleteHorse(@PathVariable long id) {
        service.deleteHorse(id);
    }
}
