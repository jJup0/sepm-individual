package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
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
    public List<Owner> allOwners() {
        return dao.getAll();
    }
}
