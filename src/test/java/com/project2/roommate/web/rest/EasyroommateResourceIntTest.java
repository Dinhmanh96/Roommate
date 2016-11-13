package com.project2.roommate.web.rest;

import com.project2.roommate.RoommateApp;

import com.project2.roommate.domain.Easyroommate;
import com.project2.roommate.repository.EasyroommateRepository;
import com.project2.roommate.repository.search.EasyroommateSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EasyroommateResource REST controller.
 *
 * @see EasyroommateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoommateApp.class)
public class EasyroommateResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";

    private static final Integer DEFAULT_RENT_PER_A_MONTH = 1;
    private static final Integer UPDATED_RENT_PER_A_MONTH = 2;

    private static final String DEFAULT_PROPERTY_TYPE = "AAAAA";
    private static final String UPDATED_PROPERTY_TYPE = "BBBBB";

    private static final String DEFAULT_AGE_RANGE = "AAAAA";
    private static final String UPDATED_AGE_RANGE = "BBBBB";

    private static final String DEFAULT_GENDER = "AAAAA";
    private static final String UPDATED_GENDER = "BBBBB";

    private static final String DEFAULT_MORE_INFOMATION = "AAAAA";
    private static final String UPDATED_MORE_INFOMATION = "BBBBB";

    private static final String DEFAULT_SEARCH = "AAAAA";
    private static final String UPDATED_SEARCH = "BBBBB";

    @Inject
    private EasyroommateRepository easyroommateRepository;

    @Inject
    private EasyroommateSearchRepository easyroommateSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEasyroommateMockMvc;

    private Easyroommate easyroommate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EasyroommateResource easyroommateResource = new EasyroommateResource();
        ReflectionTestUtils.setField(easyroommateResource, "easyroommateSearchRepository", easyroommateSearchRepository);
        ReflectionTestUtils.setField(easyroommateResource, "easyroommateRepository", easyroommateRepository);
        this.restEasyroommateMockMvc = MockMvcBuilders.standaloneSetup(easyroommateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Easyroommate createEntity(EntityManager em) {
        Easyroommate easyroommate = new Easyroommate()
                .name(DEFAULT_NAME)
                .address(DEFAULT_ADDRESS)
                .phone(DEFAULT_PHONE)
                .rent_per_a_month(DEFAULT_RENT_PER_A_MONTH)
                .property_type(DEFAULT_PROPERTY_TYPE)
                .age_range(DEFAULT_AGE_RANGE)
                .gender(DEFAULT_GENDER)
                .more_infomation(DEFAULT_MORE_INFOMATION)
                .search(DEFAULT_SEARCH);
        return easyroommate;
    }

    @Before
    public void initTest() {
        easyroommateSearchRepository.deleteAll();
        easyroommate = createEntity(em);
    }

    @Test
    @Transactional
    public void createEasyroommate() throws Exception {
        int databaseSizeBeforeCreate = easyroommateRepository.findAll().size();

        // Create the Easyroommate

        restEasyroommateMockMvc.perform(post("/api/easyroommates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(easyroommate)))
                .andExpect(status().isCreated());

        // Validate the Easyroommate in the database
        List<Easyroommate> easyroommates = easyroommateRepository.findAll();
        assertThat(easyroommates).hasSize(databaseSizeBeforeCreate + 1);
        Easyroommate testEasyroommate = easyroommates.get(easyroommates.size() - 1);
        assertThat(testEasyroommate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEasyroommate.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEasyroommate.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testEasyroommate.getRent_per_a_month()).isEqualTo(DEFAULT_RENT_PER_A_MONTH);
        assertThat(testEasyroommate.getProperty_type()).isEqualTo(DEFAULT_PROPERTY_TYPE);
        assertThat(testEasyroommate.getAge_range()).isEqualTo(DEFAULT_AGE_RANGE);
        assertThat(testEasyroommate.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testEasyroommate.getMore_infomation()).isEqualTo(DEFAULT_MORE_INFOMATION);
        assertThat(testEasyroommate.getSearch()).isEqualTo(DEFAULT_SEARCH);

        // Validate the Easyroommate in ElasticSearch
        Easyroommate easyroommateEs = easyroommateSearchRepository.findOne(testEasyroommate.getId());
        assertThat(easyroommateEs).isEqualToComparingFieldByField(testEasyroommate);
    }

    @Test
    @Transactional
    public void getAllEasyroommates() throws Exception {
        // Initialize the database
        easyroommateRepository.saveAndFlush(easyroommate);

        // Get all the easyroommates
        restEasyroommateMockMvc.perform(get("/api/easyroommates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(easyroommate.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].rent_per_a_month").value(hasItem(DEFAULT_RENT_PER_A_MONTH)))
                .andExpect(jsonPath("$.[*].property_type").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())))
                .andExpect(jsonPath("$.[*].age_range").value(hasItem(DEFAULT_AGE_RANGE.toString())))
                .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
                .andExpect(jsonPath("$.[*].more_infomation").value(hasItem(DEFAULT_MORE_INFOMATION.toString())))
                .andExpect(jsonPath("$.[*].search").value(hasItem(DEFAULT_SEARCH.toString())));
    }

    @Test
    @Transactional
    public void getEasyroommate() throws Exception {
        // Initialize the database
        easyroommateRepository.saveAndFlush(easyroommate);

        // Get the easyroommate
        restEasyroommateMockMvc.perform(get("/api/easyroommates/{id}", easyroommate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(easyroommate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.rent_per_a_month").value(DEFAULT_RENT_PER_A_MONTH))
            .andExpect(jsonPath("$.property_type").value(DEFAULT_PROPERTY_TYPE.toString()))
            .andExpect(jsonPath("$.age_range").value(DEFAULT_AGE_RANGE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.more_infomation").value(DEFAULT_MORE_INFOMATION.toString()))
            .andExpect(jsonPath("$.search").value(DEFAULT_SEARCH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEasyroommate() throws Exception {
        // Get the easyroommate
        restEasyroommateMockMvc.perform(get("/api/easyroommates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEasyroommate() throws Exception {
        // Initialize the database
        easyroommateRepository.saveAndFlush(easyroommate);
        easyroommateSearchRepository.save(easyroommate);
        int databaseSizeBeforeUpdate = easyroommateRepository.findAll().size();

        // Update the easyroommate
        Easyroommate updatedEasyroommate = easyroommateRepository.findOne(easyroommate.getId());
        updatedEasyroommate
                .name(UPDATED_NAME)
                .address(UPDATED_ADDRESS)
                .phone(UPDATED_PHONE)
                .rent_per_a_month(UPDATED_RENT_PER_A_MONTH)
                .property_type(UPDATED_PROPERTY_TYPE)
                .age_range(UPDATED_AGE_RANGE)
                .gender(UPDATED_GENDER)
                .more_infomation(UPDATED_MORE_INFOMATION)
                .search(UPDATED_SEARCH);

        restEasyroommateMockMvc.perform(put("/api/easyroommates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEasyroommate)))
                .andExpect(status().isOk());

        // Validate the Easyroommate in the database
        List<Easyroommate> easyroommates = easyroommateRepository.findAll();
        assertThat(easyroommates).hasSize(databaseSizeBeforeUpdate);
        Easyroommate testEasyroommate = easyroommates.get(easyroommates.size() - 1);
        assertThat(testEasyroommate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEasyroommate.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEasyroommate.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testEasyroommate.getRent_per_a_month()).isEqualTo(UPDATED_RENT_PER_A_MONTH);
        assertThat(testEasyroommate.getProperty_type()).isEqualTo(UPDATED_PROPERTY_TYPE);
        assertThat(testEasyroommate.getAge_range()).isEqualTo(UPDATED_AGE_RANGE);
        assertThat(testEasyroommate.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEasyroommate.getMore_infomation()).isEqualTo(UPDATED_MORE_INFOMATION);
        assertThat(testEasyroommate.getSearch()).isEqualTo(UPDATED_SEARCH);

        // Validate the Easyroommate in ElasticSearch
        Easyroommate easyroommateEs = easyroommateSearchRepository.findOne(testEasyroommate.getId());
        assertThat(easyroommateEs).isEqualToComparingFieldByField(testEasyroommate);
    }

    @Test
    @Transactional
    public void deleteEasyroommate() throws Exception {
        // Initialize the database
        easyroommateRepository.saveAndFlush(easyroommate);
        easyroommateSearchRepository.save(easyroommate);
        int databaseSizeBeforeDelete = easyroommateRepository.findAll().size();

        // Get the easyroommate
        restEasyroommateMockMvc.perform(delete("/api/easyroommates/{id}", easyroommate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean easyroommateExistsInEs = easyroommateSearchRepository.exists(easyroommate.getId());
        assertThat(easyroommateExistsInEs).isFalse();

        // Validate the database is empty
        List<Easyroommate> easyroommates = easyroommateRepository.findAll();
        assertThat(easyroommates).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEasyroommate() throws Exception {
        // Initialize the database
        easyroommateRepository.saveAndFlush(easyroommate);
        easyroommateSearchRepository.save(easyroommate);

        // Search the easyroommate
        restEasyroommateMockMvc.perform(get("/api/_search/easyroommates?query=id:" + easyroommate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(easyroommate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].rent_per_a_month").value(hasItem(DEFAULT_RENT_PER_A_MONTH)))
            .andExpect(jsonPath("$.[*].property_type").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].age_range").value(hasItem(DEFAULT_AGE_RANGE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].more_infomation").value(hasItem(DEFAULT_MORE_INFOMATION.toString())))
            .andExpect(jsonPath("$.[*].search").value(hasItem(DEFAULT_SEARCH.toString())));
    }
}
