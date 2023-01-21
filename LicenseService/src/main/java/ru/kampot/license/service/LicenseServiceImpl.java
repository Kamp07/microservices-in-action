package ru.kampot.license.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.kampot.license.config.ServiceConfig;
import ru.kampot.license.model.License;
import ru.kampot.license.model.Organization;
import ru.kampot.license.repo.LicenseRepository;
import ru.kampot.license.service.client.OrganizationDiscoveryClient;
import ru.kampot.license.service.client.OrganizationRestTemplateClient;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@Slf4j
public class LicenseServiceImpl implements LicenseService {
    private MessageSource messages;
    private final LicenseRepository licenseRepository;
    private ServiceConfig config;
    private OrganizationRestTemplateClient organizationRestTemplateClient;
    private OrganizationDiscoveryClient organizationDiscoveryClient;

    @Override
    public License getLicense(String licenseId, String organizationId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license == null) {
            throw new IllegalArgumentException(
                    String.format(messages.getMessage(
                            "license.search.error.message", null, null),
                            licenseId, organizationId));
        }
        return license.withComment(config.getProperty());
    }

    @Override
    public License getLicense(String organizationId, String licenseId, String clientType) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license == null){
            throw new IllegalArgumentException(String.format(
                    messages.getMessage("license.search.error.message", null, null),
                    licenseId, organizationId));
        }
        Organization organization = retrieveOrganizationInfo(organizationId, clientType);
        if (organization != null){
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }
        return license.withComment(config.getProperty());
    }

    @Override
    @CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    @Retry(name = "retryLicenseService", fallbackMethod = "buildFallbackLicenseList")
    @RateLimiter(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    @Bulkhead(name = "bulkheadLicenseService", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "buildFallbackLicenseList")
    public List<License> getLicensesByOrganizationId(String organizationId) {
        randomlyRunLong();
        License license = License.builder()
                .licenseId("000-1337-2341")
                .organizationId(organizationId)
                .organizationName("kampot")
                .description("testCall")
                .productName("gameEngine")
                .build();
        return licenseRepository.findAll();
    }

    @Override
    public License createLicense(License license) {
        License newLicense = License.builder()
                .licenseId(UUID.randomUUID().toString())
                .build();

        licenseRepository.save(newLicense);
        return newLicense.withComment(config.getProperty());
    }

    @Override
    public License updateLicense(License license) {
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    @Override
    public String deleteLicense(String licenseId) {
        License license = licenseRepository.findById(licenseId).orElseThrow();
        licenseRepository.delete(license);
        return String.format(messages.getMessage("license.delete.message", null, null), licenseId);
    }

    private Organization retrieveOrganizationInfo(String organizationId, String clientType){
        Organization organization = null;
        switch (clientType){
            case "rest" :
                System.out.println("I am using the rest client");
                organization = organizationRestTemplateClient.getOrganization(organizationId);
                break;
            case "discovery" :
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default :
                organization = organizationRestTemplateClient.getOrganization(organizationId);
                break;
        }
        return organization;
    }

    private List<License> buildFallbackLicenseList(String organizationId, Throwable t){
        License license = License.builder()
                .licenseId("000000-00-0000")
                .organizationId(organizationId)
                .productName("No listening information currently available")
                .build();

        return List.of(license);
    }

    private void randomlyRunLong(){
        Random random = new Random();
        int randomNum = random.nextInt(3) + 1;
        if (randomNum == 3) sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(5000);
            throw new TimeoutException();
        }catch (InterruptedException | TimeoutException e){
            Logger.getLogger("error").info("TimeOutException");
        }
    }
}
