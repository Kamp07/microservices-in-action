package ru.kampot.license.service;

import ru.kampot.license.model.License;

import java.util.List;
import java.util.Locale;

public interface LicenseService {
    License getLicense(String licenseId, String organizationId);
    License getLicense(String organizationId, String licenseId, String clientType);
    List<License> getLicensesByOrganizationId(String organizationId);
    License createLicense(License license);

    License updateLicense(License license);

    String deleteLicense(String licenseId);
}
