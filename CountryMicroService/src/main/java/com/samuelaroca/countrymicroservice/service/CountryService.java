package com.samuelaroca.countrymicroservice.service;

import com.samuelaroca.countrymicroservice.entity.Country;
import com.samuelaroca.countrymicroservice.exceptions.AppException;
import com.samuelaroca.countrymicroservice.interfaces.CountryInterface;
import com.samuelaroca.countrymicroservice.interfaces.CountryProjection;
import com.samuelaroca.countrymicroservice.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryService implements CountryInterface {

    private final CountryRepository countryRepository;

    @Override
    public List<CountryProjection> allCountries() {
        return countryRepository.findAllCountries();
    }

    @Override
    public String findById(Long idCountry) {

        Optional<Country> optionalCountry = countryRepository.findById(idCountry);
        if (optionalCountry.isEmpty()) {
            throw new AppException("Country not found", HttpStatus.NOT_FOUND);
        }
        return optionalCountry.get().getName();
    }

    @Override
    public Long findByName(String name) {
        Optional<Country> optionalCountry = countryRepository.findByNameIgnoreCase(name);
        if (optionalCountry.isEmpty()) {
            throw new AppException("Country not found", HttpStatus.NOT_FOUND);
        }
        return optionalCountry.get().getId();
    }
}
