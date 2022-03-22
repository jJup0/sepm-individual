package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.exception.IllegalEditException;
import at.ac.tuwien.sepm.assignment.individual.exception.MissingAttributeException;
import at.ac.tuwien.sepm.assignment.individual.exception.MyInternalServerError;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        try {
            return service.allHorses().stream().map(mapper::entityToDto);
        } catch (MyInternalServerError e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching all horses", e);
        }
    }

    @GetMapping("/{id}")
    public HorseDto getHorse(@PathVariable long id) {
        try {
            return mapper.entityToDto(service.getHorse(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find horse with id(" + id + ")");
        } catch (MyInternalServerError e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching horses with id(" + id + ")", e);
        }
    }

    @GetMapping("/familytree/{id}")
    public HorseDto getHorseFamilyTree(@PathVariable long id, @RequestParam(name = "d", required = false) Integer depth) {
        if (depth == null){
            depth = 3;
        }
        try {
            return mapper.entityToDto(service.getHorseFamilyTree(id, depth));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find horse with id(" + id + ")");
        } catch (MyInternalServerError e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching horse family tree with id(" + id + ")", e);
        }
    }

    @GetMapping("/horses/selection")
    public Stream<HorseDto> searchHorses(@RequestParam(name = "n", required = false) String name,
                                         @RequestParam(name = "d", required = false) String description,
                                         @RequestParam(name = "ba", required = false) String bornAfter,
                                         @RequestParam(name = "bb", required = false) String bornBefore,
                                         @RequestParam(name = "s", required = false) HorseBiologicalGender sex,
                                         @RequestParam(name = "o", required = false) Long ownerId,
                                         @RequestParam(name = "l", required = false) Integer limit) {

        LocalDate bornBeforeLD = bornBefore == null ? null : LocalDate.parse(bornBefore);
        LocalDate bornAfterLD = bornAfter == null ? null : LocalDate.parse(bornAfter);

        HorseSearchDto horseSearchDto = new HorseSearchDto(name, description, bornAfterLD, bornBeforeLD, sex, ownerId, limit);

        try {
            return service.searchHorses(horseSearchDto).stream().map(mapper::entityToDto);
        } catch (MyInternalServerError e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching for horses", e);
        }
    }

    @PostMapping
    public HorseDto addHorse(@RequestBody HorseDto horseDto) {
        try {
            return mapper.entityToDto(service.addHorse(horseDto));
        } catch (MissingAttributeException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Horse missing required property", e);
        } catch (MyInternalServerError e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching for horses", e);
        }
    }

    @PutMapping()
    public HorseDto editHorse(@RequestBody HorseDto horseDto) {
        try {
            return mapper.entityToDto(service.editHorse(horseDto));
        } catch (MissingAttributeException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Horse missing required property", e);
        } catch (IllegalEditException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Illegal edit on horse sex, birthdate or parents", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find horse with id(" + horseDto.id() + ")", e);
        } catch (MyInternalServerError e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error editing horse", e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteHorse(@PathVariable long id) {
        service.deleteHorse(id);
    }
}
