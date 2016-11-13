package com.project2.roommate.repository;

import com.project2.roommate.domain.Easyroommate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Easyroommate entity.
 */
@SuppressWarnings("unused")
public interface EasyroommateRepository extends JpaRepository<Easyroommate,Long> {

}
