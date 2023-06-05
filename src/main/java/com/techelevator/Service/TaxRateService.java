package com.techelevator.Service;

import com.techelevator.model.SalesTax;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** Tax Rate Service that consumes API and retrieves sales tax based on user state code
 */
@Service
public class TaxRateService {

    private String API_BASE_URL = "https://teapi.netlify.app/api/statetax?state=";
    private final RestTemplate restTemplate = new RestTemplate();

    public SalesTax getTaxRate(String stateCode) {
        return restTemplate.getForObject(API_BASE_URL + stateCode, SalesTax.class);
    }
}
