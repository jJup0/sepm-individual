package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HorseJdbcDao implements HorseDao {
    private static final String TABLE_NAME = "horse";
    private static final String MAX_SEARCH_RESULTS = "5";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (name, description, birthdate, sex, owner, mother, father) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_SELECT_ONE = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?;";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET name = ?, description = ?, birthdate = ?, sex = ?, owner = ?, mother = ?, father = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String SQL_SEARCH_BASE = "SELECT * FROM " + TABLE_NAME + " WHERE ";


    private final JdbcTemplate jdbcTemplate;
    private final HorseMapper mapper;

    public HorseJdbcDao(JdbcTemplate jdbcTemplate, HorseMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<Horse> getAll() {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, this::mapHorsesRowNoParents);
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not query all horses", e);
        }
    }


    @Override
    public Horse getHorse(long id) {
        List<Horse> horses = jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_SELECT_ONE);
            ps.setLong(1, id);
            return ps;
        }, this::mapHorsesRow);
        return horses.get(0);

    }

    @Override
    public Horse getHorseNoParents(long id) {
        List<Horse> horses = jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_SELECT_ONE);
            ps.setLong(1, id);
            return ps;
        }, this::mapHorsesRowNoParents);
        return horses.get(0);
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

        if (sqlSearchParams.size() == 0) {
            return getAll();
        }

        String SQL_SEARCH_QUERY = SQL_SEARCH_BASE + String.join(" AND ", sqlSearchParams) + " LIMIT + " + MAX_SEARCH_RESULTS + ";";

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
                ps.setString(parameterIndex++, globalMatchToLowerEscapeBang(horseSearchDto.owner()));
            }
            return ps;
        }, this::mapHorsesRow);


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
        ps.setString(5, horseDto.owner());
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

    private Horse mapHorsesRowNoParents(ResultSet result, int rowNum) throws SQLException {
        Horse horse = mapHorsesRowBase(result);
        horse.setMother(null);
        horse.setFather(null);
        return horse;
    }

    private Horse mapHorsesRow(ResultSet result, int rowNum) throws SQLException {
        Horse horse = mapHorsesRowBase(result);
        long motherId = result.getLong("mother");
        //TODO NoParents
        horse.setMother(result.wasNull() ? null : getHorseNoParents(motherId));
        long fatherId = result.getLong("father");
        horse.setFather(result.wasNull() ? null : getHorseNoParents(fatherId));
        return horse;
    }

    private Horse mapHorsesRowBase(ResultSet result) throws SQLException {

        Horse horse = new Horse();
        horse.setId(result.getLong("id"));
        horse.setName(result.getString("name"));
        horse.setDescription(result.getString("description"));
        horse.setBirthdate(result.getDate("birthdate").toLocalDate());
        horse.setSex(HorseBiologicalGender.valueOf(result.getString("sex")));
        horse.setOwner(result.getString("owner"));
        return horse;
    }

}
