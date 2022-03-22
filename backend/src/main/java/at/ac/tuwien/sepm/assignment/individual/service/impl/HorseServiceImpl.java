package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exception.*;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import at.ac.tuwien.sepm.assignment.individual.validator.HorseServiceValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorseServiceImpl implements HorseService {
    private final HorseDao dao;
    private final HorseServiceValidator validator;

    public HorseServiceImpl(HorseDao dao, HorseServiceValidator validator) {
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    public List<Horse> allHorses() throws MyInternalServerError {
        try {
            return dao.getAll();
        } catch (PersistenceException e) {
            throw new MyInternalServerError(e);
        }
    }

    @Override
    public Horse addHorse(HorseDto horseDto) throws MissingAttributeException, MyInternalServerError {
        try {
            validator.validateAddHorse(horseDto);
        } catch (MissingAttributeException missingAttributeException) {
            throw missingAttributeException;
        }
        try {
            return dao.addHorse(horseDto);
        } catch (PersistenceException e) {
            throw new MyInternalServerError(e);
        }
    }

    // TODO not found error
    @Override
    public Horse getHorse(long id) throws NotFoundException, MyInternalServerError {
        try {
            return dao.getHorse(id);
        } catch (PersistenceException e) {
            throw new MyInternalServerError(e);
        }
    }

    @Override
    public Horse editHorse(HorseDto horseDto) throws MissingAttributeException, IllegalEditException, NotFoundException, MyInternalServerError {
        try {
            validator.validateEditHorse(horseDto);
        } catch (PersistenceException e) {
            throw new MyInternalServerError(e);
        }
        try {
            return dao.editHorse(horseDto);
        } catch (PersistenceException e) {
            throw new MyInternalServerError(e);
        }
    }

    @Override
    public void deleteHorse(long id) {
        dao.deleteHorse(id);
    }

    @Override
    public List<Horse> searchHorses(HorseSearchDto horseSearchDto) throws MyInternalServerError {
        try {
            return dao.searchHorses(horseSearchDto);
        } catch (PersistenceException e) {
            throw new MyInternalServerError(e);
        }
    }

    @Override
    public Horse getHorseFamilyTree(long id, Integer depth) throws NotFoundException, MyInternalServerError {
        try {
            return dao.getFamilyTreeOfHorse(id, depth);
        } catch (PersistenceException e) {
            throw new MyInternalServerError(e);
        }
    }

}
