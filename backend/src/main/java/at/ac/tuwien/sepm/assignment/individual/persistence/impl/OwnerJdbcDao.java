package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.mapper.OwnerMapper;
import at.ac.tuwien.sepm.assignment.individual.persistence.OwnerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OwnerJdbcDao implements OwnerDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String ESCAPE_CHAR = "!";
    private static final String TABLE_NAME = "owner";
    private static final int SEARCH_LIMIT = 5;
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final String SQL_SELECT_WITH_ID = "SELECT * FROM " + TABLE_NAME + " WHERE ID = ?";
    private static final String SQL_SEARCH = "SELECT * FROM " + TABLE_NAME + " WHERE CONCAT(lower(firstname), ' ', lower(lastname)) LIKE ? ESCAPE '" + ESCAPE_CHAR + "' LIMIT " + SEARCH_LIMIT;



    private final JdbcTemplate jdbcTemplate;
    private final OwnerMapper mapper;


    public OwnerJdbcDao(JdbcTemplate jdbcTemplate, OwnerMapper mapper) {
        LOGGER.trace("OwnerJdbcDao constructed");
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<Owner> getAll() throws PersistenceException {
        LOGGER.trace("getAll() called");
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, this::mapOwnerRow);
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not query all owners", e);
        }
    }

    @Override
    public Owner getOwner(long id) throws NotFoundException {
        LOGGER.trace("getWithId({}) called", id);
        List<Owner> owners = jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_SELECT_WITH_ID);
            ps.setLong(1, id);
            return ps;
        }, this::mapOwnerRow);
        if (owners.size() == 0) {
            throw new NotFoundException("Could not find owner with id(" + id + ")");
        }
        return owners.get(0);
    }

    /**
     * Get owners matching search term.
     *
     * @param term String search term to search for
     * @return a list owners (maximum of 5) matching that search term
     * @throws ResponseStatusException if some internal error occurs in the database
     */
    @Override
    public List<Owner> searchOwners(String term) throws PersistenceException {
        LOGGER.trace("searchOwners({}) called", term);

        try {
            return jdbcTemplate.query(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_SEARCH);
                ps.setString(1, globalMatchToLowerEscapeBang(term));
                return ps;
            }, this::mapOwnerRow);
        } catch (DataAccessException e) {
            throw new PersistenceException("error searching for horses", e);
        }
    }


    /**
     * Maps a result set to an owner entity
     * @param result The result set to map from
     * @param rowNum The number of the current row
     * @return An owner entity representation of the result set
     * @throws SQLException if an unexpected error occurs while getting values from columns of the result set
     */
    private Owner mapOwnerRow(ResultSet result, int rowNum) throws SQLException {
        LOGGER.trace("mapOwnerRow(_, _) called");

        Owner owner = new Owner();
        owner.setId(result.getLong("id"));
        owner.setFirstName(result.getString("firstName"));
        owner.setLastName(result.getString("lastName"));
        owner.setEmail(result.getString("email"));
        return owner;
    }

    /**
     * Escapes special characters in a search term and pads the string with '%', so that it will be globally matched in an SQL query
     *
     * @param searchTerm The search term to be formatted
     * @return A formatted version of the search term
     */
    private String globalMatchToLowerEscapeBang(String searchTerm) {
        LOGGER.trace("globalMatchToLowerEscapeBang({}) called", searchTerm);

        for (char specialChar : new char[]{'!', '%', '_', '['}) {
            searchTerm = searchTerm.replace("" + specialChar, ESCAPE_CHAR + searchTerm);
        }
        return "%" + searchTerm.toLowerCase() + "%";
    }

}
