package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.exception.*;
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

/**
 * Rest endpoint for horses
 */
@RestController
@RequestMapping(path = "/horses")
public class HorseEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HorseService service;
    private final HorseMapper mapper;

    /**
     * Constructor for rest endpoint for all horse related requests
     *
     * @param service Horse service component to relay requests to
     * @param mapper  Horse mapper to map incoming entities to DTOs
     */
    public HorseEndpoint(HorseService service, HorseMapper mapper) {
        LOGGER.trace("HorseEndpoint constructed");

        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Gets all horses.
     *
     * @return a stream of all stored horses as DTOs
     * @throws ResponseStatusException if internal error occurs
     */
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

    /**
     * Get a specific horse according to the given id.
     *
     * @param id ID of the horse to be returned
     * @return The horse that was requested as a DTO
     * @throws ResponseStatusException if some internal error occurs in the database, or horse could not be found
     */
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

    /**
     * Gets the family tree according to a given horse ID.
     *
     * @param id    ID of the horse for which the family tree should be fetched
     * @param depth The amount of generations that should be returned to the family tree.
     * @return A single horse DTO with recursive mother and father DTOs with a maximum depth of ancestorDepth.
     * @throws ResponseStatusException if some internal error occurs in the database or if the ID could not be found in the database
     */
    @GetMapping("/familytree/{id}")
    public HorseDto getHorseFamilyTree(@PathVariable long id, @RequestParam(name = "depth", required = false) Integer depth) {
        LOGGER.info("family tree of horse with id {} and depth {} requested", id, depth);

        if (depth == null) {
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

    /**
     * Searches for horses matching the search DTO
     *
     * @param horseSearchDto A horse search DTO containing all the parameters that a horse must have
     * @return A stream of all the horses (as DTOs) that match all the parameters
     * @throws ResponseStatusException if some internal error occurs in the database
     */
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

    /**
     * Adds a given horse DTO.
     *
     * @param horseDto The horse that should be added
     * @return an entity version of the added horse including an ID
     * @throws PersistenceException if some internal error occurs in the database
     */
    @PostMapping
    public HorseDto addHorse(@RequestBody HorseDto horseDto) {
        LOGGER.info("add horse request with horse: {}", horseDto);

        try {
            return mapper.entityToDto(service.addHorse(horseDto));
        } catch (MissingAttributeException e) {
            LOGGER.error("Horse missing required property: \n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Horse missing required property", e);
        } catch (ConstraintViolation e) {
            LOGGER.error("Horse parents or birthday violating constraints: \n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Horse violating constraints", e);
        } catch (NotFoundException e) {
            LOGGER.error("Horse parents not found: \n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Horse parents not found", e);
        } catch (ServiceException e) {
            LOGGER.error("addHorse() Internal server error:\n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching for horses", e);
        }
    }

    /**
     * Edits a horse according to the ID given in the horseDto.
     *
     * @param horseDto A horse DTO containing all the information that should be updated for a horse.
     *                 The chosen horse that will be updated is determined by ID.
     * @return An entity version of the given horseDto
     * @throws ResponseStatusException if some internal error occurs in the database or if the ID could not be found in
     *                                 the database or if the horse is missing some required properties or if the requested
     *                                 edits violate constraints
     */
    @PutMapping()
    public HorseDto editHorse(@RequestBody HorseDto horseDto) {
        LOGGER.info("edit horse request with horse: {}", horseDto);

        try {
            return mapper.entityToDto(service.editHorse(horseDto));
        } catch (MissingAttributeException e) {
            LOGGER.error("Horse missing required property\n" + e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Horse missing required property", e);
        } catch (ConstraintViolation e) {
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

    /**
     * Deletes a horse.
     *
     * @param id ID of the horse that should be deleted
     */
    @DeleteMapping("/{id}")
    public void deleteHorse(@PathVariable long id) {

        LOGGER.info("delete horse request with id: {}", id);
        service.deleteHorse(id);
        // TODO catch internal server error
    }
}
