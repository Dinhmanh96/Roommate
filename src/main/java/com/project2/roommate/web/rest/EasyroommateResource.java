package com.project2.roommate.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.project2.roommate.domain.Easyroommate;

import com.project2.roommate.repository.EasyroommateRepository;
import com.project2.roommate.repository.search.EasyroommateSearchRepository;
import com.project2.roommate.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Easyroommate.
 */
@RestController
@RequestMapping("/api")
public class EasyroommateResource {

    private final Logger log = LoggerFactory.getLogger(EasyroommateResource.class);
        
    @Inject
    private EasyroommateRepository easyroommateRepository;

    @Inject
    private EasyroommateSearchRepository easyroommateSearchRepository;

    /**
     * POST  /easyroommates : Create a new easyroommate.
     *
     * @param easyroommate the easyroommate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new easyroommate, or with status 400 (Bad Request) if the easyroommate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/easyroommates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Easyroommate> createEasyroommate(@RequestBody Easyroommate easyroommate) throws URISyntaxException {
        log.debug("REST request to save Easyroommate : {}", easyroommate);
        if (easyroommate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("easyroommate", "idexists", "A new easyroommate cannot already have an ID")).body(null);
        }
        Easyroommate result = easyroommateRepository.save(easyroommate);
        easyroommateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/easyroommates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("easyroommate", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /easyroommates : Updates an existing easyroommate.
     *
     * @param easyroommate the easyroommate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated easyroommate,
     * or with status 400 (Bad Request) if the easyroommate is not valid,
     * or with status 500 (Internal Server Error) if the easyroommate couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/easyroommates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Easyroommate> updateEasyroommate(@RequestBody Easyroommate easyroommate) throws URISyntaxException {
        log.debug("REST request to update Easyroommate : {}", easyroommate);
        if (easyroommate.getId() == null) {
            return createEasyroommate(easyroommate);
        }
        Easyroommate result = easyroommateRepository.save(easyroommate);
        easyroommateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("easyroommate", easyroommate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /easyroommates : get all the easyroommates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of easyroommates in body
     */
    @RequestMapping(value = "/easyroommates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Easyroommate> getAllEasyroommates() {
        log.debug("REST request to get all Easyroommates");
        List<Easyroommate> easyroommates = easyroommateRepository.findAll();
        return easyroommates;
    }

    /**
     * GET  /easyroommates/:id : get the "id" easyroommate.
     *
     * @param id the id of the easyroommate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the easyroommate, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/easyroommates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Easyroommate> getEasyroommate(@PathVariable Long id) {
        log.debug("REST request to get Easyroommate : {}", id);
        Easyroommate easyroommate = easyroommateRepository.findOne(id);
        return Optional.ofNullable(easyroommate)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /easyroommates/:id : delete the "id" easyroommate.
     *
     * @param id the id of the easyroommate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/easyroommates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEasyroommate(@PathVariable Long id) {
        log.debug("REST request to delete Easyroommate : {}", id);
        easyroommateRepository.delete(id);
        easyroommateSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("easyroommate", id.toString())).build();
    }

    /**
     * SEARCH  /_search/easyroommates?query=:query : search for the easyroommate corresponding
     * to the query.
     *
     * @param query the query of the easyroommate search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/easyroommates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Easyroommate> searchEasyroommates(@RequestParam String query) {
        log.debug("REST request to search Easyroommates for query {}", query);
        return StreamSupport
            .stream(easyroommateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
