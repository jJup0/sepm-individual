package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exception.IllegalEditException;
import at.ac.tuwien.sepm.assignment.individual.exception.MissingAttributeException;
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
    public List<Horse> allHorses() {
        return dao.getAll();
    }

    @Override
    public Horse addHorse(HorseDto horseDto) throws MissingAttributeException {
        try {
            validator.validateAddHorse(horseDto);
        } catch (MissingAttributeException missingAttributeException){
            throw missingAttributeException;
        }
        return dao.addHorse(horseDto);
    }

    // TODO not found error
    @Override
    public Horse getHorse(long id) {
        return dao.getHorse(id);
    }

    @Override
    public Horse editHorse(HorseDto horseDto) throws MissingAttributeException, IllegalEditException {
        try {
            validator.validateEditHorse(horseDto);
        } catch (MissingAttributeException missingAttributeException){
            throw missingAttributeException;
        } catch (IllegalEditException illegalEditException){
            throw illegalEditException;
        }
        return dao.editHorse(horseDto);
    }

    @Override
    public void deleteHorse(long id) {
        dao.deleteHorse(id);
    }

    @Override
    public List<Horse> searchHorses(HorseSearchDto horseSearchDto) {
        return dao.searchHorses(horseSearchDto);
    }

    @Override
    public Horse getHorseFamilyTree(long id, Integer depth) {
        return dao.getHorseWithFamilyTree(id, depth);
    }

}
