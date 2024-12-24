package com.app.service.unit;


import com.app.client.CountryClient;
import com.app.dto.common.CountryDto;
import com.app.dto.common.TokenDto;
import com.app.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;


@ExtendWith(MockitoExtension.class)
public class AddressServiceImp_UnitTest {

    @Mock
    private CountryClient countryClient;
    @InjectMocks
    private AddressServiceImpl addressService;

    @Value("${api.country.token}")
    private String token;
    @Value("${api.country.email}")
    private String email;

    @Test
    void getCountryList_success() {
        //given -> mock the countryList
        List<CountryDto> mockCountryList= List.of(new CountryDto("USA"), new CountryDto("Canada"));
        ResponseEntity<List<CountryDto>> mockResponse= new ResponseEntity<>(mockCountryList, HttpStatus.OK);
        //mock tokenDto response
        TokenDto mockTokenDto= new TokenDto();
        mockTokenDto.setAuthToken("ASDBBUDIGIAHKJBD^&*(");
        //when -> mock the behavior of CountryClient
        when(countryClient.auth(email, token)).thenReturn(mockTokenDto);
        when(countryClient.getCountry("Bearer ASDBBUDIGIAHKJBD^&*(")).thenReturn(mockResponse);
        //call the method to fetch the country list
        List<String> fetchedCountries= addressService.fetchCountryListAsync();
        //then part
        assertThat(fetchedCountries).containsExactly("USA","Canada");
        List<String> result= addressService.listAllCountries();
        assertThat(result).containsExactly("USA","Canada");
    }

    @Test
    void getCountryList_fail() {
        //mock the tokenDto
        TokenDto mockTokenDto = new TokenDto();
        mockTokenDto.setAuthToken("ASDBBUDIGIAHKJBD^&*(");
        //when
        when(countryClient.auth(email, token)).thenReturn(mockTokenDto);

        //mock the response from client to fail
        ResponseEntity<List<CountryDto>> mockResponse= new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        when(countryClient.getCountry("Bearer ASDBBUDIGIAHKJBD^&*(")).thenReturn(mockResponse);

        List<String> fetchedCountries = addressService.fetchCountryListAsync();
        assertThat(fetchedCountries).containsExactly("United States", "Canada", "France", "India", "Japan", "United Kingdom");

        List<String> result = addressService.listAllCountries();
        assertThat(result).containsExactly("United States", "Canada", "France", "India", "Japan", "United Kingdom");
    }

}
