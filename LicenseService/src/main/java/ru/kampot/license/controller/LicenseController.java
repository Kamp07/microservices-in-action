package ru.kampot.license.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kampot.license.model.License;
import ru.kampot.license.service.LicenseService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
@RequiredArgsConstructor
public class LicenseController {
    private final LicenseService licenseService;

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable("licenseId") String licenseId, @PathVariable("organizationId") String organizationId){
        License license = licenseService.getLicense(licenseId, organizationId);

        license.add(linkTo(methodOn(LicenseController.class)
                .createLicense(organizationId, license))
                .withRel("createLicense"),
                linkTo(methodOn(LicenseController.class)
                        .updateLicence(organizationId, license))
                        .withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class)
                        .deleteLicense(organizationId, license.getLicenseId()))
                        .withRel("deleteLicense"));

        return ResponseEntity.ok(license);
    }

    @PutMapping
    public ResponseEntity<String> updateLicence(@PathVariable("organizationId") String organizationId, @RequestBody License licenseFromRequest){
        return ResponseEntity.ok(licenseService.updateLicense(licenseFromRequest, organizationId));
    }

    @PostMapping
    public ResponseEntity<String> createLicense(@PathVariable("organizationId") String organizationId, @RequestBody License licenseFromRequest){
        return ResponseEntity.ok(licenseService.createLicense(licenseFromRequest, organizationId));
    }

    @DeleteMapping("/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable String licenseId, @PathVariable String organizationId){
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId, organizationId));
    }
}
