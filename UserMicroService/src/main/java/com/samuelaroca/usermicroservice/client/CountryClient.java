package com.samuelaroca.usermicroservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "app-country", url = "localhost:8080/api/country")
public interface CountryClient {

    @GetMapping("/findById/{idCountry}")
    String idCountry(@PathVariable Long idCountry);

    @GetMapping("/findByName/{name}")
    Long name(@PathVariable String name);
}
