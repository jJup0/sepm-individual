package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.OwnerDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

@Repository
public class HorseJdbcDao implements HorseDao {
    private static final String TABLE_NAME = "horse";
    private static final int MAX_SEARCH_RESULTS = 1000;
    private static final int MAX_GENERATIONS = 5;
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME + " LIMIT " + MAX_SEARCH_RESULTS;
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
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.ownerDao = ownerDao;
    }

    @Override
    public List<Horse> getAll() {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, (resultSet, rowNum) ->
                    mapHorsesRowParentDepth(resultSet, 1));
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not query all horses", e);
        }
    }


    @Override
    public Horse getHorse(long id) {
        return getHorseWithFamilyTree(id, 1);
    }

    @Override
    public List<Horse> getChildren(long id) {
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
            throw new PersistenceException("Could not query all horses", e);
        }
    }

    @Override
    public Horse addHorse(HorseDto horseDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT,
                    Statement.RETURN_GENERATED_KEYS);
            fillStandardPreparedStatement(horseDto, ps);
            return ps;
        }, keyHolder);

        Horse addedHorse = mapper.dtoToEntity(horseDto);
        addedHorse.setId((long) keyHolder.getKeys().get("id"));
        return addedHorse;
    }


    // TODO key not present error
    @Override
    public Horse editHorse(HorseDto horseDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
            fillStandardPreparedStatement(horseDto, ps);
            ps.setLong(8, horseDto.id());
            return ps;
        });

        return mapper.dtoToEntity(horseDto);

    }

    // TODO key not present error
    @Override
    public void deleteHorse(long id) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_DELETE);
            ps.setLong(1, id);
            return ps;
        });
    }

    @Override
    public List<Horse> searchHorses(HorseSearchDto horseSearchDto) {
        // TODO no search parameter given error
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
        // TODO search for owner entity
        if (horseSearchDto.owner() != null) {
            sqlSearchParams.add("LOWER(owner) LIKE ? ESCAPE'" + escapeChar + "'");
        }
        int searchLimit = MAX_SEARCH_RESULTS;
        if (horseSearchDto.limit() != null && horseSearchDto.limit() >= 0) {
            searchLimit = min(searchLimit, horseSearchDto.limit());
        }

        if (sqlSearchParams.size() == 0) {
            return getAll();
        }

        String SQL_SEARCH_QUERY = SQL_SEARCH_BASE + String.join(" AND ", sqlSearchParams) + " LIMIT + " + searchLimit + ";";
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
            // TODO search for owner entity
            if (horseSearchDto.owner() != null) {
                ps.setString(parameterIndex, globalMatchToLowerEscapeBang(horseSearchDto.owner()));
            }

            return ps;
        }, (resultSet, rowNum) ->
                mapHorsesRowParentDepth(resultSet, 1));


    }

    @Override
    public Horse getHorseWithFamilyTree(long id, int ancestorDepth) {
        // TODO horse doesnt exist error
        if (ancestorDepth == -1) {
            return null;
        }
        final int limitedAncestorDepth = min(ancestorDepth, MAX_GENERATIONS);
        List<Horse> horses = jdbcTemplate.query(connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_SELECT_ONE);
                    ps.setLong(1, id);
                    return ps;
                }, (resultSet, rowNum) ->
                        mapHorsesRowParentDepth(resultSet, limitedAncestorDepth)
        );
        // TODO no horse
        return horses.get(0);

    }

    private Horse mapHorsesRowParentDepth(ResultSet result, int depthRemaining) throws SQLException {
        Horse horse = mapHorsesRowBase(result);

        long motherId = result.getLong("mother");
        horse.setMother(result.wasNull() ? null : getHorseWithFamilyTree(motherId, depthRemaining - 1));
        long fatherId = result.getLong("father");
        horse.setFather(result.wasNull() ? null : getHorseWithFamilyTree(fatherId, depthRemaining - 1));
        return horse;
    }

    private String globalMatchToLowerEscapeBang(String searchTerm) {
        for (char specialChar : new char[]{'!', '%', '_', '['}) {
            searchTerm = searchTerm.replace("" + specialChar, "!" + searchTerm);
        }
        return "%" + searchTerm.toLowerCase() + "%";
    }

    private void fillStandardPreparedStatement(HorseDto horseDto, PreparedStatement ps) throws SQLException {
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

    private Horse mapHorsesRowBase(ResultSet result) throws SQLException {

        Horse horse = new Horse();
        horse.setId(result.getLong("id"));
        horse.setName(result.getString("name"));
        horse.setDescription(result.getString("description"));
        horse.setBirthdate(result.getDate("birthdate").toLocalDate());
        horse.setSex(HorseBiologicalGender.valueOf(result.getString("sex")));
        long ownerId = result.getLong("owner");
        horse.setOwner(result.wasNull() ? null : ownerDao.getWithId(ownerId));
        return horse;
    }

}
