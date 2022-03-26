package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDtoIdReferences;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.HorseIdReferences;
import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@Repository
public class HorseJdbcDao implements HorseDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String TABLE_NAME = "horse";
    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (name, description, birthdate, sex, owner, mother, father) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET name = ?, description = ?, birthdate = ?, sex = ?, owner = ?, mother = ?, father = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String SQL_GET_RECURSIVE_BASE = "" +
            "WITH RECURSIVE hft(gen, id, name, description, birthdate, sex, ownerId, ownerFn, ownerLn, ownerE, mother, father) AS (\n" +
            "        SELECT %s ?, h.id, h.name, h.description, h.birthdate, h.sex, o.id, o.firstName, o.lastName, o.email, h.mother, h.father\n" +
            "        FROM " + TABLE_NAME + " h LEFT JOIN Owner o ON h.owner = o.id\n" +
            "        %s\n" +
            "    UNION ALL\n" +
            "        SELECT hft.gen - 1, h.id, h.name, h.description, h.birthdate, h.sex, o.id, o.firstName, o.lastName, o.email, h.mother, h.father\n" +
            "        FROM (Horse h LEFT JOIN Owner o\n ON h.owner = o.id) JOIN hft ON (hft.mother = h.id OR hft.father = h.id)\n" +
            "        WHERE hft.gen > 0\n" +
            ") SELECT * FROM hft;";

    private static final String ESCAPE_CHAR = "!";


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
        List<HorseIdReferences> horsesIdReferences;
        try {
            horsesIdReferences = jdbcTemplate.query(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_GET_RECURSIVE_BASE.formatted("", ""),
                        Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, 0);
                return ps;
            }, this::mapHorseJoinedWithOwnerRow);

        } catch (DataAccessException e) {
            throw new PersistenceException("Error getting all horses", e);
        }

        return constructRecursiveEntities(horsesIdReferences, null);
    }


    @Override
    public Horse getHorse(long id) throws PersistenceException, NotFoundException {
        LOGGER.trace("getHorse({}) called", id);

        List<HorseIdReferences> horsesIdReferences;
        try {
            horsesIdReferences = jdbcTemplate.query(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_GET_RECURSIVE_BASE.formatted("", "WHERE h.id = ?"),
                        Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, 1);
                ps.setLong(2, id);
                return ps;
            }, this::mapHorseJoinedWithOwnerRow);

        } catch (DataAccessException e) {
            throw new PersistenceException("Error getting horse with id(" + id + ")", e);
        }

        List<Horse> result = constructRecursiveEntities(horsesIdReferences, id);
        if (result.size() == 0) {
            throw new NotFoundException("Could not find horse with id(" + id + ")");
        }

        return result.get(0);
    }

    @Override
    public List<Horse> getChildren(long id) throws PersistenceException, NotFoundException {
        LOGGER.trace("getChildren({}) called", id);

        List<HorseIdReferences> horsesIdReferences;
        try {
            horsesIdReferences = jdbcTemplate.query(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_GET_RECURSIVE_BASE.formatted("", "WHERE mother = ? OR father = ?"),
                        Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, 0);
                ps.setLong(2, id);
                ps.setLong(3, id);
                return ps;
            }, this::mapHorseJoinedWithOwnerRow);
        } catch (DataAccessException e) {
            throw new PersistenceException("Error getting all children", e);
        }
        return constructRecursiveEntities(horsesIdReferences, null);

    }

    @Override
    public Horse addHorse(HorseDtoIdReferences horseDto) throws PersistenceException {
        LOGGER.trace("addHorse({}) called", horseDto);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT,
                        Statement.RETURN_GENERATED_KEYS);
                fillStandardPreparedStatement(mapper.idReferenceDtoToDto(horseDto), ps);
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new PersistenceException("Error adding horse", e);
        }


        assert (keyHolder.getKeys() != null);
        try {
            return getHorse((long) keyHolder.getKeys().get("id"));
        } catch (NotFoundException e) {
            throw new PersistenceException("Error adding horse", e);
        }

    }


    @Override
    public Horse editHorse(HorseDtoIdReferences horseDto) throws PersistenceException, NotFoundException {
        LOGGER.trace("editHorse({}) called", horseDto);

        final int affectedRows;
        try {
            affectedRows = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
                fillStandardPreparedStatement(mapper.idReferenceDtoToDto(horseDto), ps);
                ps.setLong(8, horseDto.id());
                return ps;
            });
        } catch (DataAccessException e) {
            throw new PersistenceException("error editing horse", e);
        }
        if (affectedRows != 1) {
            throw new NotFoundException("Could not edit horse id(" + horseDto.id() + "), not found");
        }

        return getHorse(horseDto.id());
    }

    @Override
    public void deleteHorse(long id) {
        LOGGER.trace("deleteHorse({}) called", id);

        final int affectedRows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_DELETE);
            ps.setLong(1, id);
            return ps;
        });
        if (affectedRows == 0) {
            LOGGER.info("No horse deleted, horse with id(" + id + ") not found");
        }
    }

    @Override
    public List<Horse> searchHorses(HorseSearchDto horseSearchDto) throws PersistenceException {
        LOGGER.trace("searchHorses({}) called", horseSearchDto);

        List<String> sqlSearchParams = new ArrayList<>();
        if (horseSearchDto.name() != null) {
            sqlSearchParams.add("LOWER(name) LIKE ? ESCAPE '" + ESCAPE_CHAR + "'");
        }
        if (horseSearchDto.description() != null) {
            sqlSearchParams.add("LOWER(description) LIKE ? ESCAPE'" + ESCAPE_CHAR + "'");
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

        String limitStr = horseSearchDto.limit() == null ? "" : "TOP " + horseSearchDto.limit();
        String SQL_SEARCH_QUERY = SQL_GET_RECURSIVE_BASE.formatted(limitStr, "WHERE " + String.join(" AND ", sqlSearchParams));

        List<HorseIdReferences> horsesIdReferences;
        try {
            horsesIdReferences = jdbcTemplate.query(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_SEARCH_QUERY);
                ps.setLong(1, 1);
                int parameterIndex = 2;
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
            }, this::mapHorseJoinedWithOwnerRow);


        } catch (DataAccessException e) {
            throw new PersistenceException("Error searching for horses", e);
        }
        return constructRecursiveEntities(horsesIdReferences, null);
    }

    @Override
    public Horse getFamilyTreeOfHorse(long id, int ancestorDepth) throws PersistenceException, NotFoundException {
        LOGGER.trace("getFamilyTreeOfHorse({}, {}) called", id, ancestorDepth);

        // prevent user from retrieving very large family tree
        List<HorseIdReferences> horsesIdReferences;
        try {
            horsesIdReferences = jdbcTemplate.query(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_GET_RECURSIVE_BASE.formatted("", "WHERE h.id = ?"));
                ps.setLong(1, ancestorDepth);
                ps.setLong(2, id);
                LOGGER.debug("Getting family tree of horse with id(" + id + "): " + ps.toString());
                return ps;
            }, this::mapHorseJoinedWithOwnerRow);
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not get family tree of horse with id(" + id + ")", e);
        }

        List<Horse> horses = constructRecursiveEntities(horsesIdReferences, id);
        if (horses.size() == 0) {
            throw new NotFoundException("Could not find horse with id (" + id + ")");
        }
        return horses.get(0);
    }

    /**
     * Maps a result set to a horse entity with number references to parents
     *
     * @param result Result set given to convert
     * @param rowNum The number of the row
     * @return A horse entity with parents referenced by ids
     * @throws SQLException if some unexpected error occurs getting column values from the result set
     */
    private HorseIdReferences mapHorseJoinedWithOwnerRow(ResultSet result, int rowNum) throws SQLException {
        LOGGER.trace("mapHorseRow() called");
        HorseIdReferences horse = new HorseIdReferences();

        horse.setId(result.getLong("id"));
        horse.setName(result.getString("name"));
        horse.setDescription(result.getString("description"));
        horse.setBirthdate(result.getDate("birthdate").toLocalDate());
        horse.setSex(HorseBiologicalGender.valueOf(result.getString("sex")));

        long ownerId = result.getLong("ownerId");
        if (!result.wasNull()) {
            Owner owner = new Owner();
            owner.setId(ownerId);
            owner.setFirstName(result.getString("ownerFn"));
            owner.setLastName(result.getString("ownerLn"));
            owner.setEmail(result.getString("ownerE"));
            horse.setOwner(owner);
        }
        long motherId = result.getLong("mother");
        horse.setMotherId(result.wasNull() ? null : motherId);
        long fatherId = result.getLong("father");
        horse.setFatherId(result.wasNull() ? null : fatherId);

        return horse;
    }

    /**
     * Turns a list of horse entities with parents as ids into recursive entities.
     * All parents must already be in the list.
     *
     * @param horsesIdReferences A list of horse entities with parents as ids
     * @param idToGet            The id of the horse of interest, null if every horse is needed
     * @return A list of horses of interest as recursive entities
     */
    private List<Horse> constructRecursiveEntities(List<HorseIdReferences> horsesIdReferences, Long idToGet) {
        HashMap<Long, HorseIdReferences> horsesIdRefMap = new HashMap<>();
        for (HorseIdReferences horseIdRef : horsesIdReferences) {
            horsesIdRefMap.put(horseIdRef.getId(), horseIdRef);
        }
        HashMap<Long, Horse> prevConstructed = new HashMap<>();
        for (HorseIdReferences horseIdRef : horsesIdReferences) {
            recHelper(horseIdRef.getId(), horsesIdRefMap, prevConstructed);
        }
        if ((idToGet) == null) {
            return new ArrayList<Horse>(prevConstructed.values());
        } else {
            if (prevConstructed.get(idToGet) == null) {
                return new ArrayList<>();
            }
            return Arrays.asList(prevConstructed.get(idToGet));
        }
    }

    /**
     * A helper function for converting horse entities with parent ids to recursive entities.
     *
     * @param horseIdToConvert ID of the horse which is to be converted
     * @param horsesIdRefMap   A mapping of ID -> horseWithParentIDs
     * @param prevConstructed  A mapping of ID -> recursiveHorseEntities (that have already been constructed in this method previously)
     */
    private void recHelper(Long horseIdToConvert, HashMap<Long, HorseIdReferences> horsesIdRefMap, HashMap<Long, Horse> prevConstructed) {
        if (horsesIdRefMap.containsKey(horseIdToConvert)) {
            HorseIdReferences horseWithIdReferences = horsesIdRefMap.get(horseIdToConvert);
            Horse horse = mapper.referenceEntityToRecurisve(horseWithIdReferences);
            if (horseWithIdReferences.getMotherId() != null) {
                recHelper(horseWithIdReferences.getMotherId(), horsesIdRefMap, prevConstructed);
                horse.setMother(prevConstructed.get(horseWithIdReferences.getMotherId()));
            }
            if (horseWithIdReferences.getFatherId() != null) {
                recHelper(horseWithIdReferences.getFatherId(), horsesIdRefMap, prevConstructed);
                horse.setFather(prevConstructed.get(horseWithIdReferences.getFatherId()));
            }
            prevConstructed.put(horseIdToConvert, horse);
        }
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

    /**
     * Fills prepared statement (from beginning index=1) with a horse DTO
     *
     * @param horseDto The DTO used to filled the prepared statement
     * @param ps       The prepared statement to fill
     * @throws SQLException if some unexpected error occurs while filling the prepared statement
     */
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
}
