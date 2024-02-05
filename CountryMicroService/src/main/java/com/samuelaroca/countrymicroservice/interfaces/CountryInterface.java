package com.samuelaroca.countrymicroservice.interfaces;

import java.util.List;

public interface CountryInterface {

    List<CountryProjection> allCountries();
    String findById(Long idCountry);
    Long findByName(String name);
}
