package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.DirtyHorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDtoIdReferences;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.exception.*;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import at.ac.tuwien.sepm.assignment.individual.validator.HorseServiceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
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
    public Horse addHorse(HorseDtoIdReferences horseDto) throws MissingAttributeException, ServiceException, ConstraintViolation, NotFoundException {
        LOGGER.trace("addHorse({}) called", horseDto);

        try {
            validator.validateAddHorse(horseDto);
        } catch (MissingAttributeException | ConstraintViolation | NotFoundException e) {
            throw e;
        } catch (PersistenceException persistenceException){
            throw new ServiceException(persistenceException);
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
    public Horse editHorse(HorseDtoIdReferences horseDto) throws MissingAttributeException, ConstraintViolation, NotFoundException, ServiceException {
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
    public List<Horse> searchHorses(DirtyHorseSearchDto dirtyHorseSearchDto) throws ServiceException, NotParsableValueException {
        LOGGER.trace("searchHorses({}) called", dirtyHorseSearchDto);

        // Spring can somehow not convert to LocalDate when parameters are passed directly to DTO in the endpoint so validate them here:
        LocalDate cleanBornBefore = validator.validateDate(dirtyHorseSearchDto.bornBefore());
        LocalDate cleanBornAfter = validator.validateDate(dirtyHorseSearchDto.bornAfter());
        HorseBiologicalGender cleanHorseSex = validator.validateSex(dirtyHorseSearchDto.sex());

        HorseSearchDto cleanSearchDto = new HorseSearchDto(dirtyHorseSearchDto.name(), dirtyHorseSearchDto.description(), cleanBornAfter, cleanBornBefore, cleanHorseSex, dirtyHorseSearchDto.ownerId(), dirtyHorseSearchDto.limit());

        try {
            return dao.searchHorses(cleanSearchDto);
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
