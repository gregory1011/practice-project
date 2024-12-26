package com.app.service.integration;


import com.app.client.CountryClient;
import com.app.dto.common.CountryDto;
import com.app.dto.common.TokenDto;
import com.app.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


//@ActiveProfiles("test")
@SpringBootTest
public class AddressServiceImpl_IntTest {

//    @Autowired
    @MockBean
    private CountryClient countryClient;
//    @Autowired
    @InjectMocks
    private AddressServiceImpl addressService;

    @Value("${api.country.token}")
    private String token;

    @Value("${api.country.email}")
    private String email;

    private TokenDto mockToken;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addressService= new AddressServiceImpl(countryClient);
        mockToken= new TokenDto();
        mockToken.setAuthToken("Bearer " + token);
    }

    @Test
    void testFetchCountryListAsync() {
        //when
        when(countryClient.auth(any(), any())).thenReturn(mockToken);

        //mock the country list response
        CountryDto c1 = new CountryDto("Test Country 1");
        CountryDto c2 = new CountryDto("Test Country 2");

        //when
        when(countryClient.getCountry(anyString())).thenReturn(new ResponseEntity<>(List.of(c1, c2), HttpStatus.OK));
        //execute the async method
        List<String> countries = addressService.fetchCountryListAsync();
        //then part
        assertNotNull(countries);
    }

    @Test
    void testScheduledCountryList() {
        when(countryClient.auth(any(), any())).thenReturn(mockToken);

        //mock country list response
        CountryDto c1 = new CountryDto("Test Country 1");
        CountryDto c2 = new CountryDto("Test Country 2");
        when(countryClient.getCountry(anyString())).thenReturn(new ResponseEntity<>(List.of(c1, c2), HttpStatus.OK));

        //execute schedule method
        addressService.scheduleCountryList();
        List<String> result = addressService.listAllCountries();
        //then part
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Test Country 1"));
        assertTrue(result.contains("Test Country 2"));
    }

    @Test
    void testListAllCountries_whenCacheIsNull() {
        //when part
        when(countryClient.auth(any(), any())).thenReturn(mockToken);

        when(countryClient.getCountry(anyString())).thenReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));
        assertTrue(addressService.listAllCountries().isEmpty());
        //mock counties list response
        CountryDto c1 = new CountryDto("Test Country 1");
        CountryDto c2 = new CountryDto("Test Country 2");
        when(countryClient.getCountry(anyString())).thenReturn(new ResponseEntity<>(List.of(c1, c2), HttpStatus.OK));

        //initially, the cache should be null
//        assertNull(addressService.listAllCountries());
        //trigger the caching
        List<String> countries = addressService.listAllCountries();
        //Initially, cache should be null
        assertNotNull(countries);

    }

    @Test
    void testListAllCountries_fallBack() {
        //when part
        when(countryClient.auth(any(), any())).thenReturn(mockToken);
        //mock failed country list
        when(countryClient.getCountry(anyString())).thenReturn(new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR));
        //execute the method
        List<String> countries = addressService.listAllCountries();
        assertNotNull(countries);
        assertTrue(countries.contains("United States"));
        assertTrue(countries.contains("Canada"));
        assertTrue(countries.contains("France"));
        assertTrue(countries.contains("India"));
        assertTrue(countries.contains("Japan"));
        assertTrue(countries.contains("United Kingdom"));
    }
}
