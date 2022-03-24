package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.exception.IllegalEditException;
import at.ac.tuwien.sepm.assignment.individual.exception.MissingAttributeException;
import at.ac.tuwien.sepm.assignment.individual.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/horses")
public class HorseEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HorseService service;
    private final HorseMapper mapper;

    public HorseEndpoint(HorseService service, HorseMapper mapper) {
        LOGGER.trace("HorseEndpoint constructed");

        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public Stream<HorseDto> allHorses() {
        LOGGER.info("allHorses requested");

        try {
            return service.allHorses().stream().map(mapper::entityToDto);
        } catch (ServiceException e) {
            LOGGER.error("allHorses() Internal server error:\n" + e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching all horses", e);
        }
    }

    @GetMapping("/{id}")
    public HorseDto getHorse(@PathVariable long id) {
        LOGGER.info("getHorse with id {} requested", id);

        try {
            return mapper.entityToDto(service.getHorse(id));
        } catch (NotFoundException e) {
            LOGGER.error("Could not find horse with id(" + id + ")\n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find horse with id(" + id + ")");
        } catch (ServiceException e) {
            LOGGER.error("Internal server error:\n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching horses with id(" + id + ")", e);
        }
    }

    @GetMapping("/familytree/{id}")
    public HorseDto getHorseFamilyTree(@PathVariable long id, @RequestParam(name = "d", required = false) Integer depth) {
        LOGGER.info("family tree of horse with id {} and depth {} requested", id, depth);

        if (depth == null){
            depth = 3;
        }
        try {
            return mapper.entityToDto(service.getHorseFamilyTree(id, depth));
        } catch (NotFoundException e) {
            LOGGER.error("Could not find horse with id(" + id + ")\n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find horse with id(" + id + ")");
        } catch (ServiceException e) {
            LOGGER.error("getHorseFamilyTree() Internal server error:\n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching horse family tree with id(" + id + ")", e);
        }
    }

    @GetMapping("/selection")
    public Stream<HorseDto> searchHorses(HorseSearchDto horseSearchDto) {
        LOGGER.info("horse search request with parameters {}", horseSearchDto);

        try {
            return service.searchHorses(horseSearchDto).stream().map(mapper::entityToDto);
        } catch (ServiceException e) {
            LOGGER.error("searchHorses() Internal server error:\n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching for horses", e);
        }
    }

    @PostMapping
    public HorseDto addHorse(@RequestBody HorseDto horseDto) {
        LOGGER.info("add horse request with horse: {}", horseDto);

        try {
            return mapper.entityToDto(service.addHorse(horseDto));
        } catch (MissingAttributeException e) {
            LOGGER.error("Horse missing required property: \n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Horse missing required property", e);
        } catch (ServiceException e) {
            LOGGER.error("addHorse() Internal server error:\n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching for horses", e);
        }
    }

    @PutMapping()
    public HorseDto editHorse(@RequestBody HorseDto horseDto) {
        LOGGER.info("edit horse request with horse: {}", horseDto);

        try {
            return mapper.entityToDto(service.editHorse(horseDto));
        } catch (MissingAttributeException e) {
            LOGGER.error("Horse missing required property\n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Horse missing required property", e);
        } catch (IllegalEditException e) {
            LOGGER.error("Illegal edit on horse sex, birthdate or parents\n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Illegal edit on horse sex, birthdate or parents", e);
        } catch (NotFoundException e) {
            LOGGER.error("Could not find horse with id(" + horseDto.id() + ")\n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find horse with id(" + horseDto.id() + ")", e);
        } catch (ServiceException e) {
            LOGGER.error("editHorse() Internal server error:\n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error editing horse", e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteHorse(@PathVariable long id) {

        LOGGER.info("delete horse request with id: {}", id);
        service.deleteHorse(id);
        // TODO catch internal server error
    }
}
