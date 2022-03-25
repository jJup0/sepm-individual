package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.mapper.OwnerMapper;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/owners")
public class OwnerEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OwnerService service;
    private final OwnerMapper mapper;

    /**
     * Constructor for rest endpoint for all owner related requests
     *
     * @param service Owner service component to relay requests to
     * @param mapper  Owner mapper to map incoming entities to DTOs
     */
    public OwnerEndpoint(OwnerService service, OwnerMapper mapper) {
        LOGGER.trace("OwnerEndpoint constructed");

        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Get all owners.
     *
     * @return a stream of all stored owners as DTOs
     * @throws ResponseStatusException if some internal error occurs in the database
     */
    @GetMapping
    public Stream<OwnerDto> allOwners() {
        LOGGER.trace("all Owners requested");

        try {
            return service.allOwners().stream().map(mapper::entityToDto);
        } catch (ServiceException e) {
            LOGGER.error("allOwners() Internal server error:\n" + e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching all owners", e);
        }
    }

    /**
     * Get owners matching search term.
     *
     * @param term String search term to search for
     * @return a stream owners (maximum of 5) matching that search term
     * @throws ResponseStatusException if some internal error occurs in the database
     */
    @GetMapping(path = "/selection")
    public Stream<OwnerDto> searchOwners(@RequestParam(name = "name", required = false) String term) {
        LOGGER.trace("Owners matching {} requested", term);

        try {
            return service.searchOwners(term).stream().map(mapper::entityToDto);
        } catch (ServiceException e) {
            LOGGER.error("allOwners() Internal server error:\n" + e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching for owners", e);
        }
    }
}
