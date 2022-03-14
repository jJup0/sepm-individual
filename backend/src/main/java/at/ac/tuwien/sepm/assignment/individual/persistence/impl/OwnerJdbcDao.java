package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.mapper.OwnerMapper;
import at.ac.tuwien.sepm.assignment.individual.persistence.OwnerDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OwnerJdbcDao implements OwnerDao {
    private static final String TABLE_NAME = "owner";
    private static final int MAX_SEARCH_RESULTS = 1000;
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME + " LIMIT " + MAX_SEARCH_RESULTS;

    private final JdbcTemplate jdbcTemplate;
    private final OwnerMapper mapper;


    public OwnerJdbcDao(JdbcTemplate jdbcTemplate, OwnerMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<Owner> getAll() {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, this::mapOwnerRow);
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not query all owners", e);
        }
    }

    private Owner mapOwnerRow(ResultSet result, int rowNum) throws SQLException {
        Owner owner = new Owner();
        owner.setId(result.getLong("id"));
        owner.setFirstName(result.getString("firstName"));
        owner.setLastName(result.getString("lastName"));
        owner.setEmail(result.getString("email"));
        return owner;
    }
}
