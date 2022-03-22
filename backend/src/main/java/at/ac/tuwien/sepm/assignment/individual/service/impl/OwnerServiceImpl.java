package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.exception.MyInternalServerError;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.OwnerDao;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OwnerServiceImpl implements OwnerService {
    private final OwnerDao dao;

    public OwnerServiceImpl(OwnerDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Owner> allOwners() throws MyInternalServerError {
        try {
            return dao.getAll();
        } catch (PersistenceException e) {
            throw new MyInternalServerError("Error fetching all owners", e);
        }
    }

    @Override
    public Owner getWithId(long id) throws NotFoundException, MyInternalServerError {
        try {
            return dao.getWithId(id);
        } catch (PersistenceException e) {
            throw new MyInternalServerError("Error fetching owner with id(" + id + ")", e);
        }
    }
}
