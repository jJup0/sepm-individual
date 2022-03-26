package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"test", "datagen"})
// enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
@EnableWebMvc
@WebAppConfiguration
public class HorseEndpointTest {

    private static final int TEST_DATA_SIZE = 10;

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void gettingNonexistentUrlReturns404() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/asdf123")
                ).andExpect(status().isNotFound());
    }

    @Test
    public void gettingAllHorses() throws Exception {
        byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/horses")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        List<HorseDto> horsesResult = objectMapper.readerFor(HorseDto.class).<HorseDto>readValues(body).readAll();

        Collections.sort(horsesResult);
        assertThat(horsesResult).isNotNull();
        assertThat(horsesResult.size()).isEqualTo(TEST_DATA_SIZE);

        HorseDto wendy = horsesResult.get(TEST_DATA_SIZE - 1);
        assertThat(wendy.id()).isEqualTo(-1);
        assertThat(wendy.name()).isEqualTo("Wendy");
        assertThat(wendy.description()).isEqualTo("first horse");
        assertThat(wendy.birthdate()).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(wendy.sex()).isEqualTo(HorseBiologicalGender.female);
        assertThat(wendy.owner().firstName()).isEqualTo("Owner 1");
        assertThat(wendy.mother()).isNull();
        assertThat(wendy.father()).isNull();

        HorseDto lady = horsesResult.get(0);
        assertThat(lady.id()).isEqualTo(-10);
        assertThat(lady.name()).isEqualTo("Lady");
    }

    @Test
    public void gettingWendy() throws Exception {
        byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/horses/-1")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        List<HorseDto> getWendyResult = objectMapper.readerFor(HorseDto.class).<HorseDto>readValues(body).readAll();

        assertThat(getWendyResult).isNotNull();
        assertThat(getWendyResult.size()).isEqualTo(1);
        assertThat(getWendyResult.get(0).id()).isEqualTo(-1);
        assertThat(getWendyResult.get(0).name()).isEqualTo("Wendy");
    }

    @Test
    public void gettingNonExistent() throws Exception {
        byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/horses/-100")
                        .accept(MediaType.APPLICATION_JSON)
                )
//                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsByteArray();

        List<HorseDto> emptyResult = objectMapper.readerFor(HorseDto.class).<HorseDto>readValues(body).readAll();
        System.out.println(emptyResult);
        assertThat(emptyResult).isNotNull();
        assertThat(emptyResult.size()).isEqualTo(0);
    }

    @Test
    public void gettingDakotasFamily() throws Exception {
        byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/horses/familytree/-7")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        List<HorseDto> DakotaFamily = objectMapper.readerFor(HorseDto.class).<HorseDto>readValues(body).readAll();

        assertThat(DakotaFamily).isNotNull();
        assertThat(DakotaFamily.size()).isEqualTo(1);

        HorseDto dakota = DakotaFamily.get(0);
        assertThat(dakota.id()).isEqualTo(-7);
        assertThat(dakota.name()).isEqualTo("Dakota");

        HorseDto lilly = dakota.mother();
        assertThat(lilly).isNotNull();
        assertThat(lilly.name()).isEqualTo("Lilly");

        HorseDto johnny = dakota.father();
        assertThat(johnny).isNotNull();
        assertThat(johnny.name()).isEqualTo("Johnny");

        HorseDto wendy = lilly.mother();
        assertThat(wendy).isNotNull();
        assertThat(wendy.name()).isEqualTo("Wendy");
    }

    @Test
    public void editingHorseInvalidDeleteName() throws Exception {

        HorseDto faultyHorse = new HorseDto(-1L, null, "", LocalDate.of(2000,1,1), HorseBiologicalGender.female, null, null, null);
        byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/horses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faultyHorse)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse().getContentAsByteArray();

        List<HorseDto> family = objectMapper.readerFor(HorseDto.class).<HorseDto>readValues(body).readAll();
        assertThat(family.size()).isEqualTo(0);
    }


    @Test
    public void gettingNonExistentHorse() throws Exception {
        byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/horses/edit/-100")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsByteArray();

        List<HorseDto> family = objectMapper.readerFor(HorseDto.class).<HorseDto>readValues(body).readAll();
        assertThat(family.size()).isEqualTo(0);
    }



    @Test
    public void gettingSelection() throws Exception {
        byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/horses/selection?name=dy")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        List<HorseDto> getWendyResult = objectMapper.readerFor(HorseDto.class).<HorseDto>readValues(body).readAll();

        Collections.sort(getWendyResult);
        assertThat(getWendyResult).isNotNull();
        assertThat(getWendyResult.size()).isEqualTo(2);
        assertThat(getWendyResult.get(0).name()).isEqualTo("Lady");
        assertThat(getWendyResult.get(1).name()).isEqualTo("Wendy");
    }


    // TODO all endpoint tests
}
