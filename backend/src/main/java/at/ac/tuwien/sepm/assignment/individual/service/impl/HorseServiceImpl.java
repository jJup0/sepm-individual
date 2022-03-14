package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorseServiceImpl implements HorseService {
    private final HorseDao dao;

    public HorseServiceImpl(HorseDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Horse> allHorses() {
        return dao.getAll();
    }

    @Override
    public Horse addHorse(HorseDto horseDto) {
        // TODO check for null date
        return dao.addHorse(horseDto);
    }

    // TODO not found error
    @Override
    public Horse getHorse(long id) {
        return dao.getHorse(id);
    }

    @Override
    public Horse editHorse(HorseDto horseDto) {
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

}
