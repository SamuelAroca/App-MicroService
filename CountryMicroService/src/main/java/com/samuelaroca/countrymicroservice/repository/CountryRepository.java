package com.samuelaroca.countrymicroservice.repository;

import com.samuelaroca.countrymicroservice.entity.Country;
import com.samuelaroca.countrymicroservice.interfaces.CountryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByNameIgnoreCase(String countryName);

    @Query("SELECT c.id AS id, c.name AS name FROM Country c")
    List<CountryProjection> findAllCountries();

}
