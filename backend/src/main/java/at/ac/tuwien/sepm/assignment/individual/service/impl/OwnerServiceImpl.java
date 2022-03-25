package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.OwnerDao;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;


@Service
public class OwnerServiceImpl implements OwnerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OwnerDao dao;

    public OwnerServiceImpl(OwnerDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Owner> allOwners() throws ServiceException {
        LOGGER.trace("allOwners() called");

        try {
            return dao.getAll();
        } catch (PersistenceException e) {
            throw new ServiceException("Error fetching all owners", e);
        }
    }

    @Override
    public Owner getWithId(long id) throws NotFoundException, ServiceException {
        LOGGER.trace("getWithId({}) called", id);

        try {
            return dao.getOwner(id);
        } catch (PersistenceException e) {
            throw new ServiceException("Error fetching owner with id(" + id + ")", e);
        }
    }

    @Override
    public List<Owner> searchOwners(String term) throws ServiceException {
        LOGGER.trace("searchOwners({}) called", term);

        try {
            return dao.searchOwners(term);
        } catch (PersistenceException e) {
            throw new ServiceException("Error searching for owners", e);
        }
    }
}
