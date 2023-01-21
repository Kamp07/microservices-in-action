package ru.kampot.license.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kampot.license.model.License;
import ru.kampot.license.service.LicenseService;
import ru.kampot.license.utils.UserContextHolder;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
@RequiredArgsConstructor
public class LicenseController {
    private final LicenseService licenseService;
    private static final Logger logger = LoggerFactory.getLogger(LicenseController.class);

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable("licenseId") String licenseId, @PathVariable("organizationId") String organizationId){
        License license = licenseService.getLicense(licenseId, organizationId);

        license.add(linkTo(methodOn(LicenseController.class)
                .createLicense(license))
                .withRel("createLicense"),
                linkTo(methodOn(LicenseController.class)
                        .updateLicence(license))
                        .withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class)
                        .deleteLicense(organizationId, license.getLicenseId()))
                        .withRel("deleteLicense"));

        return ResponseEntity.ok(license);
    }

    @GetMapping("/")
    public List<License> getLicenses(@PathVariable("organizationId") String organizationId){
        logger.debug("LicenseServiceController CorrelationId: {}",
                UserContextHolder.getContext().getCorrelationId());
        return licenseService.getLicensesByOrganizationId(organizationId);
    }

    @GetMapping("/{licenseID}/{clientType}")
    public License getLicensesWithClient(@PathVariable("organizationId") String organizationId,
                                         @PathVariable("licenseID") String licenseID,
                                         @PathVariable("clientType") String clientType){
        return licenseService.getLicense(organizationId, licenseID, clientType);
    }

    @PutMapping
    public ResponseEntity<License> updateLicence(@RequestBody License licenseFromRequest){
        return ResponseEntity.ok(licenseService.updateLicense(licenseFromRequest));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License licenseFromRequest){
        return ResponseEntity.ok(licenseService.createLicense(licenseFromRequest));
    }

    @DeleteMapping("/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable String licenseId, @PathVariable String organizationId){
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
    }
}
