package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;

import java.util.List;

public interface OwnerDao {
    List<Owner> getAll() throws PersistenceException;

    Owner getWithId(long id) throws PersistenceException, NotFoundException;
}
