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
    private static final String SQL_SEARCH_WITHOUT_SEX = "SELECT * FROM " + TABLE_NAME + " WHERE LOWER(name) LIKE ? ESCAPE '!' LIMIT + " + MAX_SEARCH_RESULTS + ";";
    private static final String SQL_SEARCH_WITH_SEX = "SELECT * FROM " + TABLE_NAME + " WHERE LOWER(name) LIKE ? ESCAPE '!' AND sex = ? LIMIT + " + MAX_SEARCH_RESULTS + ";";


    private final JdbcTemplate jdbcTemplate;
    private final HorseMapper mapper;

    public HorseJdbcDao(JdbcTemplate jdbcTemplate, HorseMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<Horse> getAll() {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, this::mapRow);
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

    @Override
    public Horse getHorse(long id) {
        List<Horse> horses = jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_SELECT_ONE);
            ps.setLong(1, id);
            return ps;
        }, this::mapRow);
        return horses.get(0);

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
        // %-signs in SQL_SEARCH_WITHOUT_SEX break prepareStatement, escape special signs
        String searchTerm = "%" + horseSearchDto.searchTerm()
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![")
                .toLowerCase() + "%";
        if (horseSearchDto.sex() == null) {
            return jdbcTemplate.query(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_SEARCH_WITHOUT_SEX);
                ps.setString(1, searchTerm);

                return ps;
            }, this::mapRow);
        } else {
            return jdbcTemplate.query(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_SEARCH_WITH_SEX);
                ps.setString(1, searchTerm);
                ps.setString(2, horseSearchDto.sex().toString());
                return ps;
            }, this::mapRow);
        }

    }

    private void fillStandardPreparedStatement(HorseDto horseDto, PreparedStatement ps) throws SQLException {
        ps.setString(1, horseDto.name());
        ps.setString(2, horseDto.description());
        ps.setDate(3, java.sql.Date.valueOf(horseDto.birthdate()));
        ps.setString(4, horseDto.sex().toString());
        ps.setString(5, horseDto.owner());
        if (horseDto.motherId() == null) {
            ps.setNull(6, Types.BIGINT);
        } else {
            ps.setLong(6, horseDto.motherId());
        }
        if (horseDto.motherId() == null) {
            ps.setNull(7, Types.BIGINT);
        } else {
            ps.setLong(7, horseDto.fatherId());
        }
    }

    private Horse mapRow(ResultSet result, int rowNum) throws SQLException {
        Horse horse = new Horse();
        horse.setId(result.getLong("id"));
        horse.setName(result.getString("name"));
        horse.setDescription(result.getString("description"));
        horse.setBirthdate(result.getDate("birthdate").toLocalDate());
        horse.setSex(HorseBiologicalGender.valueOf(result.getString("sex")));
        horse.setOwner(result.getString("owner"));

        Long motherId = result.getLong("mother");
        horse.setMotherId(result.wasNull() ? null : motherId);
        Long fatherId = result.getLong("father");
        horse.setFatherId(result.wasNull() ? null : fatherId);
        return horse;
    }
}
