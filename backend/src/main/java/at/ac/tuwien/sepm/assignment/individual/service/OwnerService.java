package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;

import java.util.List;

public interface OwnerService {
    List<Owner> allOwners() throws ServiceException;

    Owner getWithId(long id) throws NotFoundException, ServiceException;
}
