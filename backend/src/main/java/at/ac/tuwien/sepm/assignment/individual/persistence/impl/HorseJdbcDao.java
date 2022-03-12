package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class HorseJdbcDao implements HorseDao {
    private static final String TABLE_NAME = "horse";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (name, description, birthdate, sex, owner) VALUES (?, ?, ?, ?, ?);";
    private static final String SQL_SELECT_ONE = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?;";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET name = ?, description = ?, birthdate = ?, sex = ?, owner = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

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
            ps.setString(1, horseDto.name());
            ps.setString(2, horseDto.description());
            ps.setDate(3, java.sql.Date.valueOf(horseDto.birthdate()));
            ps.setString(4, horseDto.sex().toString());
            ps.setString(5, horseDto.owner());
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
            ps.setString(1, horseDto.name());
            ps.setString(2, horseDto.description());
            ps.setDate(3, java.sql.Date.valueOf(horseDto.birthdate()));
            ps.setString(4, horseDto.sex().toString());
            ps.setString(5, horseDto.owner());
            ps.setLong(6, horseDto.id());
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

    private Horse mapRow(ResultSet result, int rownum) throws SQLException {
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
