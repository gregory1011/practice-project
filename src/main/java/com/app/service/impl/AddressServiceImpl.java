package com.app.service.impl;

import com.app.annotation.ExecutionTime;
import com.app.client.CountryClient;
import com.app.dto.common.CountryDto;
import com.app.dto.common.TokenDto;
import com.app.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final CountryClient countryClient;

    @Value("${api.country.token}")
    private String token;
    @Value("${api.country.email}")
    private String email;

    private List<String> displayCountryList;

    //method
    private String getBearerToken(){
        TokenDto tokenDto = countryClient.auth(email, token);
        return "Bearer "+tokenDto.getAuthToken();
    }


    @Scheduled(fixedRate = 100_000) // execute every 100 sec
    public void scheduleCountryList(){
        fetchCountryListAsync();
    }

    @Async  // runs this method asynchronously (with another thread)
    public List<String> fetchCountryListAsync() {
        log.info("... fetching Countries with thread: {}", Thread.currentThread().getName());

        ResponseEntity<List<CountryDto>> response= countryClient.getCountry(getBearerToken());

        if (response.getStatusCode().is2xxSuccessful()) {
            displayCountryList = Objects.requireNonNull(response.getBody())
                    .stream().map(CountryDto::getCountryName).toList();
        } else if (displayCountryList == null) {
            log.info("... could not fetched Countries, assigning back-up list");
            displayCountryList= List.of("United States", "Canada", "France", "India", "Japan", "United Kingdom");
        }

        return displayCountryList;
    }

    @ExecutionTime
    @Override
    public List<String> listAllCountries() {
        return fetchCountryListAsync();
    }


}
