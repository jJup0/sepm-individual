package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;

import java.util.List;

public interface OwnerDao {
    List<Owner> getAll();

}
