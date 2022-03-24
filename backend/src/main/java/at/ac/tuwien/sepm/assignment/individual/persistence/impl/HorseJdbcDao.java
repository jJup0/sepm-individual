package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.OwnerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

@Repository
public class HorseJdbcDao implements HorseDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String TABLE_NAME = "horse";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (name, description, birthdate, sex, owner, mother, father) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_SELECT_ONE = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?;";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET name = ?, description = ?, birthdate = ?, sex = ?, owner = ?, mother = ?, father = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String SQL_SEARCH_BASE = "SELECT * FROM " + TABLE_NAME + " WHERE ";
    private static final String SQL_GET_CHILDREN = "SELECT * FROM " + TABLE_NAME + " WHERE mother = ? OR father = ?";


    private final JdbcTemplate jdbcTemplate;
    private final HorseMapper mapper;
    private final OwnerDao ownerDao;

    public HorseJdbcDao(JdbcTemplate jdbcTemplate, HorseMapper mapper, OwnerDao ownerDao) {
        LOGGER.trace("HorseJdbcDao constructed");
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.ownerDao = ownerDao;
    }

    @Override
    public List<Horse> getAll() throws PersistenceException {
        LOGGER.trace("getAll() called");
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, (resultSet, rowNum) ->
                    mapHorsesRowParentDepth(resultSet, 1));
        } catch (DataAccessException e) {
            try {
                handleDataAccessException(e, "error getting all horses");
            } catch (NotFoundException ex) {
                assert (false);
            }
            return null;
        }
    }


    @Override
    public Horse getHorse(long id) throws PersistenceException, NotFoundException {
        LOGGER.trace("getHorse({}) called", id);
        return getFamilyTreeOfHorse(id, 1);
    }

    @Override
    public List<Horse> getChildren(long id) throws PersistenceException, NotFoundException {
        LOGGER.trace("getChildren({}) called", id);

        try {
            return jdbcTemplate.query(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_GET_CHILDREN,
                        Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, id);
                ps.setLong(2, id);
                return ps;
            }, (resultSet, rowNum) ->
                    mapHorsesRowParentDepth(resultSet, 0));
        } catch (DataAccessException e) {
            handleDataAccessException(e, "error getting all children");
            assert (false);
            return null;
        }
    }

    @Override
    public Horse addHorse(HorseDto horseDto) throws PersistenceException {
        LOGGER.trace("addHorse({}) called", horseDto);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT,
                        Statement.RETURN_GENERATED_KEYS);
                fillStandardPreparedStatement(horseDto, ps);
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new PersistenceException("error adding horse", e);
        }

        Horse addedHorse = mapper.dtoToEntity(horseDto);
        assert (keyHolder.getKeys() != null);
        addedHorse.setId((long) keyHolder.getKeys().get("id"));
        return addedHorse;
    }


    @Override
    public Horse editHorse(HorseDto horseDto) throws PersistenceException, NotFoundException {
        LOGGER.trace("editHorse({}) called", horseDto);

        final int affectedRows;
        try {
            affectedRows = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
                fillStandardPreparedStatement(horseDto, ps);
                ps.setLong(8, horseDto.id());
                return ps;
            });
        } catch (DataAccessException e) {
            throw new PersistenceException("error editing horse", e);
        }
        if (affectedRows != 1) {
            throw new NotFoundException("Could not edit horse, id(" + horseDto.id() + ") not found");
        }
        return mapper.dtoToEntity(horseDto);

    }

    // TODO key not present?
    @Override
    public void deleteHorse(long id) {
        LOGGER.trace("deleteHorse({}) called", id);

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_DELETE);
            ps.setLong(1, id);
            return ps;
        });
    }

    @Override
    public List<Horse> searchHorses(HorseSearchDto horseSearchDto) throws PersistenceException {
        LOGGER.trace("searchHorses({}) called", horseSearchDto);

        char escapeChar = '!';

        List<String> sqlSearchParams = new ArrayList<>();
        if (horseSearchDto.name() != null) {
            sqlSearchParams.add("LOWER(name) LIKE ? ESCAPE '" + escapeChar + "'");
        }
        if (horseSearchDto.description() != null) {
            sqlSearchParams.add("LOWER(description) LIKE ? ESCAPE'" + escapeChar + "'");
        }
        if (horseSearchDto.bornAfter() != null) {
            sqlSearchParams.add("birthdate >= ?");
        }
        if (horseSearchDto.bornBefore() != null) {
            sqlSearchParams.add("birthdate <= ?");
        }
        if (horseSearchDto.sex() != null) {
            sqlSearchParams.add("sex = ?");
        }
        if (horseSearchDto.ownerId() != null) {
            sqlSearchParams.add("owner = ?");
        }

        if (sqlSearchParams.size() == 0) {
            try {
                return getAll();
            } catch (PersistenceException e) {
                throw new PersistenceException("error searching for horses without parameters", e);
            }
        }
        try {

            // Bulky code because SQL_SEARCH_QUERY has to be final in lambda
            String limitStr = "";
            if (horseSearchDto.limit() != null){
                limitStr = " LIMIT + " + horseSearchDto.limit() + ";";
            }
            String SQL_SEARCH_QUERY = SQL_SEARCH_BASE + String.join(" AND ", sqlSearchParams) + limitStr;


            return jdbcTemplate.query(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_SEARCH_QUERY);
                int parameterIndex = 1;
                if (horseSearchDto.name() != null) {
                    ps.setString(parameterIndex++, globalMatchToLowerEscapeBang(horseSearchDto.name()));
                }
                if (horseSearchDto.description() != null) {
                    ps.setString(parameterIndex++, globalMatchToLowerEscapeBang(horseSearchDto.description()));
                }
                if (horseSearchDto.bornAfter() != null) {
                    ps.setDate(parameterIndex++, java.sql.Date.valueOf(horseSearchDto.bornAfter()));
                }
                if (horseSearchDto.bornBefore() != null) {
                    ps.setDate(parameterIndex++, java.sql.Date.valueOf(horseSearchDto.bornBefore()));
                }
                if (horseSearchDto.sex() != null) {
                    ps.setString(parameterIndex++, horseSearchDto.sex().toString());
                }
                if (horseSearchDto.ownerId() != null) {
                    ps.setLong(parameterIndex, horseSearchDto.ownerId());
                }
                LOGGER.debug("searching horses with prepared statement: " + ps.toString());
                return ps;
            }, (resultSet, rowNum) ->
                    mapHorsesRowParentDepth(resultSet, 1));


        } catch (DataAccessException e) {
            throw new PersistenceException("error searching for horses", e);
        }
    }

    @Override
    public Horse getFamilyTreeOfHorse(long id, int ancestorDepth) throws PersistenceException, NotFoundException {
        LOGGER.trace("getFamilyTreeOfHorse({}, {}) called", id, ancestorDepth);
        if (ancestorDepth < 0) {
            return null;
        }
        // prevent user from retrieving very large family tree
        List<Horse> horses;
        try {
            horses = jdbcTemplate.query(connection -> {
                        PreparedStatement ps = connection.prepareStatement(SQL_SELECT_ONE);
                        ps.setLong(1, id);
                        return ps;
                    }, (resultSet, rowNum) ->
                            mapHorsesRowParentDepth(resultSet, ancestorDepth)
            );
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not query all horse with id (" + id + ")", e);
        }
        if (horses.size() == 0) {
            throw new NotFoundException("Could not find horse with id (" + id + ")");
        }
        return horses.get(0);
    }


    private Horse mapHorsesRowParentDepth(ResultSet result, int depthRemaining) throws SQLException {
        LOGGER.trace("mapHorsesRowParentDepth called with remaining depth {}", depthRemaining);

        Horse horse = new Horse();

        horse.setId(result.getLong("id"));
        horse.setName(result.getString("name"));
        horse.setDescription(result.getString("description"));
        horse.setBirthdate(result.getDate("birthdate").toLocalDate());
        horse.setSex(HorseBiologicalGender.valueOf(result.getString("sex")));


        long ownerId = result.getLong("owner");
        try {
            horse.setOwner(result.wasNull() ? null : ownerDao.getWithId(ownerId));
        } catch (PersistenceException e) {
            throw new SQLException("error getting owner of horse with id(" + horse.getId() + ")", e);
        } catch (NotFoundException e) {
            throw new SQLException("error finding owner of horse with id(" + horse.getId() + ")", e);
        }
        try {
            long motherId = result.getLong("mother");
            horse.setMother(result.wasNull() ? null : getFamilyTreeOfHorse(motherId, depthRemaining - 1));
            long fatherId = result.getLong("father");
            horse.setFather(result.wasNull() ? null : getFamilyTreeOfHorse(fatherId, depthRemaining - 1));
        } catch (PersistenceException e) {
            throw new SQLException("error getting family tree of mother/father", e);
        } catch (NotFoundException e) {
            // would be a foreign key violation
            assert (false);
        }


        return horse;
    }

    private String globalMatchToLowerEscapeBang(String searchTerm) {
        LOGGER.trace("globalMatchToLowerEscapeBang({}) called", searchTerm);

        for (char specialChar : new char[]{'!', '%', '_', '['}) {
            searchTerm = searchTerm.replace("" + specialChar, "!" + searchTerm);
        }
        return "%" + searchTerm.toLowerCase() + "%";
    }

    private void fillStandardPreparedStatement(HorseDto horseDto, PreparedStatement ps) throws SQLException {
        LOGGER.trace("fillStandardPreparedStatement({}, _) called", horseDto);

        ps.setString(1, horseDto.name());
        ps.setString(2, horseDto.description());
        ps.setDate(3, java.sql.Date.valueOf(horseDto.birthdate()));
        ps.setString(4, horseDto.sex().toString());

        if (horseDto.owner() == null) {
            ps.setNull(5, Types.BIGINT);
        } else {
            ps.setLong(5, horseDto.owner().id());
        }
        if (horseDto.mother() == null) {
            ps.setNull(6, Types.BIGINT);
        } else {
            ps.setLong(6, horseDto.mother().id());
        }
        if (horseDto.father() == null) {
            ps.setNull(7, Types.BIGINT);
        } else {
            ps.setLong(7, horseDto.father().id());
        }
    }

    private void handleDataAccessException(DataAccessException dataAccessException, String message) throws PersistenceException, NotFoundException {
        LOGGER.trace("handleDataAccessException(_, {}) called", message);

        Throwable cause = dataAccessException.getCause();
        // wrapped SQLException
        if (cause.getClass() == PersistenceException.class) {
            throw new PersistenceException(message, cause);
        }
        if (cause.getClass() == NotFoundException.class) {
            throw new NotFoundException(message, cause);
        }
        // true SQLException
        throw new PersistenceException("FRAMEWORK SQL EXCEPTION" + message, dataAccessException);
    }
}
