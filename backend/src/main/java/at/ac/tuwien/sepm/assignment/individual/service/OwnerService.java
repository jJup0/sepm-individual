package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;

import java.util.List;

public interface OwnerService {
    List<Owner> allOwners();

    Owner getWithId(long id);
}
