package ru.kampot.license.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.kampot.license.model.Organization;

public class OrganizationRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public Organization getOrganization(String organizationId){
        ResponseEntity<Organization> restExchange = restTemplate.exchange("http://organization-service/v1/organization/{organizationId}", HttpMethod.GET, null, Organization.class, organizationId);
        return restExchange.getBody();
    }
}
