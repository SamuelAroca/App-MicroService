package com.samuelaroca.countrymicroservice.controller;

import com.samuelaroca.countrymicroservice.interfaces.CountryProjection;
import com.samuelaroca.countrymicroservice.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/country")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/all")
    public ResponseEntity<List<CountryProjection>> allCountries() {
        return ResponseEntity.ok(countryService.allCountries());
    }

    @GetMapping("/findById/{idCountry}")
    public ResponseEntity<String> findById(@PathVariable Long idCountry) {
        return ResponseEntity.ok(countryService.findById(idCountry));
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<Long> findByName(@PathVariable String name) {
        return ResponseEntity.ok(countryService.findByName(name));
    }
}
