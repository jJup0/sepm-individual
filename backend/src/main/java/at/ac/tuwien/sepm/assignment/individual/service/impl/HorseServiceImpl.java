package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exception.*;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import at.ac.tuwien.sepm.assignment.individual.validator.HorseServiceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class HorseServiceImpl implements HorseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HorseDao dao;
    private final HorseServiceValidator validator;

    public HorseServiceImpl(HorseDao dao, HorseServiceValidator validator) {
        LOGGER.trace("HorseServiceImpl constructed");
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    public List<Horse> allHorses() throws ServiceException {
        LOGGER.trace("allHorses() called");
        try {
            return dao.getAll();
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Horse addHorse(HorseDto horseDto) throws MissingAttributeException, ServiceException {
        LOGGER.trace("addHorse({}) called", horseDto);

        try {
            validator.validateAddHorse(horseDto);
        } catch (MissingAttributeException missingAttributeException) {
            throw missingAttributeException;
        }
        try {
            return dao.addHorse(horseDto);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Horse getHorse(long id) throws NotFoundException, ServiceException {
        LOGGER.trace("getHorse({}) called", id);

        try {
            return dao.getHorse(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Horse editHorse(HorseDto horseDto) throws MissingAttributeException, IllegalEditException, NotFoundException, ServiceException {
        LOGGER.trace("editHorse({}) called", horseDto);

        try {
            validator.validateEditHorse(horseDto);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
        try {
            return dao.editHorse(horseDto);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteHorse(long id) {

        LOGGER.trace("deleteHorse({}) called", id);
        dao.deleteHorse(id);
    }

    @Override
    public List<Horse> searchHorses(HorseSearchDto horseSearchDto) throws ServiceException {
        LOGGER.trace("searchHorses({}) called", horseSearchDto);

        try {
            return dao.searchHorses(horseSearchDto);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Horse getHorseFamilyTree(long id, Integer depth) throws NotFoundException, ServiceException {
        LOGGER.trace("searchHorses({}, {}) called", id, depth);

        try {
            return dao.getFamilyTreeOfHorse(id, depth);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

}
